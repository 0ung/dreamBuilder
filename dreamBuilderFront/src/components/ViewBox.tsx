import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import reply from "../image/chat.svg";
import heartFill from "../image/heart-fill.svg";
import heart from "../image/heart.svg";
import views from "../image/views.svg";
import { PROJECT_DETAIL_VIEW } from "../constants/page_constants";
import fetcher from "../fetcher";
import { BOARD_LIKED } from "../constants/api_constants";

interface ViewBoxProps {
  data: {
    id: number;
    title: string;
    endDate: string;
    hashTags: string[];
    countLike: number;
    likeList: boolean | null;
    replyCnt: number;
    cnt: number;
  };
}

const Card = styled.div`
  width: 25rem;
  height: 15rem;
  margin-bottom: 1rem;
  overflow: hidden;
`;

const Image = styled.img`
  width: 150px;
  height: 100px;
`;

const TruncateText = styled.p`
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const StyledLink = styled.a`
  text-decoration: none;
  color: inherit;

  &:hover {
    color: #348f8f;
  }
`;

const ViewBox: React.FC<ViewBoxProps> = ({ data }) => {
  const [liked, setLiked] = useState(data.likeList ?? false);

  const navigate = useNavigate();

  const handleDisliked = async () => {
    try {
      await fetcher.put(BOARD_LIKED, {
        boardId: data.id,
      });
    } catch (error) {
      console.error("Error updating liked status:", error);
    }
    setLiked(false);
  };

  const handleLiked = async () => {
    try {
      await fetcher.put(BOARD_LIKED, {
        boardId: data.id,
      });
    } catch (error) {
      console.error("Error updating liked status:", error);
    }
    setLiked(true);
  };
  const handleTitle = (id: number) => {
    navigate(PROJECT_DETAIL_VIEW, { state: id });
  };

  useEffect(() => {
    setLiked(data.likeList ?? false);
  }, [data]);

  return (
    <Card key={data.id} className="card mb-2">
      <div className="row g-0 h-100">
        <div className="col-md-6 d-flex flex-column justify-content-center">
          <span className="ms-1">
            {liked ? (
              <img src={heartFill} onClick={handleDisliked} alt="좋아요 취소" />
            ) : (
              <img src={heart} onClick={handleLiked} alt="좋아요" />
            )}
          </span>
          <div className="card-body">
            <h5 className="card-title text-truncate">
              <StyledLink onClick={() => handleTitle(data.id)}>
                {data.title}
              </StyledLink>
            </h5>
            <p className="card-text">마감일: {data.endDate}</p>
          </div>
        </div>
        <div className="col-md-6 d-flex align-items-center justify-content-center">
          <Image
            className="img-fluid rounded-start"
            alt="프로젝트 이미지"
            src="data:image/png;base64,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"
          />
        </div>
        <div className="card-body">
          <TruncateText className="card-text">
            해시태그: {data.hashTags.join(", ")}
          </TruncateText>
        </div>
      </div>
      <div className="card-footer d-flex justify-content-between">
        <span>
          <img className="pe-1" src={heart} alt="좋아요 아이콘" />:{" "}
          {data.countLike}
        </span>
        <span>
          <img className="pe-1 pb-1" src={reply} alt="댓글 아이콘" />:{" "}
          {data.replyCnt ?? 0}
        </span>
        <span>
          <img className="pe-1" src={views} alt="조회수 아이콘" />:{" "}
          {data.cnt ?? 0}
        </span>
      </div>
    </Card>
  );
};

export default ViewBox;
