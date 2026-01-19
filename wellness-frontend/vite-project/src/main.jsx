import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import App from "./App";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";

import { ThemeProvider } from "./context/ThemeContext";

import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <ThemeProvider>
      <BrowserRouter>
        <Routes>

          {/* LAYOUT ROUTE */}
          <Route element={<App />}>
            <Route path="/" element={<HomePage />} />
          </Route>

          {/* AUTH ROUTES (NO NAVBAR / FOOTER) */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  </React.StrictMode>
);
