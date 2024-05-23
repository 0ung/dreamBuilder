import React, { useState } from "react";
import { useSelector } from "react-redux";
import fetcher from "../fetcher";
import {
  RE_REPLY_DELETE,
  RE_REPLY_POST,
  RE_REPLY_UPDATE,
} from "../constants/api_constants";
interface NestedReply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  invisible: boolean;
  updateDate: string | null;
}

interface NestedCommentProps {
  nestedReply: NestedReply;
  isAdmin: boolean;
  isUser: boolean;
}

const NestedComment: React.FC<NestedCommentProps> = ({
  nestedReply,
  isAdmin,
  isUser,
}) => {
  const [modify, setModify] = useState<boolean>(false);
  const [comment, setComment] = useState<string>(nestedReply.comment);
  const [nestedReplyState, setNestedReplyState] =
    useState<NestedReply>(nestedReply); // 대댓글 상태 변수

  const handleDelete = async () => {
    if (confirm("삭제하시겠습니까?")) {
      await fetcher.patch(`${RE_REPLY_DELETE}${nestedReply.id}`);
      setNestedReplyState({
        id: nestedReplyState.id,
        comment: nestedReplyState.comment,
        nickname: nestedReplyState.nickname,
        regDate: nestedReplyState.regDate,
        invisible: true,
        updateDate: nestedReplyState.updateDate,
      });
    }
  };
  const handleUpdate = () => {
    setModify(true);
  };
  const handleUpdateConfrim = async () => {
    await fetcher.put(`${RE_REPLY_UPDATE}${nestedReply.id}`, {
      comment: comment,
    });
    nestedReply.comment = comment;
    setComment(comment);
    setModify(false);
  };
  const handleCancel = () => {
    setModify(false);
  };
  return (
    <div className="card mb-2 ms-3">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-2">
          <h6 className="card-title mb-0">{nestedReplyState.nickname}</h6>
          <small className="text-muted">
            {nestedReplyState.updateDate == null
              ? nestedReplyState.regDate
              : `${nestedReplyState.updateDate} (수정됨)`}
          </small>
        </div>
        {nestedReplyState.invisible ? (
          <p className="text-muted">삭제된 대댓글입니다.</p>
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
          <div className="row">
            <div className="col-9">{nestedReplyState.comment}</div>
          </div>
        )}

        <div className="d-flex justify-content-end mt-2">
          {isAdmin || isUser ? (
            <>
              <button className="btn btn-danger btn-sm" onClick={handleDelete}>
                삭제
              </button>
            </>
          ) : nestedReplyState.invisible ? (
            <></>
          ) : modify ? (
            <>
              <button
                className="btn btn-primary btn-sm me-2"
                onClick={handleUpdateConfrim}
              >
                확인
              </button>
              <button className="btn btn-danger btn-sm" onClick={handleCancel}>
                취소
              </button>
            </>
          ) : (
            <>
              <button
                className="btn btn-primary btn-sm me-2"
                onClick={handleUpdate}
              >
                수정
              </button>
              <button className="btn btn-danger btn-sm" onClick={handleDelete}>
                삭제
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default NestedComment;
