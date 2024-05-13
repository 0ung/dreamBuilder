import { Route, Routes } from "react-router-dom";
import { LOGIN, MAIN, SIGNUP } from "./constants/page_constants";
import MainPage from "./pages/MainPage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";

function App() {
  return (
    <Routes>
      <Route path={MAIN} element={<MainPage />} />
      <Route path={LOGIN} element={<LoginPage />} />
      <Route path={SIGNUP} element={<SignUpPage />} />
    </Routes>
  );
}

export default App;
