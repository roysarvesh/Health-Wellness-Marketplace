import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
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

export default function OrderSummaryPage() {
  const { orderId } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState(null);

  useEffect(() => {
    loadOrder();
  }, []);

  const loadOrder = async () => {
    try {
      const res = await API.get(`/orders/${orderId}`);
      setOrder(res.data);
    } catch {
      alert("Failed to load order information");
    }
  };

  if (!order) {
    return <div className="min-h-screen flex justify-center items-center">Loading...</div>;
  }

  return (
    <div className="min-h-screen px-6 py-10 bg-gray-50 dark:bg-[#020617] text-gray-900 dark:text-white">
      <h1 className="text-3xl font-bold mb-4">Order Summary</h1>

      <div className="bg-white dark:bg-[#020617] p-6 rounded-xl shadow-lg">
        <h2 className="text-xl font-semibold mb-4">Order ID: #{order.id}</h2>

        {order.items.map((item) => (
          <div key={item.productId} className="flex justify-between mb-3">
            <span>{item.productName} × {item.quantity}</span>
            <span>₹{item.totalPrice}</span>
          </div>
        ))}

        <hr className="my-4" />

        <div className="flex justify-between text-xl font-bold">
          <span>Total</span>
          <span>₹{order.totalAmount}</span>
        </div>

        <div className="mt-6 text-sm text-gray-500 dark:text-gray-400">
          <p><strong>Shipping Address:</strong></p>
          <p>{order.shippingAddress}</p>
        </div>

        <button
          onClick={() => navigate(`/payment-success/${order.id}`)}
          className="mt-6 w-full py-3 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700 font-bold"
        >
          Confirm Payment
        </button>
      </div>
    </div>
  );
}
