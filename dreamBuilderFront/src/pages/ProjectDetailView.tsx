import React, { useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Markdown from "react-markdown";
import styled from "styled-components";

interface board {
  id: number;
  title: string;
  content: string;
  file: FileData[];
  reply: reply[];
}

interface FileData {
  name: string;
  url: string;
}

interface reply {
  id: number;
  comment: string;
  nickname: string;
  regDate: Date;
  updateDate: Date;
  nestReply: nestedReply[];
}

interface nestedReply {
  id: number;
  comment: string;
  nickname: string;
  regDate: Date;
  updateDate: Date;
}

// 더미 데이터 생성
const dummyBoard: board = {
  id: 1,
  title: "첫 번째 게시물",
  content: `
  ## 첫 번째 게시물 내용입니다
  -----
  ### 섹션 1
  
  여기에 첫 번째 섹션의 내용이 들어갑니다. **굵게** 표시하거나 _기울임_으로 표시할 수 있습니다.
  
  - 리스트 아이템 1
  - 리스트 아이템 2
  - 리스트 아이템 3
  
  ### 섹션 2
  
  여기에 두 번째 섹션의 내용이 들어갑니다. [링크](https://example.com)도 추가할 수 있습니다.
  
  \`\`\`javascript
  // 코드 블록 예제
  function greet() {
    console.log("Hello, World!");
  }
  \`\`\`
  
  ### 섹션 3
  
  다음은 이미지 예제입니다.
  
  ![이미지 설명](https://via.placeholder.com/150)
  
  ### 섹션 4
  
  마지막 섹션입니다. 여기에 마지막 섹션의 내용이 들어갑니다.
      `,
  file: [
    { name: "테스트", url: "http://www.naver.com" },
    { name: "테스트", url: "http://www.naver.com" },
    { name: "테스트", url: "http://www.naver.com" },
  ],
  reply: [
    {
      id: 1,
      comment: "첫 번째 댓글입니다.",
      nickname: "User1",
      regDate: new Date("2024-01-01"),
      updateDate: new Date("2024-01-02"),
      nestReply: [
        {
          id: 1,
          comment: "첫 번째 대댓글입니다.",
          nickname: "User2",
          regDate: new Date("2024-01-01"),
          updateDate: new Date("2024-01-02"),
        },
        {
          id: 2,
          comment: "두 번째 대댓글입니다.",
          nickname: "User3",
          regDate: new Date("2024-01-01"),
          updateDate: new Date("2024-01-02"),
        },
      ],
    },
    {
      id: 2,
      comment: "두 번째 댓글입니다.",
      nickname: "User4",
      regDate: new Date("2024-01-03"),
      updateDate: new Date("2024-01-04"),
      nestReply: [
        {
          id: 3,
          comment: "세 번째 대댓글입니다.",
          nickname: "User5",
          regDate: new Date("2024-01-03"),
          updateDate: new Date("2024-01-04"),
        },
      ],
    },
  ],
};

const Hr = styled.hr`
  border: none;
  border-top: 5px solid black;
`;

function ProjectDetailView() {
  const [data, setDate] = useState<board>(dummyBoard);
  return (
    <>
      <Header />
      <div className="container mb-5">
        <h1 className="mt-5">{data.title}</h1>
        <hr />
        <Markdown className="mt-5 p-5 border rounded shadow">
          {data.content}
        </Markdown>
        <div className="mt-3">
          <h6>첨부 파일:</h6>
          <ul className="list-group">
            {data.file.map((file, index) => (
              <li key={index} className="list-group-item">
                <a href={file.url} download={file.name}>
                  {file.name}
                </a>
              </li>
            ))}
          </ul>
        </div>
        <Hr />
      </div>
      <Footer />
    </>
  );
}

export default ProjectDetailView;
