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

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NestedController {

	private final NestedService nestedService;

	@PostMapping("/rereply/{id}")
	public ResponseEntity<?> saveNestedReply(@PathVariable(name = "id") Long id,
		@RequestBody NestedRequestDTO nestedRequestDTO,
		Principal principal) {

		try {
			NestedReply reply = nestedService.saveNestedReply(id, nestedRequestDTO, principal.getName());
			NestedResponseDTO nestedResponseDTO = new NestedResponseDTO(reply);
			return new ResponseEntity<>(nestedResponseDTO, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/rereply/{replyId}")
	public ResponseEntity<?> findAllNestedReplys(@PathVariable(name = "replyId") Long replyId) {

		List<NestedResponseDTO> nestedReplys = nestedService.findAllByReplyId(replyId);

		return ResponseEntity.ok(nestedReplys);
	}

	@GetMapping("/rereply/{replyId}/{id}")
	public ResponseEntity<NestedResponseDTO> findNestedReply(@PathVariable(name = "id") long id) {
		NestedReply nestedReply = nestedService.findById(id);

		return ResponseEntity.ok()
			.body(new NestedResponseDTO(nestedReply));
	}

	@PatchMapping("/rereply/{id}")
	public ResponseEntity<Long> deleteNestedReply(@PathVariable(name = "id") long id) {
		NestedReply deleteNestedReply = nestedService.deleteInvisible(id);
		return ResponseEntity.ok().body(deleteNestedReply.getId());
	}

	@PutMapping("/rereply/{id}")
	public ResponseEntity<?> update(
		@PathVariable(name = "id") long id,
		@RequestBody NestedUpdateDTO request) {
		nestedService.update(id, request);
		return ResponseEntity.ok(id);
	}

}
