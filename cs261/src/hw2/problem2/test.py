#!/usr/bin/env python3

# What do I do? We don't know yet!
#             T(1) = 1
# Recurrence: T(n) = 2T(n/2) + c
#                  = 2(2T(n/4) + c) + c
#                  = 2(2(2T(n/8) + c) + c) + c
#                  = 2^k * T(n/(2^k)) + k*c
#                  stops when (n/2^k) = 1 => k = log(n)
#                  = n * 1 + c*log(n)
#                  = O(n+log(n))
#                  = O(n)
def wdo(left, right, s):
  # Base case: examining one letter
  if left == right:
    if s[left] in ['a', 'e', 'i', 'o', 'u']:
      return (0, 0, 0, -1)
    return (1, 1, 1, 1)
  # Recursive case: examining more than one letter
  if left < right:
    # The middle index of the array (rounded down)
    m = (left + right) // 2
    # Recurse on the left side
    (lmaxdif, llmaxdif, lrmaxdif, ldif) = wdo(left, m, s)
    # Recurse on the right side
    (rmaxdif, rlmaxdif, rrmaxdif, rdif) = wdo(m + 1, right, s)
    maxdif = max(lmaxdif, rmaxdif, lrmaxdif + rlmaxdif)
    leftalignedmaxdif = max(llmaxdif, ldif + rlmaxdif)
    rightalignedmaxdif = max(rrmaxdif, lrmaxdif + rdif)
    dif = ldif + rdif
    # Return tuple field explanation:
    # 0: ?
    # 1: ?
    # 2: 
    # 3: The number of consonants minus the number of vowels in the whole string
    return (maxdif, leftalignedmaxdif, rightalignedmaxdif, dif)


l = []
l.append("ab")
l.append("ba")
l.append("aaa")
l.append("aab")
l.append("abb")
l.append("bbb")
l.append("bba")
l.append("baa")
l.append("bab")
l.append("aba")
l.append("baaaa")
l.append("abaaa")
l.append("aabaa")
l.append("aaaba")
l.append("aaaab")
for s in l:
  print(s + "\t" + str(wdo(0, len(s) - 1, s)))
