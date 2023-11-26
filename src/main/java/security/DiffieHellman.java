/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

/**
 *
 * @author Piero
 */
public class DiffieHellman {

    private KeyPair keyPair;
    private KeyAgreement keyAgreement;
    private SecretKey sharedSecretKey;

    public DiffieHellman() throws Exception {
        // Generar par de claves y configurar acuerdo de claves
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(2048);  // Ajusta la longitud según sea necesario
        keyPair = keyPairGenerator.generateKeyPair();

        keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(keyPair.getPrivate());
    }

    public String getPublicKey() {
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyBytes);
    }

    public void setReceiverPublicKey(String publicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey receiverPublicKey = keyFactory.generatePublic(keySpec);

        keyAgreement.doPhase(receiverPublicKey, true);
        sharedSecretKey = keyAgreement.generateSecret("AES");
    }

    public String getSharedSecretKey() {
        // Devuelve la clave secreta compartida en formato String
        return Base64.getEncoder().encodeToString(sharedSecretKey.getEncoded());
    }

    public String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sharedSecretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sharedSecretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
