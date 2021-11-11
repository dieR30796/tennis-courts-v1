package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.util.APIResponseMessages;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GuestService {

    private GuestRepository guestRepository;
    private GuestMapper guestMapper;

    public GuestDTO addNewGuest(CreateGuestDTO createGuestDTO) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(createGuestDTO)));
    }

    public GuestDTO updateGuest(GuestDTO guestDTO) {
        Guest guestToUpdate = guestMapper.map(guestDTO);
        guestToUpdate.setName(guestDTO.getName());
        return guestMapper.map(guestRepository.save(guestToUpdate));
    }

    public GuestDTO findGuestByID(Long guestId) {
        return guestRepository.findById(guestId).map(guestMapper::map).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Guest " + APIResponseMessages.NOT_FOUND);
        });
    }

    // List of GuestDTO for case if name is same for multiple entries.
    public List<GuestDTO> findGuestByName(String name) {
        List<Guest> guestsByName = guestRepository.findGuestByName(name);
        if (!guestsByName.isEmpty()) {
            return guestMapper.map(guestsByName);
        } else {
            throw new EntityNotFoundException("Guest " + APIResponseMessages.NOT_FOUND);
        }
    }

    public List<GuestDTO> findAllGuests() {
        List<Guest> allGuests = guestRepository.findAll();
        if (!allGuests.isEmpty()) {
            return guestMapper.map(allGuests);
        } else {
            throw new EntityNotFoundException("Guests " + APIResponseMessages.NOT_FOUND);
        }
    }
}
