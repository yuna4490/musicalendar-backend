package Musicalendar.musicalendarproject.controller;

import Musicalendar.musicalendarproject.domain.ShowSchedule;
import Musicalendar.musicalendarproject.service.ShowScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShowScheduleController {

    private final ShowScheduleService showScheduleService;

    @GetMapping("/calendar")
    public List<ShowSchedule> getAllSchedules(){
        return showScheduleService.getShowSchedules();
    }


}
