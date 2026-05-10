package com.FoodServe.Dilevery.UtilityClass;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA256 {

    public static String calculateHMAC(
            String data,
            String secret) throws Exception {

        Mac sha256Hmac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey =
                new SecretKeySpec(
                        secret.getBytes(),
                        "HmacSHA256"
                );

        sha256Hmac.init(secretKey);

        byte[] hash =
                sha256Hmac.doFinal(data.getBytes());

        StringBuilder sb = new StringBuilder();

        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}