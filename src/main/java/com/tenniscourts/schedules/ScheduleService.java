package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.util.APIResponseMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    public ScheduleDTO findScheduleByID(Long scheduleId) {
        return scheduleMapper.map(scheduleRepository.findById(scheduleId).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule " + APIResponseMessages.NOT_FOUND);
        }));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }

    public List<ScheduleDTO> findAllSchedules() {
        List<Schedule> allSchedules = scheduleRepository.findAll();
        if (!allSchedules.isEmpty()) {
            return scheduleMapper.map(allSchedules);
        } else {
            throw new EntityNotFoundException("Schedule " + APIResponseMessages.NOT_FOUND);
        }
    }
}
