import React, { useEffect, useState } from "react";
import {
  getMyBookings,
  cancelBooking,
  rescheduleBooking,
} from "../api/therapyApi";
import RescheduleModal from "../components/RescheduleModal";

export default function BookingHistoryPage() {
  const [bookings, setBookings] = useState([]);
  const [selected, setSelected] = useState(null);

  const loadBookings = async () => {
    const data = await getMyBookings();
    setBookings(data);
  };

  useEffect(() => {
    loadBookings();
  }, []);

  const handleReschedule = async (payload) => {
    await rescheduleBooking(selected.id, payload);
    setSelected(null);
    loadBookings();
  };

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 py-24 px-4">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-black mb-6 text-slate-900 dark:text-white">
          📅 My Therapy Sessions
        </h1>

        <div className="space-y-4">
          {bookings.map((b) => (
            <div
              key={b.id}
              className="bg-white dark:bg-slate-900 p-6 rounded-xl
                         border border-slate-200 dark:border-slate-700 shadow"
            >
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="font-bold">{b.practitionerName}</h3>
                  <p className="text-sm text-slate-600 dark:text-slate-300">
                    {b.date} at {b.time}
                  </p>
                  <p className="text-sm">
                    Status: <b>{b.status}</b>
                  </p>
                </div>

                {b.status === "SCHEDULED" && (
                  <div className="flex gap-4">
                    <button
                      onClick={() => setSelected(b)}
                      className="text-blue-600 hover:underline"
                    >
                      Reschedule
                    </button>

                    <button
                      onClick={() => cancelBooking(b.id)}
                      className="text-red-600 hover:underline"
                    >
                      Cancel
                    </button>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      {selected && (
        <RescheduleModal
          booking={selected}
          onClose={() => setSelected(null)}
          onSubmit={handleReschedule}
        />
      )}
    </div>
  );
}
