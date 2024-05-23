package codehows.dream.dreambulider.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberFormDTO {
    private String name;
    private String password;
    private String email;
}
