package Musicalendar.musicalendarproject.service;

import Musicalendar.musicalendarproject.domain.ShowSchedule;
import Musicalendar.musicalendarproject.repository.ShowScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ShowScheduleService {

    private final ShowScheduleRepository showScheduleRepository;

    public List<ShowSchedule> getShowSchedules(){
        return showScheduleRepository.findAll();
    }



}
