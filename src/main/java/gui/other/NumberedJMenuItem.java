package main.java.gui.other;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Map;

import javax.swing.JMenuItem;

import main.java.gui.MainWindow;

public class NumberedJMenuItem extends JMenuItem {
    private static final long serialVersionUID = 1L;
    private int id;

    public NumberedJMenuItem(MainWindow root, Map<Integer, String> languages,
	    String text, int identifier) {
	super(text);
	this.id = identifier;

	this.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    int id = ((NumberedJMenuItem) e.getSource()).getId();
		    root.localize(new Locale(languages.get(id)));
		} catch (Exception e1) {
		    e1.printStackTrace();
		}
	    }
	});

    }

    public int getId() {
	return id;
    }
}
