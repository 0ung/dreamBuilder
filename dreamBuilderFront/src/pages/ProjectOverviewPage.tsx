import { useEffect, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import "bootstrap/dist/css/bootstrap.min.css";
import ViewBox from "../components/ViewBox";
import styled from "styled-components";
import InfiniteScroll from "react-infinite-scroll-component";
import fetcher from "../fetcher";
import { BOARD_VIEW } from "../constants/api_constants";
import { useLocation, useNavigate } from "react-router-dom";
import { PROJECT_REG } from "../constants/page_constants";

interface Data {
  id: number;
  title: string;
  endDate: string;
  hashTags: string[];
  likedCnt: number;
  replyCnt: number;
  cnt: number;
  liked: boolean;
  countLike: number;
  likeList: boolean | null;
}

const FlexContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: flex-start;
  align-items: flex-start;
`;

const FlexItem = styled.div`
  max-width: 50%;
  box-sizing: border-box;
`;

function LoadingSpinner() {
  return (
    <div
      className="d-flex justify-content-center align-items-center"
      style={{ height: "100vh" }}
    >
      <div className="spinner-border text-primary" role="status">
        <span className="visually-hidden">Loading...</span>
      </div>
    </div>
  );
}

export default function ProjectOverviewPage() {
  const [data, setData] = useState<Data[]>([]);
  const [hasMore, setHasMore] = useState<boolean>(true);
  const [page, setPage] = useState(0);
  const [sortCriteria, setSortCriteria] = useState<string>("");
  const navigator = useNavigate();

  const location = useLocation();

  const handleData = async (sort: string, pageNum: number) => {
    try {
      const response = await fetcher.get(
        `${BOARD_VIEW + pageNum}?search=&criteria=&sort=${sort}`
      );
      const newData = response.data;
      console.log(newData);
      setData((prevData) =>
        pageNum === 0 ? newData : [...prevData, ...newData]
      );
      if (newData.length === 0) {
        setHasMore(false);
      } else {
        setHasMore(true);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
      setHasMore(false);
    }
  };

  useEffect(() => {
    if (location.state?.data) {
      setData(location.state.data);
    }
  }, [location.state]);

  useEffect(() => {
    handleData(sortCriteria, page);
  }, [page, sortCriteria]);

  useEffect(() => {
    setData([]);
    setPage(0);
  }, [sortCriteria]);

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
              onChange={(e) => {
                setSortCriteria(e.target.value);
              }}
            >
              <option value="">정렬 기준</option>
              <option value="title">제목</option>
              <option value="end_date">마감일</option>
              <option value="reg_time">생성일시</option>
            </select>
          </div>
          <div className="col-md-6 d-flex justify-content-end align-items-center">
            <div className="col text-end">
              <button
                className="btn btn-primary"
                style={{ backgroundColor: "#5FBFBF", color: "white" }}
                onClick={() => {
                  navigator(PROJECT_REG);
                }}
              >
                프로젝트 등록
              </button>
            </div>
          </div>
        </div>
        {data.length > 0 ? (
          <InfiniteScroll
            dataLength={data.length}
            next={fetchMoreData}
            hasMore={hasMore}
            loader={<div>{LoadingSpinner()}</div>}
          >
            <FlexContainer>
              {data.map((project, index) => (
                <FlexItem key={project.id + 10 * index}>
                  <ViewBox data={project} />
                </FlexItem>
              ))}
            </FlexContainer>
          </InfiniteScroll>
        ) : (
          <div>데이터가 없어용</div>
        )}
      </div>
      <Footer />
    </>
  );
}
