import CommentSection from "../components/CommentSection";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Pagination from "../components/Pagination";
import { useEffect, useState } from "react";
import fetcher from "../fetcher";
import { MANAGE_REPLY } from "../constants/page_constants";
import { MANAGE_REPLY_API } from "../constants/api_constants";

interface Reply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string | null;
  nestReply: nestedReply[];
  invisible: boolean;
}

interface nestedReply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string | null;
  invisible: boolean;
}

function ManageReply() {
  const [reply, setReply] = useState<Reply[]>([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  const handlePageChange = (pageNumber: number) => {
    setPage(pageNumber);
    console.log("페이지 설정");
  };

  const handleReplyData = async () => {
    const response = await fetcher.get(`${MANAGE_REPLY_API}${page - 1}`);
    console.log(response.data);
    setReply(response.data);
  };

  useEffect(() => {
    handleReplyData();
  }, []);
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
