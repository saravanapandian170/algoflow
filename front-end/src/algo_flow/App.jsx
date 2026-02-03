import { Routes, Route } from "react-router-dom";
import SearchSelector from "./SearchSelector";
import LinearSearch from "./algorithms/linearSearch/LinearSearch";
import BinarySearch from "./algorithms/binarySearch/BinarySearch";
import FirstAndLastOccurrence from "./algorithms/firstAndLastOccurrence/FirstAndLastOccurrence";
import "./styles/App.css";

function App() {
  return (
    <Routes>
      {/* Home / Landing Page */}
      <Route path="/" element={<SearchSelector />} />

      {/* Algorithm Pages */}
      <Route path="/linear-search" element={<LinearSearch />} />
      <Route path="/binary-search" element={<BinarySearch />} />
      <Route path="/first-last-occurrence" element={<FirstAndLastOccurrence />} />
    </Routes>
  );
}

export default App;
