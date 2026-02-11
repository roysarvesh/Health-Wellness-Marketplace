import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { getToken } from "../utils/token";

// API Setup
const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function AIRecommendationsPage() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAI = async () => {
      try {
        const res = await API.post("/ai/recommendations", {
          symptoms: ["stress", "pain", "anxiety"]   // TEMP FIX
        });

        // Backend returns a single object, convert to array if needed
        setData(res.data.recommendations || []);
      } catch (err) {
        console.error("AI Recommendation Error:", err);
      }
      setLoading(false);
    };

    fetchAI();
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50 dark:bg-slate-950">
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="text-lg text-slate-600 dark:text-slate-300"
        >
          Generating AI insights...
        </motion.div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 py-24 px-4">
      <div className="max-w-6xl mx-auto">

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center mb-12"
        >
          <div className="text-5xl mb-3">🤖</div>
          <h1 className="text-4xl font-black text-slate-900 dark:text-white">
            AI Wellness Recommendations
          </h1>
          <p className="text-slate-600 dark:text-slate-300 mt-2">
            Personalized therapy & practitioner suggestions powered by AI
          </p>
        </motion.div>

        {data.length === 0 && (
          <div className="text-center text-slate-500">
            No recommendations available yet.
          </div>
        )}

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
          {data.map((item, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              className="bg-white dark:bg-slate-900 p-6 rounded-2xl shadow-xl"
            >
              <h3 className="text-xl font-bold text-slate-900 dark:text-white">
                {item.title}
              </h3>

              <p className="text-sm text-slate-600 dark:text-slate-300 mt-2">
                {item.description}
              </p>

              {item.score && (
                <p className="mt-4 font-bold text-emerald-600">
                  AI Score: {item.score}%
                </p>
              )}

              {item.practitionerId && (
                <button
                  onClick={() =>
                    navigate(`/chat?practitioner=${item.practitionerId}`)
                  }
                  className="mt-4 text-blue-600 hover:underline"
                >
                  💬 Chat with Practitioner
                </button>
              )}
            </motion.div>
          ))}
        </div>

      </div>
    </div>
  );
}
