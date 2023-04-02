package Musicalender.musicalenderproject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ps_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 20, nullable = false)
    private String date; //type 확인 필요

    @Enumerated(EnumType.STRING)
    private Site site;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Builder
    public PersonalShow(String title, String date, Site site, Member member){
        this.title = title;
        this.date = date;
        this.site = site;
        this.member = member;
    }
}
