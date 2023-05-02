package Musicalendar.musicalendarproject.dto;

import Musicalendar.musicalendarproject.domain.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessTokenDto {

    private String grantType;
    private String accessToken;
//    private String refreshToken;
    private Long accessTokenExpiresIn;
//    private Long refreshTokenExpirationTime;
    private Authority authority;
}
