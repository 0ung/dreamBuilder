package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.ReplyDTO.ReplyDeleteDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyRequestDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyResponseDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyUpdateDTO;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/reply")
    public ResponseEntity<?> saveReply(@RequestBody ReplyRequestDTO replyRequestDTO, Principal principal) {
        try {
            Reply reply = replyService.saveReply(replyRequestDTO, principal.getName());
            ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO(reply);
            return new ResponseEntity<>(replyResponseDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/read/{boardId}/{page}")
    public ResponseEntity<?> findAllReplys(@PathVariable Optional<Integer> page,
                                           @PathVariable(name = "boardId") Long boardId) {
        Pageable pageable = PageRequest.of(page.orElse(0), 5,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Reply> replys = replyService.findAll(boardId, pageable);

        List<ReplyResponseDTO> result = replys.stream()
                .map(ReplyResponseDTO::new)
                .toList();
        return ResponseEntity.ok()
                .body(result);
    }

    @GetMapping("/read/total/{boardId}")
    public ResponseEntity<?> getTotal(@PathVariable Long boardId) {
        int total = replyService.getTotal(boardId);
        return new ResponseEntity<>(total, HttpStatus.OK);
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

    @GetMapping("/admin/reply/{page}")
    public ResponseEntity<?> getAllReply(@PathVariable(name = "page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        return new ResponseEntity<>(replyService.getAdminData(pageable), HttpStatus.OK);
    }

    @GetMapping("/admin/reply/total")
    public ResponseEntity<?> getTotal() {
        return new ResponseEntity<>(replyService.getTotal(), HttpStatus.OK);
    }

    @GetMapping("/admin/reply/title/{replyId}")
    public ResponseEntity<?> getTitle(@PathVariable(name = "replyId") Long replyId) {
        return new ResponseEntity<>(replyService.getBoardTitle(replyId), HttpStatus.OK);
    }
}
