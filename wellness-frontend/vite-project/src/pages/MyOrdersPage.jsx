import React, { useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../../utils/token";
import { Link } from "react-router-dom";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function MyOrdersPage() {
  const [orders, setOrders] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrders();
  }, [page]);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      const res = await API.get(`/orders/my?page=${page}&size=5`);
      setOrders(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      console.error("Failed to fetch orders", err);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "PLACED":
        return "bg-blue-500";
      case "PROCESSING":
        return "bg-yellow-500";
      case "SHIPPED":
        return "bg-indigo-500";
      case "DELIVERED":
        return "bg-green-600";
      case "CANCELLED":
        return "bg-red-600";
      case "RETURN_REQUESTED":
        return "bg-orange-500";
      case "RETURNED":
        return "bg-gray-600";
      case "REFUND_REQUESTED":
        return "bg-purple-500";
      default:
        return "bg-gray-400";
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-[#020617] px-6 py-10">
      <h1 className="text-3xl font-bold mb-8 text-gray-900 dark:text-white">
        My Orders
      </h1>

      {loading ? (
        <p className="text-center text-gray-500">Loading orders...</p>
      ) : orders.length === 0 ? (
        <div className="text-center text-gray-600 dark:text-gray-400 mt-20">
          <h2 className="text-xl font-semibold">You haven't placed any orders yet.</h2>
          <Link
            to="/products"
            className="mt-4 inline-block px-6 py-3 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700"
          >
            Browse Products
          </Link>
        </div>
      ) : (
        <div className="space-y-6">
          {orders.map((order) => (
            <div
              key={order.id}
              className="bg-white dark:bg-slate-900 p-6 rounded-xl shadow-md border border-gray-200 dark:border-gray-700"
            >
              <div className="flex justify-between items-center">
                <h2 className="font-bold text-lg">
                  Order #{order.id}
                </h2>

                <span
                  className={`px-4 py-1 text-sm rounded-full text-white ${getStatusColor(
                    order.status
                  )}`}
                >
                  {order.status}
                </span>
              </div>

              <p className="text-gray-500 dark:text-gray-400 mt-2">
                Placed On: {new Date(order.createdAt).toLocaleString()}
              </p>

              <p className="font-semibold mt-3 text-emerald-600 text-lg">
                Total Amount: ₹{order.totalAmount}
              </p>

              <Link
                to={`/orders/${order.id}`}
                className="mt-4 inline-block text-blue-600 dark:text-blue-400 hover:underline font-medium"
              >
                View Order Details →
              </Link>
            </div>
          ))}
        </div>
      )}

      {/* PAGINATION */}
      {totalPages > 1 && (
        <div className="flex justify-center gap-4 mt-10">
          <button
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
            className="px-4 py-2 bg-gray-300 dark:bg-gray-700 rounded disabled:opacity-50"
          >
            Prev
          </button>

          <span className="text-gray-700 dark:text-gray-300 font-medium">
            Page {page + 1} of {totalPages}
          </span>

          <button
            disabled={page === totalPages - 1}
            onClick={() => setPage(page + 1)}
            className="px-4 py-2 bg-gray-300 dark:bg-gray-700 rounded disabled:opacity-50"
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}
