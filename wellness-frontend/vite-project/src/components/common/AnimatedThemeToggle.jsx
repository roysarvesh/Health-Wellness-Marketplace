import { motion, AnimatePresence } from "framer-motion";
import { Sun, Moon } from "lucide-react";
import { useThemeMode } from "../../context/ThemeContext"; // ✅ CORRECT
import React from "react";
export default function AnimatedThemeToggle() {
  const { mode, toggleTheme } = useThemeMode();
  const isDark = mode === "dark";

  return (
    <motion.button
      onClick={toggleTheme}
      whileTap={{ scale: 0.85 }}
      className="
        w-12 h-12 rounded-full flex items-center justify-center
        bg-slate-200 dark:bg-slate-800
        hover:bg-slate-300 dark:hover:bg-slate-700
        transition-colors
      "
    >
      <AnimatePresence mode="wait" initial={false}>
        {isDark ? (
          <motion.div
            key="moon"
            initial={{ opacity: 0, rotate: -45, scale: 0.5 }}
            animate={{ opacity: 1, rotate: 0, scale: 1 }}
            exit={{ opacity: 0, rotate: 45, scale: 0.5 }}
            transition={{ duration: 0.25 }}
          >
            <Moon className="text-yellow-300" size={22} />
          </motion.div>
        ) : (
          <motion.div
            key="sun"
            initial={{ opacity: 0, rotate: 45, scale: 0.5 }}
            animate={{ opacity: 1, rotate: 0, scale: 1 }}
            exit={{ opacity: 0, rotate: -45, scale: 0.5 }}
            transition={{ duration: 0.25 }}
          >
            <Sun className="text-amber-500" size={22} />
          </motion.div>
        )}
      </AnimatePresence>
    </motion.button>
  );
}
