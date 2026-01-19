import React, { useEffect } from "react";
import { Button, Typography, Grid, Box } from "@mui/material";
import { Link } from "react-router-dom";
import PsychologyIcon from "@mui/icons-material/Psychology";
import SpaIcon from "@mui/icons-material/Spa";
import AutoAwesomeIcon from "@mui/icons-material/AutoAwesome";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import { motion } from "framer-motion";

// Shared max-width wrapper
const CONTENT_MAX_WIDTH = "max-w-[1400px] mx-auto";

export default function HomePage() {
  // Safety: prevent accidental horizontal scroll
  useEffect(() => {
    document.documentElement.style.overflowX = "hidden";
    document.body.style.overflowX = "hidden";
  }, []);

  return (
    <main className="min-h-screen w-full">

      {/* ================= HERO SECTION ================= */}
      <section className="pt-32 pb-24 w-full">
        <div className={`${CONTENT_MAX_WIDTH} w-full px-4 md:px-8`}>
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="relative w-full overflow-hidden rounded-3xl"
          >
            {/* Subtle glow */}
            <div className="absolute -top-12 -right-12 w-72 h-72 bg-emerald-200/40 dark:bg-emerald-900/20 rounded-full blur-3xl -z-10" />

            {/* HERO IMAGE */}
            <Box className="relative w-full rounded-3xl overflow-hidden shadow-2xl border border-slate-200 dark:border-slate-800">
              <img
                src="/homepage2.png"
                alt="Wellness Hero"
                className="w-full h-[480px] object-cover object-center"
              />
            </Box>

            {/* CTA */}
            <div className="flex justify-start mt-10 pl-2">
              <Button
                component={Link}
                to="/register"
                size="large"
                variant="contained"
                endIcon={<ArrowForwardIcon />}
                className="!bg-emerald-600 hover:!bg-emerald-700 
                           !text-white !rounded-full !px-12 !py-4 
                           !font-semibold tracking-wide shadow-xl"
              >
                Find Your Expert
              </Button>
            </div>
          </motion.div>
        </div>
      </section>

      {/* ================= WHY WE ARE DIFFERENT ================= */}
      <section className="py-24 w-full">
        <div className={`${CONTENT_MAX_WIDTH} w-full px-4 md:px-8`}>

          {/* SECTION TITLE */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.6 }}
            className="mb-16"
          >
            <Typography
              variant="h2"
              className="font-extrabold tracking-tight text-slate-900 dark:text-white"
              sx={{ fontFamily: "'Inter', sans-serif" }}
            >
              Why We Are Different
            </Typography>

            <div className="mt-4 w-24 h-1 bg-emerald-500 rounded-full" />
          </motion.div>

          {/* FEATURE CARDS */}
          <Grid container spacing={6}>
            {[
              {
                icon: <PsychologyIcon sx={{ fontSize: 36 }} />,
                title: "Verified Experts",
                desc: "Carefully vetted professionals you can trust.",
              },
              {
                icon: <SpaIcon sx={{ fontSize: 36 }} />,
                title: "Holistic Therapies",
                desc: "Yoga, Ayurveda & meditation under one roof.",
              },
              {
                icon: <AutoAwesomeIcon sx={{ fontSize: 36 }} />,
                title: "AI Matchmaking",
                desc: "Smart, personalized therapy recommendations.",
              },
            ].map((item, index) => (
              <Grid item xs={12} md={4} key={index}>
                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  whileInView={{ opacity: 1, y: 0 }}
                  viewport={{ once: true }}
                  transition={{ delay: index * 0.15 }}
                  whileHover={{ y: -6 }}
                  className="
                    h-full p-8 rounded-2xl flex flex-col gap-4
                    bg-white dark:bg-slate-900
                    border border-slate-200 dark:border-slate-800
                    shadow-lg shadow-slate-200/60
                    dark:shadow-black/40
                    hover:shadow-xl
                    transition-all duration-300
                  "
                >
                  <motion.div
                    whileHover={{ scale: 1.15, rotate: 2 }}
                    className="text-emerald-600"
                  >
                    {item.icon}
                  </motion.div>

                  <Typography
                    variant="h6"
                    className="font-semibold tracking-wide text-slate-900 dark:text-white"
                  >
                    {item.title}
                  </Typography>

                  <Typography className="text-slate-600 dark:text-slate-400 leading-relaxed">
                    {item.desc}
                  </Typography>
                </motion.div>
              </Grid>
            ))}
          </Grid>
        </div>
      </section>

    </main>
  );
}
