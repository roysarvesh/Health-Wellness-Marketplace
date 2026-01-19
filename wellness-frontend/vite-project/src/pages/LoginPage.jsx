import React, { useState } from "react";
import {
  TextField,
  Button,
  Paper,
  Typography,
  InputAdornment,
  IconButton,
  Box,
} from "@mui/material";
import LoginIcon from "@mui/icons-material/Login";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import AuthLayout from "../layouts/AuthLayout";
import { login } from "../utils/auth";
import { setToken } from "../utils/token";

export default function LoginPage() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const res = await login(email, password);
      setToken(res.token);
      navigate("/");
    } catch {
      setError("Invalid email or password");
    }
  };

  return (
    <AuthLayout>
      <motion.div
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6, ease: "easeOut" }}
        className="w-full"
      >
        <Paper
          elevation={0}
          className="
            w-full p-8 md:p-12 rounded-[2.5rem]
            bg-white dark:bg-slate-900
            border border-slate-200 dark:border-slate-800
            shadow-[0_20px_50px_rgba(0,0,0,0.12)]
          "
        >
          {/* HEADER */}
          <Box className="text-center mb-10">
            <Typography
              variant="h4"
              sx={{
                fontWeight: 900,
                color: "rgb(15 23 42)", // slate-900
                ".dark &": { color: "#ffffff" },
              }}
              className="tracking-tight mb-2"
            >
              Welcome Back <span className="text-emerald-500">🌿</span>
            </Typography>

            <Typography className="text-slate-600 dark:text-slate-400 font-medium">
              Login to continue your wellness journey
            </Typography>
          </Box>

          {/* ERROR */}
          {error && (
            <motion.div
              initial={{ opacity: 0, y: -5 }}
              animate={{ opacity: 1, y: 0 }}
              className="
                bg-red-50 dark:bg-red-900/20
                text-red-600 dark:text-red-400
                border border-red-200 dark:border-red-800
                p-4 rounded-2xl text-center mb-6 font-semibold
              "
            >
              {error}
            </motion.div>
          )}

          {/* FORM */}
          <form onSubmit={handleSubmit} className="space-y-6">
            <TextField
              fullWidth
              label="Email Address"
              variant="outlined"
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              sx={{
                "& .MuiOutlinedInput-root": {
                  borderRadius: "1rem",
                },
              }}
            />

            <TextField
              fullWidth
              label="Password"
              variant="outlined"
              type={showPassword ? "text" : "password"}
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              sx={{
                "& .MuiOutlinedInput-root": {
                  borderRadius: "1rem",
                },
              }}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowPassword(!showPassword)}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />

            <Button
              fullWidth
              size="large"
              type="submit"
              startIcon={<LoginIcon />}
              variant="contained"
              className="
                !bg-emerald-600 hover:!bg-emerald-700
                !rounded-2xl !py-4 !font-bold !text-lg
                shadow-lg transition
              "
            >
              Sign In
            </Button>
          </form>

          {/* FOOTER */}
          <Typography className="text-center mt-10 text-slate-600 dark:text-slate-400 font-medium">
            New to the platform?
            <Link
              to="/register"
              className="ml-1 text-emerald-600 dark:text-emerald-400 font-bold hover:underline"
            >
              Create one
            </Link>
          </Typography>
        </Paper>
      </motion.div>
    </AuthLayout>
  );
}
