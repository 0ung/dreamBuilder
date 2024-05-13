package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.ReplyDTO.ReplyRequestDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyResponseDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyUpdateDTO;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/reply")
    public ResponseEntity<?> saveReply(@RequestBody ReplyRequestDTO replyRequestDTO){
        try{
            replyService.saveReply(replyRequestDTO);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/read")
    public ResponseEntity<List<ReplyResponseDTO>> findAllReplys() {
        List<ReplyResponseDTO> replys = replyService.findAll()
                .stream()
                .map(ReplyResponseDTO::new)
                .toList();
        return ResponseEntity.ok()
                .body(replys);
    }

    @GetMapping("/reply/{id}")
    public ResponseEntity<ReplyResponseDTO> findReply(@PathVariable(name = "id") long id) {
        Reply reply = replyService.findById(id);

        return ResponseEntity.ok()
                .body(new ReplyResponseDTO(reply));
    }
    @DeleteMapping("/reply/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable(name = "id") long id) {
        replyService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/reply/{id}")
    public ResponseEntity<Reply> updateReply(@PathVariable(name = "id") long id,
                                             @RequestBody ReplyUpdateDTO replyUpdateDTO) {
        Reply updatedReply = replyService.update(id, replyUpdateDTO);

        return ResponseEntity.ok()
                .body(updatedReply);
    }
}
