package main.java.gui.cards;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.logic.util.exception.ResourceException;
import main.java.logic.util.properties.ResourceLoader;

public class CardImage extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_Image;
    private JPanel centerPanel_Image;
    private JPanel downPanel_Image;
    private JPanel backPanel_Image;

    /*
     * Files
     */
    private JButton btnBrowse;
    boolean savedDroppedFile = false;
    private JFileChooser imageChooser;
    private String[] EXTENSIONS = { "jpg", "jpeg", "gif", "png", "bmp" };
    private JTextField txtFilePath;
    private JLabel lblImageCaptioning;
    private JLabel lblBack_Image;
    private JButton btnBack_Image;
    private JButton btnNext_Image;
    private JButton btnHelp_Image;
    private JLabel lblImageOption;
    private JButton btnNoImage;
    private JLabel lblImageOption_1;

    public CardImage(MainWindow root) throws ResourceException {
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel_Image());
	this.add(getCenterPanel_Image());
	this.add(getDownPanel_Image());
    }

    public void reset() {
	savedDroppedFile = false;
	txtFilePath.setText("");
    }

    private JPanel getNorthPanel_Image() throws ResourceException {
	if (northPanel_Image == null) {
	    northPanel_Image = new JPanel();
	    northPanel_Image.setBounds(0, 0, 586, 111);
	    northPanel_Image.setBackground(SystemColor.window);
	    northPanel_Image.setLayout(null);
	    northPanel_Image.add(getLblImageCaptioning());
	}
	return northPanel_Image;
    }

    private JLabel getLblImageCaptioning() throws ResourceException {
	if (lblImageCaptioning == null) {
	    lblImageCaptioning = new JLabel(
		    root.getMessages().getString("label.image.title"));
	    lblImageCaptioning.setBounds(0, 0, 586, 111);
	    lblImageCaptioning.setHorizontalAlignment(SwingConstants.CENTER);
	    lblImageCaptioning
		    .setFont(ResourceLoader.getFont().deriveFont(40f));
	}
	return lblImageCaptioning;
    }

    private JPanel getCenterPanel_Image() throws ResourceException {
	if (centerPanel_Image == null) {
	    centerPanel_Image = new JPanel();
	    centerPanel_Image.setBackground(SystemColor.window);
	    centerPanel_Image.setBounds(0, 108, 586, 255);
	    centerPanel_Image.setLayout(null);
	    centerPanel_Image.add(getTxtFilePath());
	    centerPanel_Image.add(getBtnBrowse());
	    centerPanel_Image.add(getLblImageOption());
	    centerPanel_Image.add(getBtnNoImage());
	    centerPanel_Image.add(getLblImageOption_1());
	}
	return centerPanel_Image;
    }

    private JPanel getDownPanel_Image() throws ResourceException {
	if (downPanel_Image == null) {
	    downPanel_Image = new JPanel();
	    downPanel_Image.setBackground(SystemColor.window);
	    downPanel_Image.setBounds(0, 362, 586, 56);
	    downPanel_Image.setLayout(null);
	    downPanel_Image.add(getBackPanel_Image());
	}
	return downPanel_Image;
    }

    private JPanel getBackPanel_Image() throws ResourceException {
	if (backPanel_Image == null) {
	    backPanel_Image = new JPanel();
	    backPanel_Image.setBounds(0, 0, 586, 51);
	    backPanel_Image.setLayout(null);
	    backPanel_Image.setBackground(SystemColor.window);
	    backPanel_Image.add(getLblBack_Image());
	    backPanel_Image.add(getBtnBack_Image());
	    backPanel_Image.add(getBtnNext_Image());
	    backPanel_Image.add(getBtnHelp_Image());
	}
	return backPanel_Image;
    }

    private JLabel getLblBack_Image() throws ResourceException {
	if (lblBack_Image == null) {
	    lblBack_Image = new JLabel("<");
	    lblBack_Image.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    reset();
		    btnBack_Image.doClick();
		}
	    });
	    lblBack_Image.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblBack_Image.setBounds(10, 11, 31, 37);
	}
	return lblBack_Image;
    }

    private JButton getBtnBack_Image() {
	if (btnBack_Image == null) {
	    btnBack_Image = new JButton("");
	    btnBack_Image.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    try {
			root.show("mode");
		    } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		    }
		}
	    });
	    btnBack_Image.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/home-icon.png")));
	    btnBack_Image.setMnemonic('h');
	    btnBack_Image.setBorder(null);
	    btnBack_Image.setBackground(SystemColor.window);
	    btnBack_Image.setBounds(20, 11, 31, 37);
	}
	return btnBack_Image;
    }

    private JButton getBtnNext_Image() throws ResourceException {
	if (btnNext_Image == null) {
	    btnNext_Image = new JButton(
		    root.getMessages().getString("button.next"));
	    btnNext_Image.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

		    try {
			root.show("auto");
		    } catch (Exception ex) {
			root.showErrorMessage(ex,
				root.getMessages().getString("error.image"));
		    }
		}
	    });
	    btnNext_Image.setMnemonic('b');
	    btnNext_Image.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnNext_Image.setEnabled(false);
	    btnNext_Image.setBounds(421, 11, 115, 35);
	}
	return btnNext_Image;
    }

    private JButton getBtnHelp_Image() {
	if (btnHelp_Image == null) {
	    btnHelp_Image = new JButton("");
	    btnHelp_Image.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/help.png")));
	    btnHelp_Image.setToolTipText(
		    root.getMessages().getString("tooltip.image"));
	    btnHelp_Image.setMnemonic('b');
	    btnHelp_Image.setFont(btnHelp_Image.getFont().deriveFont(14f));
	    btnHelp_Image.setBorder(null);
	    btnHelp_Image.setBackground(SystemColor.window);
	    btnHelp_Image.setBounds(537, 7, 49, 41);
	    btnHelp_Image.setFocusable(false);
	}
	return btnHelp_Image;
    }

    private JTextField getTxtFilePath() throws ResourceException {
	if (txtFilePath == null) {
	    txtFilePath = new JTextField();
	    txtFilePath.setBounds(59, 100, 178, 30);
	    txtFilePath.setHorizontalAlignment(SwingConstants.CENTER);
	    txtFilePath.setFont(ResourceLoader.getFont().deriveFont(14f));
	    txtFilePath.setEditable(false);
	    txtFilePath.setColumns(10);
	}
	return txtFilePath;
    }

    private JFileChooser getImageChooser() {
	imageChooser = new JFileChooser("D:");
	imageChooser.setFileFilter(new ImageFilter(10));
	imageChooser.setMultiSelectionEnabled(true);
	int returnVal = imageChooser.showOpenDialog(this);
	File[] images;

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File[] selectedImages = imageChooser.getSelectedFiles();

	    if (selectedImages.length == 0) {
		images = null;
	    } else {
		if (selectedImages.length > 10) {
		    images = new File[10];
		    for (int i = 0; i < 10; i++) {
			images[i] = selectedImages[i];
		    }

		    JOptionPane.showMessageDialog(imageChooser,
			    root.getMessages().getString("error.imagenumber"));
		} else {
		    images = selectedImages;
		}
		root.setImages(images);
		txtFilePath.setText(images[0].getPath() + "...");
		unlockButtons(false);
	    }

	} else {
	    unlockButtons(true);
	}

	return imageChooser;
    }

    private JButton getBtnBrowse() throws ResourceException {
	if (btnBrowse == null) {
	    btnBrowse = new JButton(
		    root.getMessages().getString("button.browse"));
	    btnBrowse.setBounds(130, 141, 107, 23);
	    btnBrowse.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    unlockButtons(false);
		    getImageChooser();
		}
	    });
	    btnBrowse.setMnemonic('b');
	    btnBrowse.setFont(ResourceLoader.getFont().deriveFont(14f));
	}
	return btnBrowse;
    }

    /**
     * IMAGE FILTER
     */

    private class ImageFilter extends FileFilter {

	private FileNameExtensionFilter filter;
	private int limit;
	private int count;

	public ImageFilter(int limit) {
	    this.filter = new FileNameExtensionFilter(
		    root.getMessages().getString("label.image.filter"),
		    EXTENSIONS);
	    this.limit = limit;
	}

	@Override
	public boolean accept(File f) {
	    if (f.isDirectory()) {
		return true;
	    }
	    if (!filter.accept(f)) {
		return false;
	    }
	    count++;
	    return count < limit;
	}

	@Override
	public String getDescription() {
	    return filter.getDescription();
	}

    }

    private JLabel getLblImageOption() throws ResourceException {
	if (lblImageOption == null) {
	    lblImageOption = new JLabel(
		    root.getMessages().getString("label.image.option"));
	    lblImageOption.setLabelFor(getBtnBrowse());
	    lblImageOption.setHorizontalAlignment(SwingConstants.CENTER);
	    lblImageOption.setForeground(Color.decode("#0089d6"));
	    lblImageOption.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblImageOption.setBounds(10, 47, 283, 42);
	}
	return lblImageOption;
    }

    private JButton getBtnNoImage() throws ResourceException {
	if (btnNoImage == null) {
	    btnNoImage = new JButton(
		    root.getMessages().getString("label.image.button"));
	    btnNoImage.setMnemonic('n');
	    btnNoImage.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    unlockButtons(true);
		}
	    });
	    btnNoImage.setFont(ResourceLoader.getFont().deriveFont(20f));
	    btnNoImage.setBounds(336, 100, 210, 52);
	}
	return btnNoImage;
    }

    private void unlockButtons(boolean noImage) {
	btnNext_Image.setEnabled(noImage);

	// Disable this button
	btnNoImage.setFocusable(!noImage);
	btnNoImage.setEnabled(!noImage);
	btnNoImage.setSelected(noImage);

	// Enable this other one
	txtFilePath.setEnabled(noImage);
	btnBrowse.setSelected(!noImage);

	// Next
	btnNext_Image.setEnabled(true);

    }

    private JLabel getLblImageOption_1() throws ResourceException {
	if (lblImageOption_1 == null) {
	    lblImageOption_1 = new JLabel(
		    root.getMessages().getString("label.image.option2"));
	    lblImageOption_1.setLabelFor(getBtnNoImage());
	    lblImageOption_1.setHorizontalAlignment(SwingConstants.CENTER);
	    lblImageOption_1.setForeground(Color.decode("#0089d6"));
	    lblImageOption_1.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblImageOption_1.setBounds(303, 47, 283, 42);
	}
	return lblImageOption_1;
    }
}
