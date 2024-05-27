package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.dto.Board.FileDTO;
import codehows.dream.dreambulider.entity.BoardFile;
import codehows.dream.dreambulider.entity.FileManage;
import codehows.dream.dreambulider.repository.BoardFileRepository;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.FileManageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BoardFileService {

	private final BoardFileRepository boardFileRepository;
	private final BoardRepository boardRepository;
	private final FileManageRepository fileManageRepository;

	@Value("${temp.savePath}")
	private String tempSavePath;

	@Value("${savePath}")
	private String savePath;

	public void saveFiles(List<MultipartFile> files, Long boardId) throws IOException {
		if (files == null || files.isEmpty()) {
			return;
		}
		for (MultipartFile file : files) {
			saveFile(file, boardId);
		}
	}

	public void saveFile(MultipartFile file, Long boardId) throws IOException {
		String oriName = file.getOriginalFilename();
		String fileExtension = oriName.substring(oriName.lastIndexOf(".") + 1);
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String fileName = uuid + "." + fileExtension;
		String fileUrl = "/" + "files" + "/" + fileName;

		// Save file to disk
		saveFileToDisk(fileName, file, savePath);

		// Save file information to the database
		BoardFile boardFile = BoardFile.builder()
			.oriName(oriName)
			.url(fileUrl)
			.uuidName(fileName)
			.board(boardRepository.findById(boardId).orElse(null))
			.build();
		log.info(boardFile);
		boardFileRepository.save(boardFile);
	}

	public String saveTempFile(MultipartFile file) throws IOException {
		String oriName = file.getOriginalFilename();
		String fileExtension = oriName.substring(oriName.lastIndexOf(".") + 1);
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String fileName = uuid + "." + fileExtension;
		String fileUrl = "/" + "temp" + "/" + fileName;

		// Save file to disk
		saveFileToDisk(fileName, file, tempSavePath);

		// Save file information to the database
		BoardFile boardFile = BoardFile.builder()
			.oriName(oriName)
			.url(fileUrl)
			.uuidName(fileName)
			.build();
		boardFileRepository.save(boardFile);
		return fileUrl;
	}

	private void saveFileToDisk(String fileName, MultipartFile file, String path) throws IOException {
		// 절대 경로로 변환
		Path savePath = Paths.get(path);
		log.info(savePath);
		File directory = savePath.toFile();
		log.info(directory);
		// Create directory if it does not exist
		if (!directory.exists()) {
			directory.mkdirs();
		}

		Path filePath = savePath.resolve(fileName);
		File newFile = filePath.toFile();
		FileCopyUtils.copy(file.getBytes(), newFile);
	}

	public void moveFileToPermanentLocation(String tempFileUrl) throws IOException {
		// tempFileUrl에서 "/temp/"를 제거하여 파일명을 추출
		String fileName = tempFileUrl.replace("/temp/", "");

		// 임시 파일 경로 설정
		Path tempPath = Paths.get(this.tempSavePath, fileName);

		// 영구 저장 파일 경로 설정
		Path permanentPath = Paths.get(this.savePath, fileName);

		// 영구 저장 경로의 디렉토리 생성
		Files.createDirectories(permanentPath.getParent());

		// 파일 이동
		Files.move(tempPath, permanentPath);
	}


	public List<Map<String, String>> findBoardUrl(Long boardId) {
		List<BoardFile> boardFiles = boardFileRepository.findByBoardId(boardId);
		List<Map<String, String>> result = new ArrayList<>();

		boardFiles.forEach(e -> {
			Map<String, String> map = new HashMap<>();
			map.put(e.getOriName(), e.getUrl());
			result.add(map);
		});
		return result;
	}

	private void deleteFile(String fileName) throws IOException {
		File file = new File(savePath + File.separator + fileName);
		if (file.exists()) {
			file.delete();
		} else {
			throw new IOException("File not found: " + fileName);
		}
	}

	public void updateFile(Long boardId, List<MultipartFile> multipartFile) throws IOException {
		List<BoardFile> list = boardFileRepository.findByBoardId(boardId);
		if (multipartFile == null || multipartFile.isEmpty()) {
			boardFileRepository.deleteAll(list);
			return;
		}
		list.forEach(e -> {
			try {
				deleteFile(e.getUuidName());
				boardFileRepository.delete(e);
			} catch (IOException ex) {
				throw new RuntimeException("Error deleting file: " + e.getUuidName(), ex);
			}
		});
		saveFiles(multipartFile, boardId);
	}

	//관리자 파일 설정
	public void fileAdmin(FileDTO dto) {
		Optional<FileManage> file = fileManageRepository.findById(1L);

		Integer uploadSize = convertToBytes(dto.getUploadSize());	//byte로 변환
		//파일 설정을 처음 한다면 새로 추가
		if (file.isEmpty()) {
			fileManageRepository.save(FileManage.builder()
				.uploadSize(uploadSize)
				.uploadNum(dto.getUploadNum())
				.docExtension(dto.getDocExtension())
				.imageExtension(dto.getImageExtension())
				.videoExtension(dto.getVideoExtension())
				.build());
		} else { //이미 설정을 한번 했다면 수정
			FileManage fileUpdate = fileManageRepository.findById(1L)
				.orElseThrow(() -> new RuntimeException("File Not Found"));
			fileUpdate.update(uploadSize, dto.getUploadNum(), dto.getDocExtension(), dto.getImageExtension(), dto.getVideoExtension());
			fileManageRepository.save(fileUpdate);
		}
	}

	//byte 코드로 변환
	public static Integer convertToBytes(String size) {
		// 정규식 패턴을 컴파일하여 Pattern 객체 생성
		Pattern pattern = Pattern.compile("(\\d+)([a-zA-Z]+)");
		// 입력 문자열을 정규식 패턴에 매칭하는 Matcher 객체 생성
		Matcher matcher = pattern.matcher(size);

		if (matcher.matches()) {
			// 숫자 부분
			Integer value = Integer.parseInt(matcher.group(1));
			// 단위 부분
			String unit = matcher.group(2).toUpperCase();

			// 단위에 따른 바이트 변환
			switch (unit) {
				case "B":
					return value;
				case "KB":
					return value * 1024;
				case "MB":
					return value * 1024 * 1024;
				case "GB":
					return value * 1024 * 1024 * 1024;
				default:
					throw new IllegalArgumentException("Invalid size unit");
			}
		} else {
			throw new IllegalArgumentException("Invalid size format");
		}
	}

	//관리자 파일 불러오기
	public FileManage fileResponse() {
		return fileManageRepository.findById(1L)	//파일 설정이 없다면 기존 파일 설정
			.orElse(new FileManage());
	}
}
