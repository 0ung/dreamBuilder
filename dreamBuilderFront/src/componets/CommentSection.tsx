import React, { useState } from "react";
import Comment from "./Comment";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";

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
  updateDate: string | null;
  deactive: boolean;
}

interface CommentSectionProps {
  replies: Reply[];
}

const CommentSection: React.FC<CommentSectionProps> = ({ replies }) => {
  const isAdmin = useSelector((state: any) => state.admin.isAdmin);
  const dispatch = useDispatch();

  const [comment, setComment] = useState("");

  const handleReply = async () => {
    const data = {
      comment: comment,
    };
    try {
      const response = await axios.post(
        "http://localhost:8080/api/reply",
        JSON.stringify(data),
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      alert(response.status);
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
