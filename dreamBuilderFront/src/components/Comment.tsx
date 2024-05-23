import React, { useEffect, useState } from "react";
import NestedComment from "./NestedComment";
import handleJWT from "../paserJWT";
import fetcher from "../fetcher";
import { REPLY_UPDATE } from "../constants/api_constants";

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

interface CommentProps {
  reply: Reply;
  openReplies: { [key: number]: boolean };
  boardId: number;
  toggleReplies: (id: number) => void;
}

const Comment: React.FC<CommentProps> = ({
  reply,
  openReplies,
  boardId,
  toggleReplies,
}) => {
  const [isAdmin, setAdmin] = useState<boolean>(false);
  const [isUser, setUser] = useState<boolean>(false);
  const [modify, setModify] = useState<boolean>(false);
  const [comment, setComment] = useState<string>(reply.comment);
  const handleModify = () => {
    setModify(true);
  };
  const handleDelete = () => {};

  const handleModifyConfrim = async () => {
    const formdata = {
      boardId: boardId,
      comment: comment,
    };
    try {
      const response = await fetcher.put(
        `${REPLY_UPDATE}${reply.id}`,
        formdata
      );
      alert("수정완료");
    } catch (error) {
      console.log(error);
    }
  };

  const handleCancel = () => {
    setModify(false);
  };
  useEffect(() => {
    const jwt = localStorage.getItem("accessToken");
    if (jwt !== null) {
      const loginData = handleJWT(jwt);
      if (loginData.name === reply.nickname) {
        setUser(true);
        if (loginData.auth === "ROLE_ADMIN") {
          setAdmin(true);
        }
      }
    }
    setAdmin(false);
    setUser(false);
  }, []);
  return (
    <div className="card mb-3">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-2">
          {isAdmin ? (
            <h5 className="card-title mb-0">
              {reply.nickname}
              <small className="text-muted" style={{ fontSize: "15px" }}>
                {reply.invisible ? `(삭제됨)` : <></>}
              </small>
            </h5>
          ) : (
            <h5 className="card-title mb-0">{reply.nickname}</h5>
          )}
          <small className="text-muted">
            {reply.updateDate == null
              ? reply.regDate
              : `${reply.updateDate} (수정됨)`}
          </small>
        </div>
        <div className="row">
          {modify ? (
            <input
              type="text"
              className="form-control"
              placeholder="댓글을 작성하세요..."
              value={comment}
              onChange={(e) => {
                setComment(e.target.value);
              }}
            />
          ) : (
            <div className="col-9">{reply.comment}</div>
          )}
        </div>
        <div className="d-flex justify-content-between mt-2">
          <button
            className="btn btn-link p-0"
            onClick={() => toggleReplies(reply.id)}
          >
            {openReplies[reply.id] ? "댓글 숨기기" : "댓글 더보기"}
          </button>
          <div className="d-flex justify-content-start">
            {isAdmin || isUser ? (
              <></>
            ) : modify ? (
              <>
                <button
                  className="btn btn-primary btn-sm me-2"
                  onClick={handleModifyConfrim}
                >
                  확인
                </button>
                <button
                  className="btn btn-danger btn-sm"
                  onClick={handleCancel}
                >
                  취소
                </button>
              </>
            ) : (
              <>
                <button
                  className="btn btn-primary btn-sm me-2"
                  onClick={handleModify}
                >
                  수정
                </button>
                <button
                  className="btn btn-danger btn-sm"
                  onClick={handleDelete}
                >
                  삭제
                </button>
              </>
            )}
          </div>
        </div>
        {openReplies[reply.id] && reply.nestReply && (
          <div className="mt-3">
            {reply.nestReply.map((nestedReply) => (
              <NestedComment key={nestedReply.id} nestedReply={nestedReply} />
            ))}
            {isAdmin ? (
              <></>
            ) : (
              <div className="row mt-3">
                <div className="col-12 ps-4">
                  <div className="input-group">
                    <span className="input-group-text">대댓글</span>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="댓글을 작성하세요..."
                    />
                    <button
                      className="btn btn-primary"
                      style={{ backgroundColor: "#348f8f" }}
                    >
                      작성
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Comment;
