import React, { useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Pagination from "../components/Pagination";

type TableData = {
  id: number;
  userId: string;
  email: string;
  regDate: string;
  updateDate: string;
  role: string;
  deactive: boolean;
};

type TableComponentProps = {
  data: TableData[];
};

const TableComponent: React.FC<TableComponentProps> = ({ data }) => {
  return (
    <table className="table table-striped table-bordered">
      <thead className="thead-dark">
        <tr>
          <th>번호</th>
          <th>아이디</th>
          <th>이메일</th>
          <th>생성일시</th>
          <th>업데이트 일시</th>
          <th>권한</th>
          <th>탈퇴 여부</th>
          <th>복구</th>
        </tr>
      </thead>
      <tbody>
        {data.map((row, index) => (
          <tr key={index}>
            <td>{row.id}</td>
            <td>{row.userId}</td>
            <td>{row.email}</td>
            <td>{row.regDate}</td>
            <td>{row.updateDate}</td>
            <td>{row.role}</td>
            <td>{row.deactive ? "Yes" : "No"}</td>
            <td>
              <button
                className={`btn ${row.deactive ? "btn-success" : "btn-danger"}`}
                onClick={() => console.log("히히")}
              >
                {row.deactive ? "복구" : "삭제"}
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

function ManageMember() {
  const [userData, setUserData] = useState([
    {
      id: 1,
      userId: "user1",
      email: "user1@example.com",
      regDate: "2024-01-01",
      updateDate: "2024-06-01",
      role: "Admin",
      deactive: true,
    },
    {
      id: 2,
      userId: "user2",
      email: "user2@example.com",
      regDate: "2025-01-01",
      updateDate: "2025-06-01",
      role: "User",
      deactive: false,
    },
  ]);

  return (
    <>
      <Header />
      <div className="container mt-5">
        <h1>아이디 관리</h1>
        <hr />
        <TableComponent data={userData}></TableComponent>
        <Pagination
          currentPage={1}
          onPageChange={() => {}}
          totalPages={10}
        ></Pagination>
      </div>
      <Footer />
    </>
  );
}

export default ManageMember;
