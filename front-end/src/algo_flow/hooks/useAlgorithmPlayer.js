import { useEffect, useState } from "react";

export function useAlgorithmPlayer(steps, defaultSpeed = 800) {
  const [currentStep, setCurrentStep] = useState(0);
  const [isPlaying, setIsPlaying] = useState(false);
  const [speed, setSpeed] = useState(defaultSpeed);

  useEffect(() => {
    setCurrentStep(0);
    setIsPlaying(false);
  }, [steps]);

  useEffect(() => {
    if (!isPlaying) return;

    if (currentStep >= steps.length - 1) {
      return;
    }

    const timer = setTimeout(() => {
      setCurrentStep((s) => s + 1);
    }, speed);

    return () => clearTimeout(timer);
  }, [isPlaying, currentStep, speed, steps]);

  return {
    currentStep,
    setCurrentStep,
    isPlaying,
    setIsPlaying,
    speed,
    setSpeed,
  };
}
