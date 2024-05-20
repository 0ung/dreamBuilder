import React, { useEffect, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import ReactMarkdown from "react-markdown";
import styled from "styled-components";
import CommentSection from "../components/CommentSection";
import { useLocation } from "react-router-dom";
import fetcher from "../fetcher";
import { BOARD_DEATIL_VIEW } from "../constants/api_constants";
import VIDEO from "../image/video.png";
import DOCS from "../image/docs.png";

interface Board {
  id: number;
  title: string;
  content: string;
  endDate: string;
  cnt: number;
  hashTags: string[];
  file: FileData[];
}

interface FileData {
  name: string;
  url: string;
}

interface Reply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string | null;
  nestReply: NestedReply[];
  deactive: boolean;
}

interface NestedReply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string;
  deactive: boolean;
}

const Hr = styled.hr`
  border: none;
  border-top: 5px solid black;
`;

interface Extension {
  video: Set<string>;
  image: Set<string>;
  docs: Set<string>;
}
const ProjectDetailView: React.FC = () => {
  const [extension, setExtension] = useState<Extension>({
    video: new Set([
      "mp4",
      "avi",
      "mkv",
      "mov",
      "wmv",
      "flv",
      "webm",
      "mpeg",
      "mpg",
      "3gp",
    ]),
    image: new Set([
      "jpg",
      "jpeg",
      "png",
      "gif",
      "bmp",
      "tiff",
      "svg",
      "webp",
      "ico",
      "heic",
    ]),
    docs: new Set([
      "pdf",
      "docx",
      "xlsx",
      "pptx",
      "txt",
      "rtf",
      "odt",
      "ods",
      "odp",
      "csv",
      "hwp",
    ]),
  });
  const [hoveredFile, setHoveredFile] = useState<string | null>(null);
  const getFileType = (fileName: string): string => {
    const extensionMatch = fileName.split(".").pop()?.toLowerCase();
    if (!extensionMatch) return "unknown";

    if (extension.video.has(extensionMatch)) return "video";
    if (extension.image.has(extensionMatch)) return "image";
    if (extension.docs.has(extensionMatch)) return "docs";

    return "unknown";
  };

  const renderFileIcon = (
    fileType: string,
    fileUrl: string,
    fileName: string
  ) => {
    switch (fileType) {
      case "image":
        return (
          <>
            <img
              src={`http://localhost:8080${fileUrl}`}
              alt={fileName}
              style={{ width: "50px", marginRight: "10px" }}
            />
            <span>{fileName}</span>
          </>
        );
      case "video":
        return (
          <>
            <img
              src={VIDEO}
              alt="video"
              style={{ width: "50px", marginRight: "10px" }}
            />
            <span>{fileName}</span>
          </>
        );
      case "docs":
        return (
          <>
            <img
              src={DOCS}
              alt="docs"
              style={{ width: "50px", marginRight: "10px" }}
            />
            <span>{fileName}</span>
          </>
        );
      default:
        return (
          <>
            <img
              src={`http://localhost:8080${fileUrl}`}
              alt="default"
              style={{ width: "50px", marginRight: "10px" }}
            />
            <span>{fileName}</span>
          </>
        );
    }
  };

  const location = useLocation();
  const boardId = location.state;

  const [board, setBoard] = useState<Board | null>(null);
  const [reply, setReply] = useState<Reply[] | null>(null);

  useEffect(() => {
    handleBoardData();
  }, [boardId]);

  const handleBoardData = async () => {
    try {
      const response = await fetcher.get(BOARD_DEATIL_VIEW + boardId);
      const data = response.data;

      // 데이터 변환 로직
      const transformedBoard: Board = {
        id: data.id,
        title: data.title,
        content: data.content,
        endDate: data.endDate,
        cnt: data.cnt,
        hashTags: data.hashTags,
        file: data.file.flatMap((fileMap: Map<string, string>) =>
          Object.entries(fileMap).map(([name, url]) => ({
            name,
            url,
          }))
        ),
      };

      setBoard(transformedBoard);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <Header />
      <div className="container mb-5">
        {board && (
          <>
            <h1 className="mt-5">{board.title}</h1>
            <hr />
            <div className="mt-5 p-5 border rounded shadow">
              <ReactMarkdown
                components={{
                  img: ({ node, ...props }) => (
                    <div>
                      <img
                        {...props}
                        style={{ maxWidth: "50%", height: "auto" }}
                        alt={props.alt}
                      />
                    </div>
                  ),
                }}
              >
                {board.content}
              </ReactMarkdown>
            </div>
            <div className="mt-3">
              <h6>첨부 파일:</h6>
              <ul className="list-group">
                {board.file.map((file, index) => (
                  <li key={index} className="list-group-item">
                    <a href={file.url} download={file.name}>
                      <div>
                        {renderFileIcon(
                          getFileType(file.name),
                          file.url,
                          file.name
                        )}
                      </div>
                    </a>
                  </li>
                ))}
              </ul>
            </div>
          </>
        )}
      </div>

      <Hr />
      {<CommentSection replies={reply} />}
      <Footer />
    </>
  );
};

export default ProjectDetailView;
