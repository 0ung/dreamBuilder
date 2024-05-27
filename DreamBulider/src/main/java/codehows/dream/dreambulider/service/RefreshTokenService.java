package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.RefreshToken;
import codehows.dream.dreambulider.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("확인되지 않은 토큰"));
    }

    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public void removeRefreshToken(Member member) {
        refreshTokenRepository.deleteByMember(member);
    }
}
