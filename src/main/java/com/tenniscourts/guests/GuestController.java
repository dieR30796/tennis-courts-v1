package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.reservations.ReservationDTO;
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
@RequestMapping("/guest")
public class GuestController extends BaseRestController {

    private final GuestService guestService;
    private final String apiMessagePrefix = "Guest";

    @PostMapping()
    @ApiOperation("Create new guest")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.ADDED),
            @ApiResponse(code = APIResponseCodes.ERROR_OCCURRED, message = apiMessagePrefix + APIResponseMessages.NOT_ADDED)
    })
    public ResponseEntity<Void> createGuest(CreateGuestDTO createGuestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addNewGuest(createGuestDTO).getId())).build();
    }

    @PutMapping
    @ApiOperation("Update guest")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.UPDATED),
            @ApiResponse(code = APIResponseCodes.ERROR_OCCURRED, message = apiMessagePrefix + APIResponseMessages.ERROR)
    })
    public ResponseEntity<GuestDTO> updateGuest(GuestDTO guestDTO) {
        return ResponseEntity.ok(guestService.updateGuest(guestDTO));
    }

    @GetMapping("/{guestId}")
    @ApiOperation("Find guest by ID")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.FOUND),
            @ApiResponse(code = APIResponseCodes.NOT_FOUND, message = apiMessagePrefix + APIResponseMessages.NOT_FOUND)
    })
    public ResponseEntity<GuestDTO> findGuestByID(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.findGuestByID(guestId));
    }

    @GetMapping
    @ApiOperation("Find guest by name")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.FOUND),
            @ApiResponse(code = APIResponseCodes.NOT_FOUND, message = apiMessagePrefix + APIResponseMessages.NOT_FOUND)
    })
    public ResponseEntity<List<GuestDTO>> findGuestByName(String name) {
        return ResponseEntity.ok(guestService.findGuestByName(name));
    }

    @GetMapping("/all")
    @ApiOperation("Find all guests")
    @ApiResponses(value = {
            @ApiResponse(code = APIResponseCodes.SUCCESS, message = apiMessagePrefix + APIResponseMessages.FOUND),
            @ApiResponse(code = APIResponseCodes.NOT_FOUND, message = apiMessagePrefix + APIResponseMessages.NOT_FOUND)
    })
    public ResponseEntity<List<GuestDTO>> findAllGuests() {
        return ResponseEntity.ok(guestService.findAllGuests());
    }
}
