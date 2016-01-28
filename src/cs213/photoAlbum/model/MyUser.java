package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A <b>MyUser</b> <i>Class</i> that implements the User interface.
 * @author Andrii Hlyvko
 * @see User
 */
public class MyUser implements User,Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ID;
    private String userName;
    private List<Album> albums;
    
    /**
     * A constructor for the MyUser object.
     * @param <i>userId</i> The id of the user.
     * @param <i>name</i> The full name of the user.
     */
    public MyUser(String userId,String name)
    {
    	if(userId==null||name==null)
    		throw new IllegalArgumentException();
    	this.ID=userId;
    	this.userName=name;
    	albums=new ArrayList<Album>();
    }
    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getFullName() {
        return userName;
    }

    @Override
    public List<Album> getAlbums() {
        return albums;
    }

    @Override
    public void addAlbum(Album albumImpl) {
    	if(albumImpl==null)
    		return;
    	if(!albums.contains(albumImpl))//if the album is not in the list
    	{
    		this.albums.add(albumImpl);//add the new album
    		Collections.sort(this.albums);//sort the list of albums
    	}
    }

    @Override
    public void deleteAlbum(String albumName) {
    	Album temp=new MyAlbum(albumName);
		albums.remove(temp);
    }

    @Override
    public void renameAlbum(String oldAlbumName, String newAlbumName) {
    	Album temp=new MyAlbum(oldAlbumName);
    	int i=albums.indexOf(temp);
    	if(i>=0&&i<this.albums.size())
    	{
    		Album old=this.albums.get(i);
    		this.albums.remove(i);
    		Album newAlbum=new MyAlbum(newAlbumName);//create new album
    		List<Photo> photos=old.getPhotos();
    		for(int n=0;n<photos.size();n++)//add all photos
    		{
    			newAlbum.addPhoto(photos.get(n));
    		}
    		this.albums.add(i, newAlbum);//add the new album in place of old
    	}
    }

	@Override
	public void setID(String ID) {
		if(ID!=null)
			if(!ID.isEmpty())
				this.ID=ID;
	}

	@Override
	public void setFullName(String fullName) {
		if(fullName!=null)
			if(!fullName.isEmpty())
				this.userName=fullName;
	}

}
