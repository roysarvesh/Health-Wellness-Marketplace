import React, { useState } from "react";
import { register } from "../../api/authApi";
import { useNavigate, Link } from "react-router-dom";

export default function RegisterForm() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    bio: "",
    role: "PATIENT",

    // Practitioner-only
    specialization: "",
    city: "",
    address: "",
    latitude: "",
    longitude: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await register(form);
      navigate("/login");
    } catch {
      setError("Registration failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const inputClass = `
    w-full px-4 py-3 rounded-xl
    bg-white dark:bg-slate-800
    border border-slate-300 dark:border-slate-600
    text-slate-900 dark:text-white
    placeholder:text-slate-500 dark:placeholder:text-slate-400
    focus:outline-none focus:ring-2 focus:ring-emerald-500
  `;

  return (
    <form
      onSubmit={handleSubmit}
      className="
        w-full max-w-md p-10 rounded-[2rem]
        bg-white/90 dark:bg-slate-900/90
        backdrop-blur-xl
        shadow-[0_20px_60px_rgba(0,0,0,0.35)]
        border border-slate-200 dark:border-slate-700
      "
    >
      {/* HEADER */}
      <div className="text-center mb-8">
        <div className="text-4xl mb-3">🌿</div>

        <h2 className="text-3xl font-extrabold text-slate-900 dark:text-white">
          Join Wellness Marketplace
        </h2>

        <p className="mt-2 text-sm text-slate-600 dark:text-slate-400">
          Create your holistic wellness account
        </p>
      </div>

      {/* ERROR */}
      {error && (
        <p className="mb-5 text-sm text-center font-medium
          text-red-600 dark:text-red-400
          bg-red-100 dark:bg-red-900/30
          p-3 rounded-xl"
        >
          {error}
        </p>
      )}

      {/* BASIC DETAILS */}
      <div className="space-y-4">
        <input
          name="name"
          placeholder="Full Name"
          required
          onChange={handleChange}
          className={inputClass}
        />

        <input
          name="email"
          type="email"
          placeholder="Email Address"
          required
          onChange={handleChange}
          className={inputClass}
        />

        <input
          name="password"
          type="password"
          placeholder="Password"
          required
          onChange={handleChange}
          className={inputClass}
        />

        {/* BIO */}
        <textarea
          name="bio"
          rows="4"
          maxLength={1000}
          placeholder="Tell us about yourself (max 1000 characters)"
          onChange={handleChange}
          className={`${inputClass} resize-none`}
        />

        {/* ROLE */}
        <select
          name="role"
          onChange={handleChange}
          className={inputClass}
        >
          <option value="PATIENT">Patient</option>
          <option value="PRACTITIONER">Practitioner</option>
        </select>
      </div>

      {/* PRACTITIONER DETAILS */}
      {form.role === "PRACTITIONER" && (
        <div className="mt-6 pt-6 border-t border-slate-300 dark:border-slate-700 space-y-4">
          <p className="text-sm font-semibold text-slate-700 dark:text-slate-300">
            Practitioner Details
          </p>

          <input
            name="specialization"
            placeholder="Specialization (Yoga, Ayurveda, etc.)"
            onChange={handleChange}
            className={inputClass}
          />

          <input
            name="city"
            placeholder="City"
            onChange={handleChange}
            className={inputClass}
          />

          <input
            name="address"
            placeholder="Clinic / Practice Address"
            onChange={handleChange}
            className={inputClass}
          />

          <div className="grid grid-cols-2 gap-4">
            <input
              name="latitude"
              placeholder="Latitude"
              onChange={handleChange}
              className={inputClass}
            />
            <input
              name="longitude"
              placeholder="Longitude"
              onChange={handleChange}
              className={inputClass}
            />
          </div>
        </div>
      )}

      {/* SUBMIT */}
      <button
        type="submit"
        disabled={loading}
        className="
          w-full mt-8 py-3 rounded-xl
          bg-emerald-600 hover:bg-emerald-700
          text-white font-bold text-lg
          transition shadow-lg hover:shadow-emerald-500/40
          disabled:opacity-70
        "
      >
        {loading ? "Creating Account..." : "Create Account"}
      </button>

      {/* FOOTER */}
      <p className="mt-6 text-center text-sm text-slate-600 dark:text-slate-400">
        Already registered?{" "}
        <Link
          to="/login"
          className="text-emerald-600 dark:text-emerald-400 font-semibold hover:underline"
        >
          Sign in
        </Link>
      </p>
    </form>
  );
}
