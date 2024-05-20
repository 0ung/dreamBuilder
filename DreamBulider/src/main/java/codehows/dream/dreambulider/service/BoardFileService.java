package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.entity.BoardFile;
import codehows.dream.dreambulider.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardFileService {

    private final BoardFileRepository boardFileRepository;

    @Value("${uploadPath}")
    private String uploadPath;

    public void saveFiles(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IOException("No files to upload");
        }
        for (MultipartFile file : files) {
            saveFile(file);
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        String oriName = file.getOriginalFilename();
        String fileExtension = oriName.substring(oriName.lastIndexOf(".") + 1);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = uuid + "." + fileExtension;
        String fileUrl = "/" + "files" + "/" + fileName;

        // Save file to disk
        saveFileToDisk(fileName, file);

        // Save file information to the database
        BoardFile boardFile = BoardFile.builder()
                .oriName(oriName)
                .url(fileUrl)
                .uuidName(fileName)
                .build();
        boardFileRepository.save(boardFile);
        return fileUrl;
    }

    private void saveFileToDisk(String fileName, MultipartFile file) throws IOException {
        String savePath = uploadPath.replace("file:///", "");
        File directory = new File(savePath);

        // Create directory if it does not exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = savePath + File.separator + fileName;
        File newFile = new File(filePath);
        FileCopyUtils.copy(file.getBytes(), newFile);
    }
}
