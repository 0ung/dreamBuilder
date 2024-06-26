package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.Liked.LikedRequest;
import codehows.dream.dreambulider.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class LikedController {

    private final LikedService likedService;

    //좋아요 추가/삭제
    @PutMapping("/api/liked")
    public ResponseEntity<?> insert(@RequestBody LikedRequest likedRequest, Principal principal) {
        likedService.insert(likedRequest, principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
