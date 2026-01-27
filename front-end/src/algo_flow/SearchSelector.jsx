import { useNavigate } from "react-router-dom";
import "./styles/App.css";

function SearchSelector() {
  const navigate = useNavigate();

  return (
    <div className="search-selector-container">
      <div className="selection-screen">
        <h1 className="app-title">AlgoFlow</h1>
        <p className="app-subtitle">
          Visualize how algorithms work step by step
        </p>

        <h2 className="section-title">Select Search Algorithm</h2>

        <div className="algo-cards">
          <div
            className="algo-card"
            onClick={() => navigate("/linear-search")}
          >
            <h3>Linear Search</h3>
            <p>Search sequentially from start to end</p>
            <span className="complexity">O(n)</span>
          </div>

          <div
            className="algo-card"
            onClick={() => navigate("/binary-search")}
          >
            <h3>Binary Search</h3>
            <p>Divide array into halves (sorted array)</p>
            <span className="complexity">O(log n)</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SearchSelector;
