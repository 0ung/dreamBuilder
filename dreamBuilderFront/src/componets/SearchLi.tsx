import React, { ReactNode } from "react";

// 컴포넌트의 속성을 위한 인터페이스 정의
interface SearchLiProps {
  children: ReactNode; // ReactNode는 렌더링할 수 있는 모든 것을 포함합니다: 숫자, 문자열, 요소 또는 이들의 배열
  href: string;
}

function SearchLi({ children, href }: SearchLiProps) {
  return (
    <li>
      <a className="dropdown-item" href={href}>
        {children}
      </a>
    </li>
  );
}

export default SearchLi;
