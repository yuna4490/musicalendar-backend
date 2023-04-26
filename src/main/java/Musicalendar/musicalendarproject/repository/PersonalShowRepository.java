package Musicalendar.musicalendarproject.repository;

import Musicalendar.musicalendarproject.domain.PersonalShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalShowRepository extends JpaRepository<PersonalShow, Long> {
}
