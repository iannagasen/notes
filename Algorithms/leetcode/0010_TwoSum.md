
## Two Sum
Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
You may assume that each input would have exactly one solution, and you may not use the same element twice.
You can return the answer in any order.

### Example 1
- Input: nums = [2,7,11,15], target = 9
- Output: [0,1]
- Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].

### Example 2
- Input: nums = [3,2,4], target = 6
- Output: [1,2]

### Example 3
- Input: nums = [3,3], target = 6
- Output: [0,1]

### Solution: 
### **`Two-pass Hash Table`**
```java
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> numMap = new HashMap<>();
    int n = nums.length;

    // Build the hash table
    for (int i = 0; i < n; i++) {
        numMap.put(nums[i], i);
    }

    // Find the complement
    for (int i = 0; i < n; i ++) {
        int complement = target - nums[i];
        if (numMap.containsKey(complement) && numMap.get(complement) != i) {
            return new int[] {i, numMap.get(complement)};
        }
    }

    // No solution found
    return new int[]{};
}
```

### **`Brute Force`**
```java
public int[] twoSum(int[] nums, int target) {
  int n = nums.length;
  for (int i = 0; i < n - 1; i++) {
    for (int j = i + 1; j < n; j++ ) {
      if (target == nums[i] + nums[j]) {
        return new int[] {i, j};
      }
    }
  }
  return new int[] {}; // no solution
}
```