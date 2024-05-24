import React, { useEffect, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Pagination from "../components/Pagination";
import fetcher from "../fetcher";
import {
  MANAGE_PROJECT_DELETE,
  MANAGE_PROJECT_RESOTRE,
  MANAGE_PROJECT_API,
  MANAGE_PROJECT_TOTAL,
} from "../constants/api_constants";

type TableData = {
  id: number;
  title: string;
  endDate: string;
  regTime: string;
  updateTime: string;
  invisible: boolean;
};

type TableComponentProps = {
  data: TableData[];
  setData: () => void;
};

const TableComponent: React.FC<TableComponentProps> = ({ data, setData }) => {
  const handleRestore = async (id: number) => {
    await fetcher.put(`${MANAGE_PROJECT_RESOTRE}${id}`);
    alert(`게시물 ${id}가 복구되었습니다.`);
    setData();
  };

  const handleDelete = async (id: number) => {
    await fetcher.put(`${MANAGE_PROJECT_DELETE}${id}`);
    alert(`게시물 ${id}가 삭제되었습니다.`);
    setData();
  };
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
            <td>{row.regTime}</td>
            <td>{row.updateTime}</td>
            <td>{row.invisible ? "Yes" : "No"}</td>
            <td>
              {row.invisible ? (
                <button
                  className="btn-success"
                  onClick={() => {
                    handleRestore(row.id);
                  }}
                >
                  복구
                </button>
              ) : (
                <button
                  className="btn-danger"
                  onClick={() => {
                    handleDelete(row.id);
                  }}
                >
                  삭제
                </button>
              )}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

function ManageProject() {
  const [projectData, setProjectData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  const handlePage = (pageNumber: number) => {
    setPage(pageNumber);
    console.log("페이지 설정");
  };
  const handleManangeProject = async () => {
    const response = await fetcher.get(`${MANAGE_PROJECT_API}${page - 1}`);
    setProjectData(response.data);
    console.log(response.data);
  };

  const handleTotalPage = async () => {
    const response = await fetcher.get(MANAGE_PROJECT_TOTAL);
    setTotalPages(Math.floor(response.data / 10 + 1));
  };

  useEffect(() => {
    handleManangeProject();
    handleTotalPage();
  }, [page]);
  return (
    <>
      <Header />
      <div className="container mt-5">
        <h1>프로젝트 삭제 관리</h1>
        <hr />
        <TableComponent
          data={projectData}
          setData={handleManangeProject}
        ></TableComponent>
        <Pagination
          currentPage={page}
          onPageChange={handlePage}
          totalPages={totalPages}
        ></Pagination>
      </div>

      <Footer />
    </>
  );
}

export default ManageProject;
