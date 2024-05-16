import { Route, Routes } from "react-router-dom";
import {
  LOGIN,
  MAIN,
  SIGNUP,
  MYPAGE,
  PROJECT_REG,
  PROJECT_OVERVIEW,
} from "./constants/page_constants";
import MainPage from "./pages/MainPage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";
import MyPage from "./pages/MyPage";
import ProjectRegPage from "./pages/ProjectRegPage";
import ProjectOverviewPage from "./pages/ProjectOverviewPage";

function App() {
  return (
    <Routes>
      <Route path={MAIN} element={<MainPage />} />
      <Route path={LOGIN} element={<LoginPage />} />
      <Route path={SIGNUP} element={<SignUpPage />} />
      <Route path={MYPAGE} element={<MyPage />} />
      <Route path={PROJECT_REG} element={<ProjectRegPage />} />
      <Route path={PROJECT_OVERVIEW} element={<ProjectOverviewPage />} />
    </Routes>
  );
}

export default App;
