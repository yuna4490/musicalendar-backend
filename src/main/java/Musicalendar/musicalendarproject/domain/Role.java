package Musicalendar.musicalendarproject.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    //MEMBER,GUEST
    GUEST("ROLE_GUEST"), MEMBER("ROLE_MEMBER"), USER("ROLE_USER");
    private  final String key;
}
