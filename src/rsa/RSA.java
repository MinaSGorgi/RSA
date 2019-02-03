package rsa;

import java.math.BigInteger;
import java.security.SecureRandom;


public class RSA {
    private final static SecureRandom random = new SecureRandom();
    private final static BigInteger COMMON_PUBLIC_KEY = new BigInteger("65537");
    private final static BigInteger BI_ONE = new BigInteger("1");

    public final BigInteger modulus;
    public final BigInteger publicKey = COMMON_PUBLIC_KEY;
    private final BigInteger privateKey;

    RSA(int nBits) {
        BigInteger p;
        BigInteger q;
        BigInteger phi;
        do {
            p = BigInteger.probablePrime(nBits / 2, random);
            q = BigInteger.probablePrime(nBits / 2, random);
            phi = p.subtract(BI_ONE).multiply(q.subtract(BI_ONE));
        } while(!publicKey.gcd(phi).equals(BI_ONE));

        modulus    = p.multiply(q);
        privateKey = publicKey.modInverse(phi);
    }

    BigInteger encrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }

    BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, modulus);
    }

    public String toString() {
        return "public  = " + publicKey  + "\n"
                + "private = " + privateKey + "\n"
                + "modulus = " + modulus;
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