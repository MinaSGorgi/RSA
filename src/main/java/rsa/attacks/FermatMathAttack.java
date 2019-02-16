package attacks;

import core.RSA;

import java.math.BigInteger;

public class FermatMathAttack {

    private BigInteger privateKey, modulus;

    public FermatMathAttack(BigInteger publicKey, BigInteger modulus) {
        this.modulus = modulus;
        BigInteger k;
        BigInteger root = modulus.sqrt();
        k = root.pow(2).equals(modulus) ? root : root.add(BigInteger.ONE);
        BigInteger h;
        BigInteger step = BigInteger.ZERO;
        do {
            h = k.add(step).pow(2).subtract(modulus).sqrt();
            step = step.add(BigInteger.ONE);
        }while (!h.pow(2).equals(k.pow(2).subtract(modulus)));

        k = k.add(step).subtract(BigInteger.ONE);
        BigInteger p = k.add(h), q = k.subtract(h);

        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        privateKey = publicKey.modInverse(phi);
    }

    public BigInteger crack (BigInteger encryptedMessage){
        return encryptedMessage.modPow(privateKey, modulus);
    }

    public static void main (String[] args) throws RSA.RSAException {
        BigInteger message = new BigInteger(args[0]);
        int nBits = Integer.parseInt(args[1]);

        core.RSA rsa = new core.RSA(nBits);
        BigInteger encrypted = rsa.encrypt(message);

        FermatMathAttack fermatMathAttack = new FermatMathAttack(rsa.publicKey, rsa.modulus);
        BigInteger cracked = fermatMathAttack.crack(encrypted);

        System.out.println("message = " + message);
        System.out.println("cracked = " + cracked);
        System.out.println(cracked.equals(message) ?  "Success!" : "Failure!");
    }
}
