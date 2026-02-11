import { useParams, useNavigate } from "react-router-dom";
import React from 'react';
export default function PaymentSuccessPage() {
  const { orderId } = useParams();
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex flex-col justify-center items-center bg-green-50 dark:bg-black text-center px-6">
      <h1 className="text-4xl font-black text-green-700 dark:text-green-500 mb-4">
        🎉 Payment Successful!
      </h1>

      <p className="text-lg text-gray-600 dark:text-gray-300 mb-2">
        Your order <strong>#{orderId}</strong> has been placed.
      </p>

      <p className="text-gray-500 dark:text-gray-400 mb-8">
        Thank you for shopping with Wellness Marketplace!
      </p>

      <button
        onClick={() => navigate("/orders")}
        className="px-6 py-3 bg-green-600 hover:bg-green-700 text-white rounded-xl font-bold"
      >
        View My Orders
      </button>

      <button
        onClick={() => navigate("/products")}
        className="mt-3 px-6 py-2 border border-gray-400 rounded-xl text-gray-700 dark:text-gray-200 hover:bg-gray-200 dark:hover:bg-gray-800"
      >
        Continue Shopping
      </button>
    </div>
  );
}
