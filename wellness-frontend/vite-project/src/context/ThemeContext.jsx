import React, { createContext, useContext, useMemo, useState, useEffect } from "react";
import { ThemeProvider as MuiThemeProvider, createTheme, CssBaseline } from "@mui/material";

const ThemeContext = createContext();
export const useThemeMode = () => useContext(ThemeContext);

export function ThemeProvider({ children }) {
  const [mode, setMode] = useState("light");

  useEffect(() => {
    const root = document.documentElement;
    root.classList.toggle("dark", mode === "dark");
  }, [mode]);

  const toggleTheme = () =>
    setMode((prev) => (prev === "light" ? "dark" : "light"));

  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode,
          primary: { main: "#22c55e" },
          background: {
            default: mode === "dark" ? "#020617" : "#ffffff", // NAVY / WHITE
            paper: mode === "dark" ? "#0f172a" : "#ffffff",
          },
        },
        typography: {
          fontFamily: `"Inter", system-ui, sans-serif`,
        },
        shape: { borderRadius: 14 },
      }),
    [mode]
  );

  return (
    <ThemeContext.Provider value={{ mode, toggleTheme }}>
      <MuiThemeProvider theme={theme}>
        <CssBaseline />
        {children}
      </MuiThemeProvider>
    </ThemeContext.Provider>
  );
}
