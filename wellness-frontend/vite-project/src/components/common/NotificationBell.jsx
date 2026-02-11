import { Badge, IconButton } from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import { useNavigate } from "react-router-dom";
import { useNotifications } from "../context/NotificationContext";

export default function NotificationBell() {
  const { unreadCount } = useNotifications();
  const navigate = useNavigate();

  return (
    <IconButton onClick={() => navigate("/notifications")}>
      <Badge badgeContent={unreadCount} color="error">
        <NotificationsIcon className="text-slate-700 dark:text-white" />
      </Badge>
    </IconButton>
  );
}
