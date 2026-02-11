import React from 'react';
import { createContext, useContext, useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../utils/token";
import {
  connectNotificationSocket,
  disconnectNotificationSocket,
} from "../utils/notificationSocket";

const NotificationContext = createContext();

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const NotificationProvider = ({ children }) => {
  const [notifications, setNotifications] = useState([]);

  const unreadCount = notifications.filter((n) => !n.read).length;

  /* ===============================
     INITIAL LOAD + SOCKET
  =============================== */
  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;

    loadNotifications(userId);

    connectNotificationSocket(userId, (notification) => {
      setNotifications((prev) => [notification, ...prev]);
    });

    return () => disconnectNotificationSocket();
  }, []);

  const loadNotifications = async () => {
    const res = await API.get("/notifications");
    setNotifications(res.data);
  };

  const markAsRead = async (id) => {
    await API.put(`/notifications/${id}/read`);
    setNotifications((prev) =>
      prev.map((n) => (n.id === id ? { ...n, read: true } : n))
    );
  };

  const markAllAsRead = async () => {
    await API.put("/notifications/read-all");
    setNotifications((prev) => prev.map((n) => ({ ...n, read: true })));
  };

  return (
    <NotificationContext.Provider
      value={{
        notifications,
        unreadCount,
        markAsRead,
        markAllAsRead,
      }}
    >
      {children}
    </NotificationContext.Provider>
  );
};

export const useNotifications = () => useContext(NotificationContext);
