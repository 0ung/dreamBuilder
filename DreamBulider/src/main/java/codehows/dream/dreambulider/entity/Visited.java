package codehows.dream.dreambulider.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
