package cs213.photoAlbum.guiview;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JList;

import cs213.photoAlbum.control.AlbumNotFoundException;
import cs213.photoAlbum.control.PhotoNotFoundException;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.util.Utils;

/**
 * <b>DisplayPhoto<b> <i>Class<i> It is used to show the user a slide show of pictures inside the album.
 * The user can go through each picture manually. For each Picture its details are shown. The user can recaption a picture,
 * add/delete tags from the picture. After the user is done the user can go back to the current album.
 * @author Andrii Hlyvko
 * @see OpenAlbum
 * @see AddTag
 * @see Recaption
 */
public class DisplayPhoto extends JFrame {//,extends JFrame{
    private GuiView myLogic;
    private OpenAlbum ownr;


    private JTextField captionTf;
    private JTextField dateTf;

    private JButton closeB, forwardB, addTagB, deleteTagB, recaptionB, backB;

    private JList<Tag> tagList;
    private DefaultListModel<Tag> listModel;

    private CloseListener cl = new CloseListener();
    private ButtonListener bl = new ButtonListener();
    private ClickListener ck = new ClickListener();
    private ResizeListener rl = new ResizeListener();

    private Photo currentPhoto;
    private List<Photo> photos;

    private JPanel imagePanel, photoDetailsPanel;
    private JLabel imageLabel, errorL;

    private AddTag addTag;
    private Recaption recaption;

    /**
     * Constructor for DispalyPhoto class.
     * @param owner instance of OpenAlbum class that recieves data from this frame.
     * @param logic instance of GuiView that handles PicturPanel storage and logic.
     */
    public DisplayPhoto(OpenAlbum owner, GuiView logic) {
        super("Display Photo");
        this.myLogic = logic;
        this.ownr = owner;
        this.setFocusable(true);
        this.addMouseListener(ck);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setMinimumSize(new Dimension(1000, 725));
        this.setPreferredSize(new Dimension(1300, 725));
        SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);

        imagePanel = new JPanel();
        springLayout.putConstraint(SpringLayout.NORTH, imagePanel, 10, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, imagePanel, 10, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, imagePanel, -375, SpringLayout.EAST, getContentPane());
        imagePanel.setPreferredSize(new Dimension(1200, 1000));
        imagePanel.setSize(new Dimension(1200, 1000));
        getContentPane().add(imagePanel);
        imagePanel.addComponentListener(rl);

        photoDetailsPanel = new JPanel();
        springLayout.putConstraint(SpringLayout.NORTH, photoDetailsPanel, 10, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, photoDetailsPanel, 21, SpringLayout.EAST, imagePanel);
        springLayout.putConstraint(SpringLayout.SOUTH, photoDetailsPanel, 504, SpringLayout.NORTH, getContentPane());
        photoDetailsPanel.setPreferredSize(new Dimension(332, 400));
        photoDetailsPanel.setMinimumSize(new Dimension(332, 400));
        getContentPane().add(photoDetailsPanel);
        photoDetailsPanel.setLayout(null);
        photoDetailsPanel.setBorder(BorderFactory.createTitledBorder("Photo Details"));

        JLabel lblCaption = new JLabel("Caption:");
        lblCaption.setBounds(12, 35, 70, 15);
        photoDetailsPanel.add(lblCaption);

        JLabel lblDate = new JLabel("Date:");
        lblDate.setBounds(12, 89, 70, 15);
        photoDetailsPanel.add(lblDate);

        captionTf = new JTextField();
        captionTf.setEditable(false);
        captionTf.setBounds(100, 33, 222, 19);
        photoDetailsPanel.add(captionTf);
        captionTf.setColumns(10);

        dateTf = new JTextField();
        dateTf.setEditable(false);
        dateTf.setBounds(100, 87, 222, 19);
        photoDetailsPanel.add(dateTf);
        dateTf.setColumns(10);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 134, 310, 340);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tag List:"));
        photoDetailsPanel.add(scrollPane);
        listModel = new DefaultListModel<Tag>();
        tagList = new JList<Tag>(listModel);
        tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(tagList);

        recaptionB = new JButton("Recaption");
        springLayout.putConstraint(SpringLayout.EAST, recaptionB, -22, SpringLayout.EAST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, imagePanel, 0, SpringLayout.SOUTH, recaptionB);
        recaptionB.setPreferredSize(new Dimension(105, 40));
        recaptionB.setMinimumSize(new Dimension(105, 40));
        recaptionB.setMaximumSize(new Dimension(105, 40));
        getContentPane().add(recaptionB);
        recaptionB.addActionListener(bl);

        addTagB = new JButton("Add Tag");
        springLayout.putConstraint(SpringLayout.WEST, addTagB, 21, SpringLayout.EAST, imagePanel);
        springLayout.putConstraint(SpringLayout.EAST, addTagB, -233, SpringLayout.EAST, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, recaptionB, 90, SpringLayout.EAST, addTagB);
        springLayout.putConstraint(SpringLayout.NORTH, addTagB, 0, SpringLayout.NORTH, recaptionB);
        addTagB.setPreferredSize(new Dimension(105, 40));
        addTagB.setMinimumSize(new Dimension(105, 40));
        addTagB.setMaximumSize(new Dimension(105, 40));
        getContentPane().add(addTagB);
        addTagB.addActionListener(bl);

        deleteTagB = new JButton("Delete Tag");
        deleteTagB.setPreferredSize(new Dimension(105, 40));
        deleteTagB.setMinimumSize(new Dimension(111, 40));
        deleteTagB.setMaximumSize(new Dimension(105, 40));
        getContentPane().add(deleteTagB);
        deleteTagB.addActionListener(bl);

        forwardB = new JButton("Forward");
        springLayout.putConstraint(SpringLayout.WEST, deleteTagB, 21, SpringLayout.EAST, forwardB);
        springLayout.putConstraint(SpringLayout.SOUTH, forwardB, -10, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.NORTH, deleteTagB, 0, SpringLayout.NORTH, forwardB);
        springLayout.putConstraint(SpringLayout.EAST, forwardB, -375, SpringLayout.EAST, getContentPane());
        forwardB.setPreferredSize(new Dimension(100, 40));
        forwardB.setMinimumSize(new Dimension(100, 40));
        forwardB.setMaximumSize(new Dimension(100, 40));
        getContentPane().add(forwardB);
        forwardB.addActionListener(bl);

        closeB = new JButton("Close");
        springLayout.putConstraint(SpringLayout.EAST, deleteTagB, -90, SpringLayout.WEST, closeB);
        springLayout.putConstraint(SpringLayout.SOUTH, recaptionB, -26, SpringLayout.NORTH, closeB);
        springLayout.putConstraint(SpringLayout.WEST, closeB, 0, SpringLayout.WEST, recaptionB);
        springLayout.putConstraint(SpringLayout.EAST, closeB, -22, SpringLayout.EAST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, closeB, -10, SpringLayout.SOUTH, getContentPane());
        closeB.setPreferredSize(new Dimension(100, 40));
        closeB.setMinimumSize(new Dimension(100, 40));
        closeB.setMaximumSize(new Dimension(100, 40));
        closeB.addActionListener(bl);
        getContentPane().add(closeB);

        backB = new JButton("Back");
        springLayout.putConstraint(SpringLayout.WEST, backB, 10, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, backB, -10, SpringLayout.SOUTH, getContentPane());
        backB.setPreferredSize(new Dimension(100, 40));
        backB.setMinimumSize(new Dimension(100, 40));
        backB.setMaximumSize(new Dimension(100, 40));
        backB.addActionListener(bl);
        getContentPane().add(backB);

        errorL = new JLabel("");
        springLayout.putConstraint(SpringLayout.NORTH, errorL, 26, SpringLayout.SOUTH, imagePanel);
        springLayout.putConstraint(SpringLayout.WEST, errorL, 355, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, errorL, -118, SpringLayout.WEST, forwardB);
        errorL.setMinimumSize(new Dimension(200, 15));
        errorL.setPreferredSize(new Dimension(300, 15));
        getContentPane().add(errorL);


        cl = new CloseListener();
        this.setFocusable(true);
        this.addWindowListener(cl);

        bl = new ButtonListener();

        addTag = new AddTag(this);
        recaption = new Recaption(this);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(false);

    }

    /**
     * This method initializes the frame to make it display the first picture.
     * @param currentAlbum the album the user is currently in.
     * @param toDisplay Photo instance to display.
     * @throws AlbumNotFoundException gets thrown if the album does not exist.
     */
    public void initialize(String currentAlbum, Photo toDisplay) throws AlbumNotFoundException {
        // TODO Auto-generated method stub
        this.currentPhoto = toDisplay;
        this.photos = myLogic.control.listPhotos(currentAlbum);
        int i = DisplayPhoto.this.photos.indexOf(DisplayPhoto.this.currentPhoto);

        imageLabel = new JLabel();
        displayPhotoAtIndex(i);
        updateDetails();
    }

    /**
     * This method displays a picture at index i in the list of user photos.
     * @param i index of photo to display.
     */
    private void displayPhotoAtIndex(int i) {
        if (i < 0 || i >= photos.size())
            return;
        Photo l = photos.get(i);
        PicturePanel panel = myLogic.findPicturePanel(photos.get(i));
        Image image = panel.getImage();
        ImageIcon tmp = new ImageIcon(image);
        int imageX = image.getWidth(null);
        int imageY = image.getHeight(null);
        int panelX = imagePanel.getWidth();
        int panelY = imagePanel.getHeight();
        int x = imageX;
        int y = imageY;
        if (imageX == -1 || imageY == -1) {
            return;
        }
        if (panelX == 0 || panelY == 0) {
            return;
        }
        if (imageX > panelX || imageY > panelY)//need to scale down
        {
            if (imageX > panelX) {
                y = (int) (panelX * imageY) / imageX;
                x = panelX;
            }
            if (y > panelY) {
                y = panelY;
                x = (panelY * imageX) / imageY;
            }
        }


        image = image.getScaledInstance(x, y, Image.SCALE_DEFAULT);
        this.currentPhoto = this.photos.get(i);
        tmp.setImage(image);
        imageLabel.setIcon(tmp);
        imagePanel.add(imageLabel);
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    
    private void updateDetails() {
        if (this.currentPhoto == null)
            return;
        this.captionTf.setText(this.currentPhoto.getCaption());
        this.dateTf.setText(Utils.formatDate(this.currentPhoto.getDate()));
        listModel.removeAllElements();
        List<Tag> tmp2 = this.currentPhoto.getTags();
        for (int i = 0; i < tmp2.size(); i++) {
            listModel.addElement(tmp2.get(i));
        }
        tagList.setSelectedIndex(0);
        tagList.ensureIndexIsVisible(0);

    }

    /**
     * This method adds a new tag to the currently displayed picture.
     * @param tagType type of the new tag.
     * @param tagValue value of the new tag.
     * @return true if the tag was added, false otherwise.
     * @throws IllegalArgumentException gets thrown on illagal arguments.
     * @throws FileNotFoundException gets thrown when the picture file was not found.
     * @throws PhotoNotFoundException gets thrown when the Photo instance does not exist.
     */
    public boolean addTag(String tagType, String tagValue) throws IllegalArgumentException, FileNotFoundException, PhotoNotFoundException {
        boolean added = false;
        added = myLogic.control.addTag(this.currentPhoto.getFileName(), tagType, tagValue);
        if (added) {
            updateDetails();
        }
        return added;
    }

    /**
     * This method is used to recaption the currently displayed picture.
     * @param newCaption the new caption of the photo.
     * @return true if the photo was recaptioned, false otherwise.
     */
    public boolean recaption(String newCaption) {
        if (newCaption == null)
            return false;
        if (newCaption.isEmpty())
            return false;
        this.currentPhoto.setCaption(newCaption);
        updateDetails();
        boolean recaptioned=ownr.recaptionCurrentPhoto(newCaption);///recaption in open album
        return recaptioned;
    }
    
    /**
     * This method cleans this frame.
     */
    public void cleanup() {
        imagePanel.removeAll();
        imagePanel.revalidate();
        imagePanel.repaint();
        captionTf.setText("");
        dateTf.setText("");
        listModel.removeAllElements();
        displayError("");
    }

    /**
     * This method displays an error message using a JLabel in
     * red color using html.
     *
     * @param message an error message to display.
     */
    private void displayError(String message) {
        errorL.setText("<html><font color='red'>" + message + "</font></html>");

    }

    protected class CloseListener implements WindowListener {

        public void windowActivated(WindowEvent arg0) {
        }

        public void windowClosed(WindowEvent arg0) {
        }

        public void windowClosing(WindowEvent arg0) {
            //users.setSelectedIndex(0);
            myLogic.control.logout();
            System.exit(0);
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

    protected class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            errorL.setText("");
            if (e.getSource() == closeB) {
                DisplayPhoto.this.cleanup();
                ownr.open(ownr.getCurrentAlbum(), DisplayPhoto.this.currentPhoto.getFileName());
                DisplayPhoto.this.setVisible(false);
                ownr.setVisible(true);

            } else if (e.getSource() == backB) {
                if (currentPhoto == null) {
                    return;
                }
                if (photos.size() == 1 || photos.size() == 0) {
                    return;
                }
                int i = -1;
                for (int n = 0; n < photos.size(); n++) {
                    if (currentPhoto.getFileName().compareTo(photos.get(n).getFileName()) == 0) {
                        i = n;
                        break;
                    }
                }
                if (i < 0 || i >= photos.size()) {
                    return;
                }
                int next = i - 1;
                if (next < 0)
                    next = photos.size() - 1;
                displayPhotoAtIndex(next);
                updateDetails();
            } else if (e.getSource() == forwardB) {
                if (currentPhoto == null) {
                    return;
                }
                if (photos.size() == 1 || photos.size() == 0) {
                    return;
                }
                int i = -1;
                for (int n = 0; n < photos.size(); n++) {
                    if (currentPhoto.getFileName().compareTo(photos.get(n).getFileName()) == 0) {
                        i = n;
                        break;
                    }
                }
                if (i < 0 || i >= photos.size()) {
                    return;
                }
                int next = i + 1;
                if (next >= photos.size())
                    next = 0;
                displayPhotoAtIndex(next);
                updateDetails();
            } else if (e.getSource() == addTagB) {
                addTag.setVisible(true);
            } else if (e.getSource() == deleteTagB) {
                int i = tagList.getSelectedIndex();
                if (i != -1) {
                    Tag tmp = (Tag) tagList.getSelectedValue();
                    if (tmp.getType().compareToIgnoreCase("location") == 0 && tmp.getValue().compareToIgnoreCase("unspecified") == 0) {
                        displayError("Cannot delete location tag!");
                        return;
                    }
                    if (tmp != null) {
                        boolean removed = false;
                        try {
                            removed = myLogic.control.deleteTag(currentPhoto.getFileName(), tmp.getType(), tmp.getValue());
                        } catch (IllegalArgumentException e1) {
                            // TODO Auto-generated catch block
                            displayError(e1.getMessage());
                            return;
                        } catch (FileNotFoundException e1) {
                            // TODO Auto-generated catch block
                            displayError(e1.getMessage());
                            return;
                        } catch (PhotoNotFoundException e1) {
                            // TODO Auto-generated catch block
                            displayError(e1.getMessage());
                            return;
                        } catch (Exception e1) {
                        	displayError(e1.getMessage());
                            return;
                        }
                        if (removed) {
                            if (tmp.getType().compareToIgnoreCase("location") != 0) {
                                listModel.remove(i);
                                tagList.setSelectedIndex(0);
                            } else {
                                updateDetails();
                                tagList.setSelectedIndex(0);
                            }
                        }
                    }
                }
                if (tagList.isSelectionEmpty()) {
                    displayError("");
                    captionTf.setText("");
                    dateTf.setText("");

                }
            } else if (e.getSource() == recaptionB) {
                recaption.setVisible(true);
            }
        }
    }

    protected class ResizeListener implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent e) {
            // TODO Auto-generated method stub
            if (e.getSource() == imagePanel) {
                if (currentPhoto == null)
                    return;
                ImageIcon tmp = new ImageIcon(currentPhoto.getFileName());
                Image scaled = tmp.getImage();
                int imageX = scaled.getWidth(null);
                int imageY = scaled.getHeight(null);
                int panelX = imagePanel.getWidth();
                int panelY = imagePanel.getHeight();
                int x = imageX;
                int y = imageY;
                if (imageX == -1 || imageY == -1) {
                    return;
                }
                if (panelX == 0 || panelY == 0) {
                    return;
                }
                if (imageX > panelX || imageY > panelY)//need to scale down
                {
                    if (imageX > panelX) {
                        y = (int) (panelX * imageY) / imageX;
                        x = panelX;
                    }
                }
                if (y > panelY) {
                    y = panelY;
                    x = (panelY * imageX) / imageY;
                }
                scaled = scaled.getScaledInstance(x, y, Image.SCALE_DEFAULT);
                tmp.setImage(scaled);
                imageLabel.setIcon(tmp);
                imagePanel.add(imageLabel);
                imagePanel.revalidate();
                imagePanel.repaint();
            }
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void componentShown(ComponentEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void componentHidden(ComponentEvent e) {
            // TODO Auto-generated method stub

        }

    }

    protected class ClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            displayError("");
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
}
