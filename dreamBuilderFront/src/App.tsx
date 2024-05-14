import { Route, Routes } from "react-router-dom";
import { LOGIN, MAIN, SIGNUP, MYPAGE } from "./constants/page_constants";
import MainPage from "./pages/MainPage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";
import MyPage from "./pages/MyPage";

function App() {
  return (
    <Routes>
      <Route path={MAIN} element={<MainPage />} />
      <Route path={LOGIN} element={<LoginPage />} />
      <Route path={SIGNUP} element={<SignUpPage />} />
      <Route path={MYPAGE} element={<MyPage />} />
    </Routes>
  );
}

export default App;
