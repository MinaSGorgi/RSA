package rsa;

import java.math.BigInteger;

public class ChosenCipherTextAttack {

    private final static BigInteger chosenPlainText = new BigInteger("2");

    BigInteger decrypt(BigInteger encryptedMessage, RSA rsa){
        return rsa.decrypt(rsa.encrypt(chosenPlainText).multiply(encryptedMessage)).divide(chosenPlainText);
    }
}
