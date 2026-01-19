import React from "react";
import { Navigate } from "react-router-dom";
import { getUserFromToken } from "../utils/token";

export default function AdminRoute({ children }) {
  const user = getUserFromToken();
  return user?.role === "ADMIN" ? children : <Navigate to="/" />;
}
