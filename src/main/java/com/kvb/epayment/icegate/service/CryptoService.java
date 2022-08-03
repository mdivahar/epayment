package com.kvb.epayment.icegate.service;

import com.kvb.epayment.icegate.model.ChallanData;
import com.kvb.epayment.icegate.model.EPayment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;


public interface CryptoService {

	public String generateInputPayload(String inputData);

	public ChallanData decrypt(EPayment request);
	
	public EPayment encrypt(ChallanData request);
	
	public static String getSign(String inputStr, String certificatePath, String password) {
        String signedData = "";
        try {
            PrivateKey pvtkey = null;
            Enumeration<String> aliasList;
            String alias;
            File securityFileKeyPair = new File(certificatePath);
            InputStream cerFileStream = new FileInputStream(securityFileKeyPair);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(cerFileStream, password.toCharArray());
            aliasList = keyStore.aliases();
            while (aliasList.hasMoreElements()) {
                alias = aliasList.nextElement();
                KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(password.toCharArray());
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, entryPassword);
                pvtkey = privateKeyEntry.getPrivateKey();
                Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
                PrivateKey privKey = pvtkey;
                Signature sign = Signature.getInstance("SHA256withRSA");
                sign.initSign(privKey);
                byte[] bytes = inputStr.getBytes(StandardCharsets.UTF_8);
                sign.update(bytes);
                byte[] signature = sign.sign();
                signedData = Base64.getEncoder().encodeToString(signature);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signedData;
    }

	

}
