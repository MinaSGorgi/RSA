package rsa;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args){
        BigInteger message = new BigInteger(args[0]);
        int nBits = Integer.parseInt(args[1]);
        RSA rsa = new RSA(nBits);
        BigInteger encrypted = rsa.encrypt(message);
        BigInteger decrypted = rsa.decrypt(encrypted);

        System.out.println(rsa.toString());
        System.out.println("encrypted = " + encrypted);
        System.out.println("decrypted = " + decrypted + "\n");

        ChosenCipherTextAttack chosenCipherTextAttack = new ChosenCipherTextAttack();
        BigInteger brokenMessageUsingChosenCipherTextAttack = chosenCipherTextAttack.decrypt(encrypted, rsa);
        System.out.println("encrypted = " + encrypted);
        System.out.println("Chosen Cipher Attack decryption = " + brokenMessageUsingChosenCipherTextAttack + "\n");


    }

}
