import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { getToken } from "../utils/token";
import { motion } from "framer-motion";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

// Attach JWT token automatically
API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function ChatPage() {
  const [users, setUsers] = useState([]);      // List of chat users
  const [activeUser, setActiveUser] = useState(null);
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const bottomRef = useRef(null);

  /* =====================================================
     LOAD USER LIST (Patients <-> Practitioners)
  ===================================================== */
  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const res = await API.get("/users"); // Backend must return list of users
      setUsers(res.data);
    } catch (err) {
      console.error("Error loading users:", err);
    }
  };

  /* =====================================================
     OPEN CHAT + LOAD MESSAGE HISTORY
  ===================================================== */
  const openChat = async (user) => {
    setActiveUser(user);

    try {
      const res = await API.get(`/chat/history/${user.id}`);
      setMessages(res.data);
    } catch (err) {
      console.error("Failed to fetch messages:", err);
    }

    scrollToBottom();
  };

  /* =====================================================
     SEND MESSAGE
  ===================================================== */
  const sendMessage = async () => {
    if (!text.trim() || !activeUser) return;

    try {
      const res = await API.post("/chat/send", {
        receiverId: activeUser.id,
        content: text,
      });

      setMessages((prev) => [...prev, res.data]);
      setText("");
      scrollToBottom();
    } catch (err) {
      console.error("Send message failed:", err);
    }
  };

  const scrollToBottom = () =>
    setTimeout(() => {
      bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, 90);

  /* =====================================================
     UI
  ===================================================== */
  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 px-6 py-24">
      <div
        className="
          max-w-7xl mx-auto grid grid-cols-4 rounded-3xl overflow-hidden
          bg-white/90 dark:bg-slate-900/80
          border border-slate-200 dark:border-slate-700
        "
      >
        {/* ================= LEFT PANEL — USERS LIST ================= */}
        <div className="col-span-1 border-r dark:border-slate-700 p-4">
          <h2 className="text-xl font-bold mb-4">Chats</h2>

          {users.map((u) => (
            <button
              key={u.id}
              onClick={() => openChat(u)}
              className={`
                w-full text-left p-3 rounded-xl mb-2 transition
                ${
                  activeUser?.id === u.id
                    ? "bg-emerald-100 dark:bg-emerald-900"
                    : "hover:bg-slate-100 dark:hover:bg-slate-800"
                }
              `}
            >
              <p className="font-semibold">{u.name}</p>
              <p className="text-xs text-slate-500">{u.email}</p>
            </button>
          ))}
        </div>

        {/* ================= RIGHT PANEL — CHAT WINDOW ================= */}
        <div className="col-span-3 flex flex-col">
          {!activeUser ? (
            <div className="flex-1 flex items-center justify-center text-slate-500">
              Select a conversation to start chatting
            </div>
          ) : (
            <>
              {/* Chat Header */}
              <div className="p-4 border-b dark:border-slate-700 font-semibold text-lg">
                Chat with {activeUser.name}
              </div>

              {/* Chat Messages */}
              <div className="flex-1 overflow-y-auto p-6 space-y-4">
                {messages.map((m) => (
                  <motion.div
                    key={m.id}
                    initial={{ opacity: 0, y: 8 }}
                    animate={{ opacity: 1, y: 0 }}
                    className={`
                      max-w-[70%] px-4 py-2 rounded-2xl
                      ${
                        m.sender.id === activeUser.id
                          ? "bg-slate-200 dark:bg-slate-800"
                          : "ml-auto bg-emerald-600 text-white"
                      }
                    `}
                  >
                    {m.content}
                  </motion.div>
                ))}
                <div ref={bottomRef} />
              </div>

              {/* Message Input */}
              <div className="p-4 border-t dark:border-slate-700 flex gap-3">
                <input
                  value={text}
                  onChange={(e) => setText(e.target.value)}
                  onKeyDown={(e) => e.key === "Enter" && sendMessage()}
                  className="flex-1 input"
                  placeholder="Type a message..."
                />
                <button
                  onClick={sendMessage}
                  className="px-6 py-2 bg-emerald-600 text-white rounded-xl font-semibold"
                >
                  Send
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
