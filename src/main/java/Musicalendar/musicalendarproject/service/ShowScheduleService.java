package Musicalendar.musicalendarproject.service;

import Musicalendar.musicalendarproject.domain.Show;
import Musicalendar.musicalendarproject.domain.ShowSchedule;
import Musicalendar.musicalendarproject.repository.ShowRepository;
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
    private final ShowRepository showRepository;

    public List<ShowSchedule> getShowSchedules(){
        return showScheduleRepository.findAll();
    }

    public List<Show> getShows() {
        return showRepository.findAll();
    }



}
