import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { motion } from "framer-motion";
import { getToken } from "../../utils/token";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function ProductDetails() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [adding, setAdding] = useState(false);

  useEffect(() => {
    API.get(`/products/${id}`)
      .then((res) => setProduct(res.data))
      .catch(() => alert("Failed to load product"));
  }, [id]);

  const addToCart = async () => {
    setAdding(true);
    try {
      await API.post("/cart/add", { productId: id, quantity: 1 });
      alert("Added to cart");
    } catch {
      alert("Failed");
    }
    setAdding(false);
  };

  if (!product)
    return (
      <p className="p-10 text-center text-slate-500">Loading product...</p>
    );

  return (
    <div className="min-h-screen px-6 py-20 bg-slate-50 dark:bg-slate-950 transition-colors">
      <div className="max-w-4xl mx-auto bg-white/90 dark:bg-slate-900/80 rounded-2xl border border-slate-200 dark:border-slate-700 p-10 shadow-xl">

        <img
          src={product.imageUrl}
          alt={product.name}
          className="w-full h-80 object-cover rounded-xl mb-6"
        />

        <h1 className="text-4xl font-black text-slate-900 dark:text-white">
          {product.name}
        </h1>

        <p className="mt-3 text-slate-600 dark:text-slate-300 text-lg">
          {product.description}
        </p>

        <div className="mt-6 flex items-center justify-between">
          <span className="text-3xl font-bold text-emerald-600">
            ₹{product.price}
          </span>

          <span className="text-yellow-500 text-lg font-semibold">
            ⭐ Rating: {product.rating || "N/A"}
          </span>
        </div>

        <button
          onClick={addToCart}
          disabled={adding}
          className="mt-8 w-full py-4 rounded-xl bg-emerald-600 hover:bg-emerald-700 text-white font-bold text-lg disabled:bg-slate-400 transition"
        >
          {adding ? "Adding..." : "Add to Cart"}
        </button>
      </div>
    </div>
  );
}
