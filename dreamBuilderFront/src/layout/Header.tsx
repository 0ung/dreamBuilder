import styled from "styled-components";
import SearchLi from "../componets/SearchLi";
import { ReactNode, useState } from "react";
import search from "../image/search.svg";
import { useNavigate } from "react-router-dom";
import {
  LOGIN,
  MAIN,
  MYPAGE,
  PROJECT_OVERVIEW,
} from "../constants/page_constants";
import LOGOIMAGE from "../image/LogoImage.png";

const StyledLink = styled.a`
  color: white !important;
  text-decoration: none;

  &:hover {
    color: white;
    text-decoration: none;
  }
`;

const Headers = styled.div`
  height: 6rem;
  background-color: #2fcdcd;
  padding: 0 2rem;
  display: flex;
  align-items: center;
`;

const Logo = styled.div`
  color: white;
  font-size: 36px;
  line-height: 40px;
`;

const SearchDropdownButton = styled.button`
  background-color: #348f8f;
  border: none;
  color: white;
`;

const SearchButton = styled.button`
  background-color: #348f8f;
  border: none;
  color: white;
`;

// 해시태그에 적용할 스타일
const TaggedText = styled.span`
  background-color: #ffff00; // 해시태그에 노란 배경 적용
  color: black; // 텍스트 색상을 검은색으로 설정
  padding: 0.1em 0.4em;
  margin: 0 0.25em;
  border-radius: 0.375em;
`;

interface NavLiProps {
  children: React.ReactNode; // 자식 요소의 타입
  href: string; // 링크 URL
  onClick?: (event: React.MouseEvent<HTMLAnchorElement>) => void; // 옵셔널 onClick 이벤트 핸들러
}

function NavLi({ children, href, onClick }: NavLiProps) {
  return (
    <li className="nav-item">
      <a
        style={{ color: "white" }}
        className="nav-link active"
        aria-current="page"
        href={href}
        onClick={(event: React.MouseEvent<HTMLAnchorElement>) => {
          onClick?.(event); // onClick 핸들러가 제공되면 호출
        }}
      >
        {children}
      </a>
    </li>
  );
}

function Header() {
  const [searchQuery, setSearchQuery] = useState("");
  const [hashTag, setHashTag] = useState<ReactNode>([]);

  const [loggin, isLoggin] = useState(true);
  const [admin, isAdmin] = useState(false);

  const navigate = useNavigate();

  const handleSearch = (text: string) => {
    console.log(searchQuery + " 검색 문");
    const hashTagRegex = new RegExp("#(\\S+)", "g");
    const matches = text.match(hashTagRegex);
    if (matches) {
      const hashTags = matches.map((tag) => tag.slice(1)); // '#'을 제거하고 순수 텍스트만 추출
      setHashTag(hashTags);
    }
  };

  return (
    <Headers className="row align-items-center pt-4">
      <Logo
        className="col"
        onClick={() => {
          navigate(MAIN);
        }}
      >
        <StyledLink href="">Dream Builder</StyledLink>
      </Logo>
      <div className="col-6">
        <div className="row">
          <div className="input-group">
            <SearchDropdownButton
              className="btn btn-outline-secondary dropdown-toggle"
              type="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              검색
            </SearchDropdownButton>
            <ul className="dropdown-menu">
              <SearchLi href="#">제목</SearchLi>
              <SearchLi href="#">작성자</SearchLi>
              <SearchLi href="#">내용</SearchLi>
              <SearchLi href="#">제목 + 작성자</SearchLi>
              <SearchLi href="#">제목 + 내용</SearchLi>
              <SearchLi href="#">작성자 + 내용</SearchLi>
              <SearchLi href="#">제목 + 작성자 + 내용</SearchLi>
            </ul>
            <input
              type="text"
              className="form-control"
              aria-label="Text input with dropdown button"
              placeholder="검색어를 입력하세요...  (해시태그는 #으로 시작, 띄어쓰기로 구분해주세요)"
              onChange={(e) => {
                setSearchQuery(e.target.value);
              }}
            />
            <SearchButton
              className="btn btn-primary"
              onClick={() => handleSearch(searchQuery)}
            >
              <img src={search} alt="검색"></img>
            </SearchButton>
          </div>
        </div>
      </div>
      <div className="row align-items-end">
        <ul className="nav justify-content-end">
          {loggin ? (
            admin ? (
              <>
                {/*로그인 관리인*/}
                <NavLi href="#">방문자 수</NavLi>
                <NavLi href="#">프로젝트 관리</NavLi>
                <NavLi href="#">댓글 관리</NavLi>
                <NavLi href="#">회원정보 관리</NavLi>
                <NavLi href="#">로그아웃</NavLi>
              </>
            ) : (
              <>
                {/*로그인 후*/}
                <NavLi
                  href=""
                  onClick={() => {
                    navigate(PROJECT_OVERVIEW);
                  }}
                >
                  프로젝트
                </NavLi>
                <NavLi
                  href=""
                  onClick={() => {
                    navigate(MYPAGE);
                  }}
                >
                  마이페이지
                </NavLi>
                <NavLi href="#">로그아웃</NavLi>
              </>
            )
          ) : (
            <>
              {/*로그인 전*/}
              <NavLi
                href=""
                onClick={() => {
                  navigate(PROJECT_OVERVIEW);
                }}
              >
                프로젝트
              </NavLi>
              <NavLi href="" onClick={() => navigate(LOGIN)}>
                로그인
              </NavLi>
            </>
          )}
        </ul>
      </div>
    </Headers>
  );
}

export default Header;