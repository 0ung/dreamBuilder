package codehows.dream.dreambulider.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Visited {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "visited_id")
	private Long id;

	@Column(nullable = false)
	private String visitedIp;

	@Column(nullable = false)
	private LocalDate visitDate;

}
