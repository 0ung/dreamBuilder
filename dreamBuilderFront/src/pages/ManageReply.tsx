import CommentSection from "../components/CommentSection";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Pagination from "../components/Pagination";
import { useEffect, useState } from "react";
import fetcher from "../fetcher";
import {
  MANAGE_REPLY_API,
  MANAGE_REPLY_TOTAL,
} from "../constants/api_constants";

interface Reply {
  id: number;
  comment: string;
  nickname: string;
  regTime: string;
  updateTime: string | null;
  nestReply: nestedReply[];
  invisible: boolean;
}

interface nestedReply {
  id: number;
  comment: string;
  nickname: string;
  regTime: string;
  updateTime: string | null;
  invisible: boolean;
}

function ManageReply() {
  const [reply, setReply] = useState<Reply[]>([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  const handlePageChange = (pageNumber: number) => {
    setPage(pageNumber);
  };

  const handleReplyData = async () => {
    const response = await fetcher.get(`${MANAGE_REPLY_API}${page - 1}`);
    setReply(response.data);
  };

  const totalpages = async () => {
    const response = await fetcher.get(MANAGE_REPLY_TOTAL);
    const totalData: number = response.data;
    setTotalPages(Math.floor(totalData / 10 + 1));
  };

  useEffect(() => {
    handleReplyData();
    totalpages();
  }, [page]);
  return (
    <>
      <Header />
      <CommentSection replies={reply} isAdmins={true} />
      <Pagination
        currentPage={page}
        totalPages={totalPages}
        onPageChange={handlePageChange}
      ></Pagination>
      <Footer />
    </>
  );
}

export default ManageReply;
