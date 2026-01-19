import { useState } from "react";
import api from "../../api/axios";

export default function Recommendations() {
  const [symptom, setSymptom] = useState("");
  const [result, setResult] = useState("");

  const submit = async () => {
    const res = await api.post("/recommendations", { symptom });
    setResult(res.data.suggestedTherapy);
  };

  return (
    <div className="p-6 max-w-xl mx-auto">
      <h2 className="text-xl mb-4">AI Therapy Recommendation</h2>
      <input className="border p-2 w-full"
        placeholder="Enter your symptom"
        onChange={(e) => setSymptom(e.target.value)} />
      <button className="mt-3 bg-blue-600 text-white px-4 py-2"
        onClick={submit}>
        Get Recommendation
      </button>
      {result && (
        <div className="mt-4 p-3 bg-green-100">
          {result}
        </div>
      )}
    </div>
  );
}
