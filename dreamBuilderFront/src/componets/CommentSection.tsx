import React, { useState } from "react";
import Comment from "./Comment";

interface Reply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string | null;
  nestReply: NestedReply[];
}

interface NestedReply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string;
}

interface CommentSectionProps {
  replies: Reply[];
}

const CommentSection: React.FC<CommentSectionProps> = ({ replies }) => {
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
        />
        <button
          className="btn btn-primary"
          style={{ backgroundColor: "#348f8f" }}
        >
          작성
        </button>
      </div>
      {replies.map((reply) => (
        <Comment
          key={reply.id}
          reply={reply}
          openReplies={openReplies}
          toggleReplies={toggleReplies}
        />
      ))}
    </div>
  );
};

export default CommentSection;
