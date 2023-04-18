package Musicalendar.musicalendarproject.repository;

import Musicalendar.musicalendarproject.domain.ShowSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowScheduleRepository extends JpaRepository<ShowSchedule, Long> {
}
