package main.java.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    public static List<String> getSupportedLanguages() {
	try {
	    URL res = Utils.class.getResource("/main/resources/languages.txt");
	    File f = new File(res.toURI());
	    List<String> list = Files.readAllLines(f.toPath());
	    return list;
	} catch (URISyntaxException | IOException ex) {
	    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	    return null;
	}
    }

    public static Font getFont() {
	try {
	    InputStream is = Utils.class
		    .getResourceAsStream("/main/resources/sf-pro.otf");
	    Font font = Font.createFont(Font.TRUETYPE_FONT, is);
	    return font;
	} catch (FontFormatException | IOException ex) {
	    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	    return null;
	}
    }

}
