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

import java.util.List;

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
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/reply/{replyId}/rereply")
    public List<NestedReply> read(@PathVariable (name = "replyId") long id) {
        return nestedService.findAll(id);
    }

    @GetMapping("/reply/{replyId}/rereply/{id}")
    public ResponseEntity<NestedResponseDTO> findNestedReply(@PathVariable(name = "id") long id) {
        NestedReply nestedReply = nestedService.findById(id);

        return ResponseEntity.ok()
                .body(new NestedResponseDTO(nestedReply));
    }

    @DeleteMapping("/reply/{replyId}/rereply/{id}")
    public ResponseEntity<Long> deleteNestedReply(@PathVariable(name = "replyId") long replyId, @PathVariable (name = "id") Long id) {
        nestedService.delete(replyId, id);

        return ResponseEntity.ok(id);
    }

    @PutMapping("/reply/{replyId}/rereply/{id}")
    public ResponseEntity<?> update(@PathVariable (name = "replyId") long replyId,
                                       @PathVariable (name = "id") long id,
                                       @RequestBody NestedUpdateDTO request) {
        nestedService.update(replyId, id, request);
        return ResponseEntity.ok(id);
    }


}
