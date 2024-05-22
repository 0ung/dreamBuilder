import CommentSection from "../components/CommentSection";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Pagination from "../components/Pagination";

interface Reply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string | null;
  nestReply: nestedReply[];
  deactive: boolean;
}

interface nestedReply {
  id: number;
  comment: string;
  nickname: string;
  regDate: string;
  updateDate: string | null;
  deactive: boolean;
}

function ManageReply() {
  const dummyData: Reply[] = [
    {
      id: 1,
      comment: "This is the first comment",
      nickname: "User1",
      regDate: "2024-01-01",
      updateDate: null,
      deactive: true,
      nestReply: [
        {
          id: 101,
          comment: "This is a nested reply to the first comment",
          nickname: "User2",
          regDate: "2024-01-02",
          updateDate: null,
          deactive: false,
        },
        {
          id: 102,
          comment: "This is another nested reply to the first comment",
          nickname: "User3",
          regDate: "2024-01-03",
          updateDate: "2024-01-04",
          deactive: false,
        },
      ],
    },
    {
      id: 2,
      comment: "This is the second comment",
      nickname: "User4",
      regDate: "2024-02-01",
      updateDate: "2024-02-02",
      nestReply: [],
      deactive: false,
    },
    {
      id: 3,
      comment: "This is the third comment",
      nickname: "User5",
      regDate: "2024-03-01",
      updateDate: null,
      deactive: false,
      nestReply: [
        {
          id: 103,
          comment: "This is a nested reply to the third comment",
          nickname: "User6",
          regDate: "2024-03-02",
          updateDate: null,
          deactive: false,
        },
      ],
    },
  ];

  return (
    <>
      <Header />
      <CommentSection replies={dummyData} />
      <Pagination
        currentPage={1}
        totalPages={5}
        onPageChange={() => {}}
      ></Pagination>
      <Footer />
    </>
  );
}

export default ManageReply;
