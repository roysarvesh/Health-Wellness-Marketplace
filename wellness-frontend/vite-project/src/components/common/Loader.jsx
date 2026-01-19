import React from "react";
import { Box, CircularProgress, Typography } from "@mui/material";
import { useThemeMode } from "../../context/ThemeContext";

export default function Loader({ label = "Loading..." }) {
  const { mode } = useThemeMode();
  const dark = mode === "dark";

  return (
    <Box
      sx={{
        minHeight: "60vh",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        gap: 2,
      }}
    >
      <CircularProgress
        size={48}
        thickness={4}
        sx={{
          color: dark ? "#22c55e" : "#16a34a",
        }}
      />
      <Typography sx={{ opacity: 0.7 }}>{label}</Typography>
    </Box>
  );
}
