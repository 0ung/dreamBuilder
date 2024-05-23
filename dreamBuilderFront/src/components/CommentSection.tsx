import React, { useState } from "react";
import fetcher from "../fetcher";
import { REPLY_POST } from "../constants/api_constants";
import Comment from "./Comment";

interface NestedReply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string | null;
  invisible: boolean;
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

interface CommentSectionProps {
  replies: Reply[] | null;
  boardId: number;
  setReplies: React.Dispatch<React.SetStateAction<Reply[]>>;
}

const CommentSection: React.FC<CommentSectionProps> = ({
  replies,
  boardId,
  setReplies,
}) => {
  const [comment, setComment] = useState<string>("");
  const [isAdmin, setAdmin] = useState(false);

  const handleReply = async () => {
    const data = {
      comment: comment,
      boardId: boardId,
    };
    try {
      const response = await fetcher.post(REPLY_POST, JSON.stringify(data), {
        headers: {
          "Content-Type": "application/json",
        },
      });
      const newReply = response.data;
      console.log(newReply);
      setReplies((prevReplies) => [newReply, ...prevReplies]); // 새로운 댓글을 추가
      setComment(""); // 댓글 작성 후 입력 필드 초기화
    } catch (error) {
      alert("에러임");
    }
  };

  const [openReplies, setOpenReplies] = useState<{ [key: number]: boolean }>(
    {}
  );

  const toggleReplies = (id: number) => {
    setOpenReplies((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

  return (
    <div className="container mb-5">
      {isAdmin ? (
        <>
          <h1 className="mt-5">댓글 관리 </h1>
          <hr />
        </>
      ) : (
        <>
          <h1>댓글창</h1>
          <hr />
          <div className="input-group mb-3">
            <span className="input-group-text" id="basic-addon1">
              댓글
            </span>
            <input
              type="text"
              className="form-control"
              placeholder="댓글을 작성하세요..."
              aria-describedby="basic-addon1"
              value={comment}
              onChange={(e) => {
                setComment(e.target.value);
              }}
            />
            <button
              className="btn btn-primary"
              style={{ backgroundColor: "#348f8f" }}
              onClick={handleReply}
            >
              작성
            </button>
          </div>
        </>
      )}
      {(replies || []).map((reply, index) => (
        <Comment
          key={`${reply.id}-${index}`} // 고유한 key 보장
          reply={reply}
          openReplies={openReplies}
          toggleReplies={toggleReplies}
        />
      ))}
    </div>
  );
};

export default CommentSection;
