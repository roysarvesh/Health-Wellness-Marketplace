import React from "react";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";

export default function AuthLayout({ children }) {
  return (
    <div
      className="
        min-h-screen w-full 
        flex items-center justify-center 
        px-4 relative

        /* LIGHT MODE */
        bg-[#f2f4f7]

        /* DARK MODE */
        dark:bg-[#0b1220]

        transition-colors duration-300
      "
    >
      {/* BACK BUTTON */}
      <div className="absolute top-8 left-8 z-20">
        <Link
          to="/"
          className="
            flex items-center gap-2 
            text-slate-700 hover:text-emerald-600 
            dark:text-slate-300 dark:hover:text-emerald-400
            transition font-medium
          "
        >
          <ArrowBackIcon fontSize="small" />
          <span>Back to Home</span>
        </Link>
      </div>

      {/* CENTERED AUTH CARD */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="w-full max-w-md z-10"
      >
        {children}
      </motion.div>
    </div>
  );
}
