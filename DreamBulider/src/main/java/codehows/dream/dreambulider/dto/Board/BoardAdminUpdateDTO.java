package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.constats.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardAdminUpdateDTO {

    private Long id;
    private boolean invisible;
    private Authority deleteBy;
}
