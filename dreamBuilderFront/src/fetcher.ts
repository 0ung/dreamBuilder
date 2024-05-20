import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

console.log(API_BASE_URL);
const fetcher = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
});

// 요청을 가로채서 핸들링
fetcher.interceptors.request.use(
  (request) => {
    return request;
  },
  (error) => {
    console.error(error);
    return Promise.reject(error);
  }
);

//응답을 가로채서 핸들링
fetcher.interceptors.response.use(
  (response) => {
    return response;
  },

  async (error) => {
    console.log(error);
    if (error.response.status === 401) {
      await tokenRefresh();
      const accessToken = localStorage.getItem("access_token");
      error.config.headers["Authorization"] = `Bearer ${accessToken}`;
      const response = await axios.request(error.config);
      return response;
    }

    if (error.response.status === 403) {
      alert("해당 페이지 접근 권한이 없습니다.");
      window.location.href = LOGIN;
    }
    return Promise.reject(error);
  }
);

const tokenRefresh = async () => {
  const refreshToken = localStorage.getItem("refresh_token");

  try {
    const formData = {
      refreshToken: refreshToken,
    };
    const response = await axios.post(
      API_BASE_URL + TOKEN_REFRESH_API,
      JSON.stringify(formData),
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    localStorage.setItem("access_token", response.data.accessToken);
    localStorage.setItem("refresh_token", response.data.refreshToken);
  } catch (error) {
    window.location.href = LOGIN;
  }
};

export default fetcher;
