package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
