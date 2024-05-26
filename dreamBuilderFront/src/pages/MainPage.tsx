import React, { useEffect, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import main from "../image/mainImage.png";
import Markdown from "react-markdown";
import { useNavigate } from "react-router-dom";
import { MAIN, PROJECT_DETAIL_VIEW, PROJECT_REG } from "../constants/page_constants";
import fetcher from "../fetcher";
import { MAIN_PAGE_DATA } from "../constants/api_constants";

interface mainData {
  title: string;
  content: string;
  id: number;
}

function MainPage() {
  const navigator = useNavigate();
  const [data, setData] = useState<mainData[]>([]);
  const handleMainPage = async () => {
    const response = await fetcher.get(MAIN_PAGE_DATA)
    setData(response.data);
  }

  useEffect(() => {
    const kakaoAccessToken = document.cookie.split("=")[1];
    const kakaoAccessTokenKey = document.cookie.split("=")[0];
    if (kakaoAccessTokenKey === "accessToken") {
      localStorage.setItem("accessToken", kakaoAccessToken);
    }
    handleMainPage();
  }, []);
  return (
    <>
      <Header />
      <div className="container mt-5">
        <div className="row ps-5">
          <div className="col mt-5">
            <h2 className="display-4">DREAM BUILDER</h2>
            <p className="lead text-muted mt-auto pb-5">
              "함께라면, 불가능이란 없습니다. <strong>드림 빌더</strong>와 함께
              꿈을 디자인하고, 현실로 구현해보세요! 여러분의 창의력과 열정이
              만나, 새로운 가능성을 열어 갑니다. <strong>지금 바로</strong>,
              드림 빌더와 함께 놀라운 여정을 시작하세요!"
            </p>
            <div className="text-center mt-5 pb-5">
              <button
                className="btn btn-primary btn-lg"
                style={{ backgroundColor: "#348f8f" }}
                onClick={() => {
                  navigator(PROJECT_REG);
                }}
              >
                프로젝트 등록하기
              </button>
            </div>
          </div>
          <div className="col">
            <img
              src={main}
              alt="Dream Builder Main Image"
              width="500"
              height="500"
            />
          </div>
        </div>
        <hr />
        <div className="row">
          <h3 className="mb-4 mt-5">좋아요 상위 5개</h3>
          {data.map((e) => (
            <div className="col-12 mb-4" key={e.id}>
              <div className="p-4 border rounded bg-light">
                <h3 className="mb-3" onClick={
                  () => {
                    navigator(PROJECT_DETAIL_VIEW, { state: e.id })
                  }
                }>제목: {e.title}</h3>
                <Markdown>{e.content}</Markdown>
              </div>
            </div>
          ))}
        </div>
      </div>
      <Footer />
    </>
  );
}

export default MainPage;
