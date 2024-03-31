package main.java.util.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Used to compute the 512-bit hash of a string.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class HashUtil {

    private final static String ALGORITHM = "SHA-512"; // others, like "SHA-256"

    /**
     * @param text to convert into its hash representation
     * @return hexadecimal representation of their hash code
     * @throws NoSuchAlgorithmException for when hashing algorithm is not found
     */
    public static String getHash(String text) throws NoSuchAlgorithmException {
	MessageDigest digest;
	digest = MessageDigest.getInstance(ALGORITHM);
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

    }
}
