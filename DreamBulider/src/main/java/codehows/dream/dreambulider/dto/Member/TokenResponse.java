package codehows.dream.dreambulider.dto.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponse {
    private String access_token;
    private String refresh_token;
}
