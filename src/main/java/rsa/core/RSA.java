package core;

import java.math.BigInteger;
import java.security.SecureRandom;


public class RSA {
    public class RSAException extends Exception { 
        public RSAException(String errorMessage) {
            super(errorMessage);
        }
    }

    private final static SecureRandom random = new SecureRandom();
    private final static BigInteger COMMON_PUBLIC_KEY = new BigInteger("65537");

    public final BigInteger modulus;
    public final BigInteger publicKey = COMMON_PUBLIC_KEY;
    private final BigInteger privateKey;

    public RSA(int nBits) {
        BigInteger p;
        BigInteger q;
        BigInteger phi;
        do {
            p = BigInteger.probablePrime(nBits / 2, random);
            q = BigInteger.probablePrime(nBits / 2, random);
            phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        } while(!publicKey.gcd(phi).equals(BigInteger.ONE) || p.equals(q));

        modulus    = p.multiply(q);
        privateKey = publicKey.modInverse(phi);
    }

    public BigInteger encrypt(BigInteger message) throws RSAException {
        if (message.compareTo(modulus) >= 0) {
            throw new RSAException("Message: " + message + " is >= modulus: " + modulus);
        }

        return message.modPow(publicKey, modulus);
    }

    public BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, modulus);
    }

    public String toString() {
        return "public  = " + publicKey  + "\n"
                + "private = " + privateKey + "\n"
                + "modulus = " + modulus;
    }

    public static void main(String[] args) throws RSAException {
        BigInteger message = new BigInteger(args[0]);
        int nBits = Integer.parseInt(args[1]);

        RSA rsa = new RSA(nBits);
        BigInteger encrypted = rsa.encrypt(message);
        BigInteger decrypted = rsa.decrypt(encrypted);

        System.out.println(rsa.toString());
        System.out.println("encrypted = " + encrypted);
        System.out.println("decrypted = " + decrypted);
    }
}