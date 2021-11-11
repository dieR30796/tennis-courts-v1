package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.util.APIResponseCodes;
import com.tenniscourts.util.APIResponseMessages;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;
    private final String apiMessagePrefix = "Reservation";

    @PostMapping
    @ApiOperation("Book new reservation.")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.ADDED),
            @ApiResponse(code = APIResponseCodes.ERROR_OCCURRED, message = apiMessagePrefix + APIResponseMessages.ERROR)
    })
    public ResponseEntity<Void> bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping("/{reservationId}")
    @ApiOperation("Find reservation by ID.")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.FOUND),
            @ApiResponse(code = APIResponseCodes.NOT_FOUND, message = apiMessagePrefix + APIResponseMessages.NOT_FOUND)
    })
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @PutMapping("/cancel/{reservationId}")
    @ApiOperation("Cancel Reservation")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping("/reschedule")
    @ApiOperation("Reschedule")
    public ResponseEntity<ReservationDTO> rescheduleReservation(Long reservationId, Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }

    @GetMapping("/all")
    @ApiOperation("Find all reservations")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.FOUND),
            @ApiResponse(code = APIResponseCodes.NOT_FOUND, message = apiMessagePrefix + APIResponseMessages.NOT_FOUND)
    })
    public ResponseEntity<List<ReservationDTO>> findAllReservations() {
        return ResponseEntity.ok(reservationService.findAllReservations());
    }
}
