package com.tenniscourts.tenniscourts;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.schedules.ScheduleService;
import com.tenniscourts.util.APIResponseMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TennisCourtService {

    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleService scheduleService;

    private final TennisCourtMapper tennisCourtMapper;

    public TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt) {
        return tennisCourtMapper.map(tennisCourtRepository.saveAndFlush(tennisCourtMapper.map(tennisCourt)));
    }

    public TennisCourtDTO findTennisCourtById(Long tennisCourtId) {
        return tennisCourtRepository.findById(tennisCourtId).map(tennisCourtMapper::map).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Tennis Court not found.");
        });
    }

    public TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId) {
        TennisCourtDTO tennisCourtDTO = findTennisCourtById(tennisCourtId);
        tennisCourtDTO.setTennisCourtSchedules(scheduleService.findSchedulesByTennisCourtId(tennisCourtId));
        return tennisCourtDTO;
    }

    public List<TennisCourtDTO> findAllCourts() {
        List<TennisCourt> allCourts = tennisCourtRepository.findAll();
        if (!allCourts.isEmpty()) {
            return tennisCourtMapper.map(allCourts);
        } else {
            throw new EntityNotFoundException("Courts " + APIResponseMessages.NOT_FOUND);
        }
    }
}
