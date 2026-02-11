import { useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../utils/token";

const API = axios.create({ baseURL: "http://localhost:8080/api" });
API.interceptors.request.use(c => {
  c.headers.Authorization = `Bearer ${getToken()}`;
  return c;
});

export default function PractitionerAvailabilityPage() {
  const [slots, setSlots] = useState([]);
  const [date, setDate] = useState("");
  const [time, setTime] = useState("");

  useEffect(() => {
    API.get("/practitioner/availability").then(res => setSlots(res.data));
  }, []);

  const addSlot = async () => {
    await API.post("/practitioner/availability", { date, timeSlot: time });
    alert("Slot added");
    window.location.reload();
  };

  return (
    <div className="min-h-screen px-6 py-24">
      <div className="max-w-3xl mx-auto">
        <h1 className="text-3xl font-black mb-6">Availability Dashboard</h1>

        <div className="flex gap-4 mb-6">
          <input type="date" className="input" onChange={e => setDate(e.target.value)} />
          <input className="input" placeholder="Time Slot" onChange={e => setTime(e.target.value)} />
          <button onClick={addSlot} className="btn-primary">Add</button>
        </div>

        <ul className="space-y-3">
          {slots.map(s => (
            <li key={s.id} className="p-4 bg-white dark:bg-slate-900 rounded-xl">
              {s.date} • {s.timeSlot}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
