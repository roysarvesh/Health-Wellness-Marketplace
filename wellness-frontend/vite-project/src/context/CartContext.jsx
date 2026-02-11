// LOCATION: src/context/CartContext.jsx

import React, {
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";

import api from "../api/axios"; // ✅ FIXED IMPORT

import {
  getCartApi,
  addToCartApi,
  updateCartItemApi,
  removeCartItemApi,
  clearCartApi,
} from "../api/cartApi";

const CartContext = createContext();

export function CartProvider({ children }) {
  const [cart, setCart] = useState({
    items: [],
    totalAmount: 0,
  });

  const [loading, setLoading] = useState(true);

  /** Normalize cart */
  const normalizeCart = (data) => ({
    items: Array.isArray(data?.items) ? data.items : [],
    totalAmount: data?.totalAmount ?? 0,
  });

  /** Fetch cart from backend */
  const fetchCart = async () => {
    try {
      const res = await getCartApi();
      setCart(normalizeCart(res.data));
    } catch (err) {
      console.log("Cart fetch failed", err);
      setCart({ items: [], totalAmount: 0 });
    } finally {
      setLoading(false);
    }
  };

  /** Add item */
  const addToCart = async (productId, quantity) => {
    const res = await addToCartApi(productId, quantity);
    setCart(normalizeCart(res.data));
  };

  /** Update item */
  const updateQuantity = async (productId, quantity) => {
    const res = await updateCartItemApi(productId, quantity);
    setCart(normalizeCart(res.data));
  };

  /** Remove item */
  const removeItem = async (productId) => {
    const res = await removeCartItemApi(productId);
    setCart(normalizeCart(res.data));
  };

  /** Clear cart */
  const clearCart = async () => {
    await clearCartApi();
    setCart({ items: [], totalAmount: 0 });
  };

  useEffect(() => {
    fetchCart();
  }, []);

  return (
    <CartContext.Provider
      value={{
        cart,
        loading,
        fetchCart,
        addToCart,
        updateQuantity,
        removeItem,
        clearCart,
      }}
    >
      {children}
    </CartContext.Provider>
  );
}

export const useCart = () => useContext(CartContext);
