
import React from "react";
import { Container, Typography, Box, Link as MuiLink } from "@mui/material";
import { Link } from "react-router-dom";
import { useThemeMode } from "../../context/ThemeContext";

export default function Footer() {
  const { mode } = useThemeMode();
  const dark = mode === "dark";

  return (
    <Box
      component="footer"
      sx={{
        mt: 10,
        py: 6,
        borderTop: "1px solid",
        borderColor: dark
          ? "rgba(255,255,255,0.08)"
          : "rgba(0,0,0,0.08)",
        bgcolor: dark ? "#020617" : "#f8fafc",
      }}
    >
      <Container maxWidth="lg">
        <Box
          sx={{
            display: "grid",
            gridTemplateColumns: { xs: "1fr", md: "2fr 1fr 1fr" },
            gap: 4,
          }}
        >
          {/* BRAND */}
          <Box>
            <Typography variant="h6" fontWeight={800} gutterBottom>
              Wellness Marketplace
            </Typography>
            <Typography sx={{ opacity: 0.7, maxWidth: 320 }}>
              Discover trusted alternative therapies and expert practitioners
              for a healthier, balanced life.
            </Typography>
          </Box>

          {/* QUICK LINKS */}
          <Box>
            <Typography fontWeight={700} gutterBottom>
              Quick Links
            </Typography>
            <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
              <MuiLink component={Link} to="/" underline="none">
                Home
              </MuiLink>
              <MuiLink component={Link} to="/login" underline="none">
                Login
              </MuiLink>
              <MuiLink component={Link} to="/register" underline="none">
                Register
              </MuiLink>
            </Box>
          </Box>

          {/* LEGAL */}
          <Box>
            <Typography fontWeight={700} gutterBottom>
              Legal
            </Typography>
            <Typography sx={{ opacity: 0.6 }}>
              © {new Date().getFullYear()} Wellness Marketplace
            </Typography>
            <Typography sx={{ opacity: 0.6 }}>
              All rights reserved.
            </Typography>
          </Box>
        </Box>
      </Container>
    </Box>
  );
}
