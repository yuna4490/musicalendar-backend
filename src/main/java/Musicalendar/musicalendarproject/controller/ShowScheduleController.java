package Musicalendar.musicalendarproject.controller;

import Musicalendar.musicalendarproject.domain.ShowSchedule;
import Musicalendar.musicalendarproject.service.ShowScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShowScheduleController {

    private final ShowScheduleService showScheduleService;

    @GetMapping("/calendar")
    public String getAllSchedules(){
        showScheduleService.getShowSchedules();
        return "성공";
    }

    @GetMapping("/calendar/list")
    public ResponseEntity<List<ShowSchedule>> getAllSchedules2(){
        return ResponseEntity.ok(showScheduleService.getShowSchedules());
    }


}
