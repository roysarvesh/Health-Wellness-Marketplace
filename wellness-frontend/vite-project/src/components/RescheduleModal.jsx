import React, { useState } from "react";
import { motion } from "framer-motion";

export default function RescheduleModal({ booking, onClose, onSubmit }) {
  const [date, setDate] = useState(booking.date);
  const [time, setTime] = useState(booking.time);

  return (
    <div className="fixed inset-0 z-50 bg-black/50 flex items-center justify-center">
      <motion.div
        initial={{ scale: 0.9, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        className="bg-white dark:bg-slate-900 rounded-2xl p-8 w-full max-w-md
                   shadow-xl border border-slate-200 dark:border-slate-700"
      >
        <h2 className="text-xl font-bold mb-4 text-slate-900 dark:text-white">
          🔁 Reschedule Session
        </h2>

        <div className="space-y-4">
          <input
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            className="input"
          />

          <input
            type="time"
            value={time}
            onChange={(e) => setTime(e.target.value)}
            className="input"
          />
        </div>

        <div className="flex gap-3 mt-6">
          <button
            onClick={onClose}
            className="flex-1 border rounded-xl py-2
                       border-slate-300 dark:border-slate-700"
          >
            Cancel
          </button>

          <button
            onClick={() => onSubmit({ date, time })}
            className="flex-1 bg-emerald-600 hover:bg-emerald-700
                       text-white rounded-xl py-2 font-bold"
          >
            Confirm
          </button>
        </div>
      </motion.div>
    </div>
  );
}
