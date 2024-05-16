import React, { useEffect, useState } from "react";
import styled from "styled-components";
import reply from "../image/chat.svg";
import heartFill from "../image/heart-fill.svg";
import heart from "../image/heart.svg";
import views from "../image/views.svg";
import { useNavigate } from "react-router-dom";
import ProjectDetailView from "../pages/ProjectDetailView";
import { PROJECT_DETAIL_VIEW } from "../constants/page_constants";

interface ViewBoxProps {
  data: {
    id: number;
    title: string;
    endDate: string;
    hashTag: string[];
    likedCnt: number;
    replyCnt: number;
    viewCnt: number;
    liked: boolean;
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
  text-decoration: none; /* 밑줄 제거 */
  color: inherit; /* 글자 색을 부모 요소와 동일하게 */

  &:hover {
    color: #348f8f; /* 마우스를 올렸을 때 색상 변경 */
  }
`;

const ViewBox: React.FC<ViewBoxProps> = ({ data }) => {
  const [liked, setLiked] = useState(data.liked);
  const navigator = useNavigate();
  const handleDisliked = () => {
    console.log(data.id + "싫어요");
    setLiked(false);
  };
  const handleLiked = () => {
    console.log(data);
    console.log(data.id + "좋아요");
    setLiked(true);
    console.log(data);
  };

  const handleTitle = (id: number) => {
    navigator(PROJECT_DETAIL_VIEW, { state: id });
  };
  useEffect(() => {
    setLiked(data.liked);
  }, [data]);
  return (
    <Card className="card mb-2">
      <div className="row g-0 h-100">
        <div className="col-md-6 d-flex flex-column justify-content-center">
          <span className="ms-1">
            {liked ? (
              <img src={heartFill} onClick={handleDisliked} />
            ) : (
              <img src={heart} onClick={handleLiked} />
            )}
          </span>
          <div className="card-body">
            <h5 className="card-title text-truncate">
              <StyledLink
                onClick={() => {
                  handleTitle(data.id);
                }}
              >
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
            기술스택: {data.hashTag.join(", ")}
          </TruncateText>
        </div>
      </div>
      <div className="card-footer d-flex justify-content-between">
        <span>
          <img className="pe-1" src={heart} />: {data.likedCnt}
        </span>
        <span>
          <img className="pe-1 pb-1" src={reply} />: {data.replyCnt}
        </span>
        <span>
          <img className="pe-1 " src={views} />: {data.viewCnt}
        </span>
      </div>
    </Card>
  );
};
export default ViewBox;
