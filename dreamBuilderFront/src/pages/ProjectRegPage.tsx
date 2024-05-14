import React, { useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import MDEditor, { ICommand, ContextStore } from "@uiw/react-md-editor";
import Dropzone from "react-dropzone";
import DropZone from "../componets/DropZone";

export default function ProjectRegPage() {
  const [board, setBoard] = useState({
    title: "",
    content: "",
    endDate: "",
    hashTag: [],
  });

  const [hashTags, setHashTags] = useState("");
  const handleMarkdownChange = (
    value?: string,
    event?: React.ChangeEvent<HTMLTextAreaElement>,
    state?: ContextStore
  ) => {
    setBoard((prevBoard) => ({
      ...prevBoard,
      content: value || "",
    }));
    console.log(board);
  };
  const handleHashTag = () => {
    hashTags;
  };
  return (
    <>
      <Header />
      <div className="container mt-5">
        <div className="input-group mb-3">
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
            type="text"
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
            type="text"
            className="form-control"
            placeholder="해시태그 입력 (#을 달지 않고 작성하세요)"
            aria-describedby="basic-addon1"
            onChange={(e) => {
              setHashTags(e.target.value);
            }}
          />
        </div>
        <DropZone></DropZone>
      </div>
      <Footer />
    </>
  );
}
