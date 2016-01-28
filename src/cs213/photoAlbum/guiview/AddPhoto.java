package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.filechooser.FileFilter;

import cs213.photoAlbum.control.AlbumNotFoundException;

/**
 * <b>AddPhoto<b> <i>Class<i> Is used to prompt the user to add a photo. It extends JDialog.
 * This dialog has a JFileChooser that will let the user to choose an image to add. It also has an optional
 * field to enter a caption for the photo. If no caption is entered the old caption will be retained or if the
 * photo was added for the first time it will be Untitled. The data is sent to OpenAlbum instance.
 * @author Nadiia Chepurko
 * @see JDialog
 * @see OpenAlbum
 */
public class AddPhoto extends JDialog{
	private JTextField fileF;
	private JTextField captionF;
	private LengthRestrictedDocument captionRestrict=new LengthRestrictedDocument(50);
	
	private JButton addB;
	private JButton cancelB;
	private JLabel errorL;
	private JButton selectFileB;
	
	private final JFileChooser fc=new JFileChooser();
	private ButtonListener bl;
	private CloseListener cl;
	private EnterListener el;
	
	private OpenAlbum owner;
	/**
	 * Constructor for AddPhoto dialog.
	 * @param owner instance of OpenAlbum that recives the data.
	 */
	public AddPhoto(OpenAlbum owner)
	{
		super(owner,"Add Photo",true);
		this.owner=owner;
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblCaption = new JLabel("Enter Caption:");
		getContentPane().add(lblCaption);
		
		fileF = new JTextField();
		springLayout.putConstraint(SpringLayout.EAST, fileF, -10, SpringLayout.EAST, getContentPane());
		fileF.setEditable(false);
		getContentPane().add(fileF);
		fileF.setColumns(10);
		
		captionF = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, captionF, -2, SpringLayout.NORTH, lblCaption);
		springLayout.putConstraint(SpringLayout.WEST, captionF, 0, SpringLayout.WEST, fileF);
		springLayout.putConstraint(SpringLayout.EAST, captionF, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(captionF);
		captionF.setColumns(10);
		captionF.setDocument(captionRestrict);
		
		addB = new JButton("Add");
		springLayout.putConstraint(SpringLayout.WEST, addB, 0, SpringLayout.WEST, lblCaption);
		springLayout.putConstraint(SpringLayout.EAST, addB, 0, SpringLayout.EAST, lblCaption);
		getContentPane().add(addB);
		
		cancelB = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.NORTH, cancelB, 0, SpringLayout.NORTH, addB);
		springLayout.putConstraint(SpringLayout.WEST, cancelB, 378, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, cancelB, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(cancelB);
		
		errorL = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, addB, 18, SpringLayout.SOUTH, errorL);
		springLayout.putConstraint(SpringLayout.NORTH, errorL, 23, SpringLayout.SOUTH, captionF);
		springLayout.putConstraint(SpringLayout.WEST, errorL, 0, SpringLayout.WEST, lblCaption);
		springLayout.putConstraint(SpringLayout.EAST, errorL, 5, SpringLayout.EAST, fileF);
		errorL.setPreferredSize(new Dimension(300,25));
		getContentPane().add(errorL);
		
		selectFileB = new JButton("Select File");
		springLayout.putConstraint(SpringLayout.NORTH, lblCaption, 30, SpringLayout.SOUTH, selectFileB);
		springLayout.putConstraint(SpringLayout.EAST, lblCaption, 0, SpringLayout.EAST, selectFileB);
		springLayout.putConstraint(SpringLayout.WEST, fileF, 21, SpringLayout.EAST, selectFileB);
		springLayout.putConstraint(SpringLayout.WEST, selectFileB, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, fileF, 3, SpringLayout.NORTH, selectFileB);
		springLayout.putConstraint(SpringLayout.NORTH, selectFileB, 20, SpringLayout.NORTH, getContentPane());
		getContentPane().add(selectFileB);
		
		bl=new ButtonListener();
		addB.addActionListener(bl);
		cancelB.addActionListener(bl);
		selectFileB.addActionListener(bl);
		
		cl=new CloseListener();
		this.addWindowListener(cl);
		
		this.setFocusable(true);
		
		el=new EnterListener();
		this.addKeyListener(el);
		captionF.addKeyListener(el);
		
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new ImageFilter());
		
		
		this.setPreferredSize(new Dimension(500,230));
		this.setMinimumSize(new Dimension(500,230));
		this.pack();
		this.setLocationRelativeTo(null);
	}
	/**
	 * This method displays an error message using a JLabel in
	 * red color using html.
	 * @param message an error message to display.
	 */
	private void displayError(String message)
	{
		errorL.setText("<html><font color='red'>"+message+"</font></html>");
		
	}
	protected class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==cancelB)
			{
				fileF.setText("");
				captionF.setText("");
				errorL.setText("");
				AddPhoto.this.dispose();
			}
			else if(e.getSource()==addB)
			{
				String file=new String();
				String caption=new String();
				file=fileF.getText();
				caption=captionF.getText();
				file=file.trim();
				caption=caption.trim();
				if(file.isEmpty())
				{
					displayError("Choose File!");
					return;
				}
				if(caption.isEmpty())
					caption="Untitled";
				
				boolean added=false;
				try {
					added = owner.addPhoto(file, caption);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					displayError(e1.getMessage());
					return;
				} catch (AlbumNotFoundException e1) {
					// TODO Auto-generated catch block
					displayError(e1.getMessage());
					return;
				}catch (Exception e3)
				{
					displayError(e3.getMessage());
					return;
				}
				if(added)
				{
					fileF.setText("");
					captionF.setText("");
					errorL.setText("");
					AddPhoto.this.dispose();
				}
				else
				{
					displayError("Photo already exists!");
				}
				
			}
			else if(e.getSource()==selectFileB)
			{
				int stat=fc.showOpenDialog(AddPhoto.this);
				if(stat==JFileChooser.APPROVE_OPTION)
				{
					File temp=fc.getSelectedFile();
					//fileF.setText(temp.getCanonicalPath());
					fileF.setText(temp.getAbsolutePath());
				}
			}
		}
	}
	protected class EnterListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

				//login.doClick();
				
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode()==KeyEvent.VK_ENTER)
			{
				//error.setText("key: "+KeyEvent.getKeyText(e.getKeyCode()));
				addB.doClick();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}	
	protected class CloseListener implements WindowListener{

		public void windowActivated(WindowEvent arg0) {		
		}

		public void windowClosed(WindowEvent arg0) {
		}

		public void windowClosing(WindowEvent arg0) {
			fileF.setText("");
			captionF.setText("");
			errorL.setText("");
			AddPhoto.this.dispose();
		}
		public void windowDeactivated(WindowEvent arg0) {}

		public void windowDeiconified(WindowEvent arg0) {}
		
		public void windowIconified(WindowEvent arg0) {}

		public void windowOpened(WindowEvent arg0) {}
		
	}
	protected class ImageFilter extends FileFilter {

	    //Accept all directories and all gif, jpg, tiff, or png files.
	    public boolean accept(File f) {
	        if (f.isDirectory()) {
	            return true;
	        }

	        String extension = this.getExtension(f);
	        if (extension != null) {
	            if (extension.equals("tiff") ||
	                extension.equals("tif") ||
	                extension.equals("gif") ||
	                extension.equals("jpeg") ||
	                extension.equals("jpg") ||
	                extension.equals("png")) {
	                    return true;
	            } else {
	                return false;
	            }
	        }

	        return false;
	    }

	    //The description of this filter
	    public String getDescription() {
	        return "Just Images";
	    }
	    public String getExtension(File f) {
	        String ext = null;
	        String s = f.getName();
	        int i = s.lastIndexOf('.');
	 
	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        return ext;
	    }
	}
}
