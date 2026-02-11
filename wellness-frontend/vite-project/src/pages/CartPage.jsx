import React from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";

export default function CartPage() {
  const navigate = useNavigate();

  const {
    cart,
    loading,
    updateQuantity,
    removeItem,
    clearCart,
  } = useCart();

  const items = cart?.items || [];

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-lg">
        Loading cart...
      </div>
    );
  }

  if (items.length === 0) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center gap-4 bg-gray-50 dark:bg-[#020617] text-gray-900 dark:text-white">
        <h2 className="text-2xl font-semibold">Your cart is empty 🛒</h2>
        <button
          onClick={() => navigate("/products")}
          className="px-6 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700"
        >
          Browse Products
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen px-6 py-10 bg-gray-50 dark:bg-[#020617] text-gray-900 dark:text-white">
      <h1 className="text-3xl font-bold mb-8">Your Cart</h1>

      <div className="grid lg:grid-cols-3 gap-8">
        {/* ================= CART ITEMS ================= */}
        <div className="lg:col-span-2 space-y-6">
          {items.map((item) => (
            <div
              key={item.product.id}
              className="
                flex justify-between items-center
                bg-white dark:bg-[#020617]
                border border-gray-200 dark:border-gray-800
                rounded-xl p-5 shadow-sm
              "
            >
              <div>
                <h3 className="font-semibold text-lg">
                  {item.product.name}
                </h3>
                <p className="text-sm text-gray-500 dark:text-gray-400">
                  ₹{item.product.price}
                </p>
              </div>

              <div className="flex items-center gap-3">
                <button
                  onClick={() =>
                    updateQuantity(item.product.id, item.quantity - 1)
                  }
                  disabled={item.quantity <= 1}
                  className="
                    px-3 py-1 rounded
                    bg-gray-200 dark:bg-gray-700
                    disabled:opacity-50
                  "
                >
                  −
                </button>

                <span className="font-semibold">{item.quantity}</span>

                <button
                  onClick={() =>
                    updateQuantity(item.product.id, item.quantity + 1)
                  }
                  className="px-3 py-1 bg-gray-200 dark:bg-gray-700 rounded"
                >
                  +
                </button>
              </div>

              <div className="text-right">
                <p className="font-semibold">
                  ₹{item.product.price * item.quantity}
                </p>
                <button
                  onClick={() => removeItem(item.product.id)}
                  className="text-sm text-red-500 hover:underline"
                >
                  Remove
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* ================= SUMMARY ================= */}
        <div className="
          bg-white dark:bg-[#020617]
          border border-gray-200 dark:border-gray-800
          rounded-xl p-6 shadow-sm h-fit
        ">
          <h2 className="text-xl font-semibold mb-4">Order Summary</h2>

          <div className="flex justify-between mb-2">
            <span>Items</span>
            <span>{items.length}</span>
          </div>

          <div className="flex justify-between mb-4">
            <span>Total Amount</span>
            <span className="font-bold">₹{cart.totalAmount}</span>
          </div>

          <button
            onClick={() => navigate("/checkout")}
            className="
              w-full py-3 bg-emerald-600 text-white
              rounded-lg hover:bg-emerald-700 mb-3
            "
          >
            Proceed to Checkout
          </button>

          <button
            onClick={clearCart}
            className="
              w-full py-2
              text-red-500 border border-red-500
              rounded-lg hover:bg-red-500 hover:text-white
            "
          >
            Clear Cart
          </button>
        </div>
      </div>
    </div>
  );
}
