import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;

public class Crypto {
    static byte[] RetrievePass(char[] mainID, char[] id) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String[] split = (new String (Decrypt_AES(mainID, id, Decode(FromFile.File_To_Map(mainID).get(new String(id)).toCharArray())))).split(" ");
        return split[1].getBytes();
    }

    static char[] Encode(byte[] hashed){
        return DatatypeConverter.printBase64Binary(hashed).toCharArray();
    }

    static byte[] Decode(char[] encoded){
        return DatatypeConverter.parseBase64Binary(new String(encoded));
    }

    static byte[] Hash(char[] c, String type) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return MessageDigest.getInstance(type).digest(new String(c).getBytes());
    }

    static byte[] Hash(char[] c1, char[] c2, String type) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return MessageDigest.getInstance(type).digest(new String(Concatenate(c1, c2)).getBytes());
    }

    static char[] Hash_Encode(String str, String type) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return Encode(Hash(Function.GetPass(str), type));
    }

    static char[] Concatenate(char[] c1, char[] c2){
        int idx = 0;
        char[] concatenated = new char[c1.length + c2.length + 1];
        for (int i = 0; i < c1.length; i++){
            concatenated[idx++] = c1[i];
        }
        concatenated[idx++] = ' ';
        for (int i = 0; i < c2.length; i++){
            concatenated[idx++] = c2[i];
        }

        return concatenated;
    }

    static char[] Get_Random_Pass(){
        SecureRandom r = new SecureRandom();
        int length = r.nextInt(6) + 15;
        char[] randomPass = new char[length];

        for (int i = 0; i < length; i++) {
            randomPass[i] = (char) (r.nextInt(94) + 33);
        }
        System.out.println("Your new password is: " + new String(randomPass) + "\n");
        return randomPass;
    }

    static byte[] Encrypt_AES(char[] mainID, char[] id, char[] username, char[] pass) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE
                , new SecretKeySpec(Hash(mainID, FromFile.Get_Master_Pass(mainID), "SHA-256"), "AES")
                , new IvParameterSpec(Hash(mainID, id, "MD5")));
        return cipher.doFinal(new String(Concatenate(username, pass)).getBytes());
    }

    static byte[] Decrypt_AES(char[] mainID, char[] id, byte[] cipherText) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE
                , new SecretKeySpec(Hash(mainID, FromFile.Get_Master_Pass(mainID), "SHA-256"), "AES")
                , new IvParameterSpec(Hash(mainID, id, "MD5")));
        return cipher.doFinal(cipherText);
    }

    static byte[] Encrypt_RSA(char[] mainID, char[] id, char[] cert) throws IOException, CertificateException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        java.security.cert.Certificate inputCert = null;
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        Collection c = fact.generateCertificates(new FileInputStream ("Alice.crt"));
        java.security.cert.Certificate aliceCert = (java.security.cert.Certificate) c.iterator().next();
        try{
            c = fact.generateCertificates(new FileInputStream (new String(cert) + ".crt"));
            inputCert = (java.security.cert.Certificate) c.iterator().next();
            inputCert.verify(aliceCert.getPublicKey());
        }
        catch (Exception e){
            fact = null;
            c = null;
            inputCert = null;
            System.out.println("Invalid certificate.\n");
        }
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.WRAP_MODE, inputCert);
        return cipher.wrap(new SecretKeySpec(RetrievePass(mainID, id), "RSA"));
    }
}
