import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import App from "./App";

/* ===== PUBLIC PAGES ===== */
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";

/* ===== PROTECTED PAGES ===== */
import ProfilePage from "./pages/ProfilePage";
import ProductsPage from "./pages/ProductsPage";
import CartPage from "./pages/CartPage";
import ChatPage from "./pages/ChatPage";
import NotificationsPage from "./pages/NotificationsPage";
import CommunityForumPage from "./pages/CommunityForumPage";
import FitnessDashboard from "./pages/FitnessDashboard";

/* ===== AI RECOMMENDATIONS ===== */
import AIRecommendationsPage from "./pages/AIrecommendationPage";

/* ===== THERAPY & PRACTITIONER ===== */
import TherapyBookingPage from "./pages/TherapyBookingPage";
import BookingHistoryPage from "./pages/BookingHistoryPage";
import CancelBookingPage from "./pages/CancelBookingPage";
import RescheduleBookingPage from "./pages/RescheduleBookingPage";
import PractitionerSearch from "./pages/PractitionerSearch";
import PractitionerAvailabilityPage from "./pages/PractitionerAvailabilityPage";

/* ===== E-COMMERCE ===== */
import CheckoutPage from "./pages/CheckoutPage";
import OrderHistoryPage from "./pages/OrderHistoryPage";
import OrderDetailsPage from "./pages/OrderDetailsPage";
import PaymentHistoryPage from "./pages/PaymentHistoryPage";

/* ===== AUTH & CONTEXT ===== */
import ProtectedRoute from "./auth/ProtectedRoute";
import RoleProtectedRoute from "./auth/RoleProtectedRoute";
import { AuthProvider } from "./context/AuthContext";
import { ThemeProvider } from "./context/ThemeContext";
import { NotificationProvider } from "./context/NotificationContext";
import { CartProvider } from "./context/CartContext";

import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <AuthProvider>
      <ThemeProvider>
        <NotificationProvider>
          <CartProvider>
            <BrowserRouter>
              <Routes>
                <Route element={<App />}>
                  {/* PUBLIC HOME */}
                  <Route path="/" element={<HomePage />} />

                  {/* AI RECOMMENDATION */}
                  <Route
                    path="/ai-recommendations"
                    element={
                      <ProtectedRoute>
                        <AIRecommendationsPage />
                      </ProtectedRoute>
                    }
                  />

                  {/* PROFILE */}
                  <Route
                    path="/profile"
                    element={
                      <ProtectedRoute>
                        <ProfilePage />
                      </ProtectedRoute>
                    }
                  />

                  {/* PRODUCTS */}
                  <Route
                    path="/products"
                    element={
                      <ProtectedRoute>
                        <ProductsPage />
                      </ProtectedRoute>
                    }
                  />

                  {/* CART */}
                  <Route
                    path="/cart"
                    element={
                      <ProtectedRoute>
                        <CartPage />
                      </ProtectedRoute>
                    }
                  />

                  <Route
                    path="/checkout"
                    element={
                      <ProtectedRoute>
                        <CheckoutPage />
                      </ProtectedRoute>
                    }
                  />

                  {/* ORDERS */}
                  <Route
                    path="/orders"
                    element={
                      <ProtectedRoute>
                        <OrderHistoryPage />
                      </ProtectedRoute>
                    }
                  />

                  <Route
                    path="/orders/:orderId"
                    element={
                      <ProtectedRoute>
                        <OrderDetailsPage />
                      </ProtectedRoute>
                    }
                  />

                  <Route
                    path="/payments"
                    element={
                      <ProtectedRoute>
                        <PaymentHistoryPage />
                      </ProtectedRoute>
                    }
                  />

                  {/* CHAT */}
                  <Route
                    path="/chat"
                    element={
                      <ProtectedRoute>
                        <ChatPage />
                      </ProtectedRoute>
                    }
                  />

                  {/* NOTIFICATIONS */}
                  <Route
                    path="/notifications"
                    element={
                      <ProtectedRoute>
                        <NotificationsPage />
                      </ProtectedRoute>
                    }
                  />

                  {/* FORUM */}
                  <Route
                    path="/forum"
                    element={
                      <ProtectedRoute>
                        <CommunityForumPage />
                      </ProtectedRoute>
                    }
                  />

                  {/* FITNESS */}
                  <Route
                    path="/fitness"
                    element={
                      <ProtectedRoute>
                        <FitnessDashboard />
                      </ProtectedRoute>
                    }
                  />

                  {/* THERAPY */}
                  <Route
                    path="/therapy-booking"
                    element={
                      <ProtectedRoute>
                        <TherapyBookingPage />
                      </ProtectedRoute>
                    }
                  />

                  <Route
                    path="/booking-history"
                    element={
                      <ProtectedRoute>
                        <BookingHistoryPage />
                      </ProtectedRoute>
                    }
                  />

                  <Route
                    path="/booking/cancel/:id"
                    element={
                      <ProtectedRoute>
                        <CancelBookingPage />
                      </ProtectedRoute>
                    }
                  />

                  <Route
                    path="/booking/reschedule/:id"
                    element={
                      <ProtectedRoute>
                        <RescheduleBookingPage />
                      </ProtectedRoute>
                    }
                  />

                  {/* PRACTITIONER */}
                  <Route
                    path="/practitioners"
                    element={
                      <ProtectedRoute>
                        <PractitionerSearch />
                      </ProtectedRoute>
                    }
                  />

                  <Route
                    path="/practitioner/availability"
                    element={
                      <RoleProtectedRoute role="PRACTITIONER">
                        <PractitionerAvailabilityPage />
                      </RoleProtectedRoute>
                    }
                  />
                </Route>

                {/* AUTH PAGES (NO NAVBAR) */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
              </Routes>
            </BrowserRouter>
          </CartProvider>
        </NotificationProvider>
      </ThemeProvider>
    </AuthProvider>
  </React.StrictMode>
);
