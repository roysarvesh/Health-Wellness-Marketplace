import React, { useEffect, useState } from "react";
import { Link, NavLink } from "react-router-dom";
import { Container, Button, Badge } from "@mui/material";
import AnimatedThemeToggle from "./AnimatedThemeToggle";
import { useAuth } from "../../context/AuthContext";
import { useCart } from "../../context/CartContext";
import { motion } from "framer-motion";

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false);

  const { isAuthenticated, role, logout } = useAuth();
  const { cart } = useCart();

  /** ⭐ Compute cart item count safely */
  const cartCount =
    cart?.items?.reduce((sum, item) => sum + item.quantity, 0) || 0;

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
            ? "bg-white/90 dark:bg-slate-950/90 backdrop-blur border-b border-slate-300 dark:border-slate-800"
            : "bg-white/80 dark:bg-slate-950/80 backdrop-blur"
        }`}
    >
      <Container
        maxWidth={false}
        disableGutters
        className="flex items-center justify-between py-4 px-8"
      >
        {/* LEFT — LOGO */}
        <Link to="/" className="flex items-center">
          <img
            src="/logo.jpg"
            alt="Wellness Marketplace Logo"
            className="h-14 md:h-16 w-auto object-contain"
          />
        </Link>

        {/* CENTER — TITLE */}
        <div className="flex-1 flex justify-center">
          <motion.h1
            initial={{ opacity: 0, y: -6 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.4 }}
            className="text-2xl md:text-4xl font-extrabold tracking-tight 
            text-slate-900 dark:text-white text-center"
          >
            Wellness Marketplace
          </motion.h1>
        </div>

        {/* RIGHT — NAVIGATION */}
        <div className="flex items-center gap-5">
          <AnimatedThemeToggle />

          {/* ====================== NOT LOGGED IN ====================== */}
          {!isAuthenticated ? (
            <>
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
                className="!bg-emerald-600 hover:!bg-emerald-700 
                !text-white !rounded-full !px-7 !py-2.5 !font-semibold"
              >
                Get Started
              </Button>
            </>
          ) : (
            <>
              <NavLink to="/products" className="font-medium">
                Products
              </NavLink>

              <NavLink to="/ai-recommendations" className="font-medium">
                AI Recommendations
              </NavLink>

              <NavLink to="/orders" className="font-medium">
                My Orders
              </NavLink>

              {/* 🛒 CART WITH MUI BADGE (⭐ Your Requested Snippet Integrated) */}
              <NavLink to="/cart" className="font-medium relative">
                <Badge
                  badgeContent={cartCount}
                  color="error"
                  invisible={cartCount === 0}
                  overlap="circular"
                >
                  <span className="text-xl">🛒</span>
                </Badge>
              </NavLink>

              <NavLink to="/profile" className="font-medium">
                Profile
              </NavLink>

              <NavLink to="/notifications" className="text-xl">
                🔔
              </NavLink>

              {/* PATIENT ONLY */}
              {role === "PATIENT" && (
                <>
                  <NavLink to="/practitioners" className="font-medium">
                    Find Experts
                  </NavLink>
                  <NavLink to="/bookings" className="font-medium">
                    Bookings
                  </NavLink>
                  <NavLink to="/forum" className="font-medium">
                    Community
                  </NavLink>
                  <NavLink to="/chat" className="font-medium">
                    Chat
                  </NavLink>
                  <NavLink to="/fitness" className="font-medium">
                    Health
                  </NavLink>
                </>
              )}

              {/* PRACTITIONER ONLY */}
              {role === "PRACTITIONER" && (
                <>
                  <NavLink to="/chat" className="font-medium">
                    Messages
                  </NavLink>
                  <NavLink to="/reviews" className="font-medium">
                    Reviews
                  </NavLink>
                </>
              )}

              {/* ADMIN */}
              {role === "ADMIN" && (
                <NavLink to="/admin" className="font-medium">
                  Admin Panel
                </NavLink>
              )}

              {/* LOGOUT */}
              <Button
                onClick={logout}
                variant="contained"
                className="!bg-red-600 hover:!bg-red-700 
                !text-white !rounded-full !px-6 !py-2 !font-semibold"
              >
                Logout
              </Button>
            </>
          )}
        </div>
      </Container>
    </nav>
  );
}
