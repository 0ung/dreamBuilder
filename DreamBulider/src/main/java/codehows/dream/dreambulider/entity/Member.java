package codehows.dream.dreambulider.entity;

import codehows.dream.dreambulider.constats.Authority;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends BaseTimeEntity implements UserDetails {

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

	public Member updatewithdrawal(boolean withdrawal) {
		this.isWithdrawal = withdrawal;
		return this;
	}

	public Member updatemodify(String name, String password){
		this.name = name;
		this.password = password;
		return this;
	}

	public Member nameupdatemodify(String name){
		this.name = name;
		return this;
	}

	public Member pwupdatemodify(String password){
		this.password = password;
		return this;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
