package main.java.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

	public static Font getFont() {
	    try {
	        InputStream is = Utils.class.getResourceAsStream("/main/resources/sf-pro.otf");
	        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
	        return font;
	    } catch (FontFormatException | IOException ex) {
	        Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	        return null;	    }
	}
	

}
