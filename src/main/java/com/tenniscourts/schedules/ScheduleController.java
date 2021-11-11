package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.util.APIResponseCodes;
import com.tenniscourts.util.APIResponseMessages;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;
    private final String apiMessagePrefix = "Schedule";

    @GetMapping("/{scheduleId}")
    @ApiOperation("Find schedule by ID.")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.FOUND),
            @ApiResponse(code = APIResponseCodes.NOT_FOUND, message = apiMessagePrefix + APIResponseMessages.NOT_FOUND)
    })
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findScheduleByID(scheduleId));
    }

    @GetMapping("/all")
    @ApiOperation("Find all reservations")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.FOUND),
            @ApiResponse(code = APIResponseCodes.NOT_FOUND, message = apiMessagePrefix + APIResponseMessages.NOT_FOUND)
    })
    public ResponseEntity<List<ScheduleDTO>> findAllSchedules() {
        return ResponseEntity.ok(scheduleService.findAllSchedules());
    }
}
