import React, { useRef, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import MDEditor, { ICommand, ContextStore } from "@uiw/react-md-editor";
import DropZone from "../componets/DropZone";
import fetcher from "../fetcher";
import { BOARD_REGISTRATION } from "../constants/api_constants";

export default function ProjectRegPage() {
  const inputRef = useRef<HTMLInputElement>(null);
  const [filteredFiles, setFilteredFiles] = useState<File[]>([]);

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

  interface Board {
    title: string;
    content: string;
    endDate: string;
    hashTags: string[];
  }

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

  return (
    <>
      <Header />
      <div className="container mt-5">
        <div>
          <h1 className="">프로젝트 생성</h1>
          <hr></hr>
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
        <MDEditor
          className="mb-3"
          value={board.content}
          height={"600px"}
          onChange={handleMarkdownChange}
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
        </div>
        <DropZone
          filteredFiles={filteredFiles}
          setFilteredFiles={setFilteredFiles}
        ></DropZone>

        <button onClick={handleBoard}>제출</button>
      </div>
      <Footer />
    </>
  );
}
