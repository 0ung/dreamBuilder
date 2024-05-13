import { Route, Routes } from "react-router-dom";
import { MAIN } from "./constants/page_constants";
import MainPage from "./pages/MainPage";

function App() {
  return (
    <Routes>
      <Route path={MAIN} element={<MainPage />} />
    </Routes>
  );
}

export default App;
