package rsa;

import java.math.BigInteger;
import java.security.SecureRandom;


public class RSA {
    private final static SecureRandom random = new SecureRandom();
    private final static BigInteger COMMON_PUBLIC_KEY = new BigInteger("65537");
    private final static BigInteger BI_ONE = new BigInteger("1");

    private final BigInteger modulus;
    private final BigInteger publicKey = COMMON_PUBLIC_KEY;
    private final BigInteger privateKey;

    private static BigInteger bigIntegerLcm(BigInteger a, BigInteger b) {
        return a.multiply(b).divide(a.gcd(b));
    }

    RSA(int nBits) {
        BigInteger p;
        BigInteger q;
        BigInteger lambda;
        do {
            p = BigInteger.probablePrime(nBits / 2, random);
            q = BigInteger.probablePrime(nBits / 2, random);
            lambda = bigIntegerLcm(p.subtract(BI_ONE), q.subtract(BI_ONE));
        } while(!publicKey.gcd(lambda).equals(BI_ONE));

        modulus    = p.multiply(q);
        privateKey = publicKey.modInverse(lambda);
    }

    BigInteger encrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }

    BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, modulus);
    }

    public String toString() {
        return "public  = " + publicKey  + "\n"
                + "private = " + privateKey;
    }

    public static void main(String[] args) {
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