import React, { useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../utils/token";
import { useNavigate } from "react-router-dom";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function OrderHistoryPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      // ✅ Correct backend endpoint
      const res = await API.get("/orders/my");

      // Backend returns Page<Order> => extract content
      setOrders(res.data.content || []);
    } catch (err) {
      console.error("❌ Failed to load orders:", err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-lg">
        Loading orders...
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center gap-4">
        <h2 className="text-2xl font-semibold">No orders yet 📦</h2>
        <button
          onClick={() => navigate("/products")}
          className="px-6 py-2 bg-blue-600 text-white rounded-lg"
        >
          Start Shopping
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen px-6 py-10 bg-gray-50 dark:bg-[#020617] text-gray-900 dark:text-white">
      <h1 className="text-3xl font-bold mb-8">My Orders</h1>

      <div className="space-y-6">
        {orders.map((order) => (
          <div
            key={order.id}
            className="bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-6 shadow-sm"
          >
            <div className="flex justify-between items-center mb-4">
              <div>
                <p className="font-semibold">Order #{order.id}</p>
                <p className="text-sm text-gray-500 dark:text-gray-400">
                  {new Date(order.createdAt).toLocaleString()}
                </p>
              </div>

              <span
                className={`px-4 py-1 rounded-full text-sm font-semibold
                  ${
                    order.status === "PAID"
                      ? "bg-green-100 text-green-700"
                      : "bg-yellow-100 text-yellow-700"
                  }`}
              >
                {order.status}
              </span>
            </div>

            <div className="flex justify-between items-center">
              <p className="text-sm">
                Items: <strong>{order.items.length}</strong>
              </p>

              <p className="font-bold text-lg">₹{order.totalAmount}</p>
            </div>

            <button
              onClick={() => navigate(`/orders/${order.id}`)}
              className="mt-4 text-blue-600 hover:underline text-sm font-semibold"
            >
              View Order Details →
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
