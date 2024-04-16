package main;

import java.util.ArrayList;
import java.util.List;

import main.java.image.Captioner;

public class Main {

    public static void main(String[] args) {
	try {
	    Captioner c = new Captioner();
	    List<String> urls = new ArrayList<>();

	    urls.add("C:/Users/adria/Downloads/sergio.jpg/");
	    c.generate(urls);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
