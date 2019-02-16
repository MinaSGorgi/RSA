package main.java.rsa.attacks;

import core.RSA;

import java.math.BigInteger;

public class FermatMathAttack {

    private BigInteger privateKey, modulus;

    public FermatMathAttack(BigInteger publicKey, BigInteger modulus) {
        this.modulus = modulus;
        BigInteger k;
        BigInteger[] temp = modulus.sqrtAndRemainder();
        k = temp[1].equals(BigInteger.ZERO) ? temp[0] : temp[0].add(BigInteger.ONE);
        if (k.pow(2).compareTo(modulus) <= 0){
            System.out.println("stuck");
        }
        BigInteger h;
        BigInteger reminder;
        BigInteger step = BigInteger.ZERO;
        do {
            BigInteger[] result = k.add(step).pow(2).subtract(modulus).sqrtAndRemainder();
            h = result[0];
            reminder = result[1];
            if (h.pow(2).compareTo(modulus) >= 1){
                System.out.println("Failed in the loop");
            }
        }while (! reminder.equals(BigInteger.ZERO));
        BigInteger p = k.add(h), q = k.subtract(h);

        if (!p.multiply(q).equals(modulus)){
            System.out.println("Bug in the Fermat algorithm");
        }

        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        privateKey = publicKey.modInverse(phi);
    }

    BigInteger crack (BigInteger encryptedMessage){
        return encryptedMessage.modPow(privateKey, modulus);
    }

    public static void main (String[] args) throws RSA.RSAException {
        //BigInteger message = new BigInteger(args[0]);
        int nBits = Integer.parseInt(args[1]);

        /*core.RSA rsa = new core.RSA(nBits);
        BigInteger encrypted = rsa.encrypt(message);
        System.out.println(rsa.toString());

        FermatMathAttack fermatMathAttack = new FermatMathAttack(rsa.publicKey, rsa.modulus);
        BigInteger cracked = fermatMathAttack.crack(encrypted);

        System.out.println("message = " + message);
        System.out.println("cracked = " + cracked);
        System.out.println(cracked.equals(message) ?  "Success!" : "Failure!");*/

        for (int i = 3; i < 10; i++) {
            core.RSA rsa = new core.RSA(nBits);
            BigInteger encrypted = rsa.encrypt(new BigInteger(String.valueOf(i)));
            System.out.println(rsa.toString() + "\n" + i);
            FermatMathAttack fermatMathAttack = new FermatMathAttack(rsa.publicKey, rsa.modulus);
            BigInteger cracked = fermatMathAttack.crack(encrypted);

            if (!cracked.equals(new BigInteger(String.valueOf(i)))){
                System.out.println("Failure in cracking!");
            }
            System.out.println(i);
        }
    }
}
