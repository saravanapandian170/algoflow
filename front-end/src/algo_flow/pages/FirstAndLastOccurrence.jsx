import { useState } from "react";
import "../styles/BinarySearch.css";
import "../styles/LinearSearch.css";
import { useAlgorithmPlayer } from "../hooks/useAlgorithmPlayer.js";
import AlgorithmControls from "../components/AlgorithmControls";

function getStepMessage(step) {
  if (!step) return "";

  if (step.finished) {
    return "First and Last Occurrence search completed.";
  }

  switch (step.message) {
    case "Executing INIT":
      return "Initializing variables";

    case "Executing ASSIGN":
      return "Updating pointers or result indices";

    case "Executing LOOP_CHECK":
      return "Checking search boundaries";

    case "Executing COMPARE":
      return "Comparing middle element with target";

    case "Executing JUMP":
      return "Adjusting search range";

    case "Executing RETURN":
      return "Returning first and last indices";

    default:
      return step.message;
  }
}

function FirstAndLastOccurrence() {
  const [steps, setSteps] = useState([]);
  const [array, setArray] = useState([]); 
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

  const codeSnippet = `
public static int firstAndLastOccurrence(int[] array, int target) {

        int low = 0;
        int high = array.length - 1;
        int mid;

        int firstIndex = -1;
        int lastIndex = -1;

        // =====================================================
        // PHASE 1: FIRST OCCURRENCE
        // =====================================================
        while (low <= high) {

            mid = (low + high) / 2;

            if (array[mid] == target) {
                firstIndex = mid;
                high = mid - 1;              // move left
            } else if (array[mid] < target) {
                low = mid + 1;               // move right
            } else {
                high = mid - 1;              // move left
            }
        }

        // =====================================================
        // EARLY EXIT IF TARGET NOT FOUND
        // =====================================================
        if (firstIndex == -1) {
            return -1;
        }

        // =====================================================
        // RESET POINTERS FOR LAST OCCURRENCE
        // =====================================================
        low = 0;
        high = array.length - 1;

        // =====================================================
        // PHASE 2: LAST OCCURRENCE
        // =====================================================
        while (low <= high) {

            mid = (low + high) / 2;

            if (array[mid] == target) {
                lastIndex = mid;
                low = mid + 1;               // move right
            } else if (array[mid] < target) {
                low = mid + 1;               // move right
            } else {
                high = mid - 1;              // move left
            }
        }

        return lastIndex;
    }
`;

  const handleExecute = async () => {
    const array = arrayInput
      .split(",")
      .map((n) => parseInt(n.trim(), 10))
      .sort((a, b) => a - b);

    const payload = {
      array,
      target: parseInt(target, 10),
    };

    try {
      const response = await fetch(
        "http://localhost:8080/api/execute/first-and-last-occurrence",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        }
      );

      const data = await response.json();
      setArray(data.array);
      setSteps(data.steps);
      setCurrentStep(0);
    } catch (err) {
      console.error("API call failed:", err);
    }
  };

  const step = steps[currentStep];

  return (
    <div className="binary-search-container">
      <h2 className="binary-search-title">
        First & Last Occurrence Visualizer
      </h2>

      <div className="main-layout">
        {/* LEFT PANEL */}
        <div className="left-panel">
          <div className="section code-block">
            <pre>
              <code>{codeSnippet}</code>
            </pre>
          </div>

          <textarea
            className="array-input"
            placeholder="Sorted Array (e.g. 1,2,2,2,3,4)"
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

        {/* RIGHT PANEL */}
        <div className="right-panel">
          {steps.length > 0 && (
            <div className="visualization-layout">
              {/* 1️⃣ HEADER (fixed) */}
              <div className="viz-header">
                <h3>
                  Step {currentStep + 1} / {steps.length}
                </h3>
                <p className="step-message">
                  {getStepMessage(step)}
                </p>
              </div>

              {/* 2️⃣ VARIABLES (fixed) */}
              <div className="viz-variables">
                {step.pointers?.low !== undefined && (
                  <div className="pointer-value">
                    low = {step.pointers.low}
                  </div>
                )}
                {step.pointers?.mid !== undefined && (
                  <div className="pointer-value">
                    mid = {step.pointers.mid}
                  </div>
                )}
                {step.pointers?.high !== undefined && (
                  <div className="pointer-value">
                    high = {step.pointers.high}
                  </div>
                )}
                {step.pointers?.firstIndex !== undefined && (
                  <div className="pointer-value">
                    firstIndex = {step.pointers.firstIndex}
                  </div>
                )}
                {step.pointers?.lastIndex !== undefined && (
                  <div className="pointer-value">
                    lastIndex = {step.pointers.lastIndex}
                  </div>
                )}

                <div className="target-display">
                  🎯 Target value: <strong>{target}</strong>
                </div>
              </div>

              {/* 3️⃣ SCROLLABLE (array + controls) */}
              <div className="viz-scrollable">
                <div className="array-container">
                  {array.map((value, index) => {
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
                          {index === mid && (
                            <div className="pointer mid-ptr">M</div>
                          )}
                        </div>
                      </div>
                    );
                  })}
                </div>

                {step.finished && (
                  <div className="result-box">
                    🎯 Result:{" "}
                    <strong>
                      [{step.pointers.firstIndex},{" "}
                      {step.pointers.lastIndex}]
                    </strong>
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
                  onSpeedChange={(e) =>
                    setSpeed(Number(e.target.value))
                  }
                />
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default FirstAndLastOccurrence;
