import React, { useRef, useState, useCallback } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import MDEditor from "@uiw/react-md-editor";
import { useDropzone } from "react-dropzone";
import axios from "axios";
import DropZone from "../components/DropZone"; // 경로 확인 필요
import fetcher from "../fetcher";
import { BOARD_REGISTRATION } from "../constants/api_constants";

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
  const inputRef = useRef<HTMLInputElement>(null);
  const [filteredFiles, setFilteredFiles] = useState<File[]>([]);

  const [board, setBoard] = useState<Board>({
    title: "",
    content: "",
    endDate: "",
    hashTags: [],
  });

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

    // 보드 데이터를 추가
    // board 객체를 JSON 문자열로 변환하여 추가
    sendData.append(
      "board",
      new Blob([JSON.stringify(board)], { type: "application/json" })
    );

    // 파일 배열을 추가
    filteredFiles.forEach((file) => {
      sendData.append("files", file);
    });
    console.log(sendData.get("board")?.toString);
    try {
      const response = await fetcher.post(BOARD_REGISTRATION, sendData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      alert(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <Header />
      <div className="container mt-5">
        <div>
          <h1 className="">프로젝트 생성</h1>
          <hr />
        </div>
        <div className="input-group mt-5 mb-3">
          <span className="input-group-text" id="basic-addon1">
            제목
          </span>
          <input
            type="text"
            className="form-control"
            placeholder="제목 입력"
            aria-describedby="basic-addon1"
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
            type="date"
            className="form-control"
            placeholder="마김일 등록"
            aria-describedby="basic-addon1"
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
        <button onClick={handleBoard}>제출</button>
      </div>
      <Footer />
    </>
  );
}
