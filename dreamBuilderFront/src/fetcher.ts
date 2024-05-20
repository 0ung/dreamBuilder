import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

console.log(API_BASE_URL);
const fetcher = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
});

export default fetcher;
