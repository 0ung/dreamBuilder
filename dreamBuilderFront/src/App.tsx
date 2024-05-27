import { Route, Routes } from "react-router-dom";
import {
  LOGIN,
  MAIN,
  SIGNUP,
  MYPAGE,
  PROJECT_REG,
  PROJECT_OVERVIEW,
  PROJECT_DETAIL_VIEW,
  MANAGE_VISITOR,
  MANAGE_REPLY,
  MANAGE_PROJECT,
  MANAGE_MEMBER,
  MANAGE_FILEUPLOAD,
} from "./constants/page_constants";
import MainPage from "./pages/MainPage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";
import MyPage from "./pages/MyPage";
import ProjectRegPage from "./pages/ProjectRegPage";
import ProjectOverviewPage from "./pages/ProjectOverviewPage";
import ProjectDetailView from "./pages/ProjectDetailView";
import ManageVisitor from "./pages/MangeVisitor";
import ManageProject from "./pages/ManageProject";
import ManageMember from "./pages/ManageMember";
import ManageReply from "./pages/ManageReply";
import ManageFileUpload from "./pages/ManageFileUpload";

function App() {
  return (
    <Routes>
      <Route path={MAIN} element={<MainPage />} />
      <Route path={LOGIN} element={<LoginPage />} />
      <Route path={SIGNUP} element={<SignUpPage />} />
      <Route path={MYPAGE} element={<MyPage />} />
      <Route path={PROJECT_REG} element={<ProjectRegPage />} />
      <Route path={PROJECT_OVERVIEW} element={<ProjectOverviewPage />} />
      <Route path={PROJECT_DETAIL_VIEW} element={<ProjectDetailView />} />
      <Route path={MANAGE_VISITOR} element={<ManageVisitor />} />
      <Route path={MANAGE_PROJECT} element={<ManageProject />} />
      <Route path={MANAGE_REPLY} element={<ManageReply />} />
      <Route path={MANAGE_MEMBER} element={<ManageMember />} />
      <Route path={MANAGE_FILEUPLOAD} element={<ManageFileUpload />} />
    </Routes>
  );
}

export default App;
