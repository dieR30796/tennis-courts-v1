package com.tenniscourts.reservations;

import com.tenniscourts.schedules.Schedule;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(new BigDecimal(10), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));
    }

    @Test
    @DisplayName("Test to check that the refund value within 24 hours but greater/equal to 12 hours is 75% of the total value.")
    public void getRefundValueLessThan24HoursGreaterThan12Hours() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(20);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(new BigDecimal("7.50"), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));
    }

    @Test
    @DisplayName("Test to check that the refund value within 12 hours but greater/equal to 2 hours is 50% of the total value.")
    public void getRefundValueLessThan12HoursGreaterThan2Hours() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(5);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(new BigDecimal("5.0"), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));
    }

    @Test
    @DisplayName("Test to check that the refund value within 2 hours but greater than 0 hours is 75% of the total value.")
    public void getRefundValueLessThan2HoursGreaterThan0Hours() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(1);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(new BigDecimal("2.50"), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));
    }
}