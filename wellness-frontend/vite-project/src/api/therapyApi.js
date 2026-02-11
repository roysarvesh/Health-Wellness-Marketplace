import axios from "axios";
import { getToken } from "../utils/token";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((req) => {
  const token = getToken();
  if (token) req.headers.Authorization = `Bearer ${token}`;
  return req;
});

/* =======================
   PRACTITIONERS
======================= */
export const getPractitioners = async () => {
  const res = await API.get("/practitioners");
  return res.data;
};

/* =======================
   CREATE BOOKING
======================= */
export const bookTherapySession = async (data) => {
  const res = await API.post("/therapy-sessions", data);
  return res.data;
};

/* =======================
   USER BOOKINGS
======================= */
export const getMyBookings = async () => {
  const res = await API.get("/therapy-sessions/my");
  return res.data;
};

/* =======================
   CANCEL / RESCHEDULE
======================= */
export const cancelBooking = async (id) => {
  await API.put(`/therapy-sessions/${id}/cancel`);

  
};

export const rescheduleBooking = async (id, payload) => {
  await API.put(`/therapy-sessions/${id}/reschedule`, payload);
};

