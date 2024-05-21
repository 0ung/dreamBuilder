import React, { useEffect, useState } from "react";
import Footer from "../layout/Footer";
import kakaoLogin from "../image/kakaoLogin.png";
import { useNavigate } from "react-router-dom";
import { MAIN, SIGNUP } from "../constants/page_constants";
import styled from "styled-components";
import axios from "axios";
import { LOGIN_API } from "../constants/api_constants";
import fetcher from "../fetcher";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../store/index";
import { toggleAdmin } from "../store/slices/adminSlice";
import { toggleLogin } from "../store/slices/loginSlice";

interface SignUPProps {
  children: React.ReactNode; // 자식 요소의 타입
  placeholder: string;
  type: string;
  onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

function LoginInput({ children, placeholder, type, onChange }: SignUPProps) {
  return (
    <>
      <div className="input-group mb-3">
        <span className="input-group-text" id="inputGroup-sizing">
          {children}
        </span>
        <input
          type={type}
          className="form-control form-control-lg"
          placeholder={placeholder}
          aria-label="Sizing example input"
          aria-describedby="inputGroup-sizing-default"
          onChange={onChange}
        />
      </div>
    </>
  );
}

export default function LoginPage() {
  const navigator = useNavigate();
  const [email, setEamil] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const dispatch = useDispatch();

  const handleLogin = async () => {
    try {
      const formData = {
        email: email,
        password: password,
      };
      const response = await fetcher.post(LOGIN_API, JSON.stringify(formData), {
        headers: {
          "Content-Type": "application/json",
        },
      });
      console.log(response.data);
      if (response.data.Role === "ROLE_ADMIN") {
        dispatch(toggleAdmin());
      }
      dispatch(toggleLogin());
      navigator(MAIN);
    } catch (error) {
      console.error;
      alert("아이디 또는 비밀번호를 다시 확인해주세요");
    }
  };

  useEffect(() => {
    // 페이지가 마운트될 때 배경색을 설정
    document.body.style.backgroundColor = "#eeeeee";

    // 페이지가 언마운트될 때 배경색을 원래대로 되돌림
    return () => {
      document.body.style.backgroundColor = "";
    };
  }, []);

  const StyledLink = styled.a`
    color: black !important;
    text-decoration: none;

    &:hover {
      color: black;
      text-decoration: none;
    }
  `;

  return (
    <>
      <div className="d-flex justify-content-center align-items-center min-vh-100">
        <div
          className="container text-center border rounded p-4 shadow-sm"
          style={{ maxWidth: "400px" }}
        >
          <h1
            onClick={() => {
              navigator(MAIN);
            }}
          >
            <StyledLink href="#">DreamBuilder</StyledLink>
          </h1>
          <hr style={{ width: "100%", margin: "0 auto" }} />
          <div className="mt-4">
            <LoginInput
              type="text"
              placeholder="아이디"
              onChange={(e) => {
                setEamil(e.target.value);
              }}
            >
              아이디
            </LoginInput>
            <LoginInput
              type="password"
              placeholder="비밀번호"
              onChange={(e) => {
                setPassword(e.target.value);
              }}
            >
              비밀번호
            </LoginInput>
            <div className="row mb-2">
              <div className="col d-flex justify-content-center">
                <a
                  href="https://kauth.kakao.com/oauth/authorize?client_id=95a542009e6abdf2635c87b2de0b4c5f&redirect_uri=http://localhost:8080/member/auth/callback&response_type=code"
                  className="w-100"
                >
                  <img
                    src={kakaoLogin}
                    alt="카카오 로그인"
                    className="img-fluid w-100"
                  />
                </a>
              </div>
            </div>
            <div className="row">
              <div className="col d-flex flex-column">
                <button
                  className="btn btn-primary mb-2 w-100"
                  onClick={() => {
                    navigator(SIGNUP);
                  }}
                >
                  회원가입
                </button>
                <button
                  className="btn btn-secondary w-100"
                  onClick={handleLogin}
                >
                  로그인
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
}
