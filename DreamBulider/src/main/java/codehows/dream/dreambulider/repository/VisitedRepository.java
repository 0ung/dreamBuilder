package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Visited;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VisitedRepository extends JpaRepository<Visited, Long> {

    @Query("SELECT v FROM Visited v WHERE v.visitDate = :date")
    List<Visited> findAllByVisitDate(LocalDate date);

    @Query("SELECT v FROM Visited v WHERE v.visitDate BETWEEN :startDate AND :endDate")
    List<Visited> findAllByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Visited> findByVisitedIpAndVisitDate(String visitedIp, LocalDate visitDate);
}
