import React, { ReactNode } from "react";

// 컴포넌트의 속성을 위한 인터페이스 정의
interface SearchLiProps {
  children: ReactNode; // ReactNode는 렌더링할 수 있는 모든 것을 포함합니다: 숫자, 문자열, 요소 또는 이들의 배열
  href: string;
  onClick?: (event: React.MouseEvent<HTMLAnchorElement>) => void; // 옵셔널 onClick 이벤트 핸들러
}

function SearchLi({ children, href, onClick }: SearchLiProps) {
  return (
    <li>
      <a className="dropdown-item" href={href} onClick={onClick}>
        {children}
      </a>
    </li>
  );
}

export default SearchLi;
