package Musicalender.musicalenderproject.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "show")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_id", unique = true, nullable = false)
    private Long show_id;

    @Column(length = 30, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public Show(String title, Category category){
        this.title = title;
        this.category = category;
    }

}