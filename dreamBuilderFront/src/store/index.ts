import { configureStore } from "@reduxjs/toolkit";
import adminReducer from "./slices/adminSlice";
import loginReducer from "./slices/loginSlice";

const store = configureStore({
  reducer: {
    admin: adminReducer,
    login: loginReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
