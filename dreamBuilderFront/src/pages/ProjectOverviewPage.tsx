import React, { useEffect, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import "bootstrap/dist/css/bootstrap.min.css";
import ViewBox from "../components/ViewBox";
import styled from "styled-components";
import InfiniteScroll from "react-infinite-scroll-component";
import fetcher from "../fetcher";
import { BOARD_VIEW } from "../constants/api_constants";
import { useLocation } from "react-router-dom";

interface Data {
  id: number;
  title: string;
  endDate: string;
  hashTags: string[];
  likedCnt: number;
  replyCnt: number;
  viewCnt: number;
  liked: boolean;
  countLike: number;
  likeList: boolean | null;
}

const FlexContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: flex-start; /* 왼쪽 정렬 */
  align-items: flex-start; /* 항목들을 상단에 정렬 */
`;

const FlexItem = styled.div`
  max-width: 50%;
  box-sizing: border-box;
`;

export default function ProjectOverviewPage() {
  const [data, setData] = useState<Data[]>([]);
  const [hasMore, setHasMore] = useState<boolean>(true);
  const [page, setPage] = useState(0);
  //검색결과 전달
  const location = useLocation();

  const handleData = async () => {
    try {
      const response = await fetcher.get(
        `${BOARD_VIEW + page}?search=&criteria=`
      );
      const newData = response.data;
      setData((prevData) => [...prevData, ...newData]);
      if (newData.length === 0) {
        setHasMore(false);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
      setHasMore(false);
    }
  };
  useEffect(() => {
    // location.state?.data가 존재하면 초기 데이터로 설정
    if (location.state?.data) {
      setData(location.state.data);
    }
  }, [location.state]);

  useEffect(() => {
    handleData();
  }, [page]);

  const fetchMoreData = () => {
    setPage((prevPage) => prevPage + 1);
  };

  return (
    <>
      <Header />
      <div className="container">
        <div className="h1 mt-5">프로젝트 구인</div>
        <hr />
        <div className="row mb-4">
          <div className="col-md-6 d-flex align-items-center">
            <select
              className="form-select form-select-lg mb-3"
              aria-label=".form-select-lg example"
            >
              <option value="1">제목</option>
              <option value="2">마감일</option>
              <option value="3">이름</option>
              <option value="4">생성일시</option>
            </select>
          </div>
          <div className="col-md-6 d-flex justify-content-end align-items-center">
            <div className="col text-end">
              <button
                className="btn btn-primary"
                style={{ backgroundColor: "#5FBFBF", color: "white" }}
              >
                프로젝트 등록
              </button>
            </div>
          </div>
        </div>
        <InfiniteScroll
          dataLength={data.length}
          next={fetchMoreData}
          hasMore={hasMore}
          loader={<h4></h4>}
        >
          <FlexContainer>
            {data.map((project) => (
              <FlexItem key={project.id + 1}>
                <ViewBox data={project} />
              </FlexItem>
            ))}
          </FlexContainer>
        </InfiniteScroll>
      </div>
      <Footer />
    </>
  );
}
