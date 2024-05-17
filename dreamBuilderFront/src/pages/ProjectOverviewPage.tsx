import React, { useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import "bootstrap/dist/css/bootstrap.min.css";
import ViewBox from "../componets/ViewBox";
import styled from "styled-components";
import InfiniteScroll from "react-infinite-scroll-component";

interface Data {
  id: number;
  title: string;
  endDate: string;
  hashTag: string[];
  likedCnt: number;
  replyCnt: number;
  viewCnt: number;
  liked: boolean;
}

const dummyData: Data[] = [
  {
    id: 1,
    title: "프로젝트 A",
    endDate: "2024-12-31",
    hashTag: ["React", "TypeScript", "CSS"],
    likedCnt: 120,
    replyCnt: 45,
    viewCnt: 1500,
    liked: true,
  },
  {
    id: 2,
    title: "프로젝트 B",
    endDate: "2024-11-30",
    hashTag: ["JavaScript", "Node.js", "Express", "Express"],
    likedCnt: 80,
    replyCnt: 30,
    viewCnt: 1200,
    liked: true,
  },
  {
    id: 3,
    title: "프로젝트 C",
    endDate: "2024-10-15",
    hashTag: ["Python", "Django", "REST"],
    likedCnt: 200,
    replyCnt: 60,
    viewCnt: 1800,
    liked: false,
  },
  {
    id: 4,
    title: "프로젝트 D",
    endDate: "2024-09-25",
    hashTag: ["Java", "Spring Boot", "MySQL"],
    likedCnt: 150,
    replyCnt: 50,
    viewCnt: 1600,
    liked: true,
  },
  {
    id: 5,
    title: "프로젝트 E",
    endDate: "2024-08-20",
    hashTag: ["C#", "ASP.NET", "Azure"],
    likedCnt: 90,
    replyCnt: 35,
    viewCnt: 1100,
    liked: false,
  },
  {
    id: 6,
    title: "프로젝트 F",
    endDate: "2024-07-15",
    hashTag: ["Kotlin", "Android", "Mobile"],
    likedCnt: 70,
    replyCnt: 20,
    viewCnt: 900,
    liked: true,
  },
];

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
  const [data, setData] = useState<Data[]>(dummyData);
  const [hasMore, setHasMore] = useState<boolean>(true);

  const fetchMoreData = async () => {
    // 실제 API 호출 또는 데이터 로드 로직 추가
    // 여기는 예시로 타임아웃을 사용하여 데이터 로드를 시뮬레이션합니다.
    await new Promise((resolve) => setTimeout(resolve, 100));
    const newData: Data[] = [
      {
        id: data.length + 1,
        title: `프로젝트 ${String.fromCharCode(65 + data.length)}`,
        endDate: "2024-08-20",
        hashTag: ["Kotlin", "Android", "Mobile"],
        likedCnt: 90,
        replyCnt: 35,
        viewCnt: 1100,
        liked: false,
      },
      {
        id: data.length + 2,
        title: `프로젝트 ${String.fromCharCode(65 + data.length + 1)}`,
        endDate: "2024-07-15",
        hashTag: ["Kotlin", "Android", "Mobile"],
        likedCnt: 70,
        replyCnt: 20,
        viewCnt: 900,
        liked: false,
      },
      {
        id: data.length + 1,
        title: `프로젝트 ${String.fromCharCode(65 + data.length)}`,
        endDate: "2024-08-20",
        hashTag: ["Kotlin", "Android", "Mobile"],
        likedCnt: 90,
        replyCnt: 35,
        viewCnt: 1100,
        liked: false,
      },
      {
        id: data.length + 2,
        title: `프로젝트 ${String.fromCharCode(65 + data.length + 1)}`,
        endDate: "2024-07-15",
        hashTag: ["Kotlin", "Android", "Mobile"],
        likedCnt: 70,
        replyCnt: 20,
        viewCnt: 900,
        liked: false,
      },
    ];
    setData((prevData) => [...prevData, ...newData]);
    if (data.length + newData.length >= 50) {
      // 예시로 총 데이터가 50개 이상일 때 더 이상 로드하지 않도록 설정
      setHasMore(false);
    }
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
          style={{ display: "flex" }}
          hasMore={hasMore}
          loader={<></>}
        >
          <FlexContainer>
            {data.map((project, index) => (
              <FlexItem key={index}>
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
