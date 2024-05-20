package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    void deleteByMember(Member member);
}
