import { useEffect, useState } from "react";
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

export default function CheckoutPage() {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [placingOrder, setPlacingOrder] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    loadCart();
  }, []);

  const loadCart = async () => {
    try {
      const res = await API.get("/cart");
      setCart(res.data);
    } catch {
      navigate("/cart");
    } finally {
      setLoading(false);
    }
  };

  const placeOrder = async () => {
    try {
      setPlacingOrder(true);

      // 1️⃣ Place order
      const orderRes = await API.post("/orders/place", {
        paymentMethod: "ONLINE",
        shippingAddress: "Default Address",
      });

      const orderId = orderRes.data.orderId;

      // 2️⃣ Initiate payment
      await API.post(`/payments/initiate?orderId=${orderId}`);

      alert("Payment initiated successfully!");
      navigate(`/orders/${orderId}`);
    } catch (err) {
      alert("Checkout failed. Try again.");
    } finally {
      setPlacingOrder(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-lg">
        Loading checkout...
      </div>
    );
  }

  if (!cart || cart.items.length === 0) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center gap-4">
        <h2 className="text-2xl font-semibold">Your cart is empty</h2>
        <button
          onClick={() => navigate("/products")}
          className="px-6 py-2 bg-blue-600 text-white rounded-lg"
        >
          Shop Now
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen px-6 py-10 bg-gray-50 dark:bg-[#020617] text-gray-900 dark:text-white">
      <h1 className="text-3xl font-bold mb-8">Checkout</h1>

      <div className="grid lg:grid-cols-3 gap-8">
        {/* ITEMS */}
        <div className="lg:col-span-2 space-y-4">
          {cart.items.map((item) => (
            <div
              key={item.product.id}
              className="flex justify-between items-center bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-5 shadow-sm"
            >
              <div>
                <h3 className="font-semibold">{item.product.name}</h3>
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

        {/* SUMMARY */}
        <div className="bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-6 shadow-sm h-fit">
          <h2 className="text-xl font-semibold mb-4">Order Summary</h2>

          <div className="flex justify-between mb-2">
            <span>Items</span>
            <span>{cart.items.length}</span>
          </div>

          <div className="flex justify-between mb-4">
            <span>Total</span>
            <span className="font-bold text-lg">₹{cart.totalAmount}</span>
          </div>

          <button
            onClick={placeOrder}
            disabled={placingOrder}
            className="w-full py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:opacity-60"
          >
            {placingOrder ? "Processing..." : "Place Order & Pay"}
          </button>

          <button
            onClick={() => navigate("/cart")}
            className="w-full mt-3 py-2 border rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800"
          >
            Back to Cart
          </button>
        </div>
      </div>
    </div>
  );
}
