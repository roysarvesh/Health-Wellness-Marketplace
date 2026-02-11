import axios from "axios";
import { getToken } from "../utils/token";
import { useParams, useNavigate } from "react-router-dom";

const API = axios.create({ baseURL: "http://localhost:8080/api" });
API.interceptors.request.use(c => {
  c.headers.Authorization = `Bearer ${getToken()}`;
  return c;
});

export default function CancelBookingPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const cancelBooking = async () => {
    await API.put(`/therapy/bookings/${id}/cancel`);
    alert("Booking cancelled");
    navigate("/booking-history");
  };

  return (
    <ConfirmPage
      title="Cancel Booking"
      action={cancelBooking}
      danger
    />
  );
}

const ConfirmPage = ({ title, action, danger }) => (
  <div className="min-h-screen flex items-center justify-center">
    <div className="bg-white dark:bg-slate-900 p-8 rounded-2xl">
      <h2 className="text-xl font-bold mb-6">{title}</h2>
      <button
        onClick={action}
        className={`px-6 py-3 rounded-xl font-bold text-white
          ${danger ? "bg-red-600" : "bg-emerald-600"}`}>
        Confirm
      </button>
    </div>
  </div>
);
