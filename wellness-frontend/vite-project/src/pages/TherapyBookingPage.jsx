import { useEffect, useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { getToken } from "../utils/token";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function TherapyBookingPage() {
  const [practitioners, setPractitioners] = useState([]);
  const [selectedPractitioner, setSelectedPractitioner] = useState(null);

  const [date, setDate] = useState("");
  const [slots, setSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);

  const [loading, setLoading] = useState(true);
  const [booking, setBooking] = useState(false);

  /* ===============================
     LOAD PRACTITIONERS
  =============================== */
  useEffect(() => {
    fetchPractitioners();
  }, []);

  const fetchPractitioners = async () => {
    try {
      const res = await API.get("/therapy/practitioners");
      setPractitioners(res.data);
    } catch (err) {
      console.error("Failed to load practitioners");
    } finally {
      setLoading(false);
    }
  };

  /* ===============================
     LOAD AVAILABILITY
  =============================== */
  const fetchAvailability = async (practitionerId, selectedDate) => {
    try {
      const res = await API.get(
        `/therapy/practitioners/${practitionerId}/availability`,
        { params: { date: selectedDate } }
      );
      setSlots(res.data);
    } catch {
      setSlots([]);
    }
  };

  /* ===============================
     BOOK SESSION
  =============================== */
  const bookSession = async () => {
    if (!selectedPractitioner || !selectedSlot || !date) return;

    setBooking(true);
    try {
      await API.post("/therapy/book", {
        practitionerId: selectedPractitioner.id,
        date,
        timeSlot: selectedSlot,
      });

      alert("Therapy session booked successfully!");
      setSelectedSlot(null);
      setSlots([]);
    } catch {
      alert("Booking failed. Try again.");
    } finally {
      setBooking(false);
    }
  };

  /* ===============================
     UI
  =============================== */
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-lg">
        Loading therapy booking...
      </div>
    );
  }

  return (
    <div className="min-h-screen px-6 py-24 bg-slate-50 dark:bg-slate-950 transition-colors">

      {/* HEADER */}
      <div className="max-w-5xl mx-auto mb-12 text-center">
        <h1 className="text-4xl font-black text-slate-900 dark:text-white">
          Book a Therapy Session
        </h1>
        <p className="mt-2 text-slate-600 dark:text-slate-300">
          Choose a practitioner and schedule your session
        </p>
      </div>

      <div className="max-w-5xl mx-auto space-y-10">

        {/* PRACTITIONER SELECT */}
        <section className="bg-white/90 dark:bg-slate-900/80
                            backdrop-blur-xl border border-slate-200 dark:border-slate-700
                            rounded-2xl p-6">
          <h2 className="text-xl font-bold mb-4">Select Practitioner</h2>

          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {practitioners.map((p) => (
              <motion.button
                key={p.id}
                whileHover={{ scale: 1.03 }}
                onClick={() => {
                  setSelectedPractitioner(p);
                  setSlots([]);
                  setSelectedSlot(null);
                }}
                className={`p-4 rounded-xl border text-left transition
                  ${
                    selectedPractitioner?.id === p.id
                      ? "border-emerald-600 bg-emerald-50 dark:bg-emerald-900/30"
                      : "border-slate-300 dark:border-slate-700 hover:bg-slate-100 dark:hover:bg-slate-800"
                  }`}
              >
                <p className="font-semibold">{p.name}</p>
                <p className="text-sm text-slate-600 dark:text-slate-300">
                  {p.specialization}
                </p>
                <p className="text-xs mt-1">⭐ {p.rating || "N/A"}</p>
              </motion.button>
            ))}
          </div>
        </section>

        {/* DATE PICKER */}
        {selectedPractitioner && (
          <section className="bg-white/90 dark:bg-slate-900/80
                              border border-slate-200 dark:border-slate-700
                              rounded-2xl p-6">
            <h2 className="text-xl font-bold mb-4">Select Date</h2>

            <input
              type="date"
              value={date}
              onChange={(e) => {
                setDate(e.target.value);
                fetchAvailability(selectedPractitioner.id, e.target.value);
              }}
              className="input max-w-xs"
            />
          </section>
        )}

        {/* TIME SLOTS */}
        {slots.length > 0 && (
          <section className="bg-white/90 dark:bg-slate-900/80
                              border border-slate-200 dark:border-slate-700
                              rounded-2xl p-6">
            <h2 className="text-xl font-bold mb-4">Available Slots</h2>

            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
              {slots.map((slot) => (
                <button
                  key={slot}
                  onClick={() => setSelectedSlot(slot)}
                  className={`py-2 rounded-xl font-semibold transition
                    ${
                      selectedSlot === slot
                        ? "bg-emerald-600 text-white"
                        : "bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700"
                    }`}
                >
                  {slot}
                </button>
              ))}
            </div>
          </section>
        )}

        {/* CONFIRM */}
        {selectedSlot && (
          <button
            onClick={bookSession}
            disabled={booking}
            className="w-full py-4 rounded-2xl
                       bg-emerald-600 hover:bg-emerald-700
                       disabled:bg-slate-400
                       text-white font-bold text-lg"
          >
            {booking ? "Booking..." : "Confirm Booking"}
          </button>
        )}
      </div>
    </div>
  );
}
