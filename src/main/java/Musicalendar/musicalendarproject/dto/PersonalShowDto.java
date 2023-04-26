package Musicalendar.musicalendarproject.dto;

import Musicalendar.musicalendarproject.domain.Member;
import Musicalendar.musicalendarproject.domain.PersonalShow;
import Musicalendar.musicalendarproject.domain.Site;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalShowDto {

    private String title;
    private Site site;
    private String date;
//    private Member member;


    @Builder
    public PersonalShowDto(String title, String date, Site site){
        this.title = title;
        this.date = date;
        this.site = site;
//        this.member = member;
    }

    public PersonalShow toEntity(){
        return PersonalShow.builder()
                .title(title)
                .site(site)
                .date(date)
//                .member(member)
                .build();
    }
}
