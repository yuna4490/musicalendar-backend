package Musicalendar.musicalendarproject.repository;

import Musicalendar.musicalendarproject.domain.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    boolean existsByTitle(String title);
    Show findByTitle(String title);
}
