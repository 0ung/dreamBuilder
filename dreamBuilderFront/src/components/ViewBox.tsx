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
            src="https://search.pstatic.net/sunny/?src=https%3A%2F%2Fpng.pngtree.com%2Fpng-clipart%2F20220225%2Fourlarge%2Fpngtree-tinned-beverage-can-small-coke-png-image_4453928.png&type=a340"
          />
        </div>
        <div className="card-body">
          <TruncateText className="card-text">
            기술스택: {data.hashTags.join(", ")}
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
