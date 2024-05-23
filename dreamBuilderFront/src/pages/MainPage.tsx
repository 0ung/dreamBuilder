import React, { useEffect, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import main from "../image/mainImage.png";
import Markdown from "react-markdown";
import { useNavigate } from "react-router-dom";
import { MAIN, PROJECT_REG } from "../constants/page_constants";

function MainPage() {
  const navigator = useNavigate();
  const [markdown, setMarkdown] = useState();

  useEffect(() => {
    const kakaoAccessToken = document.cookie.split("=")[1];
    const kakaoAccessTokenKey = document.cookie.split("=")[0];
    if (kakaoAccessTokenKey === "accessToken") {
      localStorage.setItem("accessToken", kakaoAccessToken);
    }
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
          <div className="container p-5">
            <Markdown className="ps-5 pe-5">{markdown}</Markdown>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default MainPage;
