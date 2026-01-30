import { useState } from "react";
import "../styles/LinearSearch.css";

function getStepMessage(step) {
  if (!step) return "";

  if (step.finished) {
    return "Search completed.";
  }

  switch (step.message) {
    case "Executing INIT":
      return "Initializing index i = 0";

    case "Executing LOOP_CHECK":
      return "Checking if index is within array bounds";

    case "Executing COMPARE":
      return "Comparing current element with target";

    case "Executing INCREMENT":
      return "Target not found at this index. Moving to next index (i++)";

    case "Executing RETURN":
      return "Target found. Returning index.";

    default:
      return "";
  }
}

function LinearSearch() {
  const [steps, setSteps] = useState([]);
  const [currentStep, setCurrentStep] = useState(0);
  const [arrayInput, setArrayInput] = useState("");
  const [target, setTarget] = useState("");
  const linearSearchCode = `
  class LinearSearch {
    public int linearSearch(int[] arr, int target) {
        for(int i = 0; i < arr.length; i++) {
          if(arr[i] == target) {
              return i;
          }
        }
        return -1;
    }
  }
  `;

  const handleExecute = async () => {
    const array = arrayInput.split(",").map((n) => parseInt(n.trim(), 10));

    const payload = {
      array,
      target: parseInt(target, 10),
    };

    try {
      const response = await fetch(
        "http://localhost:8080/api/execute/linear-search",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        },
      );

      const data = await response.json();
      setSteps(data);
      setCurrentStep(0);
    } catch (err) {
      console.error("API call failed:", err);
    }
  };

  return (
    <div className="linear-search-container">
      <h2 className="linear-search-title">Linear Search Visualizer</h2>

      <div className="app-content">
        <div className="main-layout">
          {/* LEFT PANEL */}
          <div className="left-panel">
            <div className="section code-block">
              <pre>
                <code>{linearSearchCode}</code>
              </pre>
            </div>

            <div className="section input-column">
              <textarea
                className="array-input"
                placeholder="Array (e.g. 4,2,7,1)"
                value={arrayInput}
                onChange={(e) => setArrayInput(e.target.value)}
              />

              <input
                className="input target-input"
                type="number"
                placeholder="Target"
                value={target}
                onChange={(e) => setTarget(e.target.value)}
              />
            </div>

            <div className="section">
              <button className="execute-button" onClick={handleExecute}>
                Execute
              </button>
            </div>
          </div>

          {/* RIGHT PANEL */}
          <div className="right-panel">
            {steps.length > 0 && (
              <div className="visualization-content">
                <h3>
                  Step {currentStep + 1} / {steps.length}
                </h3>

                <div className="target-display">
                  🎯 Target value: <strong>{target}</strong>
                </div>

                <p className="step-message">
                  {getStepMessage(steps[currentStep])}
                </p>

                {steps[currentStep].pointers?.i !== undefined && (
                  <div className="pointer-value">
                    Current index{" "}
                    <strong>i = {steps[currentStep].pointers.i}</strong>
                  </div>
                )}

                <div className="array-container">
                  {steps[currentStep].arraySnapshot.map((value, index) => {
                    const isPointer = steps[currentStep].pointers?.i === index;

                    return (
                      <div key={index} className="array-item">
                        {/* INDEX */}
                        <div className="array-index">{index}</div>

                        {/* VALUE */}
                        <div
                          className={`array-box ${isPointer ? "active" : ""}`}
                        >
                          {value}
                          {isPointer && <div className="pointer">i</div>}
                        </div>
                      </div>
                    );
                  })}
                </div>

                {steps[currentStep].finished && (
                  <div className="result-box">
                    🎯 Result: <span>{steps[currentStep].returnValue}</span>
                  </div>
                )}

                <div className="navigation">
                  <button
                    disabled={currentStep === 0}
                    onClick={() => setCurrentStep((s) => s - 1)}
                  >
                    Prev
                  </button>

                  <button
                    disabled={currentStep === steps.length - 1}
                    onClick={() => setCurrentStep((s) => s + 1)}
                  >
                    Next
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default LinearSearch;
