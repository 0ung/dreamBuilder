package codehows.dream.dreambulider.dto.Member;

import codehows.dream.dreambulider.constats.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberListResponseDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private boolean isWithdrawal;
    private Authority authority;
}
