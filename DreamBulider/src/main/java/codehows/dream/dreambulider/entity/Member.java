package codehows.dream.dreambulider.entity;

import codehows.dream.dreambulider.constats.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.extern.apachecommons.CommonsLog;

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(unique = true,nullable = false)
	private String email;  
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	private Authority authority;
	private boolean isWithdrawal;
}
