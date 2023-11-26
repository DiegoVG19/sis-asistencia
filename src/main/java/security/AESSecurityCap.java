/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESSecurityCap {

    private PublicKey publicKey;
    private KeyAgreement keyAgreement;
    private SecretKey sharedSecretKey;

    String ALGO = "AES";

    AESSecurityCap() {
        generateDiffieHellmanParameters();
    }

    private void generateDiffieHellmanParameters() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH");
            KeyPair keyPair = kpg.generateKeyPair();
            publicKey = keyPair.getPublic();
            keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(keyPair.getPrivate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setReceiverPublicKey(PublicKey receiverPublicKey) {
        try {
            keyAgreement.doPhase(receiverPublicKey, true);
            SecretKey secretKey = keyAgreement.generateSecret("AES");
            sharedSecretKey = new SecretKeySpec(secretKey.getEncoded(), ALGO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String msg) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(msg.getBytes());
            // Aquí deberías retornar o enviar el mensaje cifrado (encVal) en lugar de la cadena original
            return new BASE64Encoder().encode(encVal);
        } catch (BadPaddingException | InvalidKeyException
                | NoSuchPaddingException | IllegalBlockSizeException
                | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String decrypt(String encryptedData) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            return new String(decValue);
        } catch (BadPaddingException | InvalidKeyException
                | NoSuchPaddingException | IllegalBlockSizeException
                | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return encryptedData;
    }

    private Key generateKey() {
        return sharedSecretKey;
    }

}