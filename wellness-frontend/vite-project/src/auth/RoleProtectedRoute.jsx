import React from 'react';
import { Navigate } from "react-router-dom";

export default function RoleProtectedRoute({ children, allowed }) {
  const role = localStorage.getItem("role");

  // allowed = ["PATIENT"] or ["PRACTITIONER"]
  if (!allowed.includes(role)) {
    return <Navigate to="/" replace />;
  }

  return children;
}
