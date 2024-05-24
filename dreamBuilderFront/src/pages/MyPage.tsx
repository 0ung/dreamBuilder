import React, { ReactNode, useState, useEffect } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Pagination from "../components/Pagination";
import userDefaultImage from "../image/userDefaultImage.jpg";
import styled from "styled-components";
import { Button, Modal } from "react-bootstrap";
import base64 from "base-64";
import fetcher from "../fetcher";
import { withdrawal_API, modify_API, LOGOUT_API } from "../constants/api_constants";
import { useNavigate } from "react-router-dom";
import { MAIN } from "../constants/page_constants";

const dumpData = [
  {
    id: 1,
    title: "제목1",
    views: 123,
  },
  {
    id: 2,
    title: "제목asdasdadasdaasdadasdasdsdasdassd",
    views: 123,
  },
  {
    id: 3,
    title: "제목1asdasdass",
    views: 123,
  },
];

const CustomButton = styled.button`
  background-color: #348f8f;
`;

interface ViewsProps {
  children: ReactNode;
  cnt: number;
}
function Views({ children, cnt }: ViewsProps) {
  return (
    <div className="d-flex justify-content-center align-items-center mt-5">
      <div
        className="d-flex flex-column justify-content-center align-items-center border p-4 custom-font"
        style={{ width: "200px", height: "200px" }}
      >
        <p style={{ fontSize: "19px" }} className="mb-2 fw-bold">
          {children}
        </p>
        <span className="h4 fw-bold">{cnt}</span>
      </div>
    </div>
  );
}
// 수정 input

interface SignUPProps {
  children: React.ReactNode; // 자식 요소의 타입
  placeholder: string;
  type: string;
  onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

interface validation {
  children: String;
  data: () => boolean;
  onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
}
function SignupInput({ children, placeholder, type, onChange }: SignUPProps) {
  return (
    <>
      <div className="input-group mb-3">
        <span className="input-group-text" id="inputGroup-sizing">
          {children}
        </span>
        <input
          type={type}
          className="form-control"
          placeholder={placeholder}
          aria-label="Sizing example input"
          aria-describedby="inputGroup-sizing-default"
          onChange={onChange}
        />
      </div>
    </>
  );
}

function Inputvalidation({ children, data, onChange }: validation) {
  const isVliad = data();
  return (
    <>
      {isVliad ? (
        <p style={{ color: "green" }}>가입 가능합니다.</p>
      ) : (
        <p style={{ color: "red" }}>
          {children + `(이)가 올바르게 입력되지 않았습니다.`}
        </p>
      )}
    </>
  );
}

function MyPage() {
  const navigate = useNavigate();
  const [nickName, setNickName] = useState("");
  const [password, setPassword] = useState("");
  const [checkPassword, setCheckPassword] = useState("");
  const [totalPages, setTotalPages] = useState(
    Math.floor(dumpData.length / 10) + 1
  );
  const [currentPage, setCurrentPage] = useState(1);

  //kakaouser 구분
  const [accessToken, setAccessToken] = useState<string>("");
  const regex = /\w*_kakao/;
  const [kakaouser, setKakaouser] = useState(false);
  useEffect(() => {
    const sendAccessToken: string | null = localStorage.getItem("accessToken");
    if (sendAccessToken !== null && sendAccessToken !== undefined) {
      setAccessToken(sendAccessToken);
      console.log(setAccessToken(sendAccessToken));
      if(regex.test(handleJWT(sendAccessToken))){
        setKakaouser(true)
      }else{
        setKakaouser(false)
      }
    }
  }, [accessToken]);

  const handleJWT = (jwt: string) => {
    const paylaod = jwt.substring(jwt.indexOf(".") + 1, jwt.lastIndexOf("."));
    const decodeInfo = base64.decode(paylaod);
    const json = JSON.parse(decodeInfo);

    return json.sub;
  };

  // 모달 상태 관리
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  const handleCloseUpdateModal = () => setShowUpdateModal(false);
  const handleShowUpdateModal = () => setShowUpdateModal(true);

  const handleCloseDeleteModal = () => setShowDeleteModal(false);
  const handleCloseDeleteModal1 = async ()=>{
    try{
      const sendAccessToken: string | null = localStorage.getItem("accessToken");
      if (sendAccessToken !== null) {
        const formData = {
          email: handleJWT(sendAccessToken),
        };
        const request = await fetcher.post(
          withdrawal_API,
          JSON.stringify(formData),
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        handleLogout();
        navigate(MAIN);
      }
  
    }catch(error){

    }
  }
  const handleLogout = async () => {
    try {
      const response = await fetcher.post(LOGOUT_API);
      localStorage.removeItem("accessToken");
      setAccessToken("");
      console.log(response.data);
    } catch (error) {
      console.error;
    }
  };
  const handleShowDeleteModal = () => setShowDeleteModal(true);

  // 개인정보 수정
  const handleUsermodify = async ()=>{
    const regExp1 = new RegExp("^[A-Za-z0-9]{8,15}$");
    if(!regExp1.test(nickName)){
      alert("닉네임을 확인해주세요")
      return;
    }
    const regExp2 = new RegExp(
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,20}$"
    );
    if(!regExp2.test(password)){
      alert("비밀번호를 확인해주세요")
      return;
    }
    try{
      const sendAccessToken: string | null = localStorage.getItem("accessToken");
      if (sendAccessToken !== null) {
        const formData = {
          email: handleJWT(sendAccessToken),
          name: nickName,
          password: password,
        };
        const request = await fetcher.post(
          modify_API,
          JSON.stringify(formData),
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
      }
      alert("회원정보가 수정되었습니다.");
      handleCloseUpdateModal();
    }catch(error){
      alert("입력정보를 확인해주세요")
    }
  }

  //입력 검증
  const handleNickName = (e: string) => {
    const regExp = new RegExp("^[A-Za-z0-9]{8,15}$");
    return regExp.test(e);
  };
  const handlePassword = (e: string) => {
    const regExp = new RegExp(
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,20}$"
    );
    return regExp.test(e);
  };

  const handleCheckPassword = (e: string) => {
    if (e === password) {
      return true;
    }
    return false;
  };

  return (
    <>
      <Header />
      <div className="container border rounded p-5 shadow-sm mt-5 mb-5">
        <div className="text-center">
          <h1>마이페이지</h1>
          <hr style={{ width: "100%", margin: "0 auto" }} />
        </div>
        <div className="row justify-content-center mt-5">
          <img
            src={userDefaultImage}
            alt="User"
            className="img-fluid"
            style={{ width: "200px", height: "200px" }}
          />
        </div>
        <div className="row text-center mt-3">
          <div className="col">
            <h2>{nickName}</h2>
          </div>
        </div>
        <div className="row justify-content-center mt-3">
          <div className="col text-center">
            <CustomButton
              className="btn btn-primary me-2"
              onClick={() => {
                handleShowUpdateModal();
              }}
            >
              개인 정보 수정
            </CustomButton>
            <CustomButton
              className="btn btn-primary"
              onClick={() => {
                handleShowDeleteModal();
              }}
            >
              탈퇴
            </CustomButton>
          </div>
        </div>

        <div className="row justify-content-center mt-5">
          <div className="col-3">
            <Views cnt={1}>
              이번 달에 받은
              <br /> 좋아요 개수
              <br />
              &nbsp;
            </Views>
          </div>
          <div className="col-3">
            <Views cnt={1}>
              이번 달에 작성한
              <br /> 게시물 개수
              <br />
              &nbsp;
            </Views>
          </div>
          <div className="col-3">
            <Views cnt={1}>
              이번 달에 작성한
              <br /> 댓글 개수
              <br />
              <span style={{ fontSize: "10px" }}>(대댓글 포함)</span>
            </Views>
          </div>
        </div>

        <hr className="mt-5 mb-5" style={{ width: "100%", margin: "0 auto" }} />
        <div className="text-center">
          <h3>{nickName}님이 작성한 글</h3>
        </div>
        <div className="container mt-5 ">
          <table
            style={{ tableLayout: "fixed" }}
            className="table text-center table-hover"
          >
            <thead>
              <tr>
                <th>번호</th>
                <th>제목</th>
                <th>조회수</th>
              </tr>
            </thead>
            <tbody>
              {dumpData.map((e) => {
                return (
                  <tr key={e.id}>
                    <td>{e.id}</td>
                    <td
                      style={{
                        whiteSpace: "nowrap",
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                      }}
                    >
                      {e.title}
                    </td>
                    <td>{e.views}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={(pageNumber) => {
              setCurrentPage(pageNumber);
            }}
          />
        </div>
      </div>
      <Footer />

      {/* 개인 정보 수정 모달 */}
      <Modal show={showUpdateModal} onHide={handleCloseUpdateModal}>
        <Modal.Header closeButton>
          <Modal.Title>개인 정보 수정</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div>
            <SignupInput
              placeholder="닉네임을 입력해주세요 (8~15자)"
              type="text"
              onChange={(e) => {
                setNickName(e.target.value);
              }}
            >
              닉네임
            </SignupInput>
          </div>
          <div>
            { kakaouser ? (
                <></>
            ):(<>
              <SignupInput
              placeholder="비밀번호를 입력해주세요 (8~20자 영대소문자, 숫자, 특수문자 하나씩 기입 )"
              type="password"
              onChange={(e) => {
                setPassword(e.target.value);
              }}
            >
              비밀번호
            </SignupInput>
            
            <SignupInput
              type="password"
              placeholder=""
              onChange={(e) => {
                setCheckPassword(e.target.value);
              }}
            >
              비밀번호 확인
            </SignupInput>
            
            </>)}

          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseUpdateModal}>
            닫기
          </Button>
          <Button variant="primary" onClick={handleUsermodify}>
            저장
          </Button>
        </Modal.Footer>
      </Modal>

      {/* 탈퇴 모달 */}
      <Modal show={showDeleteModal} onHide={handleCloseDeleteModal}>
        <Modal.Header closeButton>
          <Modal.Title>탈퇴</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="mb-3">
            { kakaouser ? (
                <>
                <label>
                  정말 탈퇴하시겠습니까?
                </label>
                </>
            ):(<>
            <label htmlFor="passwordInput" className="form-label">
              탈퇴하시려면 비밀번호를 입력해주세요
            </label>
            <input
              type="password"
              className="form-control"
              id="passwordInput"
              placeholder="비밀번호 입력"
              onChange={(e) => setPassword(e.target.value)}
            />
            </>)}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseDeleteModal}>
            닫기
          </Button>
          <Button variant="danger" 
          onClick={handleCloseDeleteModal1}>
            탈퇴
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default MyPage;
