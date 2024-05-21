import React, { useRef, useState, useCallback, useEffect } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom"; // useParams 추가
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import MDEditor from "@uiw/react-md-editor";
import { useDropzone } from "react-dropzone";
import fetcher from "../fetcher";
import DropZone from "../components/DropZone"; // 경로 확인 필요
import {
  BOARD_REGISTRATION,
  BOARD_DEATIL_VIEW,
  BOARD_UPDATE,
} from "../constants/api_constants"; // BOARD_DETAILS 추가
import { PROJECT_OVERVIEW } from "../constants/page_constants";

interface FileData {
  oriName: string;
  url: string;
}

interface Board {
  title: string;
  content: string;
  endDate: string;
  hashTags: string[];
}

interface MarkdownWithDropzoneProps {
  board: Board;
  handleMarkdownChange: (content: string) => void;
}

const MarkdownWithDropzone: React.FC<MarkdownWithDropzoneProps> = ({
  board,
  handleMarkdownChange,
}) => {
  const onDrop = useCallback(
    (acceptedFiles: File[]) => {
      acceptedFiles.forEach((file) => {
        const reader = new FileReader();
        reader.onload = () => {
          uploadImage(file).then((url) => {
            handleMarkdownChange(`${board.content}\n![image](${url})`);
          });
        };
        reader.readAsArrayBuffer(file);
      });
    },
    [board.content, handleMarkdownChange]
  );

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    noClick: true, // 클릭을 막음
  });

  const uploadImage = async (file: File): Promise<string> => {
    const formData = new FormData();
    formData.append("file", file);
    try {
      const response = await fetcher.post("/api/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      return `http://localhost:8080${response.data.fileUrl}`; // 업로드된 이미지 URL 반환
    } catch (error) {
      console.error("이미지 업로드 오류:", error);
      return "";
    }
  };

  return (
    <div className="editor-container" {...getRootProps()}>
      <input {...getInputProps()} />
      {isDragActive && <div className="overlay">이미지를 드롭하세요...</div>}
      <MDEditor
        className="mb-3"
        value={board.content}
        height={600}
        onChange={(value) => handleMarkdownChange(value || "")}
      />
    </div>
  );
};

export default function ProjectRegPage() {
  const { id } = useParams<{ id: string }>(); // URL 파라미터에서 ID를 가져옴
  const inputRef = useRef<HTMLInputElement>(null);
  const [filteredFiles, setFilteredFiles] = useState<File[]>([]);
  const [modify, setModify] = useState<boolean>(!!id); // ID가 있으면 수정 모드
  const [board, setBoard] = useState<Board>({
    title: "",
    content: "",
    endDate: "",
    hashTags: [],
  });
  const location = useLocation();
  const navigate = useNavigate();
  useEffect(() => {
    setModify(location.state?.modify);

    const loadFile = async (fileUrl: string, oriName: string) => {
      const response = await fetch(fileUrl);
      const blob = await response.blob();
      return new File([blob], oriName, { type: blob.type });
    };

    const fetchFiles = async (files: { oriName: string; url: string }[]) => {
      const filePromises = files.map((file) =>
        loadFile(`http://localhost:8080${file.url}`, file.oriName)
      );
      const fileObjects = await Promise.all(filePromises);
      setFilteredFiles(fileObjects);
    };

    if (modify) {
      // 수정 모드일 때 기존 데이터 로드
      fetcher
        .get(`${BOARD_DEATIL_VIEW}${location.state?.boardId}`)
        .then((response) => {
          const existingBoard = response.data;
          console.log(existingBoard);
          setBoard({
            title: existingBoard.title,
            content: existingBoard.content,
            endDate: existingBoard.endDate,
            hashTags: existingBoard.hashTags,
          });

          // 파일 데이터를 URL에서 File 객체로 변환하여 설정
          const fileData: { oriName: string; url: string }[] =
            existingBoard.file.flatMap((file: any) =>
              Object.entries(file).map(([oriName, url]) => ({
                oriName,
                url,
              }))
            );

          fetchFiles(fileData);
        })
        .catch((error) => {
          console.error("기존 데이터 로드 오류:", error);
        });
    }
  }, [modify, id]);

  const handleMarkdownChange = (value?: string) => {
    setBoard((prevBoard) => ({
      ...prevBoard,
      content: value || "",
    }));
  };

  const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === " " || event.key === "Enter") {
      const data = event.currentTarget.value.trim();
      setBoard((prevBoard) => ({
        ...prevBoard,
        hashTags: [...prevBoard.hashTags, data],
      }));
      if (inputRef.current) {
        inputRef.current.value = "";
      }
      event.preventDefault();
    }
  };

  const handleRemoveHashTag = (index: number) => {
    setBoard((prevBoard) => ({
      ...prevBoard,
      hashTags: prevBoard.hashTags.filter((_, i) => i !== index),
    }));
  };

  const handleBoard = async () => {
    const sendData = new FormData();
    sendData.append(
      "board",
      new Blob([JSON.stringify(board)], { type: "application/json" })
    );

    // Append files if they exist
    filteredFiles?.forEach((file) => {
      sendData.append("files", file);
    });

    const url = modify
      ? `${BOARD_UPDATE}${location.state?.boardId}`
      : BOARD_REGISTRATION;

    try {
      if (modify) {
        await fetcher.put(url, sendData);
      } else {
        await fetcher.post(url, sendData);
      }
      console.log(url);
      alert(modify ? "업데이트 성공" : "등록 성공");
      navigate(PROJECT_OVERVIEW);
    } catch (error) {
      console.error("Error:", error);
      alert(
        "An error occurred while processing your request. Please try again."
      );
    }
  };

  return (
    <>
      <Header />
      <div className="container mt-5 mb-5">
        <div>
          <h1 className="">프로젝트 {modify ? "수정" : "등록"}</h1>
          <hr />
        </div>
        <div className="input-group mt-5 mb-3">
          <span className="input-group-text" id="basic-addon1">
            제목
          </span>
          <input
            required
            type="text"
            className="form-control"
            placeholder="제목 입력"
            aria-describedby="basic-addon1"
            value={board.title}
            onChange={(e) => {
              setBoard((prevBoard) => ({
                ...prevBoard,
                title: e.target.value || "",
              }));
            }}
          />
        </div>
        <MarkdownWithDropzone
          board={board}
          handleMarkdownChange={handleMarkdownChange}
        />
        <div className="input-group mb-3">
          <span className="input-group-text" id="basic-addon1">
            마김일
          </span>
          <input
            required
            type="date"
            className="form-control"
            placeholder="마김일 등록"
            aria-describedby="basic-addon1"
            value={board.endDate}
            onChange={(e) => {
              setBoard((prevBoard) => ({
                ...prevBoard,
                endDate: e.target.value || "",
              }));
            }}
          />
        </div>
        <div className="input-group mb-3">
          <span className="input-group-text" id="basic-addon1">
            해시태그
          </span>
          <input
            required
            ref={inputRef}
            type="text"
            className="form-control"
            placeholder="해시태그 입력 (#을 달지 않고 작성하세요)"
            aria-describedby="basic-addon1"
            onKeyDown={handleKeyPress}
          />
        </div>
        <div>
          {board.hashTags.map((tag, index) => (
            <span key={index} className="badge bg-primary me-2">
              {tag}
              <button
                type="button"
                className="btn-close btn-close-white ms-2"
                aria-label="Close"
                onClick={() => handleRemoveHashTag(index)}
              ></button>
            </span>
          ))}
        </div>
        <DropZone
          filteredFiles={filteredFiles}
          setFilteredFiles={setFilteredFiles}
        />
        <div className="d-flex justify-content-end">
          <button
            className="btn btn-primary"
            style={{
              backgroundColor: " #348f8f",
              border: "none",
              color: "white",
            }}
            onClick={handleBoard}
          >
            제출
          </button>
        </div>
      </div>
      <Footer />
    </>
  );
}
