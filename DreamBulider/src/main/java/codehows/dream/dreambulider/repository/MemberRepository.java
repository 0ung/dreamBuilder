package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsMemberByEmail(String email);

    Optional<Member> findMemberByEmail(String email);

    Page<Member> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Member> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
