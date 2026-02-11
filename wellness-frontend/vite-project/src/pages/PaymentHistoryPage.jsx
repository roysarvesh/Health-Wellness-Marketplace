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

export default function PaymentHistoryPage() {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchPayments();
  }, []);

  const fetchPayments = async () => {
    try {
      const res = await API.get("/payments/history");
      setPayments(res.data);
    } catch (err) {
      console.error("Failed to load payments");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-lg">
        Loading payment history...
      </div>
    );
  }

  if (payments.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <h2 className="text-xl font-semibold">
          No payment records found 💳
        </h2>
      </div>
    );
  }

  return (
    <div className="min-h-screen px-6 py-10 bg-gray-50 dark:bg-[#020617] text-gray-900 dark:text-white">
      <h1 className="text-3xl font-bold mb-8">Payment History</h1>

      <div className="overflow-x-auto">
        <table className="w-full border border-gray-200 dark:border-gray-800 rounded-xl overflow-hidden">
          <thead className="bg-gray-100 dark:bg-gray-800">
            <tr>
              <th className="p-4 text-left">Payment ID</th>
              <th className="p-4 text-left">Order ID</th>
              <th className="p-4 text-left">Method</th>
              <th className="p-4 text-left">Status</th>
              <th className="p-4 text-right">Amount</th>
              <th className="p-4 text-left">Date</th>
            </tr>
          </thead>

          <tbody>
            {payments.map((p) => (
              <tr
                key={p.id}
                className="border-t dark:border-gray-800"
              >
                <td className="p-4 font-semibold">#{p.id}</td>
                <td className="p-4">#{p.orderId}</td>
                <td className="p-4">{p.method}</td>
                <td className="p-4">
                  <span
                    className={`px-3 py-1 rounded-full text-sm font-semibold
                      ${
                        p.status === "SUCCESS"
                          ? "bg-green-100 text-green-700"
                          : "bg-red-100 text-red-700"
                      }`}
                  >
                    {p.status}
                  </span>
                </td>
                <td className="p-4 text-right font-bold">
                  ₹{p.amount}
                </td>
                <td className="p-4 text-sm">
                  {new Date(p.createdAt).toLocaleString()}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
