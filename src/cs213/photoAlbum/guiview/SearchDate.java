package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * <b>SearchDate<b> <i>Class<i> Is a dialog that gets a range of dates as input to search 
 * for photos. The range of dates are send to an instance of the Search class.
 * @author Nadiia Chepurko
 * @see JDialog
 * @see Search
 */
public class SearchDate extends JDialog{
	private Search ownr;
	
	private JTextField startDateTf,endDateTf;
	private JButton searchB,cancelB;
	private JLabel errorL;
	
	private ButtonListener bl;
	private CloseListener cl;
	private EnterListener el;
	
	private LengthRestrictedDocument endRestrict=new LengthRestrictedDocument(50);
	private LengthRestrictedDocument startRestrict=new LengthRestrictedDocument(50);
	
	/**
	 * Constructor for the SearchDate class.
	 * @param owner an instance of the Search class that receives the data.
	 */
	public SearchDate(Search owner) {
		super(owner,"Search By Date",true);
		this.ownr=owner;
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblTagType = new JLabel("<html><font color='black'>"+"Start Date"+"<font color='red'>"+"*"+"<font color='black'>"+"</font></html>");
		getContentPane().add(lblTagType);
		
		JLabel lblTagValue = new JLabel("<html><font color='black'>"+"End Date"+"<font color='red'>"+"*"+"<font color='black'>"+"</font></html>");
		springLayout.putConstraint(SpringLayout.NORTH, lblTagValue, 87, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblTagType, -34, SpringLayout.NORTH, lblTagValue);
		springLayout.putConstraint(SpringLayout.WEST, lblTagType, 0, SpringLayout.WEST, lblTagValue);
		springLayout.putConstraint(SpringLayout.WEST, lblTagValue, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblTagValue);
		
		startDateTf = new JTextField();
		getContentPane().add(startDateTf);
		startDateTf.setColumns(10);
		
		endDateTf = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, endDateTf, 21, SpringLayout.EAST, lblTagValue);
		springLayout.putConstraint(SpringLayout.EAST, endDateTf, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, startDateTf, 0, SpringLayout.WEST, endDateTf);
		springLayout.putConstraint(SpringLayout.SOUTH, startDateTf, -30, SpringLayout.NORTH, endDateTf);
		springLayout.putConstraint(SpringLayout.EAST, startDateTf, 0, SpringLayout.EAST, endDateTf);
		springLayout.putConstraint(SpringLayout.NORTH, endDateTf, 85, SpringLayout.NORTH, getContentPane());
		getContentPane().add(endDateTf);
		endDateTf.setColumns(10);
		
		searchB = new JButton("Search");
		springLayout.putConstraint(SpringLayout.WEST, searchB, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, searchB, -26, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, searchB, -315, SpringLayout.EAST, getContentPane());
		getContentPane().add(searchB);
		
		cancelB = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.WEST, cancelB, 315, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, cancelB, 0, SpringLayout.EAST, startDateTf);
		getContentPane().add(cancelB);
		
		errorL = new JLabel("");
		errorL.setPreferredSize(new Dimension(480, 32));
		errorL.setMaximumSize(new Dimension(480, 32));
		springLayout.putConstraint(SpringLayout.NORTH, cancelB, 6, SpringLayout.SOUTH, errorL);
		springLayout.putConstraint(SpringLayout.SOUTH, errorL, -6, SpringLayout.NORTH, searchB);
		springLayout.putConstraint(SpringLayout.NORTH, errorL, 6, SpringLayout.SOUTH, lblTagValue);
		errorL.setMinimumSize(new Dimension(480, 32));
		
		springLayout.putConstraint(SpringLayout.WEST, errorL, 0, SpringLayout.WEST, lblTagType);
		springLayout.putConstraint(SpringLayout.EAST, errorL, 0, SpringLayout.EAST, startDateTf);
		getContentPane().add(errorL);
		
		cl=new CloseListener();
		bl=new ButtonListener();
		el=new EnterListener();
		
		this.addWindowListener(cl);
		searchB.addActionListener(bl);
		cancelB.addActionListener(bl);
		startDateTf.addKeyListener(el);
		endDateTf.addKeyListener(el);
		startDateTf.setDocument(startRestrict);
		endDateTf.setDocument(endRestrict);
		
		
		this.setPreferredSize(new Dimension(500,225));
		this.setMinimumSize(new Dimension(500,225));
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
			// TODO Auto-generated method stub
			if(e.getSource()==cancelB)
			{
				startDateTf.setText("");
				endDateTf.setText("");
				errorL.setText("");
				SearchDate.this.dispose();
			}
			else if(e.getSource()==searchB)
			{
				String startDate=startDateTf.getText();
				String endDate=endDateTf.getText();
				endDate=endDate.trim();
				startDate=startDate.trim();
				if(startDate.isEmpty()&&endDate.isEmpty())
				{
					displayError("Enter the date range!(MM/DD/YYYY-HH:MM:SS or MM/DD/YYYY)");
					return;
				}
				else if(startDate.isEmpty())
				{
					displayError("Enter the start date!(MM/DD/YYYY-HH:MM:SS or MM/DD/YYYY)");
					return;
				}
				else if(endDate.isEmpty())
				{
					displayError("Enter the end date!(MM/DD/YYYY-HH:MM:SS or MM/DD/YYYY)");
					return;
				}
				boolean success=false;
				try {
					success = ownr.searchByDate(startDate, endDate);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					displayError(e1.getMessage());
					return;
				} catch (Exception e3) {
					displayError(e3.getMessage());
					return;
				}
				if(!success)
				{
					displayError("Could not find photos any photos!");
				}
				else
				{
					startDateTf.setText("");
					endDateTf.setText("");
					errorL.setText("");
					SearchDate.this.dispose();
				}
			}
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
				searchB.doClick();
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
			startDateTf.setText("");
			endDateTf.setText("");
			errorL.setText("");
			SearchDate.this.dispose();
		}
		public void windowDeactivated(WindowEvent arg0) {}

		public void windowDeiconified(WindowEvent arg0) {}
		
		public void windowIconified(WindowEvent arg0) {}

		public void windowOpened(WindowEvent arg0) {}
		
	}
}
