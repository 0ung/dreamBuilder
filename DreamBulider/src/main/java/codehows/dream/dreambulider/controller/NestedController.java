package codehows.dream.dreambulider.controller;


import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedRequestDTO;
import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedResponseDTO;
import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedUpdateDTO;
import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.service.NestedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NestedController {

    private final NestedService nestedService;

    @PostMapping("/reply/{id}/rereply")
    public ResponseEntity<?> saveNestedReply(@PathVariable(name = "id") Long id,
                                                       @RequestBody NestedRequestDTO nestedRequestDTO) {

        try {
            nestedService.saveNestedReply(id, nestedRequestDTO);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/reply/{replyId}/rereply")
    public ResponseEntity<?> findAllNestedReplys(@PathVariable(name = "replyId") long id) {

        NestedReply nestedReply = nestedService.findById(id);

        List<NestedResponseDTO> nestedReplys = nestedService.findAll()
                        .stream()
                        .map(NestedResponseDTO::new)
                        .toList();

        return ResponseEntity.ok().body(nestedReplys);
    }

    @GetMapping("/reply/{replyId}/rereply/{id}")
    public ResponseEntity<NestedResponseDTO> findNestedReply(@PathVariable(name = "id") long id) {
        NestedReply nestedReply = nestedService.findById(id);

        return ResponseEntity.ok()
                .body(new NestedResponseDTO(nestedReply));
    }

    @PatchMapping("/reply/{replyId}/rereply/{id}")
    public ResponseEntity<Long> deleteNestedReply(@PathVariable (name = "id") long id) {

       NestedReply deleteNestedReply =  nestedService.deleteInvisible(id);

        return ResponseEntity.ok().body(deleteNestedReply.getId());
    }

    @PutMapping("/reply/{replyId}/rereply/{id}")
    public ResponseEntity<?> update(@PathVariable (name = "replyId") long replyId,
                                       @PathVariable (name = "id") long id,
                                       @RequestBody NestedUpdateDTO request) {
        nestedService.update(replyId, id, request);
        return ResponseEntity.ok(id);
    }


}
