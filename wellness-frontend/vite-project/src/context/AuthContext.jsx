import { createContext, useContext, useEffect, useState } from "react";
import { getToken } from "../utils/token";
import React from 'react';
const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [role, setRole] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  // ✅ RESTORE AUTH ON REFRESH
  useEffect(() => {
    const token = getToken();
    const storedRole = localStorage.getItem("role");
    const storedUserId = localStorage.getItem("userId");

    if (token) {
      setIsAuthenticated(true);
      setRole(storedRole);
      setUser({ userId: storedUserId });
    }

    setLoading(false);
  }, []);

  // ✅ LOGIN
  const login = (response) => {
    const { token, role, userId } = response;

    localStorage.setItem("token", token);
    localStorage.setItem("role", role);
    localStorage.setItem("userId", userId);

    setUser({ userId });
    setRole(role);
    setIsAuthenticated(true);
  };

  // ✅ LOGOUT
  const logout = () => {
    localStorage.clear();
    setUser(null);
    setRole(null);
    setIsAuthenticated(false);
    window.location.href = "/login";
  };

  return (
    <AuthContext.Provider
      value={{ user, role, isAuthenticated, login, logout, loading }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
