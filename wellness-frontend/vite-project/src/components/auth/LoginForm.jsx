import React, { useState } from "react";
import { login } from "../../api/authApi";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

export default function LoginForm() {
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { loginUser } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const res = await login(form);
      loginUser(res.data);

      const role = res.data.role;
      if (role === "ADMIN") navigate("/admin");
      else if (role === "PRACTITIONER") navigate("/practitioner");
      else navigate("/");
    } catch {
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <motion.form
      onSubmit={handleSubmit}
      initial={{ opacity: 0, y: 30 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, ease: "easeOut" }}
      className="
        w-full max-w-md
        bg-white dark:bg-slate-900
        border border-slate-200 dark:border-slate-800
        p-8 rounded-2xl shadow-xl
      "
    >
      {/* HEADER */}
      <div className="text-center mb-6">
        <h2
          className="
            text-3xl font-extrabold
            text-slate-900 dark:text-white
          "
        >
          Welcome Back{" "}
          <span className="text-emerald-600 dark:text-emerald-400">🌿</span>
        </h2>

        <p className="text-slate-600 dark:text-slate-400 mt-2">
          Login to continue your wellness journey
        </p>
      </div>

      {/* ERROR MESSAGE */}
      {error && (
        <motion.p
          initial={{ x: 0 }}
          animate={{ x: [-8, 8, -6, 6, 0] }}
          transition={{ duration: 0.4 }}
          className="
            bg-red-50 dark:bg-red-900/20
            text-red-600 dark:text-red-400
            border border-red-200 dark:border-red-800
            p-3 rounded-lg text-center text-sm mb-4
          "
        >
          {error}
        </motion.p>
      )}

      {/* EMAIL INPUT */}
      <input
        type="email"
        name="email"
        placeholder="Email Address"
        onChange={handleChange}
        required
        className="
          w-full p-3 mb-4 rounded-xl
          bg-slate-100 dark:bg-slate-800
          border border-slate-300 dark:border-slate-700
          text-slate-900 dark:text-white
          placeholder-slate-500 dark:placeholder-slate-400
          focus:outline-none focus:ring-2 focus:ring-emerald-500
          transition
        "
      />

      {/* PASSWORD INPUT */}
      <input
        type="password"
        name="password"
        placeholder="Password"
        onChange={handleChange}
        required
        className="
          w-full p-3 mb-6 rounded-xl
          bg-slate-100 dark:bg-slate-800
          border border-slate-300 dark:border-slate-700
          text-slate-900 dark:text-white
          placeholder-slate-500 dark:placeholder-slate-400
          focus:outline-none focus:ring-2 focus:ring-emerald-500
          transition
        "
      />

      {/* SUBMIT BUTTON */}
      <motion.button
        whileTap={{ scale: 0.97 }}
        whileHover={{ scale: 1.02 }}
        disabled={loading}
        className="
          w-full py-3 rounded-xl font-semibold text-white
          bg-emerald-600 hover:bg-emerald-700
          transition shadow-md
          flex items-center justify-center gap-2
        "
      >
        {loading ? (
          <motion.span
            className="h-5 w-5 border-2 border-white border-t-transparent rounded-full"
            animate={{ rotate: 360 }}
            transition={{ repeat: Infinity, duration: 1, ease: "linear" }}
          />
        ) : (
          "Sign In"
        )}
      </motion.button>

      {/* FOOTER */}
      <p className="text-center text-sm mt-4 text-slate-600 dark:text-slate-400">
        New to the platform?{" "}
        <span
          onClick={() => navigate("/register")}
          className="text-emerald-600 dark:text-emerald-400 font-semibold cursor-pointer hover:underline"
        >
          Create one
        </span>
      </p>
    </motion.form>
  );
}
