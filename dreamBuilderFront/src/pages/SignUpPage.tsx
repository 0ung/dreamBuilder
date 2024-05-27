import React, { useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import kakao from "../image/kakao.png";
import { LOGIN } from "../constants/page_constants";
import fetcher from "../fetcher";
import { useNavigate } from "react-router-dom";
import { SIGNUP_API } from "../constants/api_constants";
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

function Inputvalidation({ children, data }: validation) {
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

export default function SignUpPage() {
  const [email, setEmail] = useState("");
  const [nickName, setNickName] = useState("");
  const [password, setPassword] = useState("");
  const [checkPassword, setCheckPassword] = useState("");
  const navigator = useNavigate();

  const handleEmail = (e: string) => {
    const regExp = new RegExp("^[A-Za-z0-9]+@[a-z]+\\.[a-z]{2,6}$");
    return regExp.test(e);
  };
  const handleNickName = (e: string) => {
    const regExp = new RegExp("^[A-Za-z0-9]{4,10}$");
    return regExp.test(e);
  };
  const handlePassword = (e: string) => {
    const regExp = new RegExp(
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,20}$"
    );
    return regExp.test(e);
  };

  const handleSignup = async () => {
    try {
      const formData = {
        email: email,
        name: nickName,
        password: password,
      };
      await fetcher.post(SIGNUP_API, JSON.stringify(formData), {
        headers: {
          "Content-Type": "application/json",
        },
      });
      alert("회원가입이 완료되었습니다.");
      navigator(LOGIN);
    } catch (error) {
      alert("입력한 정보를 다시 확인해주세요");
    }
  };

  const handleCheckPassword = (e: string) => {
    if (e === password) {
      return true;
    }
    return false;
  };

  return (
    <>
      <>
        <Header />
        <div className="container mt-5">
          <h1>회원가입</h1>
          <hr></hr>
          <SignupInput
            placeholder="아이디를 입력해주세요 (8~15자 영대소문자, 숫자만 가능)"
            type="text"
            onChange={(e) => {
              setEmail(e.target.value);
            }}
          >
            이메일
          </SignupInput>
          <Inputvalidation
            data={() => {
              return handleEmail(email);
            }}
          >
            아이디
          </Inputvalidation>
          <SignupInput
            placeholder="닉네임을 입력해주세요 (4~10자)"
            type="text"
            onChange={(e) => {
              setNickName(e.target.value);
            }}
          >
            닉네임
          </SignupInput>
          <Inputvalidation
            data={() => {
              return handleNickName(nickName);
            }}
          >
            닉네임
          </Inputvalidation>
          <SignupInput
            placeholder="비밀번호를 입력해주세요 (8~20자 영대소문자, 숫자, 특수문자 하나씩 기입 )"
            type="password"
            onChange={(e) => {
              setPassword(e.target.value);
            }}
          >
            비밀번호
          </SignupInput>
          <Inputvalidation
            data={() => {
              return handlePassword(password);
            }}
          >
            비밀번호
          </Inputvalidation>
          <SignupInput
            type="password"
            placeholder=""
            onChange={(e) => {
              setCheckPassword(e.target.value);
            }}
          >
            비밀번호 확인
          </SignupInput>
          <Inputvalidation
            data={() => {
              return handleCheckPassword(checkPassword);
            }}
          >
            비밀번호 확인
          </Inputvalidation>
          <div style={{ display: "flex", justifyContent: "flex-end" }}>
            <button
              className="btn btn-primary"
              style={{ width: "15%", backgroundColor: "#348f8f" }}
              onClick={handleSignup}
            >
              가입
            </button>
          </div>
          <div
            className="mt-2"
            style={{ display: "flex", justifyContent: "flex-end" }}
          >
            <a
              className=""
              href="https://kauth.kakao.com/oauth/authorize?client_id=95a542009e6abdf2635c87b2de0b4c5f&redirect_uri=http://222.119.100.90:8111/auth/callback&response_type=code"
              style={{ width: "15%" }}
            >
              <img src={kakao} style={{ width: "100%", height: "100%" }} />
            </a>
          </div>
        </div>
        <Footer />
      </>
    </>
  );
}
