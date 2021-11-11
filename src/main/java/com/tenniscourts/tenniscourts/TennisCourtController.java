package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.guests.GuestDTO;
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
@RequestMapping("court")
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;
    private final String apiMessagePrefix = "Tennis Court";

    @PostMapping
    @ApiOperation("Add tennis court.")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.ADDED),
            @ApiResponse(code = APIResponseCodes.ERROR_OCCURRED, message = apiMessagePrefix + APIResponseMessages.ERROR)
    })
    public ResponseEntity<Void> addTennisCourt(TennisCourtDTO tennisCourtDTO) {
        return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }

    @GetMapping("/{tennisCourtId}")
    @ApiOperation("Find tennis court by ID.")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.FOUND),
            @ApiResponse(code = APIResponseCodes.NOT_FOUND, message = apiMessagePrefix + APIResponseMessages.NOT_FOUND)
    })
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    @GetMapping("/scheduled/{tennisCourtId}")
    @ApiOperation("Find tennis courts with schedules by ID")
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }

    @GetMapping("/all")
    @ApiOperation("Find all courts")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.FOUND),
            @ApiResponse(code = APIResponseCodes.NOT_FOUND, message = apiMessagePrefix + APIResponseMessages.NOT_FOUND)
    })
    public ResponseEntity<List<TennisCourtDTO>> findAllCourts() {
        return ResponseEntity.ok(tennisCourtService.findAllCourts());
    }
}
