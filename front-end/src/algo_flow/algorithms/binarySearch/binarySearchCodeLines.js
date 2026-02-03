export const binarySearchCodeLines = [
  "public class BinarySearch {",                                //0
  "",                                                           //1
  "  public static int binarySearch(int[] arr, int target) {",  //2
  "    int low = 0;",                                           //3                     
  "    int high = arr.length - 1;",                             //4              
  "",                                                           //5    
  "    while (low <= high) {",                                  //6
  "      int mid = low + (high - low) / 2;",                    //7
  "",                                                           //8  
  "      if (arr[mid] == target) {",                            //9
  "        return mid;",                                        //10 
  "      } else if (arr[mid] < target) {",                      //11  
  "        low = mid + 1;",                                     //12
  "      } else {",                                             //13
  "        high = mid - 1;",                                    //14 
  "      }",                                                    //15
  "    }",                                                      //16
  "",                                                           //17 
  "    return -1;",                                             //18
  "  }",                                                        //19
  "}"                                                           //20
];
