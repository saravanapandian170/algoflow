function AlgorithmControls({
  currentStep,
  totalSteps,
  isPlaying,
  onPrev,
  onNext,
  onPlay,
  onPause,
  onReset,
  speed,
  onSpeedChange,
}) {
  return (
    <div className="navigation">
      <button disabled={currentStep === 0} onClick={onPrev}>
        Prev
      </button>

      <button
        disabled={currentStep === totalSteps - 1}
        onClick={onNext}
      >
        Next
      </button>

      <button onClick={onPlay} disabled={isPlaying}>
        ▶ Play
      </button>

      <button onClick={onPause} disabled={!isPlaying}>
        ⏸ Pause
      </button>

      <select value={speed} onChange={onSpeedChange}>
        <option value={1200}>Slow</option>
        <option value={800}>Normal</option>
        <option value={400}>Fast</option>
      </select>

      <button onClick={onReset}>
        ⏹ Reset
      </button>
    </div>
  );
}

export default AlgorithmControls;
