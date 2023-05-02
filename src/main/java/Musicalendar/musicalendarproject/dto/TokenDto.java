package Musicalendar.musicalendarproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private AccessTokenDto accessTokenDto;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
}
