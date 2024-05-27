package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashTagService {

	private final HashTagRepository hashTagRepository;
	private final BoardRepository boardRepository;

	//해시태그 저장
	@Transactional
	public HashTag saveHashTag(Long boardId, String hashTag) {

		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("not found : " + boardId));
		return hashTagRepository.save(HashTag.builder()
			.board(board)
			.hashTag(hashTag).build());
	}

	//해시태그 목록 조회
	public List<String> findAll(long boardId) {
		return hashTagRepository.findByBoardId(boardId);
	}

	//해시태그 상세 조회
	public List<String> findById(Long boardId) {
		//        Board board = boardRepository.findById(boardId)
		//                .orElseThrow(() -> new IllegalArgumentException("not found: " + boardId));

		return hashTagRepository.findByBoardId(boardId);
	}

	//해시태그 수정
	@Transactional
	public void updateHash(Long boardId, List<String> newHashTags) {
		//
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));

		List<HashTag> hashTags = hashTagRepository.findHashTagsByBoardId(boardId);

		//기존 해시태그 >= 수정된 해시태그
		if (hashTags.size() >= newHashTags.size()) {
			//기존 해시태그는 업데이트
			for (int i = 0; i < newHashTags.size(); i++) {
				String hashContent = newHashTags.get(i);
				hashTags.get(i).update(hashContent);
			}
			//남은 기존 해시태그는 삭제
			for (int i = newHashTags.size(); i < hashTags.size(); i++) {
				hashTagRepository.delete(hashTags.get(i));
			}
			//기존 해시태그 < 수정된 해시태그
		} else if (hashTags.size() < newHashTags.size()) {
			for (int i = 0; i < newHashTags.size(); i++) {
				String hashContent = newHashTags.get(i);
				if (i < hashTags.size()) {
					// 기존 해시태그가 남아있으면 업데이트
					hashTags.get(i).update(hashContent);
				} else {
					// 기존 해시태그가 부족하면 새로운 해시태그 추가
					HashTag newHashTag = new HashTag();
					newHashTag.setBoard(board);
					newHashTag.setHashTag(hashContent);
					hashTagRepository.save(newHashTag);
				}
			}
		}
	}
}

