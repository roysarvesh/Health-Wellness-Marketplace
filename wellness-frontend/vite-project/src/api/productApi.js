import axios from "./axios"; // your axios instance with interceptors

// Get all products
export const getAllProducts = () => axios.get("/products");

// Get product by ID
export const getProductById = (productId) =>
  axios.get(`/products/${productId}`);

// Advanced search with filters
export const searchProducts = (params) =>
  axios.get("/products/search/advanced", { params });

// Create product (ADMIN)
export const createProduct = (data) =>
  axios.post("/products", data);

// Update product (ADMIN)
export const updateProduct = (productId, data) =>
  axios.put(`/products/${productId}`, data);

// Delete product (ADMIN)
export const deleteProduct = (productId) =>
  axios.delete(`/products/${productId}`);
