import React, { useEffect, useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";
import { getToken } from "../../utils/token";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function ProductList() {
  const navigate = useNavigate();

  /* ===============================
     STATE
  =============================== */
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [adding, setAdding] = useState(null);

  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("");
  const [minPrice, setMinPrice] = useState(0);
  const [maxPrice, setMaxPrice] = useState(20000);
  const [inStock, setInStock] = useState(false);

  const [sortBy, setSortBy] = useState("price");
  const [sortDir, setSortDir] = useState("asc");

  /* ===============================
     FETCH PRODUCTS
  =============================== */
  useEffect(() => {
    const timeout = setTimeout(fetchProducts, 300);
    return () => clearTimeout(timeout);
  }, [search, category, minPrice, maxPrice, inStock, sortBy, sortDir]);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const res = await API.get("/products/search/advanced", {
        params: {
          keyword: search || null,
          category: category || null,
          minPrice,
          maxPrice,
          sortBy,
          direction: sortDir,
        },
      });

      setProducts(res.data);
    } catch (err) {
      console.error("Failed to load products", err);
    } finally {
      setLoading(false);
    }
  };

  /* ===============================
     ADD TO CART
  =============================== */
  const addToCart = async (productId) => {
    try {
      setAdding(productId);
      await API.post("/cart/add", { productId, quantity: 1 });
      alert("Added to cart");
    } catch {
      alert("Failed to add to cart");
    } finally {
      setAdding(null);
    }
  };

  /* ===============================
     UI
  =============================== */
  return (
    <div className="min-h-screen px-6 py-24 bg-slate-50 dark:bg-slate-950 transition-colors">
      <div className="max-w-7xl mx-auto mb-12 text-center">
        <h1 className="text-4xl font-black text-slate-900 dark:text-white">
          Wellness Products
        </h1>
        <p className="text-slate-600 dark:text-slate-300 mt-2">
          Smart search powered by backend filtering
        </p>
      </div>

      {/* FILTER PANEL */}
      <div className="max-w-7xl mx-auto mb-10 bg-white/90 dark:bg-slate-900/80 backdrop-blur-xl border border-slate-200 dark:border-slate-700 rounded-2xl p-6 grid md:grid-cols-6 gap-4">

        <input
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Search products..."
          className="input md:col-span-2"
        />

        <select
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          className="input"
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

        <div>
          <label className="text-xs text-slate-500">Max ₹{maxPrice}</label>
          <input
            type="range"
            min="0"
            max="20000"
            step="100"
            value={maxPrice}
            onChange={(e) => setMaxPrice(e.target.value)}
            className="w-full"
          />
        </div>

        <label className="flex items-center gap-2 text-sm">
          <input
            type="checkbox"
            checked={inStock}
            onChange={(e) => setInStock(e.target.checked)}
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
          className="input"
        >
          <option value="price-asc">Price ↑</option>
          <option value="price-desc">Price ↓</option>
        </select>
      </div>

      {/* PRODUCTS LIST */}
      <div className="max-w-7xl mx-auto grid sm:grid-cols-2 lg:grid-cols-3 gap-8">

        {loading && (
          <p className="col-span-full text-center text-slate-500">
            Loading products...
          </p>
        )}

        {!loading && products.length === 0 && (
          <p className="col-span-full text-center text-slate-500">
            No products found
          </p>
        )}

        {products.map((product) => (
          <motion.div
            key={product.id}
            whileHover={{ y: -6 }}
            className="bg-white/90 dark:bg-slate-900/80 rounded-2xl border border-slate-200 dark:border-slate-700 shadow-lg p-6 flex flex-col"
          >
            {/* IMAGE */}
            <img
              src={product.imageUrl}
              alt={product.name}
              className="h-40 w-full object-cover rounded-xl mb-4"
            />

            <h3 className="text-xl font-bold text-slate-900 dark:text-white">
              {product.name}
            </h3>

            <p className="text-sm text-slate-600 dark:text-slate-300 mt-1 line-clamp-2">
              {product.description}
            </p>

            <div className="mt-4 flex justify-between items-center">
              <span className="text-lg font-bold text-emerald-600">
                ₹{product.price}
              </span>
              <span className="text-sm text-yellow-500 font-semibold">
                ⭐ {product.rating || "N/A"}
              </span>
            </div>

            <div className="mt-6 flex gap-3">
              <button
                onClick={() => navigate(`/products/${product.id}`)}
                className="flex-1 py-2 rounded-xl border border-slate-300 dark:border-slate-600 hover:bg-slate-100 dark:hover:bg-slate-800 font-semibold transition"
              >
                View
              </button>

              <button
                disabled={adding === product.id}
                onClick={() => addToCart(product.id)}
                className="flex-1 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-700 disabled:bg-slate-400 text-white font-bold transition"
              >
                {adding === product.id ? "Adding..." : "Add"}
              </button>
            </div>
          </motion.div>
        ))}
      </div>
    </div>
  );
}
