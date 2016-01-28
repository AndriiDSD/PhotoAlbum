package cs213.photoAlbum.model;

import java.io.Serializable;

/**
 * A <b>Tag Class</b> is a combination of tag <i>type</i> and tag <i>value</i>. This objects is held 
 * by a photo. A tag describes details about the photo like location, and people in
 * the photo. A tag can be of any type and value.
 * @author Andrii Hlyvko
 */
public class Tag implements Comparable<Tag>,Serializable {

    public static final String LOCATION_UNSPECIFIED = "unspecified";
    public static final String TYPE_LOCATION = "location";
    public static final String TYPE_PEOPLE = "people";

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * String type is used to hold the type of this tag object.
     */
    private String type;
    /**
     * String value is used to hold the value of this tag object.
     */
    private String value;
/**
 * Tag object constructor. It will create a new tag object 
 * of type tagType with a value of tagValue.
 * @param <i>tagType</i> The type of this tag.
 * @param <i>tagValue</i> The value of this tag.
 * @throws IllegalArgumentException When tagType or TagValue are null or empty.
 */
    public Tag(String tagType, String tagValue)
    {
    	if(tagType==null||tagValue==null) //cannot construct a tag object with null or empty fields
    		throw new IllegalArgumentException();
    	if(tagType.isEmpty()||tagValue.isEmpty())
    		throw new IllegalArgumentException();
    	this.type=tagType.trim();
    	this.value=tagValue.trim();
    }
    /**
     * Gets the type  of this tag object.
     * @return Type of this tag.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Gets the value of this tag object.
     * @return Value of this tag.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the type of this tag.
     * @param <i>type</i> The new tag type.
     */
    public void setType(String type) {
    	if(type==null)
    		return;
    	if(type.isEmpty())
    		return;
    	this.type = type;
    }

    /**
     * Sets the value of this tag object.
     * @param <i>value</i> The new tag value.
     */
    public void setValue(String value) {
    	if(value==null)
    		return;
    	if(value.isEmpty())
    		return;
    	this.value = value;
    }
    
    /**
     * Compares this tag to a different tag. 
     * @param <i>other</i> A tag object to compare to.
     * @return <i>0</i> if they are equal, <i>-1</i> if this tag is less then, <i>1</i> if this tag is greater then.
     * @throws IllegalArgumentException When the other Tag is null.
     */
    @Override
    public int compareTo(Tag other) throws IllegalArgumentException{
    	if(other==null)
    		throw new IllegalArgumentException();
    	if(this.type.compareToIgnoreCase(other.type)==0)//if they have the same type compare based on value
    	{
    		return this.value.compareToIgnoreCase(other.value);
    	}
    	else {
            // make location tag always first after sorting
            if (Tag.TYPE_LOCATION.equalsIgnoreCase(this.type)) {
                return -1;
            } else if (Tag.TYPE_LOCATION.equalsIgnoreCase(other.type)) {
                return 1;
            } else if (Tag.TYPE_PEOPLE.equalsIgnoreCase(this.type)) {
                return -1;
            } else if(Tag.TYPE_PEOPLE.equalsIgnoreCase(other.type)) {
                return 1;
            } else {
    	        return this.type.compareToIgnoreCase(other.type); //else compare based on type
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;
        if (type != null ? !type.equalsIgnoreCase(tag.type) : tag.type != null) return false;
        if (value != null ? !value.equalsIgnoreCase(tag.value) : tag.value != null) return false;
        return true;
    }    
    public String toString()
    {
    	return this.type+":"+this.value;
    }
}
