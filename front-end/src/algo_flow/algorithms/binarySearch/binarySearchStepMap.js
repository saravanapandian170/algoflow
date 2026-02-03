export const binarySearchStepToCodeLineMap = {
  "Executing INIT": [3, 4],
  "Executing LOOP_CHECK": [6],
  "Executing ASSIGN": [7],
  "Executing COMPARE": [9],
  "Executing JUMP": [11, 13],
  "Executing RETURN": [10, 17],
};

/*
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
} */