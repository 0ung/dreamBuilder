package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<BoardFile,Long> {
}
