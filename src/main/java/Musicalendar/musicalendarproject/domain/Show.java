package Musicalendar.musicalendarproject.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Show {

    @Id
    @Column(name = "show_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(length = 30, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public Show(String title, Category category){
        this.title = title;
        this.category = category;
    }

    @Override
    public String toString(){
        return "#"+id+" 타이틀: "+title;
    }
}