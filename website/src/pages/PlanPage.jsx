import { useEffect, useState } from "react";
import { getPlan } from "../api/api";
import { useParams } from "react-router-dom";
import AssignmentList from "../components/AssignmentList";

export default function PlanPage() {
  const { id } = useParams();
  const [plan, setPlan] = useState(null);

  useEffect(() => {
    async function fetchData() {
      const data = await getPlan(id);
      setPlan(data);
    }
    fetchData();
  }, [id]);

  if (!plan) return <div>Loading plan...</div>;

  return (
    <div style={{ padding: "40px" }}>
      <AssignmentList assignments={plan.assignments} />
    </div>
  );
}
