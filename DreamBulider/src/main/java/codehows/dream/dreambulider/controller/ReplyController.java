package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.ReplyDTO.ReplyDeleteDTO;
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
    public ResponseEntity<?> saveReply(@RequestBody ReplyRequestDTO replyRequestDTO, Principal principal){

        try{
            replyService.saveReply(replyRequestDTO, principal.getName());
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch(RuntimeException e){
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

        ReplyResponseDTO response = replyService.findById(id);

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/reply/{id}")
    public ResponseEntity<?> deleteReply(@PathVariable(name = "id") long id) {

        ReplyDeleteDTO deleteReply = replyService.deleteInvisible(id);

        return ResponseEntity.ok(deleteReply);
    }

    @PutMapping("/reply/{id}")
    public ResponseEntity<?> updateReply(@PathVariable(name = "id") long id,
                                             @RequestBody ReplyUpdateDTO replyUpdateDTO) {
        Reply updatedReply = replyService.update(id, replyUpdateDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
