package cs213.photoAlbum.guiview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

/**
 * <b>SearchTags<b> <i>Class<i> It is a dialog that gets the list of tags to be searched
 * for and sends them to an instance of the Search class.
 * @author Nadiia Chepurko
 * @see JDialog
 * @see Search
 */
public class SearchTags extends JDialog {
    private JTextField tagValueTf;
    private JTextField tagTypeTf;
    private JLabel errorL;
    private JButton searchB, cancelB, addTagB, enterTagB, discardB, removeTagB;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private List<String> search;

    //Listeners
    private ButtonListener bl;
    private CloseListener cl;
    private EnterListener el;

    Search ownr;

    private LengthRestrictedDocument typeRestrict = new LengthRestrictedDocument(50);
    private LengthRestrictedDocument valueRestrict = new LengthRestrictedDocument(50);

    /**
     * Constructor for the SearchTags class. 
     * @param owner a Search instance to receive the data.
     */
    public SearchTags(Search owner) {
        super(owner, "Search By Tags", true);
        ownr = owner;
        this.setPreferredSize(new Dimension(480, 760));
        this.setMinimumSize(new Dimension(480, 760));
        SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);

        searchB = new JButton("Search");
        springLayout.putConstraint(SpringLayout.WEST, searchB, 10, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, searchB, -10, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, searchB, 140, SpringLayout.WEST, getContentPane());
        getContentPane().add(searchB);

        cancelB = new JButton("Cancel");
        springLayout.putConstraint(SpringLayout.WEST, cancelB, 340, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, cancelB, -10, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, cancelB, -10, SpringLayout.EAST, getContentPane());
        getContentPane().add(cancelB);

        JScrollPane scrollPane = new JScrollPane();
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, searchB);
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -36, SpringLayout.NORTH, searchB);
        springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, getContentPane());
        scrollPane.setBorder(new TitledBorder(null, "Current Search Tags", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        getContentPane().add(scrollPane);

        listModel = new DefaultListModel<String>();
        list = new JList<String>(listModel);
        search = new ArrayList<String>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(list);

        JPanel panel = new JPanel();
        springLayout.putConstraint(SpringLayout.NORTH, panel, 10, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, searchB);
        springLayout.putConstraint(SpringLayout.SOUTH, panel, -480, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panel, -10, SpringLayout.EAST, getContentPane());
        panel.setBorder(new TitledBorder(null, "Add A Search Tag", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel label = new JLabel("<html><font color=black>Enter Tag Value<font color='red'>*<font color=black>:</font></html>");
        label.setBounds(25, 40, 123, 15);
        panel.add(label);

        JLabel label_1 = new JLabel("Enter Tag Type:");
        label_1.setBounds(25, 80, 111, 15);
        panel.add(label_1);

        tagValueTf = new JTextField();
        tagValueTf.setEditable(false);
        tagValueTf.setColumns(10);
        tagValueTf.setBounds(171, 35, 265, 25);
        panel.add(tagValueTf);

        tagTypeTf = new JTextField();
        tagTypeTf.setEditable(false);
        tagTypeTf.setColumns(10);
        tagTypeTf.setBounds(171, 75, 265, 25);
        panel.add(tagTypeTf);

        addTagB = new JButton("Add To List");
        addTagB.setEnabled(false);
        addTagB.setBounds(25, 127, 123, 25);
        panel.add(addTagB);

        enterTagB = new JButton("Enter A Tag");
        enterTagB.setBounds(171, 127, 123, 25);
        panel.add(enterTagB);

        errorL = new JLabel("");
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 19, SpringLayout.SOUTH, errorL);
        springLayout.putConstraint(SpringLayout.WEST, errorL, 169, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.NORTH, errorL, 6, SpringLayout.SOUTH, panel);
        springLayout.putConstraint(SpringLayout.EAST, errorL, -24, SpringLayout.EAST, getContentPane());
        errorL.setMinimumSize(new Dimension(370, 15));
        errorL.setPreferredSize(new Dimension(370, 15));

        discardB = new JButton("Discard");
        discardB.setEnabled(false);
        discardB.setPreferredSize(new Dimension(115, 25));
        discardB.setMinimumSize(new Dimension(115, 25));
        discardB.setMaximumSize(new Dimension(115, 25));
        discardB.setBounds(321, 127, 115, 25);
        panel.add(discardB);
        getContentPane().add(errorL);

        removeTagB = new JButton("Remove Tag");
        springLayout.putConstraint(SpringLayout.NORTH, removeTagB, 0, SpringLayout.NORTH, searchB);
        springLayout.putConstraint(SpringLayout.WEST, removeTagB, 172, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, removeTagB, -32, SpringLayout.WEST, cancelB);
        getContentPane().add(removeTagB);

        tagTypeTf.setDocument(typeRestrict);
        tagValueTf.setDocument(valueRestrict);

        //Listeners
        bl = new ButtonListener();
        cl = new CloseListener();
        el = new EnterListener();
        this.addWindowListener(cl);
        searchB.addActionListener(bl);
        cancelB.addActionListener(bl);
        addTagB.addActionListener(bl);
        discardB.addActionListener(bl);
        removeTagB.addActionListener(bl);
        enterTagB.addActionListener(bl);
        tagValueTf.addKeyListener(el);
        tagTypeTf.addKeyListener(el);

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void clear() {
        errorL.setText("");
        tagValueTf.setText("");
        tagValueTf.setEditable(false);
        tagTypeTf.setText("");
        tagTypeTf.setEditable(false);
        discardB.setEnabled(false);
        addTagB.setEnabled(false);
        enterTagB.setEnabled(true);
        listModel.removeAllElements();
        search.clear();

    }

    private void displayError(String message) {
        errorL.setText("<html><font color='red'>" + message + "</font></html>");

    }

    protected class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            if (e.getSource() == cancelB) {
                clear();
                SearchTags.this.dispose();
            } else if (e.getSource() == searchB)
            {
                if (listModel.getSize() == 0) {
                    displayError("Add Search Tags First!");
                    return;
                }
                try{
                	ownr.searchByTags(search);
                } catch (Exception ee)
                {
                	displayError(ee.getMessage());
                    return;
                }
                cancelB.doClick();
            } else if (e.getSource() == addTagB) {
                String tagType = tagTypeTf.getText();
                String tagValue = tagValueTf.getText();
                tagType = tagType.trim();
                tagValue = tagValue.trim();
                if (tagValue.isEmpty()) {
                    displayError("Enter a tag value as a minimum!");
                    return;
                }
                String searchValue = new String();
                if (tagType.isEmpty()) {
                    searchValue = tagValue;
                } else {
                    searchValue = tagType + ":" + tagValue;
                }
                int i = search.indexOf(searchValue);
                if (i >= 0 && i < search.size()) {
                    if (search.get(i).compareTo(searchValue) == 0) {
                        displayError("Cannot add duplicate search tag!");
                        return;
                    }
                }
                search.add(searchValue);
                Collections.sort(search);

                listModel.addElement(searchValue);
                list.setSelectedIndex(listModel.getSize() - 1);
                list.ensureIndexIsVisible(listModel.getSize() - 1);

                errorL.setText("");
                discardB.doClick();
            } else if (e.getSource() == removeTagB) {
                int i = list.getSelectedIndex();
                String currentTag = new String();
                if (i >= 0 && i < listModel.size()) {
                    currentTag = (String) list.getSelectedValue();
                    listModel.remove(i);
                    search.remove(currentTag);
                    list.setSelectedIndex(0);
                    list.ensureIndexIsVisible(0);
                }
                errorL.setText("");
            } else if (e.getSource() == enterTagB) {
                enterTagB.setEnabled(false);
                addTagB.setEnabled(true);
                discardB.setEnabled(true);
                tagValueTf.setEditable(true);
                tagTypeTf.setEditable(true);
            } else if (e.getSource() == discardB) {
                errorL.setText("");
                tagValueTf.setText("");
                tagValueTf.setEditable(false);
                tagTypeTf.setText("");
                tagTypeTf.setEditable(false);
                discardB.setEnabled(false);
                addTagB.setEnabled(false);
                enterTagB.setEnabled(true);
            }
        }

    }

    protected class CloseListener implements WindowListener {

        public void windowActivated(WindowEvent arg0) {
        }

        public void windowClosed(WindowEvent arg0) {
        }

        public void windowClosing(WindowEvent arg0) {
            clear();
            SearchTags.this.dispose();
        }

        public void windowDeactivated(WindowEvent arg0) {
        }

        public void windowDeiconified(WindowEvent arg0) {
        }

        public void windowIconified(WindowEvent arg0) {
        }

        public void windowOpened(WindowEvent arg0) {
        }

    }

    protected class EnterListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            	addTagB.doClick();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub

        }
    }
}
