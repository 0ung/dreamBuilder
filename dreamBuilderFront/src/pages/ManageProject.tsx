import React, { useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Pagination from "../componets/Pagination";

type TableData = {
  id: number;
  title: string;
  endDate: string;
  regDate: string;
  updateDate: string;
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
          <th>제목</th>
          <th>마감일시</th>
          <th>생성일시</th>
          <th>업데이트일시</th>
          <th>비활성화</th>
          <th>삭제(복구)버튼</th>
        </tr>
      </thead>
      <tbody>
        {data.map((row, index) => (
          <tr key={index}>
            <td>{row.id}</td>
            <td>{row.title}</td>
            <td>{row.endDate}</td>
            <td>{row.regDate}</td>
            <td>{row.updateDate}</td>
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

function ManageProject() {
  const [projectData, setProjectData] = useState([
    {
      id: 1,
      title: "Project 1",
      endDate: "2024-12-31",
      regDate: "2024-01-01",
      updateDate: "2024-06-01",
      deactive: true,
    },
    {
      id: 2,
      title: "Project 2",
      endDate: "2025-12-31",
      regDate: "2025-01-01",
      updateDate: "2025-06-01",
      deactive: false,
    },
  ]);
  return (
    <>
      <Header />
      <div className="container mt-5">
        <h1>프로젝트 삭제 관리</h1>
        <hr />
        <TableComponent data={projectData}></TableComponent>
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

export default ManageProject;
