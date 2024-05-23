import React, { useEffect, useState } from "react";
import NestedComment from "./NestedComment";
import handleJWT from "../paserJWT";
import fetcher from "../fetcher";
import {
  REPLY_DELETE,
  REPLY_UPDATE,
  RE_REPLY_POST,
  RE_REPLY_READ_ALL,
} from "../constants/api_constants";

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
  const [rereply, setRereply] = useState<string>("");
  const [nestedReplies, setNestedReplies] = useState<NestedReply[]>(
    reply.nestReply
  ); // 대댓글 상태 변수
  const [replyState, setReplyState] = useState<Reply>(reply);
  const handleModify = () => {
    setModify(true);
  };
  const handleDelete = async () => {
    if (confirm("삭제하시겠습니까?")) {
      try {
        await fetcher.patch(`${REPLY_DELETE}${reply.id}`);
        setReplyState({
          id: reply.id,
          comment: reply.comment,
          nickname: reply.nickname,
          regDate: reply.regDate,
          updateDate: reply.updateDate,
          nestReply: reply.nestReply,
          invisible: true,
        });
      } catch (error) {
        console.log(error);
      }
    } else {
      setModify(false);
    }
  };

  const handleModifyConfirm = async () => {
    const formData = {
      boardId: reply.id,
      comment: comment,
    };
    try {
      const response = await fetcher.put(
        `${REPLY_UPDATE}${reply.id}`,
        formData
      );
      alert("수정 완료");
      reply.comment = comment; // 상태 업데이트
      setModify(false); // 수정 모드 종료
    } catch (error) {
      console.log(error);
    }
  };

  const handleCancel = () => {
    setModify(false);
    setComment(reply.comment); // 취소 시 원래 댓글로 되돌림
  };

  const handleReReply = async () => {
    const formData = {
      comment: rereply,
      replyId: reply.id,
      invisible: false,
    };
    try {
      const response = await fetcher.post(
        `${RE_REPLY_POST}${reply.id}`,
        formData
      );
      setNestedReplies((prevNestedReplies) => [
        response.data,
        ...prevNestedReplies,
      ]); // 새로운 댓글을 추가
    } catch (error) {}
  };

  const fetchNestedReplies = async (replyId: number) => {
    try {
      const response = await fetcher.get(`${RE_REPLY_READ_ALL}${replyId}`);
      setNestedReplies(response.data);
      setRereply("");
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const toggleAndFetchReplies = (id: number) => {
    if (!openReplies[id]) {
      fetchNestedReplies(id);
    }
    toggleReplies(id);
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
              {replyState.nickname}
              <small className="text-muted" style={{ fontSize: "15px" }}>
                {replyState.invisible ? `(삭제됨)` : <></>}
              </small>
            </h5>
          ) : (
            <h5 className="card-title mb-0">{replyState.nickname}</h5>
          )}
          <small className="text-muted">
            {replyState.updateDate == null
              ? replyState.regDate
              : `${replyState.updateDate} (수정됨)`}
          </small>
        </div>
        <div className="row">
          {replyState.invisible ? (
            <p className="text-muted">삭제된 댓글입니다.</p>
          ) : modify ? (
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
            <div className="col-9">{replyState.comment}</div>
          )}
          {}
        </div>
        <div className="d-flex justify-content-between mt-2">
          <button
            className="btn btn-link p-0"
            onClick={() => toggleAndFetchReplies(replyState.id)}
          >
            {openReplies[replyState.id] ? "댓글 숨기기" : "댓글 더보기"}
          </button>
          <div className="d-flex justify-content-start">
            {isAdmin || isUser ? (
              <>
                <button
                  className="btn btn-danger btn-sm"
                  onClick={handleDelete}
                >
                  삭제
                </button>
              </>
            ) : modify ? (
              <>
                <button
                  className="btn btn-primary btn-sm me-2"
                  onClick={handleModifyConfirm}
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
            ) : replyState.invisible ? (
              <></>
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
        {openReplies[replyState.id] && (
          <div className="mt-3">
            {nestedReplies &&
              nestedReplies.map((nestedReply) => (
                <NestedComment
                  key={nestedReply.id}
                  nestedReply={nestedReply}
                  isAdmin={isAdmin}
                  isUser={isUser}
                />
              ))}
            {!isAdmin && (
              <div className="row mt-3">
                <div className="col-12 ps-4">
                  <div className="input-group">
                    <span className="input-group-text">대댓글</span>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="댓글을 작성하세요..."
                      value={rereply}
                      onChange={(e) => setRereply(e.target.value)}
                    />
                    <button
                      className="btn btn-primary"
                      style={{ backgroundColor: "#348f8f" }}
                      onClick={handleReReply}
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
