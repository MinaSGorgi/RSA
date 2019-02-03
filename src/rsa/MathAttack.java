package rsa;

import java.math.BigInteger;
import java.util.Random;

public class MathAttack {
    private final static BigInteger BI_ONE = new BigInteger("1");
    private final static BigInteger X0     = new BigInteger("2");
    private final static BigInteger Y0     = new BigInteger("2");
    private final static BigInteger D0     = new BigInteger("1");

    private final BigInteger modulus;
    private final BigInteger privateKey;
    public final int nSteps;

    private static BigInteger polyMod(BigInteger x, BigInteger n) {
        return x.pow(2).add(BI_ONE).mod(n);
    }

    public MathAttack(BigInteger e, BigInteger n) {
        BigInteger x = X0;
        BigInteger y = Y0;
        BigInteger d = D0;
        int steps = 0;

        do {
            while (d.equals(BI_ONE)) {
                ++steps;
                x = polyMod(x, n);
                y = polyMod(polyMod(y, n), n);
                d = x.subtract(y).abs().gcd(n);
            }
            x = x.add(BI_ONE);
        } while (d.equals(n));

        BigInteger q   = n.divide(d);
        BigInteger phi = d.subtract(BI_ONE).multiply(q.subtract(BI_ONE));
        privateKey = e.modInverse(phi);
        modulus    = n;
        nSteps     = steps;
    }

    public BigInteger decrypt(BigInteger message) {
        return message.modPow(privateKey, modulus);
    }

    public static void main(String[] args) {
        int nBits = Integer.parseInt(args[0]);
        Random random = new Random();
        BigInteger message = new BigInteger(nBits - 1, random);

        RSA rsa = new RSA(nBits);
        BigInteger encrypted = rsa.encrypt(message);

        MathAttack mathAttack = new MathAttack(rsa.publicKey, rsa.modulus);
        BigInteger decrypted = mathAttack.decrypt(encrypted);

        System.out.println("message = " + message);
        System.out.println("cracked = " + decrypted);
        System.out.println(decrypted.equals(message) ?  "Success!" : "Failure!");
    }
}
