package Musicalendar.musicalendarproject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

//    @Column(length = 15, nullable = false, unique = true)
//    private String username;

    @Column(length = 15, nullable = false)
    private String nickname;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    private String provider;    //  구글? 카카오?
    private String provideId;   //  해당 소셜 상의 아이디

//    @Column(length = 50, nullable = false)
//    private String password;
//
//    @Column(length = 20, nullable = false)
//    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public Member update(String nickname){
        this.nickname=nickname;

        return this;
    }

    @Builder
    public Member(String nickname, String email, String provider, String provideId, Authority authority){
//        this.username=username;
        this.nickname = nickname;
        this.email = email;
        this.provider=provider;
        this.provideId=provideId;
        this.authority=authority;
//        this.password = password;
//        this.phoneNumber = phoneNumber;
    }


}
