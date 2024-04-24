package main.java.gui.cards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.util.exception.ResourceException;
import main.java.util.properties.ResourceLoader;

public class CardFile extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_File;
    private JPanel centerPanel_File;
    private JPanel downPanel_File;
    private JPanel backPanel_File;

    /*
     * Files
     */
    private JButton btnBrowse;
    boolean savedDroppedFile = false;
    private JFileChooser fileChooser;

    /*
     * Labels & text
     */
    private JLabel lblDrag;
    private JLabel lblDragText;
    private JTextField txtFilePath;
    private JLabel lblStartTranslating;
    private JLabel lblBack_File;
    private JButton btnBack_File;
    private JButton btnNext_File;
    private JButton btnHelp_File;
    private JLabel lblDndWarning;

    public CardFile(MainWindow root) throws ResourceException {
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel_File());
	this.add(getCenterPanel_File());
	this.add(getDownPanel_File());
    }

    public void reset() {
	root.reset();
	txtFilePath.setText("");
	lblDndWarning.setVisible(false);
	btnNext_File.setEnabled(false);
    }

    private JPanel getNorthPanel_File() throws ResourceException {
	if (northPanel_File == null) {
	    northPanel_File = new JPanel();
	    northPanel_File.setBounds(0, 0, 586, 81);
	    northPanel_File.setBackground(SystemColor.window);
	    northPanel_File.setLayout(null);
	    northPanel_File.add(getLblStartTranslating());
	}
	return northPanel_File;
    }

    private JLabel getLblStartTranslating() throws ResourceException {
	if (lblStartTranslating == null) {
	    lblStartTranslating = new JLabel(
		    root.getMessages().getString("label.file.title"));
	    lblStartTranslating.setBounds(0, 0, 586, 110);
	    lblStartTranslating.setHorizontalAlignment(SwingConstants.CENTER);
	    lblStartTranslating
		    .setFont(ResourceLoader.getFont().deriveFont(40f));
	}
	return lblStartTranslating;
    }

    private JLabel getLblDragText() throws ResourceException {
	if (lblDragText == null) {
	    lblDragText = new JLabel(
		    root.getMessages().getString("label.file.dnd"));
	    lblDragText.setForeground(Color.decode("#0089d6"));
	    lblDragText.setBounds(151, 100, 281, 19);
	    lblDragText.setLabelFor(getLblDrag());
	    lblDragText.setHorizontalAlignment(SwingConstants.CENTER);
	    lblDragText.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblDragText;
    }

    private JPanel getCenterPanel_File() throws ResourceException {
	if (centerPanel_File == null) {
	    centerPanel_File = new JPanel();
	    centerPanel_File.setBackground(SystemColor.window);
	    centerPanel_File.setBounds(0, 81, 586, 281);
	    centerPanel_File.setLayout(null);
	    centerPanel_File.add(getLblDragText());
	    centerPanel_File.add(getLblDrag());
	    centerPanel_File.add(getTxtFilePath());
	    centerPanel_File.add(getBtnBrowse());
	    centerPanel_File.add(getLblDndWarning());
	}
	return centerPanel_File;
    }

    private JLabel getLblDrag() {
	if (lblDrag == null) {
	    lblDrag = new JLabel("");
	    lblDrag.setBounds(142, 23, 305, 141);
	    lblDrag.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/dnd.png")));
	    lblDrag.setToolTipText(
		    root.getMessages().getString("tooltip.file"));

	    // Drag and drop functionality
	    new DropTarget(lblDrag, new DnDListener());
	}
	return lblDrag;
    }

    private JPanel getDownPanel_File() throws ResourceException {
	if (downPanel_File == null) {
	    downPanel_File = new JPanel();
	    downPanel_File.setBackground(SystemColor.window);
	    downPanel_File.setBounds(0, 362, 586, 56);
	    downPanel_File.setLayout(new BorderLayout(0, 0));
	    downPanel_File.add(getBackPanel_File());
	}
	return downPanel_File;
    }

    private JPanel getBackPanel_File() throws ResourceException {
	if (backPanel_File == null) {
	    backPanel_File = new JPanel();
	    backPanel_File.setLayout(null);
	    backPanel_File.setBackground(SystemColor.window);
	    backPanel_File.add(getLblBack_File());
	    backPanel_File.add(getBtnBack_File());
	    backPanel_File.add(getBtnNext_File());
	    backPanel_File.add(getBtnHelp_File());
	}
	return backPanel_File;
    }

    private JLabel getLblBack_File() throws ResourceException {
	if (lblBack_File == null) {
	    lblBack_File = new JLabel("<");
	    lblBack_File.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    btnBack_File.doClick();
		}
	    });
	    lblBack_File.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblBack_File.setBounds(10, 11, 31, 37);
	}
	return lblBack_File;
    }

    private JButton getBtnBack_File() {
	if (btnBack_File == null) {
	    btnBack_File = new JButton("");
	    btnBack_File.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    reset();
		    root.show("main");
		}
	    });
	    btnBack_File.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/home-icon.png")));
	    btnBack_File.setMnemonic('b');
	    btnBack_File.setBorder(null);
	    btnBack_File.setBackground(SystemColor.window);
	    btnBack_File.setBounds(20, 11, 31, 37);
	}
	return btnBack_File;
    }

    private JButton getBtnNext_File() throws ResourceException {
	if (btnNext_File == null) {
	    btnNext_File = new JButton(
		    root.getMessages().getString("button.next"));
	    btnNext_File.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    root.inputFile();
		    root.show("mode");
		}
	    });
	    btnNext_File.setMnemonic('n');
	    btnNext_File.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnNext_File.setEnabled(false);
	    btnNext_File.setBounds(421, 11, 115, 35);
	}
	return btnNext_File;
    }

    private JButton getBtnHelp_File() {
	if (btnHelp_File == null) {
	    btnHelp_File = new JButton("");
	    btnHelp_File.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/help.png")));
	    btnHelp_File.setToolTipText(
		    root.getMessages().getString("tooltip.file"));
	    btnHelp_File.setMnemonic('h');
	    btnHelp_File.setFont(btnHelp_File.getFont().deriveFont(14f));
	    btnHelp_File.setBorder(null);
	    btnHelp_File.setBackground(SystemColor.window);
	    btnHelp_File.setBounds(537, 7, 49, 41);
	    btnHelp_File.setFocusable(false);
	}
	return btnHelp_File;
    }

    private class DnDListener implements DropTargetListener {

	@Override
	public void drop(DropTargetDropEvent event) {
	    // Get dropped item data
	    event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
	    savedDroppedFile = false;
	    Transferable tr = event.getTransferable();
	    DataFlavor[] formats = tr.getTransferDataFlavors();
	    boolean complete = false;
	    File f = null;

	    // Only check for files
	    for (DataFlavor flavor : formats) {
		try {
		    if (flavor.isFlavorJavaFileListType()) {
			@SuppressWarnings("unchecked")
			List<File> files = (List<File>) tr
				.getTransferData(flavor);
			f = files.get(0);
			complete = true;

			if (f != null) {
			    if (ResourceLoader.getFileExtension(f.getPath())
				    .get().equals("properties")) {

				root.from(f.getAbsolutePath());
				txtFilePath.setText(f.getName());
				btnNext_File.setEnabled(true);
				savedDroppedFile = true;

			    } else {
				reset();
			    }
			}
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    event.dropComplete(complete);
		    getLblDndWarning().setVisible(!savedDroppedFile);
		}
	    }
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}
    }

    private JTextField getTxtFilePath() throws ResourceException {
	if (txtFilePath == null) {
	    txtFilePath = new JTextField();
	    txtFilePath.setBounds(122, 175, 212, 30);
	    txtFilePath.setHorizontalAlignment(SwingConstants.CENTER);
	    txtFilePath.setFont(ResourceLoader.getFont().deriveFont(14f));
	    txtFilePath.setEditable(false);
	    txtFilePath.setColumns(10);
	}
	return txtFilePath;
    }

    private JFileChooser getFileChooser() {
	fileChooser = new JFileChooser("D:");

	FileNameExtensionFilter filter = new FileNameExtensionFilter(
		root.getMessages().getString("label.filter"), "properties");
	fileChooser.setFileFilter(filter);

	int returnVal = fileChooser.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    String path = fileChooser.getSelectedFile().getPath();
	    String name = fileChooser.getSelectedFile().getName();

	    root.from(path);
	    txtFilePath.setText(name);
	    btnNext_File.setEnabled(true);
	    lblDndWarning.setVisible(false);
	} else {
	    btnNext_File.setEnabled(false);
	}

	return fileChooser;
    }

    private JButton getBtnBrowse() throws ResourceException {
	if (btnBrowse == null) {
	    btnBrowse = new JButton(
		    root.getMessages().getString("button.browse"));
	    btnBrowse.setBounds(348, 179, 107, 23);
	    btnBrowse.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    getFileChooser();
		}
	    });
	    btnBrowse.setMnemonic('s');
	    btnBrowse.setFont(ResourceLoader.getFont().deriveFont(14f));
	}
	return btnBrowse;
    }

    private JLabel getLblDndWarning() throws ResourceException {
	if (lblDndWarning == null) {
	    lblDndWarning = new JLabel(
		    root.getMessages().getString("label.file.wrong"));
	    lblDndWarning.setForeground(new Color(220, 20, 60));
	    lblDndWarning.setBounds(122, 215, 333, 14);
	    lblDndWarning.setVisible(false);
	    lblDndWarning.setFont(ResourceLoader.getFont().deriveFont(14f));
	}
	return lblDndWarning;
    }
}
