import { useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../utils/token";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function FitnessDashboard() {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);

  const [type, setType] = useState("");
  const [duration, setDuration] = useState("");
  const [calories, setCalories] = useState("");

  useEffect(() => {
    fetchActivities();
  }, []);

  const fetchActivities = async () => {
    try {
      const res = await API.get("/fitness/activities");
      setActivities(res.data);
    } catch (err) {
      console.error("Failed to load fitness data");
    } finally {
      setLoading(false);
    }
  };

  const addActivity = async () => {
    if (!type || !duration || !calories) return;

    await API.post("/fitness/activities", {
      type,
      duration,
      caloriesBurned: calories,
    });

    setType("");
    setDuration("");
    setCalories("");
    fetchActivities();
  };

  const totalCalories = activities.reduce(
    (sum, a) => sum + a.caloriesBurned,
    0
  );

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-lg">
        Loading fitness dashboard...
      </div>
    );
  }

  return (
    <div className="min-h-screen px-6 py-10 bg-gray-50 dark:bg-[#020617] text-gray-900 dark:text-white">
      <h1 className="text-3xl font-bold mb-8">Fitness Dashboard</h1>

      {/* STATS */}
      <div className="grid md:grid-cols-3 gap-6 mb-10">
        <StatCard title="Total Activities" value={activities.length} />
        <StatCard title="Total Calories Burned" value={`${totalCalories} kcal`} />
        <StatCard
          title="Active Days"
          value={new Set(activities.map(a => a.date)).size}
        />
      </div>

      {/* ADD ACTIVITY */}
      <div className="bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-6 mb-10 shadow-sm">
        <h2 className="text-xl font-semibold mb-4">Add Activity</h2>

        <div className="grid md:grid-cols-3 gap-4">
          <input
            className="input"
            placeholder="Activity (Running, Yoga)"
            value={type}
            onChange={(e) => setType(e.target.value)}
          />

          <input
            className="input"
            type="number"
            placeholder="Duration (minutes)"
            value={duration}
            onChange={(e) => setDuration(e.target.value)}
          />

          <input
            className="input"
            type="number"
            placeholder="Calories Burned"
            value={calories}
            onChange={(e) => setCalories(e.target.value)}
          />
        </div>

        <button
          onClick={addActivity}
          className="mt-4 px-6 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700"
        >
          Add Activity
        </button>
      </div>

      {/* ACTIVITY LIST */}
      <h2 className="text-2xl font-semibold mb-4">Activity History</h2>

      <div className="space-y-4">
        {activities.map((a) => (
          <div
            key={a.id}
            className="flex justify-between items-center bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-4"
          >
            <div>
              <p className="font-semibold">{a.type}</p>
              <p className="text-sm text-gray-500">
                {new Date(a.date).toLocaleDateString()}
              </p>
            </div>

            <div className="text-right">
              <p className="font-semibold">{a.duration} min</p>
              <p className="text-sm text-gray-500">
                {a.caloriesBurned} kcal
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

/* ---------- Reusable Stat Card ---------- */
function StatCard({ title, value }) {
  return (
    <div className="bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-6 shadow-sm">
      <p className="text-sm text-gray-500 dark:text-gray-400">{title}</p>
      <p className="text-2xl font-bold mt-2">{value}</p>
    </div>
  );
}
