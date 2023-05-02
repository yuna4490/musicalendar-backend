package Musicalendar.musicalendarproject.controller;

import Musicalendar.musicalendarproject.service.UserService;
import Musicalendar.musicalendarproject.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final UserService userService;

    @PostMapping("/signUp") //겹치기 떄문에 sign-up에서 signUp으로 변경
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        userService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

    @GetMapping("/jwtTest") //jwt-test -> jwtTest
    public String jwtTest() {

        return "jwtTest 요청 성공";
    }
}
