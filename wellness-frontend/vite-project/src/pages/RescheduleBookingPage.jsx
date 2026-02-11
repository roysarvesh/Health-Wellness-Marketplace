import { useState } from "react";
import axios from "axios";
import { getToken } from "../utils/token";
import { useParams, useNavigate } from "react-router-dom";

const API = axios.create({ baseURL: "http://localhost:8080/api" });
API.interceptors.request.use(c => {
  c.headers.Authorization = `Bearer ${getToken()}`;
  return c;
});

export default function RescheduleBookingPage() {
  const { id } = useParams();
  const [date, setDate] = useState("");
  const [slot, setSlot] = useState("");
  const navigate = useNavigate();

  const reschedule = async () => {
    await API.put(`/therapy/bookings/${id}/reschedule`, {
      date, timeSlot: slot
    });
    alert("Rescheduled");
    navigate("/booking-history");
  };

  return (
    <Page title="Reschedule Session">
      <input type="date" className="input" onChange={e => setDate(e.target.value)} />
      <input className="input" placeholder="Time Slot" onChange={e => setSlot(e.target.value)} />
      <button onClick={reschedule} className="btn-primary mt-4">
        Confirm
      </button>
    </Page>
  );
}

const Page = ({ title, children }) => (
  <div className="min-h-screen px-6 py-24">
    <div className="max-w-md mx-auto space-y-4">
      <h1 className="text-2xl font-bold">{title}</h1>
      {children}
    </div>
  </div>
);
