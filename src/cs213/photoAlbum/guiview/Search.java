package cs213.photoAlbum.guiview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JLabel;

import cs213.photoAlbum.control.AlbumNotFoundException;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.Utils;

import javax.swing.border.TitledBorder;

/**
 * <b>Search<b> <i>Class<i> 
 * It is the main window of the search functionality. It searches for photos by dates and tags and displays 
 * the results to the user. It also can create a new album from search results. Extends JFrame and implements the CreateAlbumReciever interface.
 * @author Andrii Hlyvko
 * @see JFrame
 * @see CreateAlbumReciever
 */
public class Search extends JFrame implements CreateAlbumReciever {

    private GuiView myLogic;

    //Frames
    private InteractiveMode ownr;

    //Dialogs
    private CreateAlbum createAlbum;
    private SearchDate searchDateDialog;
    private SearchTags searchTagDialog;

    private PicturePanel selectedPhoto;
    private JScrollPane scrollPane;
    private JPanel thumbnails;

    //Buttons
    private JButton searchTagB, searchDateB, closeB, createAlbumB, clearResultsB;

    //Listeners
    private CloseListener cl = new CloseListener();
    private ClickListener clickL = new ClickListener();
    //private JButton backB,createAlbumB,searchByTag,searchByDate;
    private ButtonListener bl;

    //Labels
    private JLabel errorL;

    GridBagConstraints constraints = new GridBagConstraints();

    GridBagLayout gbLayout;


    /**
     * Constructor for the Search window.
     * @param owner an instance of InteractiveMode for state switching and sending data.
     */
    public Search(InteractiveMode owner, GuiView logic) {

        super("Search Photos");

        setMinimumSize(new Dimension(780, 700));

        setPreferredSize(new Dimension(780, 700));

        myLogic = logic;

        ownr = owner;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{570, 138, 0};
        gridBagLayout.rowHeights = new int[]{15, 25, 25, 167, 25, 38, 239, 38, 25, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);

        scrollPane = new JScrollPane();
        scrollPane.setBorder(new TitledBorder(null, "Search Results", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.ipadx = 10;

        scrollPane.setMinimumSize(new Dimension(580, 546));

        gbc_scrollPane.weighty = 1.0;
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.insets = new Insets(5, 5, 5, 0);
        gbc_scrollPane.gridheight = 6;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 1;
        getContentPane().add(scrollPane, gbc_scrollPane);

        thumbnails = new JPanel();
        thumbnails.setSize(new Dimension(660, 546));
        thumbnails.setMinimumSize(new Dimension(660, 546));
        thumbnails.setMaximumSize(new Dimension(660, 546));
        thumbnails.setFocusable(true);

        scrollPane.setViewportView(thumbnails);

        gbLayout = new GridBagLayout();
        gbLayout.columnWidths = new int[]{135, 135, 135, 135};
        gbLayout.rowHeights = new int[]{135, 135, 135, 135};
        gbLayout.columnWeights = new double[]{Double.MIN_VALUE};
        gbLayout.rowWeights = new double[]{Double.MIN_VALUE};

        thumbnails.setLayout(gbLayout);

        searchTagB = new JButton("Search By Tags");
        searchTagB.setPreferredSize(new Dimension(165, 25));
        searchTagB.setMinimumSize(new Dimension(165, 25));
        searchTagB.setMaximumSize(new Dimension(165, 25));

        GridBagConstraints gbc_searchTagB = new GridBagConstraints();
        gbc_searchTagB.weightx = 1.0;
        gbc_searchTagB.ipady = 15;
        gbc_searchTagB.gridheight = 2;
        gbc_searchTagB.anchor = GridBagConstraints.NORTHEAST;
        gbc_searchTagB.insets = new Insets(12, 10, 5, 17);
        gbc_searchTagB.gridx = 1;
        gbc_searchTagB.gridy = 1;

        getContentPane().add(searchTagB, gbc_searchTagB);

        //	addPhotoB.addActionListener(bl);

        searchDateB = new JButton("Search By Date");
        searchDateB.setPreferredSize(new Dimension(165, 25));
        searchDateB.setMinimumSize(new Dimension(165, 25));
        searchDateB.setMaximumSize(new Dimension(165, 25));

        GridBagConstraints gbc_searchDateB = new GridBagConstraints();
        gbc_searchDateB.weightx = 1.0;
        gbc_searchDateB.ipady = 15;
        gbc_searchDateB.gridheight = 2;
        gbc_searchDateB.anchor = GridBagConstraints.NORTHEAST;
        gbc_searchDateB.insets = new Insets(35, 10, 5, 17);
        gbc_searchDateB.gridx = 1;
        gbc_searchDateB.gridy = 2;

        getContentPane().add(searchDateB, gbc_searchDateB);

        clearResultsB = new JButton("Clear Results");
        clearResultsB.setPreferredSize(new Dimension(165, 25));
        clearResultsB.setMinimumSize(new Dimension(165, 25));
        clearResultsB.setMaximumSize(new Dimension(165, 25));

        GridBagConstraints gbc_clearResultsB = new GridBagConstraints();
        gbc_clearResultsB.weightx = 1.0;
        gbc_clearResultsB.ipady = 15;
        gbc_clearResultsB.anchor = GridBagConstraints.NORTHEAST;
        gbc_clearResultsB.gridheight = 2;
        gbc_clearResultsB.insets = new Insets(0, 0, 5, 17);
        gbc_clearResultsB.gridx = 1;
        gbc_clearResultsB.gridy = 4;

        getContentPane().add(clearResultsB, gbc_clearResultsB);

        //movePhotoB.addActionListener(bl);
        createAlbumB = new JButton("Create Album");
        createAlbumB.setPreferredSize(new Dimension(165, 25));
        createAlbumB.setMinimumSize(new Dimension(165, 25));
        createAlbumB.setMaximumSize(new Dimension(165, 25));

        GridBagConstraints gbc_createAlbumB = new GridBagConstraints();
        gbc_createAlbumB.ipady = 15;
        gbc_createAlbumB.weightx = 1.0;
        gbc_createAlbumB.anchor = GridBagConstraints.NORTHEAST;
        gbc_createAlbumB.insets = new Insets(0, 10, 5, 17);
        gbc_createAlbumB.gridx = 1;
        gbc_createAlbumB.gridy = 6;
        getContentPane().add(createAlbumB, gbc_createAlbumB);

        errorL = new JLabel("");

        GridBagConstraints gbc_errorL = new GridBagConstraints();
        gbc_errorL.gridheight = 2;
        gbc_errorL.fill = GridBagConstraints.BOTH;
        gbc_errorL.insets = new Insets(0, 150, 20, 5);
        gbc_errorL.gridx = 0;
        gbc_errorL.gridy = 7;

        getContentPane().add(errorL, gbc_errorL);

        closeB = new JButton("Close");
        closeB.setMaximumSize(new Dimension(165, 25));
        closeB.setMinimumSize(new Dimension(165, 25));
        closeB.setPreferredSize(new Dimension(165, 25));

        GridBagConstraints gbc_closeB = new GridBagConstraints();
        gbc_closeB.ipady = 15;
        gbc_closeB.weightx = 1.0;
        gbc_closeB.insets = new Insets(0, 10, 0, 17);
        gbc_closeB.gridheight = 2;
        gbc_closeB.anchor = GridBagConstraints.NORTHEAST;
        gbc_closeB.gridx = 1;
        gbc_closeB.gridy = 7;

        getContentPane().add(closeB, gbc_closeB);

        //closeB.addActionListener(bl);
        constraints = new GridBagConstraints();

        //photos=new ArrayList<Picture>();
        searchTagDialog = new SearchTags(this);
        searchDateDialog = new SearchDate(this);

        createAlbum = new CreateAlbum(this, this);

        //Add Listeners
        bl = new ButtonListener();
        cl = new CloseListener();
        clickL = new ClickListener();

        this.addWindowListener(cl);
        searchTagB.addActionListener(bl);
        searchDateB.addActionListener(bl);
        closeB.addActionListener(bl);
        createAlbumB.addActionListener(bl);
        clearResultsB.addActionListener(bl);

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(false);
    }

    /**
     * This method prepares this window to be closed. It clears all previous search results and the error label.
     */
    public void close() {
        clearSearch();
    }

    /**
     * This method is used to select a new thumbnail in the search results.
     * When a new thumbnail is selected its border changes to blue.
     * @param next
     */
    public void selectNewPhoto(PicturePanel next) {

        if (next == null) {
            return;
        }

        if (this.selectedPhoto == null) {

            this.selectedPhoto = next;

            next.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

            next.revalidate();
            next.repaint();

        } else {
            this.selectedPhoto.setBorder(BorderFactory.createRaisedSoftBevelBorder());

            this.selectedPhoto = next;

            next.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

            //scrollPane.getHorizontalScrollBar().setValue(next.getX());

            //scrollPane.revalidate();

            next.revalidate();
            next.repaint();

        }

        int max = scrollPane.getHorizontalScrollBar().getMaximum();
        int min = scrollPane.getHorizontalScrollBar().getMinimum();

        int x = next.getX();
        int y = next.getY();

        //errorL.setText("min: "+min+" ,max: "+max+" , x="+x+" ,y="+y);

        //scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());

        scrollPane.getHorizontalScrollBar().setValue(x);

    }
    
    /**
     * This method searches for photos using a range of dates.
     * @param startDate the start date of the range.
     * @param endDate the end date of the range.
     * @return true if matches were found, false otherwise.
     * @throws ParseException gets thrown when the date formats are not correct.
     */
    public boolean searchByDate(String startDate, String endDate) throws ParseException {
        List<Photo> photos = myLogic.control.getPhotosByDate(startDate, endDate);
        if(!photos.isEmpty())
        	displayResults(photos);
        return !Utils.isEmpty(photos);
    }

    /**
     * This method searches for photos using a list of tags.
     * @param tag a list of type String that contains search tags.
     * @throws IllegalArgumentException gets thrown when the the tags are in illegal format.
     */
    public void searchByTags(List<String> tag) throws IllegalArgumentException {
        List<Photo> photos = myLogic.control.getPhotosByTag(tag);
        displayResults(photos);
    }

    /**
     * This method is used to display the thumbnails of search results.
     * @param results a list of photos to be displayed.
     */
    public void displayResults(List<Photo> results) {
        if (!Utils.isEmpty(results)) {
            this.thumbnails.removeAll();

            for (int i = 0; i < results.size(); i++) {
                //ImageIcon t=new ImageIcon(photos.get(i).getFileName());
                //Picture tmp=new Picture(photos.get(i));
                //PicturePanel tmp=new PicturePanel(results.get(i));
                PicturePanel tmp = myLogic.findPicturePanel(results.get(i));
                //PicturePanel tmp=myLogic.getPictureAlbumPanel(results.get(i).getFileName());

                if (tmp == null) {
                    return;
                }

                tmp.addMouseListener(clickL);

                int t = i % 4;

                if (t != 3) {
                    constraints.gridwidth = 1;
                } else {
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    int c = gbLayout.rowHeights.length;
                    c++;
                    gbLayout.rowHeights = new int[c];
                    for (int l = 0; l < gbLayout.rowHeights.length; l++) {
                        gbLayout.rowHeights[l] = 135;

                    }
                }

                constraints.weighty = 1;
                constraints.weightx = 1;
                constraints.insets = new Insets(5, 5, 0, 5);
                constraints.anchor = GridBagConstraints.NORTHWEST;

                gbLayout.setConstraints(tmp, constraints);

                thumbnails.add(tmp, constraints);

            }
            if (thumbnails.getComponentCount() > 0) {

                this.selectNewPhoto((PicturePanel) thumbnails.getComponent(0));
            }

            thumbnails.validate();
            thumbnails.repaint();
        } else {
            clearSearch();
            displayMessage("Could not find photos!");
        }
    }

    private void displayMessage(String message) {
        errorL.setText(message);
    }

    private void displayError(String message) {
        errorL.setText("<html><font color='red'>" + message + "</font></html>");
    }

    @Override
    public boolean createAlbum(String albumName) {
        // TODO Auto-generated method stub
        boolean created = false;

        created = ownr.createAlbum(albumName);

        if (created) {

            List<Photo> photos = new ArrayList<Photo>();

            Component[] comps = thumbnails.getComponents();

            for (int i = 0; i < comps.length; i++) {

                photos.add(((PicturePanel) comps[i]).getPhoto());

            }
            for (int n = 0; n < photos.size(); n++) {
                try {

                    myLogic.control.addPhoto(photos.get(n).getFileName(), photos.get(n).getCaption(), albumName);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    displayError(e.getMessage());
                    return false;
                } catch (AlbumNotFoundException e) {
                    // TODO Auto-generated catch block
                    displayError(e.getMessage());
                    return false;
                }
            }

            ownr.updateDetails();

            displayMessage("Album " + albumName + " successfully created!");
        }

        return created;

    }

    protected class ClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            PicturePanel src = (PicturePanel) e.getSource();
            Search.this.selectNewPhoto(src);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
        }

    }

    protected class CloseListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public void windowClosing(WindowEvent e) {
            // TODO Auto-generated method stub

            myLogic.control.logout();
            System.exit(0);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            // TODO Auto-generated method stub

            myLogic.control.logout();
            System.exit(0);
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

    protected class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub

            if (e.getSource() == closeB) {

                clearResultsB.doClick();

                close();

                ownr.setVisible(true);

                Search.this.setVisible(false);

            } else if (e.getSource() == searchTagB) {

                clearResultsB.doClick();

                //errorL.setText("");

                searchTagDialog.setVisible(true);

            } else if (e.getSource() == searchDateB) {

                //errorL.setText("");

                clearResultsB.doClick();

                searchDateDialog.setVisible(true);

            } else if (e.getSource() == createAlbumB) {

                if (thumbnails.getComponentCount() > 0) {
                    displayError("");

                    createAlbum.setVisible(true);
                } else {
                    displayError("Must have search results to create album!");
                }

            } else if (e.getSource() == clearResultsB) {
                clearSearch();
            }

        }

    }

    private void clearSearch() {
        thumbnails.removeAll();
        gbLayout.rowHeights = new int[]{};
        thumbnails.validate();
        thumbnails.repaint();
        displayMessage("");
    }

}

