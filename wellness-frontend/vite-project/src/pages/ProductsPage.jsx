// LOCATION: src/pages/ProductsPage.jsx

import React, { useEffect, useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";
import { getToken } from "../utils/token";
import { useCart } from "../context/CartContext";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function ProductsPage() {
  const navigate = useNavigate();
  const { addToCart, cart } = useCart();

  /** STATE */
  const [products, setProducts] = useState([]);
  const [quantities, setQuantities] = useState({});
  const [adding, setAdding] = useState(null);
  const [loading, setLoading] = useState(true);

  /** Filters */
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("");
  const [maxPrice, setMaxPrice] = useState(20000);
  const [inStock, setInStock] = useState(false);

  /** Sorting */
  const [sortBy, setSortBy] = useState("price");
  const [sortDir, setSortDir] = useState("asc");

  /** ====================================
   *   Fetch Products (WITH FILTERING)
   *  ==================================== */
  useEffect(() => {
    const timeout = setTimeout(fetchProducts, 350);
    return () => clearTimeout(timeout);
  }, [search, category, maxPrice, inStock, sortBy, sortDir]);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const res = await API.get("/products/search/advanced", {
        params: {
          keyword: search || null,
          category: category || null,
          minPrice: 0,
          maxPrice,
          inStock,
          sortBy,
          direction: sortDir,
        },
      });

      setProducts(res.data);

      /** Default all quantities = 0 */
      const q = {};
      res.data.forEach((p) => (q[p.id] = 0));
      setQuantities(q);
    } finally {
      setLoading(false);
    }
  };

  /** ====================================
   *   Category Limit (20 per category)
   *  ==================================== */
  const getCategoryCount = (category) => {
    if (!cart || !cart.items) return 0;

    return cart.items
      .filter((i) => i.product?.category === category)
      .reduce((s, i) => s + i.quantity, 0);
  };

  /** Quantity Handlers */
  const increase = (p) => {
    const current = quantities[p.id] || 0;
    const existing = getCategoryCount(p.category);

    if (existing + current >= 20) {
      alert(`Max 20 items allowed per category (${p.category})`);
      return;
    }

    setQuantities((prev) => ({
      ...prev,
      [p.id]: current + 1,
    }));
  };

  const decrease = (id) =>
    setQuantities((prev) => ({
      ...prev,
      [id]: Math.max(0, prev[id] - 1),
    }));

  /** Add to Cart */
  const handleAdd = async (p) => {
    const qty = quantities[p.id];
    if (qty === 0) return alert("Select quantity first");

    try {
      setAdding(p.id);
      await addToCart(p.id, qty);

      /** Reset quantity after adding */
      setQuantities((prev) => ({
        ...prev,
        [p.id]: 0,
      }));
    } finally {
      setAdding(null);
    }
  };

  return (
    <div className="min-h-screen px-6 py-24 bg-slate-50 dark:bg-slate-950">

      {/* HEADER */}
      <div className="max-w-7xl mx-auto mb-12 text-center">
        <h1 className="text-4xl font-black text-slate-900 dark:text-white">
          Wellness Products
        </h1>
        <p className="mt-2 text-slate-600 dark:text-slate-400">
          Select quantity • Max 20 per category • Smart filtering
        </p>
      </div>

      {/* FILTER PANEL */}
      <div
        className="
          max-w-7xl mx-auto mb-12
          bg-white dark:bg-slate-900
          border border-slate-200 dark:border-slate-700
          rounded-2xl p-6
          shadow-[0_20px_50px_rgba(0,0,0,0.35)]
          grid md:grid-cols-6 gap-4
        "
      >
        <input
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Search..."
          className="
            md:col-span-2 px-4 py-2 rounded-xl
            bg-slate-100 dark:bg-slate-800
            border border-slate-300 dark:border-slate-700
          "
        />

        <select
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          className="
            px-4 py-2 rounded-xl
            bg-slate-100 dark:bg-slate-800
          "
        >
          <option value="">All Categories</option>
          <option value="SUPPLEMENTS">Supplements</option>
          <option value="AYURVEDA">Ayurveda</option>
          <option value="FITNESS">Fitness</option>
          <option value="MENTAL_WELLNESS">Mental Wellness</option>
          <option value="PERSONAL_CARE">Personal Care</option>
          <option value="HEALTH_DEVICES">Health Devices</option>
          <option value="HEALTHY_FOOD">Healthy Food</option>
          <option value="SEXUAL_WELLNESS">Sexual Wellness</option>
        </select>

        <div className="flex flex-col text-sm">
          <span>Max ₹{maxPrice}</span>
          <input
            type="range"
            min="0"
            max="20000"
            step="100"
            value={maxPrice}
            onChange={(e) => setMaxPrice(e.target.value)}
            className="accent-emerald-600"
          />
        </div>

        <label className="flex items-center gap-2 text-sm">
          <input
            type="checkbox"
            checked={inStock}
            onChange={(e) => setInStock(e.target.checked)}
            className="accent-emerald-600"
          />
          In Stock
        </label>

        <select
          value={`${sortBy}-${sortDir}`}
          onChange={(e) => {
            const [by, dir] = e.target.value.split("-");
            setSortBy(by);
            setSortDir(dir);
          }}
          className="
            px-4 py-2 rounded-xl
            bg-slate-100 dark:bg-slate-800
          "
        >
          <option value="price-asc">Price ↑</option>
          <option value="price-desc">Price ↓</option>
          <option value="rating-desc">Rating ↓</option>
        </select>
      </div>

      {/* PRODUCT GRID */}
      <div className="max-w-7xl mx-auto grid sm:grid-cols-2 lg:grid-cols-3 gap-8">

        {loading && (
          <p className="col-span-full text-center">Loading...</p>
        )}

        {!loading && products.length === 0 && (
          <p className="col-span-full text-center">No products found</p>
        )}

        {!loading &&
          products.map((p) => {
            const qty = quantities[p.id];
            const limitReached =
              getCategoryCount(p.category) + qty >= 20;

            return (
              <motion.div
                key={p.id}
                whileHover={{ y: -6 }}
                className="
                  bg-white dark:bg-slate-900 p-6 rounded-2xl 
                  border border-slate-200 dark:border-slate-700 shadow-lg
                "
              >
                <img
                  src={p.imageUrl}
                  alt={p.name}
                  className="h-40 w-full object-cover rounded-xl mb-4"
                />

                <h3 className="text-xl font-bold text-slate-900 dark:text-white">
                  {p.name}
                </h3>

                <p className="text-sm text-slate-600 dark:text-slate-400 mt-1 line-clamp-2">
                  {p.description}
                </p>

                <div className="mt-4 flex justify-between">
                  <span className="text-lg font-bold text-emerald-600">
                    ₹{p.price}
                  </span>
                  <span className="text-sm text-yellow-400">
                    ⭐ {p.rating || "N/A"}
                  </span>
                </div>

                {/* QUANTITY */}
                <div className="flex items-center justify-center gap-4 my-4">
                  <button
                    onClick={() => decrease(p.id)}
                    className="px-3 py-1 bg-slate-200 dark:bg-slate-700 rounded-lg"
                  >
                    −
                  </button>

                  <span className="font-bold">{qty}</span>

                  <button
                    disabled={limitReached}
                    onClick={() => increase(p)}
                    className={`px-3 py-1 rounded-lg ${
                      limitReached
                        ? "bg-slate-400 cursor-not-allowed"
                        : "bg-slate-200 dark:bg-slate-700"
                    }`}
                  >
                    +
                  </button>
                </div>

                <div className="flex gap-3">
                  <button
                    onClick={() => navigate(`/products/${p.id}`)}
                    className="flex-1 border rounded-xl py-2"
                  >
                    View
                  </button>

                  <button
                    disabled={adding === p.id || qty === 0}
                    onClick={() => handleAdd(p)}
                    className="flex-1 bg-emerald-600 text-white rounded-xl py-2 disabled:bg-slate-400"
                  >
                    {adding === p.id ? "Adding..." : "Add"}
                  </button>
                </div>
              </motion.div>
            );
          })}
      </div>
    </div>
  );
}
