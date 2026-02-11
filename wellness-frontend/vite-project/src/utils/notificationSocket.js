import SockJS from "sockjs-client/dist/sockjs";
import { Client } from "@stomp/stompjs";
import { getToken } from "./token";

let stompClient = null;

export const connectNotificationSocket = (onMessage) => {
  const token = getToken();

  const socket = new SockJS("http://localhost:8080/ws");

  stompClient = new Client({
    webSocketFactory: () => socket,

    connectHeaders: {
      Authorization: `Bearer ${token}`,
    },

    debug: () => {},

    reconnectDelay: 5000,

    onConnect: () => {
      console.log("🔔 Notifications connected");

      stompClient.subscribe("/user/queue/notifications", (msg) => {
        onMessage(JSON.parse(msg.body));
      });
    },
  });

  stompClient.activate();
};

export const disconnectNotificationSocket = () => {
  stompClient?.deactivate();
};
