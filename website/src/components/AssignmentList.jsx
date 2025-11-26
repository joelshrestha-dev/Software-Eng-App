export default function AssignmentList({ assignments }) {
  return (
    <div>
      <h2>Generated Plan</h2>

      <table border="1" cellPadding="8" style={{ marginTop: "10px" }}>
        <thead>
          <tr>
            <th>Title</th>
            <th>Due Date</th>
            <th>Est. Time (min)</th>
          </tr>
        </thead>

        <tbody>
          {assignments.map((a, i) => (
            <tr key={i}>
              <td>{a.title}</td>
              <td>{a.dueDate}</td>
              <td>{a.estTime}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
