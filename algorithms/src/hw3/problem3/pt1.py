# fails for 567890123
# O(n^2) because worst case is the whole thing, which takes n * 1n/2
def findNextItem(sequence, start):
  if start == len(sequence) - 1:
    return sequence[-1:]
  minVal = sequence[start]
  minIndex = start
  for i in range(start, len(sequence)):
    if sequence[i] < minVal:
      minVal = sequence[i]
      minIndex = i
  return [minVal] + (findNextItem(sequence, minIndex + 1))


print(findNextItem([5, 6, 7, 8, 9, 0, 1, 2, 3], 0))
