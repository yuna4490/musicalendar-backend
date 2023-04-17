package Musicalendar.musicalendarproject.service;

import Musicalendar.musicalendarproject.domain.Show;
import Musicalendar.musicalendarproject.domain.ShowSchedule;
import Musicalendar.musicalendarproject.domain.ShowScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShowScheduleService {

    private final ShowScheduleRepository showScheduleRepository;

    public List<ShowSchedule> getShowSchedules(){
        List<ShowSchedule> showSchedules = showScheduleRepository.findAll();
        return showSchedules;
    }



}
