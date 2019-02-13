package attacks;

import java.math.BigInteger;

import core.RSA;
import core.RSA.RSAException;


public class ChosenCipherTextAttack {
    private final static BigInteger CHOSEN_PLAIN_TEXT = new BigInteger("2");

    public BigInteger crack(BigInteger encryptedMessage, RSA rsa) throws RSAException {
        return rsa.decrypt(rsa.encrypt(CHOSEN_PLAIN_TEXT)
                  .multiply(encryptedMessage))
                  .divide(CHOSEN_PLAIN_TEXT);
    }

    public static void main(String[] args) throws RSAException {
        BigInteger message = new BigInteger(args[0]);
        int nBits = Integer.parseInt(args[1]);

        RSA rsa = new RSA(nBits);
        BigInteger encrypted = rsa.encrypt(message);
        System.out.println(rsa.toString());

        ChosenCipherTextAttack chosenCipherTextAttack = new ChosenCipherTextAttack();
        BigInteger cracked = chosenCipherTextAttack.crack(encrypted, rsa);

        System.out.println("message = " + message);
        System.out.println("cracked = " + cracked);
        System.out.println(cracked.equals(message) ?  "Success!" : "Failure!");
    }
}
