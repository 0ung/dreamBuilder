import React, { useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import main from "../image/mainImage.png";
import Markdown from "react-markdown";

function MainPage() {
  const [markdown, setMarkdown] = useState(`## 창의적 여정을 시작하세요

  여러분의 창의력을 마음껏 발휘하고, 실현 가능한 아이디어로 세상에 영향을 미치세요. **드림 빌더**는 모든 창조적 개인과 팀을 위한 공간을 제공합니다. 우리는 여러분이 세상을 바꾸는 아이디어를 현실로 만들 수 있도록 지원합니다.
  
  - **프로젝트 제안**: 아이디어를 제안하고 팀원을 모집하세요.
  - **협업**: 다양한 전문가와 함께 아이디어를 구체화하고, 실행 계획을 세우세요.
  - **실현**: 프로젝트를 완성하고, 그 결과를 공유하여 영감을 주세요.
  
  > "함께라면, 불가능이란 없습니다. 오늘 바로 시작해 보세요!"
  
  [프로젝트 등록하기](#) - 새로운 가능성을 향한 첫걸음을 내딛어 보세요!`);
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
