import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface AdminState {
  isAdmin: boolean;
}

const initialState: AdminState = {
  isAdmin: true,
};

const adminSlice = createSlice({
  name: "admin",
  initialState,
  reducers: {
    toggleAdmin(state) {
      state.isAdmin = !state.isAdmin;
    },
    setAdmin(state, action: PayloadAction<boolean>) {
      state.isAdmin = action.payload;
    },
  },
});

export const { toggleAdmin, setAdmin } = adminSlice.actions;

export default adminSlice.reducer;
