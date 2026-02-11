import API from "./axios";

/**
 * Fetch AI-based recommendations for logged-in user
 * Backend: /api/recommendations
 */
export const getAIRecommendations = async () => {
  const res = await API.get("/recommendations");
  return res.data;
};
