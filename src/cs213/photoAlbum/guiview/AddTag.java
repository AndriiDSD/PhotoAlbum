package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;

import javax.swing.JDialog;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import cs213.photoAlbum.control.PhotoNotFoundException;
import cs213.photoAlbum.util.Utils;

/**
 * <b>AddTag<b> <i>Class<i> Is used to prompt the user to create a tag object.
 * The data will be sent to a DisplayPhoto window. To add a tag both tag type and tag value have to be entered.
 * @author Andrii Hlyvko
 * @see JDailog
 * @see DisplayPhoto
 */
public class AddTag extends JDialog{
	private JTextField tagTypeTf;
	private JTextField tagValueTf;
	private LengthRestrictedDocument typeRestrict=new LengthRestrictedDocument(50);
	private LengthRestrictedDocument valueRestrict=new LengthRestrictedDocument(50);
	
	private JButton addTagB,cancelB;
	private DisplayPhoto ownr;
	
	private ButtonListener bl;
	private CloseListener cl;
	private EnterListener el;
	private JLabel errorL;
	/**
	 * Constructor for AddTag dialog.
	 * @param owner a DisplayPhoto instance that will recieve the data.
	 */
	public AddTag(DisplayPhoto owner)
	{
		super(owner,"Add Tag",true);
		this.ownr=owner;
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblTagType = new JLabel("<html><font color='black'>"+"Tag Type"+"<font color='red'>"+"*"+"<font color='black'>"+"</font></html>");
		getContentPane().add(lblTagType);
		
		JLabel lblTagValue = new JLabel("<html><font color='black'>"+"Tag Value"+"<font color='red'>"+"*"+"<font color='black'>"+"</font></html>");
		springLayout.putConstraint(SpringLayout.NORTH, lblTagValue, 87, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblTagType, -34, SpringLayout.NORTH, lblTagValue);
		springLayout.putConstraint(SpringLayout.WEST, lblTagType, 0, SpringLayout.WEST, lblTagValue);
		springLayout.putConstraint(SpringLayout.WEST, lblTagValue, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblTagValue);
		
		tagTypeTf = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, tagTypeTf, 27, SpringLayout.EAST, lblTagType);
		getContentPane().add(tagTypeTf);
		tagTypeTf.setColumns(10);
		tagTypeTf.setDocument(typeRestrict);
		
		tagValueTf = new JTextField();
		springLayout.putConstraint(SpringLayout.SOUTH, tagTypeTf, -30, SpringLayout.NORTH, tagValueTf);
		springLayout.putConstraint(SpringLayout.EAST, tagTypeTf, 0, SpringLayout.EAST, tagValueTf);
		springLayout.putConstraint(SpringLayout.NORTH, tagValueTf, 85, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, tagValueTf, 21, SpringLayout.EAST, lblTagValue);
		getContentPane().add(tagValueTf);
		tagValueTf.setColumns(10);
		tagValueTf.setDocument(valueRestrict);
		
		addTagB = new JButton("Add Tag");
		springLayout.putConstraint(SpringLayout.WEST, addTagB, 0, SpringLayout.WEST, lblTagType);
		springLayout.putConstraint(SpringLayout.SOUTH, addTagB, -37, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, addTagB, -211, SpringLayout.EAST, getContentPane());
		getContentPane().add(addTagB);
		
		cancelB = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.EAST, tagValueTf, 0, SpringLayout.EAST, cancelB);
		springLayout.putConstraint(SpringLayout.NORTH, cancelB, 0, SpringLayout.NORTH, addTagB);
		springLayout.putConstraint(SpringLayout.WEST, cancelB, 211, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, cancelB, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(cancelB);
		
		errorL = new JLabel("");
		springLayout.putConstraint(SpringLayout.WEST, errorL, 0, SpringLayout.WEST, lblTagType);
		springLayout.putConstraint(SpringLayout.SOUTH, errorL, -10, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(errorL);
		
		cl=new CloseListener();
		bl=new ButtonListener();
		el=new EnterListener();
		
		this.addWindowListener(cl);
		addTagB.addActionListener(bl);
		cancelB.addActionListener(bl);
		tagTypeTf.addKeyListener(el);
		tagValueTf.addKeyListener(el);
		
		
		this.setPreferredSize(new Dimension(335,225));
		this.setMinimumSize(new Dimension(335,225));
		this.setResizable(false);
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
			if (e.getSource() == cancelB) {
				tagTypeTf.setText("");
				tagValueTf.setText("");
				displayError("");
				AddTag.this.dispose();
			} else if (e.getSource() == addTagB) {
				String tagType = tagTypeTf.getText();
				String tagValue = tagValueTf.getText();
				tagType = tagType.trim();
				tagValue = tagValue.trim();

				if (validateTag(tagType, tagValue)) {

					boolean added = false;
					try {
						added = ownr.addTag(tagType, tagValue);
					} catch (IllegalArgumentException e1) {
						displayError(e1.getMessage());
						return;
					} catch (FileNotFoundException e1) {
						displayError(e1.getMessage());
						return;
					} catch (PhotoNotFoundException e1) {
						displayError(e1.getMessage());
						return;
					} catch (Exception e1) {
						displayError(e1.getMessage());
						return;
					}
					if (added) {
						tagTypeTf.setText("");
						tagValueTf.setText("");
						displayError("");


						AddTag.this.dispose();
					} else {
						displayError("Tag already exists!");
					}
				}

			}
		}
		
	}

	private boolean validateTag(String tagType, String tagValue) {
		if (Utils.isBlank(tagType) && Utils.isBlank(tagValue)) {
            displayError("Enter tag type and value first!");
			return false;
        }
		if (Utils.isEmpty(tagValue)) {
            displayError("Enter tag value!");
			return false;
        }
		if (Utils.isEmpty(tagType)) {
            displayError("Enter tag type!");
			return false;
        }
		return true;
	}

	protected class CloseListener implements WindowListener{

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			tagTypeTf.setText("");
			tagValueTf.setText("");
			displayError("");
			AddTag.this.dispose();
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	protected class EnterListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

				
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode()==KeyEvent.VK_ENTER)
			{
				//error.setText("key: "+KeyEvent.getKeyText(e.getKeyCode()));
				addTagB.doClick();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
