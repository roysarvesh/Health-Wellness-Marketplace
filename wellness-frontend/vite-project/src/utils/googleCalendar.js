export const addToGoogleCalendar = ({ title, start, end }) => {
  const url = new URL("https://www.google.com/calendar/render");
  url.searchParams.append("action", "TEMPLATE");
  url.searchParams.append("text", title);
  url.searchParams.append("dates", `${start}/${end}`);
  window.open(url.toString(), "_blank");
};
