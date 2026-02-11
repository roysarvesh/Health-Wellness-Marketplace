
import axios from "./axios";
export const getMyProfile = async () => {
  const res = await axios.get("/users/me");
  return res.data;
};
export const updateMyProfile = async (payload) => {
  const res = await axios.put("/users/me", payload);
  return res.data;
};

export const getPractitionerProfile = async (id) => {
  const res = await axios.get(`/practitioners/${id}`);
  return res.data;
};
export const updatePractitionerProfile = async (id, payload) => {
  const res = await axios.put(`/practitioners/${id}/update`, {
    specialization: payload.specialization,
    bio: payload.bio,
  });
  return res.data;
};
