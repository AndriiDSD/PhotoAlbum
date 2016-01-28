package cs213.photoAlbum.guiview;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <b>LenghtRestrictedDocument<b> <i>Class<i> This class is used to 
 * restrict the number of characters typed into a JTextField.
 * @author Andrii Hlyvko
 * @see PlainDocument
 * @see JTextField
 */
public final class LengthRestrictedDocument extends PlainDocument {

	  private final int limit;

	  /**
	   * Constructor for this LengthRestrictedDocument.
	   * @param limit the maximum number of characters.
	   */
	  public LengthRestrictedDocument(int limit) {
	    this.limit = limit;
	  }

	  @Override
	  public void insertString(int offs, String str, AttributeSet a)
	      throws BadLocationException {
	    if (str == null)
	      return;

	    if ((getLength() + str.length()) <= limit) {
	      super.insertString(offs, str, a);
	    }
	  }
}
