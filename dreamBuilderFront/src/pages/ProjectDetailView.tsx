import React, { useEffect, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import ReactMarkdown from "react-markdown";
import styled from "styled-components";
import CommentSection from "../components/CommentSection";
import { useLocation, useNavigate } from "react-router-dom";
import fetcher from "../fetcher";
import {
  BOARD_DEATIL_VIEW,
  BOARD_DELETE,
  REPLY_READ_ALL,
} from "../constants/api_constants";
import VIDEO from "../image/video.png";
import DOCS from "../image/docs.png";
import { PROJECT_DETAIL_VIEW, PROJECT_REG } from "../constants/page_constants";
import InfiniteScroll from "react-infinite-scroll-component";

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
  invisible: boolean;
}

interface NestedReply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string | null;
  invisible: boolean;
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
  const navigator = useNavigate();
  const [extension] = useState<Extension>({
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
  console.log("boardId: ", boardId); // boardId 확인

  const [board, setBoard] = useState<Board | null>(null);
  const [reply, setReply] = useState<Reply[]>([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  const handleModify = () => {
    navigator(PROJECT_REG, { state: { boardId: boardId, modify: true } });
  };

  const handleDelete = async () => {
    if (confirm("삭제하시겠습니까?")) {
      try {
        await fetcher.delete(BOARD_DELETE + boardId);
        alert("삭제 성공");
        navigator(PROJECT_DETAIL_VIEW); // 삭제 후 프로젝트 목록으로 이동
      } catch (error) {
        alert("삭제 실패");
      }
    }
  };

  useEffect(() => {
    handleBoardData();
    fetchMoreData();
  }, []);

  const handleBoardData = async () => {
    try {
      const response = await fetcher.get(BOARD_DEATIL_VIEW + boardId);
      const data = response.data;

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

  const fetchMoreData = async () => {
    try {
      console.log("호출"); // 함수 호출 확인
      console.log(`${REPLY_READ_ALL}${boardId}/${page}`); // 요청 URL 확인
      const response = await fetcher.get(`${REPLY_READ_ALL}${boardId}/${page}`);
      const data = response.data;
      console.log(data);
      setReply((prevReplies) => [...prevReplies, ...data]);

      if (data.length === 5) {
        // 데이터 길이가 정확히 5인지 확인
        setPage((prevPage) => prevPage + 1);
        setHasMore(true);
      } else {
        setHasMore(false);
      }
    } catch (error) {
      console.log(error);
      setHasMore(false);
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
            <div className="d-flex justify-content-end mt-2">
              <div>
                <button
                  className="btn btn-primary me-2"
                  style={{
                    backgroundColor: " #348f8f",
                    border: "none",
                    color: "white",
                  }}
                  onClick={handleModify}
                >
                  수정
                </button>
                <button
                  className="btn btn-primary"
                  style={{
                    backgroundColor: " #348f8f",
                    border: "none",
                    color: "white",
                  }}
                  onClick={handleDelete}
                >
                  삭제
                </button>
              </div>
            </div>
            <div className="mt-3">
              {board.file.length === 0 ? (
                <p>첨부 파일 없음</p>
              ) : (
                <h6>첨부 파일:</h6>
              )}

              <ul className="list-group">
                {board.file.map((file, index) => (
                  <li key={`${file.name}-${index}`} className="list-group-item">
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
      <InfiniteScroll
        dataLength={reply.length}
        next={fetchMoreData}
        hasMore={hasMore}
        loader={<h4>Loading...</h4>}
      >
        <CommentSection
          key={boardId}
          replies={reply}
          boardId={boardId}
          setReplies={setReply}
        />
      </InfiniteScroll>
      <Footer />
    </>
  );
};

export default ProjectDetailView;
