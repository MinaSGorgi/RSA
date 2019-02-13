package attacks;

import java.math.BigInteger;

import core.RSA;
import core.RSA.RSAException;


public class MathAttack {
    private final static BigInteger X0 = new BigInteger("2");
    private final static BigInteger Y0 = new BigInteger("2");
    private final static BigInteger D0 = BigInteger.ONE;

    private final BigInteger modulus;
    private final BigInteger privateKey;
    public final int nSteps;

    private static BigInteger polyMod(BigInteger x, BigInteger n) {
        return x.pow(2).add(BigInteger.ONE).mod(n);
    }

    public MathAttack(BigInteger e, BigInteger n) {
        BigInteger x = X0;
        BigInteger y = Y0;
        BigInteger d = D0;
        int steps = 0;

        do {
            while (d.equals(BigInteger.ONE)) {
                ++steps;
                x = polyMod(x, n);
                y = polyMod(polyMod(y, n), n);
                d = x.subtract(y).abs().gcd(n);
            }
            x = x.add(BigInteger.ONE);
        } while (d.equals(n));

        BigInteger q   = n.divide(d);
        BigInteger phi = d.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        privateKey = e.modInverse(phi);
        modulus    = n;
        nSteps     = steps;
    }

    public BigInteger crack(BigInteger message) {
        return message.modPow(privateKey, modulus);
    }

    public static void main(String[] args) throws RSAException  {
        BigInteger message = new BigInteger(args[0]);
        int nBits = Integer.parseInt(args[1]);

        RSA rsa = new RSA(nBits);
        BigInteger encrypted = rsa.encrypt(message);
        System.out.println(rsa.toString());

        MathAttack mathAttack = new MathAttack(rsa.publicKey, rsa.modulus);
        BigInteger cracked = mathAttack.crack(encrypted);

        System.out.println("message = " + message);
        System.out.println("cracked = " + cracked);
        System.out.println(cracked.equals(message) ?  "Success!" : "Failure!");
    }
}
