package com.tenniscourts.tenniscourts;

import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TennisCourtMapper {
    TennisCourtDTO map(TennisCourt source);

    @InheritInverseConfiguration
    TennisCourt map(TennisCourtDTO source);

    List<TennisCourtDTO> map(List<TennisCourt> source);
}
