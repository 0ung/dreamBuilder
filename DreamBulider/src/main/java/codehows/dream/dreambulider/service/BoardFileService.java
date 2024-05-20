package codehows.dream.dreambulider.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import codehows.dream.dreambulider.entity.BoardFile;
import codehows.dream.dreambulider.repository.BoardFileRepository;
import codehows.dream.dreambulider.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardFileService {

	private final BoardFileRepository boardFileRepository;
	private final BoardRepository boardRepository;

	@Value("${uploadPath}")
	private String uploadPath;

	@Value("${temp.uploadPath}")
	private String tempPath;

	public void saveFiles(List<MultipartFile> files, Long boardId) throws IOException {
		if (files == null || files.isEmpty()) {
			throw new IOException("No files to upload");
		}
		for (MultipartFile file : files) {
			saveFile(file, boardId);
		}
	}

	public String saveFile(MultipartFile file, Long boardId) throws IOException {
		String oriName = file.getOriginalFilename();
		String fileExtension = oriName.substring(oriName.lastIndexOf(".") + 1);
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String fileName = uuid + "." + fileExtension;
		String fileUrl = "/" + "files" + "/" + fileName;

		// Save file to disk
		saveFileToDisk(fileName, file, uploadPath);

		// Save file information to the database
		BoardFile boardFile = BoardFile.builder()
			.oriName(oriName)
			.url(fileUrl)
			.uuidName(fileName)
			.board(boardRepository.findById(boardId).orElse(null))
			.build();
		boardFileRepository.save(boardFile);
		return fileUrl;
	}

	public String saveTempFile(MultipartFile file) throws IOException {
		String oriName = file.getOriginalFilename();
		String fileExtension = oriName.substring(oriName.lastIndexOf(".") + 1);
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String fileName = uuid + "." + fileExtension;
		String fileUrl = "/" + "temp" + "/" + fileName;

		// Save file to disk
		saveFileToDisk(fileName, file, tempPath);

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
		String savePath = path.replace("file:///", "");
		File directory = new File(savePath);

		// Create directory if it does not exist
		if (!directory.exists()) {
			directory.mkdirs();
		}

		String filePath = savePath + File.separator + fileName;
		File newFile = new File(filePath);
		FileCopyUtils.copy(file.getBytes(), newFile);
	}

	public void moveFileToPermanentLocation(String tempFileUrl) throws IOException {
		Path tempPath = Paths.get(this.tempPath.replace("file:///", ""), tempFileUrl.replace("/temp/", ""));
		Path permanentPath = Paths.get(this.uploadPath.replace("file:///", ""), tempFileUrl.replace("/temp/", ""));

		Files.createDirectories(permanentPath.getParent());
		Files.move(tempPath, permanentPath);
	}

	public List<Map<String,String >> findBoardUrl(Long boardId){
		List<BoardFile> boardFiles = boardFileRepository.findByBoardId(boardId);
		List<Map<String,String >> result = new ArrayList<>();

		boardFiles.forEach(e->{
			Map<String,String> map = new HashMap<>();
			map.put(e.getOriName(),e.getUrl());
			result.add(map);
		});
		return result;
	}
}
