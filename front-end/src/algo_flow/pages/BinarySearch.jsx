import { useState } from "react";
import "../styles/BinarySearch.css";
import "../styles/LinearSearch.css";
import { useAlgorithmPlayer } from "../hooks/useAlgorithmPlayer.js";
import AlgorithmControls from "../components/AlgorithmControls";

function getStepMessage(step) {
  if (!step) return "";

  if (step.finished) {
    return "Binary Search completed.";
  }

  switch (step.message) {
    case "Executing INIT":
      return "Initializing search boundaries";

    case "Executing ASSIGN":
      return "Updating pointer values";

    case "Executing LOOP_CHECK":
      return "Checking if low ≤ high";

    case "Executing COMPARE":
      return "Comparing middle element with target";

    case "Executing JUMP":
      return "Updating search range";

    case "Executing RETURN":
      return "Target found or search exhausted";

    default:
      return step.message;
  }
}

function BinarySearch() {
  const [steps, setSteps] = useState([]);
  const [arrayInput, setArrayInput] = useState("");
  const [target, setTarget] = useState("");
  const {
    currentStep,
    setCurrentStep,
    isPlaying,
    setIsPlaying,
    speed,
    setSpeed,
  } = useAlgorithmPlayer(steps);

  const binarySearchCode = `
    public class BinarySearch {

      public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
      }
                    `;

  const handleExecute = async () => {
    const array = arrayInput
      .split(",")
      .map((n) => parseInt(n.trim(), 10))
      .sort((a, b) => a - b); // ensure sorted

    const payload = {
      array,
      target: parseInt(target, 10),
    };

    try {
      const response = await fetch(
        "http://localhost:8080/api/execute/binary-search",
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

  const step = steps[currentStep];

  return (
    <div className="binary-search-container">
      <h2 className="binary-search-title">Binary Search Visualizer</h2>

      <div className="main-layout">
        <div className="left-panel">
          <div className="section code-block">
            <pre>
              <code>{binarySearchCode}</code>
            </pre>
          </div>
          <textarea
            className="array-input"
            placeholder="Sorted Array (e.g. 1,3,5,7,9)"
            value={arrayInput}
            onChange={(e) => setArrayInput(e.target.value)}
          />

          <input
            className="input"
            type="number"
            placeholder="Target"
            value={target}
            onChange={(e) => setTarget(e.target.value)}
          />

          <button className="execute-button" onClick={handleExecute}>
            Execute
          </button>
        </div>

        <div className="right-panel">
          {steps.length > 0 && (
            <div className="visualization-layout">
              {/* 1️⃣ HEADER (fixed) */}
              <div className="viz-header">
                <h3>
                  Step {currentStep + 1} / {steps.length}
                </h3>

                <p className="step-message">{getStepMessage(step)}</p>
              </div>

              <div className="viz-variables">
                {step.pointers?.low !== undefined && (
                  <div className="pointer-value">low = {step.pointers.low}</div>
                )}
                {step.pointers?.mid !== undefined && (
                  <div className="pointer-value">mid = {step.pointers.mid}</div>
                )}
                {step.pointers?.high !== undefined && (
                  <div className="pointer-value">
                    high = {step.pointers.high}
                  </div>
                )}

                <div className="target-display">
                  🎯 Target value: <strong>{target}</strong>
                </div>
              </div>

              <div className="viz-scrollable">
                <div className="array-container">
                  {step.arraySnapshot.map((value, index) => {
                    const { low, mid, high } = step.pointers || {};

                    let className = "array-box";
                    if (index === mid) className += " mid";
                    else if (index === low) className += " low";
                    else if (index === high) className += " high";

                    return (
                      <div key={index} className="array-item">
                        <div className="array-index">{index}</div>

                        <div className={className}>
                          {value}
                          {index === low && (
                            <div className="pointer low-ptr">L</div>
                          )}
                          {index === mid && (
                            <div className="pointer mid-ptr">M</div>
                          )}
                          {index === high && (
                            <div className="pointer high-ptr">H</div>
                          )}
                        </div>
                      </div>
                    );
                  })}
                </div>

                {step.finished && (
                  <div className="result-box">
                    🎯 Result: <strong>{step.returnValue}</strong>
                  </div>
                )}

                <AlgorithmControls
                  currentStep={currentStep}
                  totalSteps={steps.length}
                  isPlaying={isPlaying}
                  onPrev={() => setCurrentStep((s) => s - 1)}
                  onNext={() => setCurrentStep((s) => s + 1)}
                  onPlay={() => setIsPlaying(true)}
                  onPause={() => setIsPlaying(false)}
                  onReset={() => {
                    setIsPlaying(false);
                    setCurrentStep(0);
                  }}
                  speed={speed}
                  onSpeedChange={(e) => setSpeed(Number(e.target.value))}
                />
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default BinarySearch;
