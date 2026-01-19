import React from "react";
import Navbar from "./components/common/Navbar";
import Footer from "./components/common/Footer";
import { Outlet } from "react-router-dom";

export default function App() {
  return (
    <div className="min-h-screen w-full bg-white dark:bg-[#020617] transition-colors duration-300">
      <Navbar />
      <Outlet />
      <Footer />
    </div>
  );
}
