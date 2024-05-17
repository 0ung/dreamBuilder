package codehows.dream.dreambulider.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "refresh_Token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "refresh_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false,unique = true)
    private Member member;

    @Column(name="Refresh_Token",nullable = false)
    private String refreshToken;

    public RefreshToken(Member member, String refreshToken){
        this.member = member;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String refreshToken){
        this.refreshToken = refreshToken;
        return this;
    }


}
