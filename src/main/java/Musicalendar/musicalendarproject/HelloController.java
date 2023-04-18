package Musicalendar.musicalendarproject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class HelloController {
    @GetMapping("api/hello")
    public List<String> Hello(){
        return Arrays.asList("리액트 스프링", "연결 완료");
    }
}