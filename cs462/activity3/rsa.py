#!/usr/bin/env python3

def brute_force_factor(n):
    """
    Uses brute force to find the prime factorization of n.
    """
    factors = []
    i = 2
    while n != 1:
        if n % i == 0:
            factors.append(i)
            n /= i
        else:
            i += 1
    return factors

def brute_force_modular_inverse(n, mod):
    """
    Uses brute force to compute the modular inverse of n mod (mod).
    """
    for i in range(mod):
        if (n * i) % mod == 1:
            return i
    return None

def fast_modular_exponentiation(base, exponent, mod):
    """
    Uses fast modular exponentiation to compute base^exponent mod (mod).
    """
    result = 1
    base %= mod
    while exponent > 0:
        if exponent % 2 == 1:
            result = (result * base) % mod
        exponent //= 2
        base = (base * base) % mod;
    return result

def decrypt(y, private_key, mod):
    """
    Decodes a number encrypted with RSA given the private key and public
    modulus.
    """
    return fast_modular_exponentiation(y, private_key, mod)

def split_ascii(n):
    """
    Converts the given number into binary and separates it into 7-bit chunks
    to convert to ASCII.
    """
    binary = bin(n)[2:]
    decoded = ""
    for i in range(0, len(binary), 7):
        decoded += chr(int(binary[i:i + 7], 2))
    return decoded

if __name__ == '__main__':
    # Values given in the email and class assignment
    ciphertext = "1000011 0011100 1010110 1011100".replace(" ", "")
    y = int(ciphertext, 2)
    n = 276811163
    e = 73
    print("Ciphertext: {} base 2 = {} base 10".format(ciphertext, y))
    print("n: {}".format(n))
    print("e: {}".format(e))

    # Use brute force to find p and q by factoring the public key product n
    factors = brute_force_factor(n)
    assert len(factors) == 2
    p, q = factors
    print("Factored n = p * q = {} * {}".format(p, q))

    # Compute Euler's totient function using p and q and use brute force
    # to determine the private key by calculating the modular inverse of
    # the public exponent e.
    phi = (p - 1) * (q - 1)
    print("phi(n) = (p - 1) * (q - 1) = {}".format(phi))
    private_key = brute_force_modular_inverse(e, phi)
    print("private key = e^-1 mod phi = {}".format(private_key))

    # Convert the binary ciphertext into a decimal number and use fast
    # modular exponentiation to decrypt it.
    x = decrypt(y, private_key, n)
    print("Decrypted ciphertext base 10: {}".format(x))

    # Convert the decrypted decimal number to binary and back into ASCII.
    plaintext = split_ascii(x)
    print("Decrypted ciphertext in ASCII: {}".format(plaintext))
