import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Container, Button } from "@mui/material";
import { motion } from "framer-motion";
import AnimatedThemeToggle from "./AnimatedThemeToggle";

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false);

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener("scroll", onScroll);
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  return (
    <nav
      className={`fixed top-0 left-0 w-full z-50 transition-all duration-300
        ${
          scrolled
            ? "bg-white/90 dark:bg-slate-950/90 backdrop-blur border-b border-slate-200 dark:border-slate-800"
            : "bg-white/80 dark:bg-slate-950/80 backdrop-blur"
        }`}
    >
      <Container
        maxWidth={false}
        disableGutters
        className="relative flex items-center justify-between py-4 px-8"
      >
        {/* ================= LEFT LOGO ================= */}
        <Link to="/" className="flex items-center gap-3 z-10">
          <img
            src="/logo.jpg"
            alt="Wellness Marketplace Logo"
            className="h-14 md:h-16 w-auto object-contain"
          />
        </Link>

        {/* ================= CENTER TITLE ================= */}
        <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
          <motion.h1
            initial={{ opacity: 0, y: -8 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            className="
              text-3xl md:text-5xl
              font-extrabold
              tracking-tight
              text-slate-900 dark:text-white
              drop-shadow-lg
            "
          >
            Wellness Marketplace
          </motion.h1>
        </div>

        {/* ================= RIGHT CONTROLS ================= */}
        <div className="relative z-10 flex items-center gap-5">
          <AnimatedThemeToggle />

          <Button
            component={Link}
            to="/login"
            className="!font-semibold !capitalize text-slate-800 dark:text-slate-200"
          >
            Login
          </Button>

          <Button
            component={Link}
            to="/register"
            variant="contained"
            className="
              !bg-emerald-600 hover:!bg-emerald-700
              !text-white !rounded-full
              !px-7 !py-2.5
              !font-semibold
              shadow-md hover:shadow-xl
            "
          >
            Get Started
          </Button>
        </div>
      </Container>
    </nav>
  );
}
