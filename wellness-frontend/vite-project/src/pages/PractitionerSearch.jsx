import { useEffect, useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { getToken } from "../utils/token";
import { useNavigate } from "react-router-dom";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function PractitionerSearch() {
  const navigate = useNavigate();

  /* ===============================
     STATE
  =============================== */
  const [lat, setLat] = useState(null);
  const [lng, setLng] = useState(null);
  const [radius, setRadius] = useState(10);

  const [query, setQuery] = useState("");
  const [specialization, setSpecialization] = useState("");
  const [verifiedOnly, setVerifiedOnly] = useState(false);

  const [sortBy, setSortBy] = useState("distance");
  const [sortDir, setSortDir] = useState("asc");

  const [loading, setLoading] = useState(true);
  const [practitioners, setPractitioners] = useState([]);

  /* ===============================
     GET USER LOCATION
  =============================== */
  useEffect(() => {
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        setLat(pos.coords.latitude);
        setLng(pos.coords.longitude);
      },
      () => {
        // fallback location (Delhi)
        setLat(28.6139);
        setLng(77.209);
      }
    );
  }, []);

  /* ===============================
     FETCH PRACTITIONERS (BACKEND)
  =============================== */
  useEffect(() => {
    if (!lat || !lng) return;

    const timeout = setTimeout(fetchPractitioners, 400);
    return () => clearTimeout(timeout);
  }, [lat, lng, radius, query, specialization, verifiedOnly, sortBy, sortDir]);

  const fetchPractitioners = async () => {
    setLoading(true);
    try {
      const res = await API.get("/location/practitioners/search", {
        params: {
          lat,
          lng,
          radius,
          q: query || null,
          specialization: specialization || null,
          verified: verifiedOnly,
          sortBy,
          sortDir,
        },
      });
      setPractitioners(res.data);
    } catch (err) {
      console.error("Failed to fetch practitioners", err);
    } finally {
      setLoading(false);
    }
  };

  /* ===============================
     UI
  =============================== */
  return (
    <div className="min-h-screen px-6 py-24 bg-slate-50 dark:bg-slate-950 transition-colors">

      {/* HEADER */}
      <div className="max-w-7xl mx-auto mb-12 text-center">
        <h1 className="text-4xl font-black text-slate-900 dark:text-white">
          Find Wellness Practitioners
        </h1>
        <p className="mt-2 text-slate-600 dark:text-slate-300">
          Location-based discovery with trusted experts
        </p>
      </div>

      {/* FILTER PANEL */}
      <div
        className="max-w-7xl mx-auto mb-10
                   bg-white/90 dark:bg-slate-900/80
                   backdrop-blur-xl
                   border border-slate-200 dark:border-slate-700
                   rounded-2xl p-6 grid
                   md:grid-cols-6 gap-4"
      >
        {/* SEARCH */}
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search by name or city..."
          className="input md:col-span-2"
        />

        {/* SPECIALIZATION */}
        <select
          value={specialization}
          onChange={(e) => setSpecialization(e.target.value)}
          className="input"
        >
          <option value="">All Specializations</option>
          <option value="YOGA">Yoga</option>
          <option value="AYURVEDA">Ayurveda</option>
          <option value="THERAPY">Therapy</option>
          <option value="NUTRITION">Nutrition</option>
        </select>

        {/* RADIUS */}
        <div>
          <label className="text-xs text-slate-500">
            Radius: {radius} km
          </label>
          <input
            type="range"
            min="1"
            max="50"
            step="1"
            value={radius}
            onChange={(e) => setRadius(e.target.value)}
            className="w-full"
          />
        </div>

        {/* VERIFIED */}
        <label className="flex items-center gap-2 text-sm">
          <input
            type="checkbox"
            checked={verifiedOnly}
            onChange={(e) => setVerifiedOnly(e.target.checked)}
          />
          Verified only
        </label>

        {/* SORT */}
        <select
          value={`${sortBy}-${sortDir}`}
          onChange={(e) => {
            const [by, dir] = e.target.value.split("-");
            setSortBy(by);
            setSortDir(dir);
          }}
          className="input"
        >
          <option value="distance-asc">Nearest first</option>
          <option value="rating-desc">Top rated</option>
        </select>
      </div>

      {/* RESULTS */}
      <div className="max-w-7xl mx-auto grid sm:grid-cols-2 lg:grid-cols-3 gap-8">

        {loading && (
          <p className="col-span-full text-center text-slate-500">
            Searching practitioners...
          </p>
        )}

        {!loading && practitioners.length === 0 && (
          <p className="col-span-full text-center text-slate-500">
            No practitioners found
          </p>
        )}

        {practitioners.map((p) => (
          <motion.div
            key={p.id}
            whileHover={{ y: -6 }}
            className="bg-white/90 dark:bg-slate-900/80
                       rounded-2xl
                       border border-slate-200 dark:border-slate-700
                       shadow-lg p-6 flex flex-col"
          >
            {/* AVATAR */}
            <div
              className="h-24 w-24 rounded-full mx-auto mb-4
                         bg-gradient-to-br from-emerald-400 to-emerald-600
                         flex items-center justify-center
                         text-white text-2xl font-bold"
            >
              {p.name.charAt(0)}
            </div>

            {/* INFO */}
            <h3 className="text-xl font-bold text-center text-slate-900 dark:text-white">
              {p.name}
            </h3>

            <p className="text-center text-slate-600 dark:text-slate-300">
              {p.specialization}
            </p>

            <div className="mt-4 flex justify-between text-sm">
              <span>⭐ {p.rating || "N/A"}</span>
              <span>{p.distance.toFixed(1)} km</span>
            </div>

            {p.verified && (
              <span className="mt-2 text-xs text-center
                               bg-emerald-100 text-emerald-700
                               dark:bg-emerald-900 dark:text-emerald-300
                               px-3 py-1 rounded-full">
                Verified Practitioner
              </span>
            )}

            {/* ACTIONS */}
            <div className="mt-6 flex gap-3">
              <button
                onClick={() => navigate(`/practitioners/${p.id}`)}
                className="flex-1 py-2 rounded-xl
                           border border-slate-300 dark:border-slate-600
                           hover:bg-slate-100 dark:hover:bg-slate-800
                           font-semibold transition"
              >
                View Profile
              </button>

              <button
                onClick={() => navigate(`/chat?user=${p.id}`)}
                className="flex-1 py-2 rounded-xl
                           bg-emerald-600 hover:bg-emerald-700
                           text-white font-bold transition"
              >
                Message
              </button>
            </div>
          </motion.div>
        ))}
      </div>
    </div>
  );
}
