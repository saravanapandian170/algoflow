import { Routes, Route } from "react-router-dom";
import SearchSelector from "./SearchSelector";
import LinearSearch from "./pages/LinearSearch";
import BinarySearch from "./pages/BinarySearch";
import FirstAndLastOccurrence from "./pages/FirstAndLastOccurrence";
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
