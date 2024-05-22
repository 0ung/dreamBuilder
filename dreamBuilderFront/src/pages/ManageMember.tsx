import React, { useState,useEffect } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Pagination from "../componets/Pagination";
import fetcher from "../fetcher";
import { MANAGE_MEMBERS,withdrawal_API,restore_API } from "../constants/api_constants";

type TableData = {
  id: number;
  name: string;
  email: string;
  regTime: string;
  updateTime: string;
  authority: string;
  deactive: boolean;
};

type TableComponentProps = {
  data: TableData[];
};

const TableComponent: React.FC<TableComponentProps> = ({ data }) => {

  
  const handleWithdrawal = async (email: string)=>{
    try{
      const formData = {
        email: email
      };
      const request = await fetcher.post(
        withdrawal_API,
        JSON.stringify(formData),
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
    }catch(error){

    }
  }

    const handleRestore = async (email: string)=>{
      try{
        const formData = {
          email: email
        };
        const request = await fetcher.post(
          restore_API,
          JSON.stringify(formData),
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
      }catch(error){
  
      }
  };
  

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
            <td>{row.name}</td>
            <td>{row.email}</td>
            <td>{row.regTime}</td>
            <td>{row.updateTime}</td>
            <td>{row.authority}</td>
            <td>{row.deactive ? "Yes" : "No"}</td>
            <td>
              {row.deactive ?
              <button
                className={`btn "btn-success"`}
                onClick={() => handleRestore(row.email)}
              >
                "복구"
              </button>
              : 
              <button
                className={`btn "btn-danger"`}
                onClick={() => handleWithdrawal(row.email)}
              >
                "삭제"
              </button>}
              
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

  const ManageMember: React.FC = () => {
  const [userData, setUserData] = useState<TableData[]>([]);
  const [currentPage, setCurrentPage] = useState<number>(0);

  const handleMemberData = async ()=>{
    try {
      const response = await fetcher.get(`${MANAGE_MEMBERS}${currentPage}`)
      setUserData(response.data);
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  }


  useEffect(() => {
    handleMemberData();
  }, []);

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
