import React, { useEffect, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Pagination from "../components/Pagination";
import fetcher from "../fetcher";
import { MANAGE_MEMBER_API } from "../constants/api_constants";

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
};

const TableComponent: React.FC<TableComponentProps> = ({ data }) => {
  const handleRestore = ()=>{

  }

  const handleDelete = ()=>{
    
  }
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
              {
                row.invisible ? <button
                className="btn-success"
                onClick={handleRestore}
              >
                복구
              </button> : <button
                className="btn-danger"
                onClick={handleDelete}
              >
                삭제
              </button>
              }
              
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

  const handleManangeProject = async () => {
    const response = await fetcher.get(`${MANAGE_MEMBER_API}${page - 1}`);
    setProjectData(response.data);
    console.log(response.data);
  };

  useEffect(() => {
    handleManangeProject();
  }, []);
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
