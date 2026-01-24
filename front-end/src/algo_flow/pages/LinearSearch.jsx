import { useState } from "react";

function LinearSearch() {
  const [code, setCode] = useState("");
  const [arrayInput, setArrayInput] = useState("");
  const [target, setTarget] = useState("");

  const handleExecute = async () => {
    const array = arrayInput
      .split(",")
      .map((n) => parseInt(n.trim(), 10));

    const payload = {
      code,
      array,
      target: parseInt(target, 10),
    };

    console.log("Sending payload:", payload);
    try {
        const response = await fetch(
        "http://localhost:8080/api/execute/linear-search",
        {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        }
        );

        const data = await response.json();
        console.log("Execution Steps:", data);
    } catch (err) {
    console.error("API call failed:", err);
  }
  };

  return (
    <div>
      <h2>Linear Search Visualizer</h2>

      <textarea
        rows={10}
        cols={70}
        placeholder="Paste Linear Search Java code here"
        value={code}
        onChange={(e) => setCode(e.target.value)}
      />

      <br /><br />

      <input
        type="text"
        placeholder="Array (e.g. 4,2,7,1)"
        value={arrayInput}
        onChange={(e) => setArrayInput(e.target.value)}
      />

      <br /><br />

      <input
        type="number"
      
        placeholder="Target"
        value={target}
        onChange={(e) => setTarget(e.target.value)}
      />

      <br /><br />

      <button onClick={handleExecute}>Execute</button>
    </div>
  );
}

export default LinearSearch;
