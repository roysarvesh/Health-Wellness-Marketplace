import { useEffect, useState } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import { getToken } from "../utils/token";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function OrderDetailsPage() {
  // Support both :id and :orderId routes
  const params = useParams();
  const orderId = params.orderId || params.id;

  const navigate = useNavigate();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrder();
  }, [orderId]);

  const fetchOrder = async () => {
    try {
      const res = await API.get(`/orders/${orderId}`);
      setOrder(res.data);
    } catch (err) {
      console.error("Failed to fetch order details:", err);
    } finally {
      setLoading(false);
    }
  };

  /* ============================
     LOADING STATE
  ============================ */
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-lg">
        Loading order details...
      </div>
    );
  }

  /* ============================
     ORDER NOT FOUND
  ============================ */
  if (!order) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center text-center">
        <h2 className="text-2xl font-bold mb-3">Order Not Found</h2>
        <button
          onClick={() => navigate("/orders")}
          className="text-blue-600 hover:underline"
        >
          ← Back to Orders
        </button>
      </div>
    );
  }

  /* ============================
     ORDER DETAILS UI
  ============================ */
  return (
    <div className="min-h-screen px-6 py-10 bg-gray-50 dark:bg-[#020617] text-gray-900 dark:text-white">
      <button
        onClick={() => navigate("/orders")}
        className="text-blue-600 mb-6 font-semibold hover:underline"
      >
        ← Back to Orders
      </button>

      <h1 className="text-3xl font-bold mb-6">Order #{order.id}</h1>

      {/* ORDER SUMMARY CARD */}
      <div className="bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-6 shadow-sm mb-8">
        <div className="flex justify-between mb-4">
          <span className="text-gray-600 dark:text-gray-400">Status</span>
          <span className="font-semibold">{order.status}</span>
        </div>

        <div className="flex justify-between mb-4">
          <span className="text-gray-600 dark:text-gray-400">Payment Method</span>
          <span>{order.paymentMethod || "N/A"}</span>
        </div>

        <div className="flex justify-between mb-4">
          <span className="text-gray-600 dark:text-gray-400">Placed On</span>
          <span>{new Date(order.createdAt).toLocaleString()}</span>
        </div>

        <div className="flex justify-between">
          <span className="text-lg font-semibold">Total Amount</span>
          <span className="text-xl font-bold text-emerald-600">
            ₹{order.totalAmount}
          </span>
        </div>
      </div>

      {/* ITEMS LIST */}
      <h2 className="text-xl font-semibold mb-4">Items</h2>

      <div className="space-y-4">
        {order.items.map((item) => (
          <div
            key={item.product.id}
            className="flex justify-between items-center bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-4 shadow-sm"
          >
            <div>
              <p className="font-semibold">{item.product.name}</p>
              <p className="text-sm text-gray-500 dark:text-gray-400">
                Qty: {item.quantity}
              </p>
            </div>

            <p className="font-semibold">
              ₹{item.product.price * item.quantity}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}
