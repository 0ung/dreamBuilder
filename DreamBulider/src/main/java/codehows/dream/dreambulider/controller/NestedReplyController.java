package codehows.dream.dreambulider.controller;


import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedReplyRequestDTO;
import codehows.dream.dreambulider.service.NestedReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NestedReplyController {

    private final NestedReplyService nestedReplyService;

    @PostMapping("/rereply")
    public ResponseEntity<?> saveNestedReply(@RequestBody NestedReplyRequestDTO nestedReplyRequestDTO) {

        try {
            nestedReplyService.saveNestedReply(nestedReplyRequestDTO);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
