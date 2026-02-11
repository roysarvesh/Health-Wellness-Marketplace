import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { getToken } from "../utils/token";
import { motion } from "framer-motion";
import { useSearchParams } from "react-router-dom";
import React from 'react';
const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default function ChatPage() {
  const [searchParams] = useSearchParams();
  const selectedUserId = searchParams.get("user");

  const [rooms, setRooms] = useState([]);
  const [activeRoom, setActiveRoom] = useState(null);
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const messagesEndRef = useRef(null);

  /* ===============================
     LOAD CHAT ROOMS
  =============================== */
  useEffect(() => {
    fetchRooms();
  }, []);

  const fetchRooms = async () => {
    const res = await API.get("/chat/rooms");
    setRooms(res.data);

    if (selectedUserId) {
      const room = res.data.find(
        (r) => r.otherUser.id === Number(selectedUserId)
      );
      if (room) selectRoom(room);
    }
  };

  /* ===============================
     SELECT ROOM
  =============================== */
  const selectRoom = async (room) => {
    setActiveRoom(room);
    const res = await API.get(`/chat/rooms/${room.id}/messages`);
    setMessages(res.data);
    scrollBottom();
  };

  /* ===============================
     SEND MESSAGE
  =============================== */
  const sendMessage = async () => {
    if (!text.trim() || !activeRoom) return;

    await API.post("/chat/send", {
      roomId: activeRoom.id,
      content: text,
    });

    setMessages((prev) => [
      ...prev,
      {
        id: Date.now(),
        content: text,
        sender: { id: "me" },
        createdAt: new Date(),
      },
    ]);

    setText("");
    scrollBottom();
  };

  const scrollBottom = () =>
    setTimeout(
      () => messagesEndRef.current?.scrollIntoView({ behavior: "smooth" }),
      100
    );

  /* ===============================
     UI
  =============================== */
  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 px-6 py-20">
      <div
        className="max-w-7xl mx-auto
                   bg-white/90 dark:bg-slate-900/80
                   backdrop-blur-xl
                   border border-slate-200 dark:border-slate-700
                   rounded-3xl shadow-xl
                   grid grid-cols-4 overflow-hidden"
      >
        {/* SIDEBAR */}
        <div className="col-span-1 border-r dark:border-slate-700 p-4">
          <h2 className="font-bold text-lg mb-4">Chats</h2>

          <div className="space-y-2">
            {rooms.map((room) => (
              <button
                key={room.id}
                onClick={() => selectRoom(room)}
                className={`w-full text-left p-3 rounded-xl transition
                  ${
                    activeRoom?.id === room.id
                      ? "bg-emerald-100 dark:bg-emerald-900"
                      : "hover:bg-slate-100 dark:hover:bg-slate-800"
                  }`}
              >
                <p className="font-semibold">{room.otherUser.name}</p>
                <p className="text-xs text-slate-500 truncate">
                  {room.lastMessage?.content || "No messages yet"}
                </p>
              </button>
            ))}
          </div>
        </div>

        {/* CHAT WINDOW */}
        <div className="col-span-3 flex flex-col">
          {!activeRoom ? (
            <div className="flex-1 flex items-center justify-center text-slate-500">
              Select a conversation
            </div>
          ) : (
            <>
              {/* HEADER */}
              <div className="p-4 border-b dark:border-slate-700 font-semibold">
                Chat with {activeRoom.otherUser.name}
              </div>

              {/* MESSAGES */}
              <div className="flex-1 overflow-y-auto p-6 space-y-4">
                {messages.map((m) => (
                  <motion.div
                    key={m.id}
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    className={`max-w-[70%] px-4 py-2 rounded-2xl
                      ${
                        m.sender.id === "me"
                          ? "ml-auto bg-emerald-600 text-white"
                          : "bg-slate-200 dark:bg-slate-800"
                      }`}
                  >
                    {m.content}
                  </motion.div>
                ))}
                <div ref={messagesEndRef} />
              </div>

              {/* INPUT */}
              <div className="p-4 border-t dark:border-slate-700 flex gap-3">
                <input
                  value={text}
                  onChange={(e) => setText(e.target.value)}
                  onKeyDown={(e) => e.key === "Enter" && sendMessage()}
                  placeholder="Type a message..."
                  className="flex-1 input"
                />
                <button
                  onClick={sendMessage}
                  className="px-6 py-2 rounded-xl
                             bg-emerald-600 hover:bg-emerald-700
                             text-white font-bold"
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
