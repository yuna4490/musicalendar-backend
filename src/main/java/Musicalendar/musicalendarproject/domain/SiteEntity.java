package Musicalendar.musicalendarproject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "site")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteEntity {

    @Id
    @Column(name = "site_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String site;

    @Builder
    public SiteEntity(String site){
        this.site=site;
    }
}
