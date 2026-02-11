import axios from "axios";
import { clearToken } from "./token";
export const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("role");
  localStorage.removeItem("userId");

  window.location.href = "/login";
};
