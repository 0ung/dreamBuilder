import React from "react";

interface NestedReply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string;
}

interface NestedCommentProps {
  nestedReply: NestedReply;
}

const NestedComment: React.FC<NestedCommentProps> = ({ nestedReply }) => {
  return (
    <div className="card mb-2 ms-3">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-2">
          <h6 className="card-title mb-0">{nestedReply.nickname}</h6>
          <small className="text-muted">
            {nestedReply.updateDate == null
              ? nestedReply.regDate
              : `${nestedReply.updateDate} (수정됨)`}
          </small>
        </div>
        <div className="row">
          <div className="col-9">{nestedReply.comment}</div>
        </div>
        <div className="d-flex justify-content-end mt-2">
          <button className="btn btn-primary btn-sm me-2">수정</button>
          <button className="btn btn-danger btn-sm">삭제</button>
        </div>
      </div>
    </div>
  );
};

export default NestedComment;
