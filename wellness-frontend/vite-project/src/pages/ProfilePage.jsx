// LOCATION: src/pages/ProfilePage.jsx

import React, { useEffect, useState } from "react";
import { Button } from "@mui/material";
import { motion } from "framer-motion";

import {
  getMyProfile,
  updateMyProfile,
  getPractitionerProfile,
  updatePractitionerProfile,
} from "../api/profileApi";

export default function ProfilePage() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  /* ============================================
     FETCH PROFILE BASED ON ROLE
  ============================================ */
  useEffect(() => {
    const fetchData = async () => {
      const role = localStorage.getItem("role");
      const userId = localStorage.getItem("userId");

      try {
        let data;
        if (role === "PRACTITIONER") {
          data = await getPractitionerProfile(userId);
        } else {
          data = await getMyProfile();
        }

        setProfile({ ...data, role });
      } catch (err) {
        console.error("Error loading profile:", err);
      }

      setLoading(false);
    };

    fetchData();
  }, []);

  /* ============================================
     HANDLE INPUT CHANGES
  ============================================ */
  const handleChange = (e) => {
    setProfile({ ...profile, [e.target.name]: e.target.value });
  };

  const handlePractitionerChange = (e) => {
    setProfile({
      ...profile,
      practitionerProfile: {
        ...profile.practitionerProfile,
        [e.target.name]: e.target.value,
      },
    });
  };

  /* ============================================
     SAVE PROFILE
  ============================================ */
  const handleSave = async () => {
    setSaving(true);

    const role = profile.role;
    const userId = localStorage.getItem("userId");

    try {
      if (role === "PRACTITIONER") {
        await updatePractitionerProfile(userId, {
          specialization: profile.practitionerProfile?.specialization,
          city: profile.practitionerProfile?.city,
          address: profile.practitionerProfile?.address,
          latitude: profile.practitionerProfile?.latitude,
          longitude: profile.practitionerProfile?.longitude,
          bio: profile.bio,
        });
      } else {
        await updateMyProfile({
          name: profile.name,
          bio: profile.bio,
        });
      }
    } catch (err) {
      console.error("Error updating profile:", err);
    }

    setSaving(false);
  };

  /* ============================================
     LOADING STATE
  ============================================ */
  if (loading) {
    return (
      <div className="w-full h-screen flex justify-center items-center text-xl text-slate-600 dark:text-slate-300">
        Loading profile...
      </div>
    );
  }

  const isPractitioner = profile.role === "PRACTITIONER";

  /* ============================================
     INPUT STYLES (Reusable)
  ============================================ */
  const inputClass = `
    w-full px-4 py-3 rounded-xl
    bg-slate-100 dark:bg-slate-800
    border border-slate-300 dark:border-slate-600
    text-slate-900 dark:text-white
    placeholder:text-slate-500 dark:placeholder:text-slate-400
    focus:ring-2 focus:ring-emerald-500
    focus:outline-none
  `;

  /* ============================================
     MAIN RENDER
  ============================================ */
  return (
    <div
      className="
        min-h-screen flex justify-center items-start py-24 px-4
        bg-gradient-to-b from-slate-100 to-slate-200
        dark:from-slate-900 dark:to-slate-950
      "
    >
      <motion.div
        initial={{ opacity: 0, y: 40 }}
        animate={{ opacity: 1, y: 0 }}
        className="
          w-full max-w-3xl
          bg-white/90 dark:bg-slate-900/80
          backdrop-blur-xl rounded-[2rem]
          shadow-[0_20px_60px_rgba(0,0,0,0.35)]
          border border-slate-200 dark:border-slate-700
          p-12
        "
      >
        {/* HEADER */}
        <div className="text-center mb-10">
          <div className="text-5xl mb-3">🌿</div>
          <h1 className="text-3xl font-black text-slate-900 dark:text-white">
            My Profile
          </h1>
          <p className="text-slate-600 dark:text-slate-400 mt-1">
            Manage your personal information
          </p>
        </div>

        {/* BASIC INFO */}
        <section className="space-y-5">
          <label className="block text-slate-700 dark:text-slate-300 font-semibold">
            Full Name
          </label>
          <input
            name="name"
            value={profile.name || ""}
            onChange={handleChange}
            placeholder="Full Name"
            className={inputClass}
          />

          <label className="block text-slate-700 dark:text-slate-300 font-semibold">
            Email Address
          </label>
          <input
            value={profile.email}
            disabled
            className={`
              ${inputClass}
              opacity-70 cursor-not-allowed text-slate-700 dark:text-slate-400
            `}
          />

          <label className="block text-slate-700 dark:text-slate-300 font-semibold">
            Bio
          </label>
          <textarea
            name="bio"
            value={profile.bio || ""}
            onChange={handleChange}
            rows={4}
            placeholder="Tell us about yourself"
            className={`${inputClass} resize-none`}
          />
        </section>

        {/* PRACTITIONER SECTION */}
        {isPractitioner && (
          <>
            <hr className="my-10 border-slate-300 dark:border-slate-700" />

            <h2 className="text-xl font-bold text-slate-900 dark:text-white mb-6">
              Practitioner Details
            </h2>

            <div className="space-y-6">
              <input
                name="specialization"
                value={profile.practitionerProfile?.specialization || ""}
                onChange={handlePractitionerChange}
                placeholder="Specialization (Yoga, Ayurveda, etc.)"
                className={inputClass}
              />

              <div className="grid grid-cols-2 gap-6">
                <input
                  name="city"
                  value={profile.practitionerProfile?.city || ""}
                  onChange={handlePractitionerChange}
                  placeholder="City"
                  className={inputClass}
                />
                <input
                  name="address"
                  value={profile.practitionerProfile?.address || ""}
                  onChange={handlePractitionerChange}
                  placeholder="Address"
                  className={inputClass}
                />
              </div>

              <div className="grid grid-cols-2 gap-6">
                <input
                  name="latitude"
                  value={profile.practitionerProfile?.latitude || ""}
                  onChange={handlePractitionerChange}
                  placeholder="Latitude"
                  className={inputClass}
                />
                <input
                  name="longitude"
                  value={profile.practitionerProfile?.longitude || ""}
                  onChange={handlePractitionerChange}
                  placeholder="Longitude"
                  className={inputClass}
                />
              </div>

              <div className="flex gap-6 text-sm text-slate-600 dark:text-slate-400">
                <span>⭐ Rating: {profile.practitionerProfile?.rating}</span>
                <span>
                  {profile.practitionerProfile?.verified
                    ? "✅ Verified"
                    : "⏳ Not Verified"}
                </span>
              </div>
            </div>
          </>
        )}

        {/* SAVE BUTTON */}
        <Button
          fullWidth
          disabled={saving}
          onClick={handleSave}
          className="
            !mt-12 !bg-emerald-600 hover:!bg-emerald-700
            !text-white !py-3 !rounded-xl
            !font-bold text-lg shadow-xl
          "
        >
          {saving ? "Saving..." : "Save Changes"}
        </Button>
      </motion.div>
    </div>
  );
}
