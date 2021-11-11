package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import com.tenniscourts.util.APIResponseMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final ScheduleRepository scheduleRepository;

    private final ReservationMapper reservationMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        Guest guest = guestRepository.findById(createReservationRequestDTO.getGuestId()).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Guest " + APIResponseMessages.NOT_FOUND);
        });
        Schedule schedule = scheduleRepository.findById(createReservationRequestDTO.getScheduleId()).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule " + APIResponseMessages.NOT_FOUND);
        });

        isScheduleReadyToPlayOnAnyReservations(schedule);

        Reservation reservation = Reservation.builder()
                .guest(guest)
                .schedule(schedule)
                .value(BigDecimal.valueOf(50)) // setting to 50 as could not see clear indication of price.
                .refundValue(BigDecimal.valueOf(10))
                .reservationStatus(ReservationStatus.READY_TO_PLAY)
                .build();
        return reservationMapper.map(reservationRepository.saveAndFlush(reservation));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (minutes >= 1440L) {
            return reservation.getValue();
        } else if (minutes >= 720L) {
            return reservation.getValue().multiply(BigDecimal.valueOf(.75));
        } else if (minutes >= 120L) {
            return reservation.getValue().multiply(BigDecimal.valueOf(.5));
        } else if (minutes > 0L){
            return reservation.getValue().multiply(BigDecimal.valueOf(.25));
        } else {
            return BigDecimal.ZERO;
        }
    }


    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule " + APIResponseMessages.NOT_FOUND);
        });

        isScheduleReadyToPlayOnAnyReservations(schedule);

        Reservation previousReservation = cancel(previousReservationId);

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }

    public List<ReservationDTO> findAllReservations() {
        List<Reservation> allReservations = reservationRepository.findAll();
        if (!allReservations.isEmpty()) {
            return reservationMapper.map(allReservations);
        } else {
            throw new EntityNotFoundException("Reservations " + APIResponseMessages.NOT_FOUND);
        }
    }

    // Method takes in schedule and determines if any reservations are ready to play currently for it. If so throw error.
    private void isScheduleReadyToPlayOnAnyReservations(Schedule schedule) {
        List <Reservation> checkExistingReservationsByScheduleID = reservationRepository.findBySchedule_Id(schedule.getId());
        if (!checkExistingReservationsByScheduleID.isEmpty()) {
            for (Reservation reservation : checkExistingReservationsByScheduleID) {
                if (reservation.getReservationStatus().equals(ReservationStatus.READY_TO_PLAY)) {
                    throw new AlreadyExistsEntityException("Tennis court is already booked for requested schedule.");
                }
            }
        }
    }
}
