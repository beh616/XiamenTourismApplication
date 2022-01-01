package com.example.xiamentourismapplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Hash {
    public Md5Hash() {
    }

    public String getMd5Hash(String password){
        String md5 = "MD5";
        try {
            MessageDigest digest = MessageDigest.getInstance(md5);
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hashedPassword = new StringBuilder();

            for(byte adigest: messageDigest){
                String h = Integer.toHexString(0xFF & adigest);
                while (h.length() < 2){
                    h = "0" + h;
                }
                hashedPassword.append(h);

            }
            return hashedPassword.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }
}
