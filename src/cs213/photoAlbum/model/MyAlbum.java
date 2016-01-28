package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * A <b>MyAlbum</b> <i>Class</i> that implements the Album interface.
 * @author Andrii Hlyvko
 * @see Album
 */
public class MyAlbum implements Album, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String albumName;
    private List<Photo> photos;

    /**
     * Constructor of the MyAlbum class.
     * @param <i>name</i> The name of this album
     */
    public MyAlbum(String name) {
        if (name != null)
            if (!name.isEmpty())
            {
                this.albumName = name;
        		photos = new ArrayList<Photo>();
            }
            else
            	throw new IllegalArgumentException();
    }

    @Override
    public String getName() {
        return this.albumName;
    }

    @Override
    public List<Photo> getPhotos() {
        return this.photos;
    }

    @Override
    public void addPhoto(Photo photo) {
    	if(photo==null)
    		return;
    	if(!photos.contains(photo))
    	{
    		this.photos.add(photo);
    		Collections.sort(this.photos);//photos need to be comparable based of filename
    	}
    }

    @Override
    public void deletePhoto(String fileName) {
    	Photo temp=new MyPhoto(fileName,"");
        photos.remove(temp);
    }

    @Override
    public void recaption(String fileName, String newCaption) {
    	if(fileName==null||newCaption==null)
    		throw new IllegalArgumentException();
    	Photo temp=new MyPhoto(fileName,newCaption);
    	int index=photos.indexOf(temp);//search for this photo in this album
    	if(!(index<0))
    	{
    		photos.get(index).setCaption(newCaption);//if it exists set the new caption
    	}
    	
    }


	@Override
	public int compareTo(Album other) {
		// TODO Auto-generated method stub
		if(other==null)
			throw new IllegalArgumentException();
		return this.albumName.compareTo(other.getName());
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyAlbum)) return false;

        MyAlbum myAlbum = (MyAlbum) o;
        if (!albumName.equals(myAlbum.albumName)) return false;
        return true;
    }

}
