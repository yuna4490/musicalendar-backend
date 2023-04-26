package Musicalendar.musicalendarproject.controller;

import Musicalendar.musicalendarproject.domain.Show;
import Musicalendar.musicalendarproject.domain.ShowSchedule;
import Musicalendar.musicalendarproject.dto.PersonalShowDto;
import Musicalendar.musicalendarproject.repository.ShowScheduleRepository;
import Musicalendar.musicalendarproject.service.ShowScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShowScheduleController {

    private final ShowScheduleService showScheduleService;
    private final ShowScheduleRepository showScheduleRepository;

//    @GetMapping("/calendar")
//    public String getAllSchedules(){
//        showScheduleRepository.findAll();
//        return "성공";
//    }

    @GetMapping("/calendar")
    public List<ShowSchedule> getAllSchedules(){
        return showScheduleService.getShowSchedules();
    }

    @GetMapping("/show")
    public List<Show> getShows(){
        return showScheduleService.getShows();
    }

    @PostMapping("/calendar")
    public Long saveSchedule(@RequestBody PersonalShowDto personalShowDto){
        return showScheduleService.saveSchedule(personalShowDto);
    }

}
