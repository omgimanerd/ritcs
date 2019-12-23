import sys


# O(n^2.1)
# Counterexample:   1 8 3 6 5 4 7 2 9
# Optimal solution: 1 3 5 7 9
# Output:           3 6 7 9
# Reason:           The algorithm tries to find the *first* index greater than i
#                   such that a_i < a_j. In the above input, the optimal
#                   solution is reached by considering a j that is not the first
#                   index which satisfies the condition.
def try2(sequence):
  num_loops = 0
  # Create a list to hold all of the valid subsequences
  subsequences = []
  # Try the following with all l = 1, 2, ..., n
  for l in range(0, len(sequence)):
    num_loops += 1
    # Let i = l
    i = l
    subsequence = []
    # Include a_i in the subsequence
    subsequence.append(sequence[l])
    # for every remaining element of the sequence,
    while i < len(sequence):
      num_loops += 1
      # Attempt to find a number after it in the sequence with a larger value
      for j in range(i, len(sequence)):
        num_loops += 1
        if sequence[i] < sequence[j]:
          # Should that number exist, add it to the sequence and repeat
          subsequence.append(sequence[j])
          i = j
          break
        if j == (len(sequence) - 1):
          i = len(sequence)
          break
    subsequences.append(subsequence)
  # Out of the n obtained sequences, output one with the longest length
  sort = sorted(subsequences, key=lambda x: len(x), reverse=True)
  return sort[0] if len(sort) > 0 else None
  # return num_loops


def f(z):
  return (z ** 2.1)


# print("z\tOut\tf(z)\tout-f(z)")
# for z in range(1, 257):
#   t = try2(list(range(z+1)))
#   fz = f(z)
#   print("%d\t%d\t%d\t%d" % ( z, t, fz, t-fz ))
print(try2([int(x) for x in sys.argv[1:]]))
