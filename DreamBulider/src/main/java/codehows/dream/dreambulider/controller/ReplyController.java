package codehows.dream.dreambulider.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codehows.dream.dreambulider.dto.ReplyDTO.ReplyDeleteDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyRequestDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyResponseDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyUpdateDTO;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.service.ReplyService;
import lombok.RequiredArgsConstructor;

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
	public ResponseEntity<List<ReplyResponseDTO>> findAllReplys(@PathVariable Optional<Integer> page,
		@PathVariable(name = "boardId") Long boardId) {
		Pageable pageable = PageRequest.of(page.orElse(0), 5,
			Sort.by(Sort.Direction.DESC, "id"));
		List<ReplyResponseDTO> replys = replyService.findAll(boardId, pageable)
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
