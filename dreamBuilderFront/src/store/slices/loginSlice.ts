import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface LoginState {
  isLogin: boolean;
}

const initialState: LoginState = {
  isLogin: false,
};

const loginSlice = createSlice({
  name: "admin",
  initialState,
  reducers: {
    toggleLogin(state) {
      state.isLogin = !state.isLogin;
    },
    setLogin(state, action: PayloadAction<boolean>) {
      state.isLogin = action.payload;
    },
  },
});

export const { toggleLogin, setLogin } = loginSlice.actions;

export default loginSlice.reducer;
