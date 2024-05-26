import React, { useState, useEffect } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";

import Pagination from "../components/Pagination";
import fetcher from "../fetcher";
import { MANAGE_MEMBERS, withdrawal_API, restore_API, MANAGE_MEMBERS_TOTAL } from "../constants/api_constants";
import formatDateTime from "../dataPaser";

type TableData = {
  id: number;
  name: string;
  email: string;
  regTime: string;
  updateTime: string;
  authority: string;
  withdrawal: boolean;
};

type TableComponentProps = {
  data: TableData[];
  onWithdrawalChange: (updatedUser: TableData) => void;
};

const TableComponent: React.FC<TableComponentProps> = ({ data, onWithdrawalChange }) => {
  const handleWithdrawal = async (email: string) => {
    try {
      const formData = {
        email: email,
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

      if (request.status === 200) {
        const updatedUser = request.data; // 여기서 response.data로 데이터에 접근합니다.
        onWithdrawalChange(updatedUser);
        console.log(updatedUser); // 서버로부터 받은 데이터를 출력합니다.
      } else {
        console.error("서버 응답 오류:", request.data);
      }

    } catch (error) {

    }
  }

  const handleRestore = async (email: string) => {
    try {
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
      if (request.status === 200) {
        const updatedUser = request.data; // 여기서 response.data로 데이터에 접근합니다.
        onWithdrawalChange(updatedUser);
        console.log(updatedUser); // 서버로부터 받은 데이터를 출력합니다.
      } else {
        console.error("서버 응답 오류:", request.data);
      }
    } catch (error) {

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
            <td> {formatDateTime(row.regTime)}</td>
            <td>{formatDateTime(row.updateTime)}</td>
            <td>{row.authority}</td>
            <td>{row.withdrawal ? "Yes" : "No"}</td>
            <td>
              {row.withdrawal ?
                <button
                  className={`btn ${"btn-success"}`}
                  onClick={() => handleRestore(row.email)}
                >
                  복구
                </button>
                :
                <button
                  className={`btn ${"btn-danger"}`}
                  onClick={() => handleWithdrawal(row.email)}
                >
                  삭제
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
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [totalPage, setTotalPage] = useState(10);

  const handleTotalPage = async () => {
    const response = await fetcher.get(MANAGE_MEMBERS_TOTAL)
    const totalPages = Math.floor(response.data / 10 + 1);
    setTotalPage(totalPages);

  }

  const handlePage = (pageNumber: number) => {
    setCurrentPage(pageNumber);
  };
  const handleMemberData = async () => {
    try {
      const response = await fetcher.get(`${MANAGE_MEMBERS}${currentPage - 1}`)
      setUserData(response.data);
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  }

  const handleWithdrawalChange = (updatedUser: TableData) => {
    setUserData(prevData =>
      prevData.map(user =>
        user.email === updatedUser.email ? updatedUser : user
      )
    );
  };


  useEffect(() => {
    handleMemberData();
    handleTotalPage();
  }, [currentPage]);

  return (
    <>
      <Header />
      <div className="container mt-5">
        <h1>아이디 관리</h1>
        <hr />
        <TableComponent data={userData} onWithdrawalChange={handleWithdrawalChange}></TableComponent>
        <Pagination
          currentPage={currentPage}
          onPageChange={handlePage}
          totalPages={totalPage}
        ></Pagination>
      </div>
      <Footer />
    </>
  );
}


export default ManageMember;
