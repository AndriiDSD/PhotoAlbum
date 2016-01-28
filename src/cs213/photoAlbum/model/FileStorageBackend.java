package cs213.photoAlbum.model;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The <b>FileStorageBackend</b> <i>Class</i> implements the Backend interface that provides
 * functionality to store and retrieve information on disc.
 * @see Backend
 * @author Andrii Hlyvko
 */
public class FileStorageBackend implements Backend, Serializable {
	private static final File dataDir=new File("data");
	private static final File dataFile=new File(dataDir+File.separator+"users.ser");
	static final long serialVersionUID=1L;
	
	public FileStorageBackend()
	{
		//check if data folder exists. If now create the data folder.
		if(!dataDir.exists())
		{
			dataDir.mkdir();
			dataDir.setWritable(true, false);//anyone can write to folder.
			dataDir.setReadable(true, false);//anyone can read.
		}
	}
    public User readUser(String ID){
    	if(ID==null)
    		return null;

    	ObjectInputStream in;
    	try {
			in=new ObjectInputStream(new FileInputStream(dataFile));
				List<User> users= (ArrayList<User>)in.readObject();
				User n=null;
				if(users==null)
				{
					return null;
				}
				for(int i=0;i<users.size();i++)
				{
					if(users.get(i).getID().compareTo(ID)==0)
					{
						n=users.get(i);
						break;
					}
				}
				in.close(); //close the reader.
				return n;
			}catch (IOException e) {
				//System.out.println("Error: Could Not Read User.");
				return null;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Error: Could Not Read User.");
				return null;
			} 
	}

    public void writeUser(User user) {
    	if(user==null)
    		return;
    	List<User> existingUsers=null;
    	ObjectInputStream in;
    	if(!dataFile.exists())
    	{
    			try {
    					dataFile.createNewFile();
    					dataFile.setReadable(true,false);
    					dataFile.setWritable(true,false);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    	}
    	try {
			in=new ObjectInputStream(new FileInputStream(dataFile));
			existingUsers= (ArrayList<User>)in.readObject();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			existingUsers=new ArrayList<User>();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(existingUsers==null)
    	{
    		return;
    	}
    	for(int i=0;i<existingUsers.size();i++)
    	{
    		if(existingUsers.get(i).getID().compareTo(user.getID())==0)//remove the old user object
    			existingUsers.remove(i);
    	}
    	existingUsers.add(user);//add new user object
    	try {
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(dataFile));
			out.writeObject(existingUsers);//write the user object to file
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void deleteUser(String ID){
    	if(ID==null)
    		return;
    	List<User> existingUsers=null;
    	ObjectInputStream in;
    	try {
			in=new ObjectInputStream(new FileInputStream(dataFile));
			existingUsers= (ArrayList<User>)in.readObject();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(existingUsers==null)
    	{
    		return;
    	}
    	for(int i=0;i<existingUsers.size();i++)
    	{
    		if(existingUsers.get(i).getID().compareTo(ID)==0)
    		{
    			existingUsers.remove(i);
    			break;
    		}
    	}
    	try {
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(dataFile));
			out.writeObject(existingUsers);//write the user object to file
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public List<User> getUsers() throws IOException, ClassNotFoundException {
    	List<User> users=new ArrayList<User>();
    	if(!dataFile.exists())
    	{
    		return users;
    	}
    	ObjectInputStream in;
    	try{
    	in=new ObjectInputStream(new FileInputStream(dataFile));
		users= (List<User>)in.readObject();
    	} catch(IOException e)
    	{
    		return users;
    	}
		in.close();
		if(users==null)
			return new ArrayList<User>();
    	return users;
	}

}
