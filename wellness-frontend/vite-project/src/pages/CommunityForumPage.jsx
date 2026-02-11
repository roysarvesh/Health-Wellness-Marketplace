import { useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../utils/token";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function CommunityForumPage() {
  const [posts, setPosts] = useState([]);
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchPosts();
  }, []);

  const fetchPosts = async () => {
    try {
      const res = await API.get("/forum/posts");
      setPosts(res.data);
    } catch (err) {
      console.error("Failed to fetch forum posts");
    } finally {
      setLoading(false);
    }
  };

  const createPost = async () => {
    if (!content.trim()) return;

    await API.post("/forum/posts", { content });
    setContent("");
    fetchPosts();
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        Loading community...
      </div>
    );
  }

  return (
    <div className="min-h-screen px-6 py-10 bg-gray-50 dark:bg-[#020617] text-gray-900 dark:text-white">
      <h1 className="text-3xl font-bold mb-6">Community Forum</h1>

      {/* CREATE POST */}
      <div className="bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-4 mb-8">
        <textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="Ask a question or share your experience..."
          className="w-full p-3 rounded-lg bg-transparent border dark:border-gray-700"
          rows={3}
        />
        <button
          onClick={createPost}
          className="mt-3 px-5 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700"
        >
          Post
        </button>
      </div>

      {/* POSTS */}
      <div className="space-y-6">
        {posts.map((post) => (
          <div
            key={post.id}
            className="bg-white dark:bg-[#020617] border dark:border-gray-800 rounded-xl p-5"
          >
            <p className="mb-3">{post.content}</p>
            <p className="text-sm text-gray-500">
              Posted by {post.authorName} •{" "}
              {new Date(post.createdAt).toLocaleString()}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}
