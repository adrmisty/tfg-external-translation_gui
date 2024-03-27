package main.java.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Used to compute the 512-bit hash of a string.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class HashUtil {

    /**
     * @param text to convert into its hash representation
     * @return hexadecimal representation of their 512-bit hash code
     * @throws Exception in case of issues with SHA-512 computation
     */
    public static String getHash(String text) throws Exception {
	MessageDigest digest;
	try {
	    digest = MessageDigest.getInstance("SHA-512");
	    byte[] hashBytes = digest.digest(text.getBytes());
	    StringBuilder hexadecimal = new StringBuilder();

	    for (byte hb : hashBytes) {
		String hex = Integer.toHexString(0xff & hb);
		if (hex.length() == 1) {
		    hexadecimal.append('0');
		}
		hexadecimal.append(hex);
	    }

	    return hexadecimal.toString();
	} catch (NoSuchAlgorithmException e) {
	    throw new Exception(
		    "ERROR: Could not carry out SHA-512 computation.");
	}
    }
}
