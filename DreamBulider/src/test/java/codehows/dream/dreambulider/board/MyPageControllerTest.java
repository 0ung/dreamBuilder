package codehows.dream.dreambulider.board;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import codehows.dream.dreambulider.controller.BoardController;
import codehows.dream.dreambulider.controller.MypageController;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.repository.LikedRepository;
import codehows.dream.dreambulider.repository.MemberRepository;
import codehows.dream.dreambulider.service.BoardService;
import codehows.dream.dreambulider.service.LikedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Optional;

@WebMvcTest(MypageController.class)
@ExtendWith(MockitoExtension.class)
public class MyPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LikedService likedService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LikedRepository likedRepository;
    @Mock
    private BoardService boardService;

    @InjectMocks
    private MypageController mypageController;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testCountLike() throws Exception {
        String email = "test@example.com";
        Principal principal = new UserPrincipal(email);


        Member member = new Member();
        member.setId(1L);
        member.setEmail(email);

        when(memberRepository.findMemberByEmail(email)).thenReturn(Optional.of(member));
        when(likedRepository.countByMemberId(member.getId())).thenReturn(5L);

        mockMvc.perform(get("/api/myPage/like"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(5L));
    }

    private static class UserPrincipal implements Principal {
        private final String name;

        public UserPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
