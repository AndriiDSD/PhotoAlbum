package cs213.photoAlbum.simpleview;

import cs213.photoAlbum.control.AlbumNotFoundException;
import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.control.MyControl;
import cs213.photoAlbum.control.PhotoNotFoundException;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.util.Utils;

import java.io.*;
import java.text.ParseException;
import java.util.*;


//@author Andrii Hlyvko
 // A class that implements a text base view of the program.
public class CmdView {
    private static Control control;
	private static BufferedReader commandReader;
	private static StringTokenizer tokenizer;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length<1) {
            printError("Not Enough Arguments");
        }

        try {
            control = new MyControl();
            String command = args[0];
            if (command.compareTo("listusers") == 0 && args.length == 1) {
                printUsers();
            } else if (command.compareTo("adduser") == 0 && args.length == 3) {
                addNewUser(args[1], args[2]);
            } else if (command.compareTo("deleteuser") == 0 && args.length == 2) {
                deleteExistingUser(args[1]);
            } else if (command.compareTo("login") == 0 && args.length == 2) {
                goToUserMode(args[1]);
            } else {
                printError("Invalid arguments");
            }

        } catch (Exception e) {
            printError(e.getMessage());
        }

    }
	public static void printUsers() throws Exception {
		List<String> users=new ArrayList<String>();
        users=control.listUsers();
        if(users==null)
		{
			printError("Could Not Retrieve List of Users");
			return;
		}
		if(users.isEmpty())
			System.out.println("no users exist");
		for(int i=0;i<users.size();i++)
			System.out.println(users.get(i));
	}
	public static void addNewUser(String id,String name) throws Exception {
		if(id==null||name==null)
			return;
		id=id.trim();
        name=name.trim();
		if(id.isEmpty()||name.isEmpty()||id.contains(" "))
		{
			printError("Incorect ID or Name. User ID and name cannot be empty and ID cannot contain whitespace.");
			return;
		}
        boolean added= false;
        
        added = control.addUser(id, name);
        if(added)
			System.out.println("created user "+id+" with name "+name);
		else
		{
			User temp=control.login(id);
			System.out.println("user "+id+" already exists with name "+temp.getFullName());
			control.logout();
		}
	}

	public static void deleteExistingUser(String Id) throws Exception {
		Id=Id.trim();
		if(Id.contains(" "))
		{
			printError("ID cannot contain whitespace.");
			return;
		}
		boolean deleted=control.deleteUser(Id);
		if(deleted)
			System.out.println("deleted user "+Id);
		else
			System.out.println("user "+Id+" does not exist");
	}

	public static void goToUserMode(String ID) {
		ID=ID.trim();
		if(ID.contains(" "))
		{
			printError("ID cannot contain spaces.");
			return;
		}
        User user = control.login(ID);
        if (user != null) {

            commandReader=new BufferedReader(new InputStreamReader(System.in));
            while(true)
            {
                String line=null;
                try {
                    line=commandReader.readLine();
                } catch (IOException e) {
                	printError("Could Not Read Input Line");
                	continue;
                }
                if(line==null)
                {
                    printError("Could Not Read Input Line");
                    continue;
                }
                else if(line.isEmpty())
                {
                    printError("Command Sould Not Be Empty");
                    continue;
                }
                tokenizer=new StringTokenizer(line, " ");//break up input line based on spaces
                String command=new String();
                if(tokenizer.hasMoreTokens())
                	{
                		command=tokenizer.nextToken();
                	}
                else
                	{
                		printError("Could Not Read the Command");
                		continue;
                	}
                if(command.compareTo("createAlbum")==0)//works
                {
                	boolean created=false;
                	String albumName=new String();
                	try
                	{
                		String tmp=tokenizer.nextToken("\"");//skip space
                		if(tmp.compareTo(" ")!=0)//if the token was not space print error
                			throw new NoSuchElementException();
                		String tmp2=tokenizer.nextToken("\0");  //read the rest of the token(prevents from reading after double quotes)
                		if(!tmp2.endsWith("\""))  //check if it ends with quote
                			throw new NoSuchElementException();
                		tokenizer=new StringTokenizer(tmp2);//to read stuff before quote
                		albumName=tokenizer.nextToken("\"");//read the album name
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -createAlbum \"<albumName>\"");
                		continue;
                	}
                	if(albumName==null)
                	{
                		printError("Could Not Create Album");
                		continue;
                	}
                	albumName=albumName.trim();
                    if(albumName.isEmpty())
                	{
                		printError("Album name cannot be empty.");
                		continue;
                	}
                	
                	created=control.createAlbum(albumName);
                	if(created)
                	{
                		System.out.println("created album for user "+user.getID()+":");
                		System.out.println(albumName);
                	}
                	else
                	{
                		System.out.println("album exists for user "+user.getID()+":");
                		System.out.println(albumName);
                	}
                }
                else if(command.compareTo("deleteAlbum")==0)//works
                {
                	boolean deleted=false;
                	String albumName=new String();
                	try
                	{
                		String tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		String tmp2=tokenizer.nextToken("\0");
                		if(!tmp2.endsWith("\""))
                			throw new NoSuchElementException();
                		tokenizer=new StringTokenizer(tmp2);
                		albumName=tokenizer.nextToken("\"");
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -deleteAlbum \"<albumName>\"");
                		continue;
                	}
                	if(albumName==null)
                	{
                		printError("Could Not Delete Album");
                		continue;
                	}
                	albumName=albumName.trim();
                	if(albumName.isEmpty())
                	{
                		printError("Album Name Cannot be Empty.");
                		continue;
                	}
                	
                	deleted=control.deleteAlbum(albumName);
                	if(deleted)
                		{
                       		System.out.println("deleted album from user "+user.getID()+":");
                       		System.out.println(albumName);
                			}
                	else
               			{
                       		System.out.println("album does not exist for user "+user.getID()+":");
                       		System.out.println(albumName);
               			}               	
                }
                else if(command.compareTo("listAlbums")==0)//works
                {
                	if(tokenizer.hasMoreTokens())
                	{
                		printError("Correct Format -listAlbums");
                		continue;
                	}
                	List<Album> userAlbums=new ArrayList<Album>();
                	userAlbums=control.listAlbums();
                	if(userAlbums==null)
                	{
                		printError("Could not retrieve the list of albums");
                		continue;
                	}
                	if(userAlbums.isEmpty())
                	{
                		System.out.println("No albums exist for user "+user.getID());
                	}
                	else{
                		System.out.println("Albums for user "+user.getID());
                		for(int i=0;i<userAlbums.size();i++)
                		{//control needs methods for getting start and end dates of an album and number of photos in it
                			int numOfPhotos=userAlbums.get(i).getPhotos().size();
                			System.out.print(userAlbums.get(i).getName()+" number of photos: "+numOfPhotos);
                			if(numOfPhotos!=0)
                				{
                					System.out.print(", "+control.getStartAndEndDate(userAlbums.get(i)));
                				}
                			System.out.println();
                		}
                	}
                }
                else if(command.compareTo("listPhotos")==0)
                {
                	String albumName=new String();
                	try
                	{
                		String tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		String tmp2=tokenizer.nextToken("\0");
                		if(!tmp2.endsWith("\""))
                			throw new NoSuchElementException();
                		tokenizer=new StringTokenizer(tmp2);
                		albumName=tokenizer.nextToken("\"");
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -listPhotos \"<albumName>\"");
                		continue;
                	}
                	if(albumName==null)
                	{
                		printError("Could Not List Photos.");
                		continue;
                	}
                	albumName=albumName.trim();
                	if(albumName.isEmpty())
                	{
                		printError("Album Name Cannot Be Empty.");
                		continue;
                	}
                    List<Photo> photos= null;
                    try {
                        photos = control.listPhotos(albumName);

                        if(photos==null)
                        {
                            printError("Could Not List Photos");
                            continue;
                        }
                        System.out.println("Photos for album "+albumName+":");
                        for(int i=0;i<photos.size();i++)
                        {
                            System.out.println(photos.get(i).getFileName()+" - "+Utils.formatDate(photos.get(i).getDate()));
                        }
                    } catch (AlbumNotFoundException e) {
                        System.out.println("Album "+albumName+" does not exist");
                        continue;
                    }
                }
                else if(command.compareTo("addPhoto")==0)//more testing needed
                {
                	String fileName=new String();
                	String caption=new String();
                	String albumName=new String();
                	try
                	{
                		String tmp=tokenizer.nextToken("\"");//skip space
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		fileName=tokenizer.nextToken("\"");//get file name
                		if(fileName.compareTo(" ")==0)
                			throw new NoSuchElementException();
                		tmp=tokenizer.nextToken("\"");//skip space
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		caption=tokenizer.nextToken("\"");
                		if(caption.compareTo(" ")==0)
                		{
                			caption="";
                			String tmp2=tokenizer.nextToken("\0");
                    		if(!tmp2.endsWith("\""))
                    			throw new NoSuchElementException();
                    		tokenizer=new StringTokenizer(tmp2);
                			albumName=tokenizer.nextToken("\"");
                		}
                		else
                		{
                			tokenizer.nextToken("\"");
                			String tmp2=tokenizer.nextToken("\0");
                    		if(!tmp2.endsWith("\""))
                    			throw new NoSuchElementException();
                    		tokenizer=new StringTokenizer(tmp2);
                			albumName=tokenizer.nextToken("\"");
                		}
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -addPhoto \"<fileName>\" \"<caption>\" \"<albumName>\"");
                		continue;
                	}
                	if(fileName==null||caption==null||albumName==null)
                	{
                		printError("Correct Format -"
                				+ "addPhoto \"<fileName>\" \"<caption>\" \"<albumName>\"");
                		continue;
                	}
                	caption=caption.trim();
                	fileName=fileName.trim();
                	albumName=albumName.trim();
                	if(fileName.isEmpty()||albumName.isEmpty())
                	{
                		printError("Correct Format -"
                				+ "addPhoto \"<fileName>\" \"<caption>\" \"<albumName>\"");
                		continue;
                	}
                	fileName=fileName.trim();
                	albumName=albumName.trim();
                	if(!caption.isEmpty())
                		caption=caption.trim();
                	List<Album> albs=control.listAlbums();
                	boolean albumExists=false;
                	if(albs!=null)
                	{
                		for(int i=0;i<albs.size();i++)
                		{
                			if(albs.get(i).getName().compareTo(albumName)==0)
                				albumExists=true;
                		}
                	}
                	if(!albumExists)
                	{
                		System.out.println("Album "+albumName+" does not exist");
                		continue;
                	}
                	File photoFile=new File(fileName);
                	if(!photoFile.exists()||photoFile.isDirectory())
                	{
                		System.out.println("File "+photoFile.getAbsolutePath()+" does not exist");
                		continue;
                	}
                    boolean added= false;
                    try {
                        added = control.addPhoto(fileName, caption, albumName);

                        if(added)
                        {
                        	Photo addedPhoto=control.getPhotoByFileName(fileName);
                            System.out.println("Added photo "+fileName+":");
                            System.out.println(addedPhoto.getCaption()+" - Album: " +albumName);
                        }
                        else{
                            System.out.println("Photo "+photoFile.getAbsolutePath()+" already exists in album "+albumName);
                        }
                    }catch (AlbumNotFoundException e) {
                        System.out.println("Album "+albumName+" does not exist");
                        continue;
                    } catch (FileNotFoundException e) {
                    	System.out.println("File "+photoFile.getAbsolutePath()+" does not exist");
                        continue;
                    } 
                    catch(IllegalArgumentException e) {
                    	printError(e.getMessage());
                    	continue;
                    }
                }
                else if(command.compareTo("movePhoto")==0)
                {
                	String fileName=new String();
                	String oldAlbum=new String();
                	String newAlbum=new String();
                	try
                	{
                		String tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		fileName=tokenizer.nextToken("\"");
                		tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		oldAlbum=tokenizer.nextToken("\"");
                		tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		String tmp2=tokenizer.nextToken("\0");
                		if(!tmp2.endsWith("\""))
                			throw new NoSuchElementException();
                		tokenizer=new StringTokenizer(tmp2);
                		newAlbum=tokenizer.nextToken("\"");
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -movePhoto \"<fileName>\" \"<oldAlbumName>\" \"<newAlbumName>\"");
                		continue;
                	}
                	if(fileName==null||oldAlbum==null||newAlbum==null)
                	{
                		printError("Correct Format -"
                				+ "movePhoto \"<fileName>\" \"<oldAlbumName>\" \"<newAlbumName>\"");
                		continue;
                	}
                	fileName=fileName.trim();
                	oldAlbum=oldAlbum.trim();
                	newAlbum=newAlbum.trim();
                	if(fileName.isEmpty()||oldAlbum.isEmpty()||newAlbum.isEmpty())
                	{
                		printError("Correct Format -"
                				+ "movePhoto \"<fileName>\" \"<oldAlbumName>\" \"<newAlbumName>\"");
                		continue;
                	}
                    boolean moved= false;
                    boolean inOldAlbum=false,oldAlbumExists=false,newAlbumExists=false;
                    List<Album> albsAll=control.listAlbums();
                	for(int i=0;i<albsAll.size();i++)
                	{
                		if(albsAll.get(i).getName().compareTo(oldAlbum)==0)
                			oldAlbumExists=true;
                		if(albsAll.get(i).getName().compareTo(newAlbum)==0)
                			newAlbumExists=true;
                	}
                	if(!oldAlbumExists)
                	{
                		System.out.println("Album "+oldAlbum+" does not exist");
                		if(!newAlbumExists)
                			System.out.println("Album "+newAlbum+" does not exist");
                		continue;
                	}
                	if(!newAlbumExists)
                	{
                		System.out.println("Album "+newAlbum+" does not exist");
                		continue;
                	}
                    try {
                    	List<String>albs=control.listsPhotoAlbumNamesInfo(fileName);
                    	if(albs==null)
                    	{
                    		System.out.println("Photo "+fileName+" does not exist in "+oldAlbum);
                    		continue;
                    	}
                    	
                    	for(int i=0;i<albs.size();i++)
                    	{
                    		if(albs.get(i).compareTo(newAlbum)==0)//if the photo exists in old album do not do anything
                    		{
                    			inOldAlbum=true;
                    		}
                    	}
                    	if(inOldAlbum)
                    		continue;
                    	
                        moved = control.movePhoto(fileName, oldAlbum, newAlbum);
                        if(moved)
                        {
                            System.out.println("Moved photo "+fileName);
                            System.out.println(fileName+" - From album "+oldAlbum+" to album "+newAlbum);
                        }
                        else
                        {
                            System.out.println("Photo "+fileName+" does not exist in "+oldAlbum);
                        }
                    } catch (AlbumNotFoundException e) {
                        printError(e.getMessage());
                        continue;
                    } catch (PhotoNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("Photo "+fileName+" does not exist");
						continue;
                    } catch (FileNotFoundException e) {
                    	System.out.println("Photo "+fileName+" does not exist");
						continue;
					}
                }
                else if(command.compareTo("removePhoto")==0)//works
                {
                	String fileName=new String();
                	String albumName=new String();
                	try
                	{
                		String tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		fileName=tokenizer.nextToken("\"");
                		tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		String tmp2=tokenizer.nextToken("\0");
                		if(!tmp2.endsWith("\""))
                			throw new NoSuchElementException();
                		tokenizer=new StringTokenizer(tmp2);
                		albumName=tokenizer.nextToken("\"");
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -removePhoto \"<fileName>\" \"<albumName>\"");
                		continue;
                	}
                	if(fileName==null||albumName==null)
                	{
                		printError("Correct Format -"
                				+ "removePhoto \"<fileName>\" \"<albumName>\"");
                		continue;
                	}
                	fileName=fileName.trim();
                	albumName=albumName.trim();
                	if(fileName.isEmpty()||albumName.isEmpty())
                	{
                		printError("Correct Format -"
                				+ "removePhoto \"<fileName>\" \"<albumName>\"");
                		continue;
                	}
                	boolean albumExists=false;
                    List<Album> albsAll=control.listAlbums();
                	for(int i=0;i<albsAll.size();i++)
                	{
                		if(albsAll.get(i).getName().compareTo(albumName)==0)
                			albumExists=true;
                	}
                	if(!albumExists)
                	{
                		System.out.println("Album "+albumName+" does not exist");
                		continue;
                	}
                    boolean removed= false;
                    try {
                        removed = control.removePhoto(fileName, albumName);
                        if(removed)
                        {
                            System.out.println("Removed photo:");
                            System.out.println(fileName+" - From album "+albumName);
                        }
                        else
                        {
                            System.out.println("Photo "+fileName+" is not in album "+albumName);
                        }
                    } catch (AlbumNotFoundException e) {
                        System.out.println("Album "+albumName+" does not exist");
                        continue;
                    } catch (FileNotFoundException e) {
                    	System.out.println("Photo "+fileName+" does not exist");
                        continue;
                    }
                }
                else if(command.compareTo("addTag")==0)//works
                {
                	String fileName=new String();
                	String tagValue=new String();
                	String tagType=new String();
                	try
                	{
                		String tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		fileName=tokenizer.nextToken("\"");//file name
                		tokenizer.nextToken(" ");
                		tagType=tokenizer.nextToken(" :");//tag type
                		tokenizer.nextToken("\"");
                		String tmp2=tokenizer.nextToken("\0");
                		if(!tmp2.endsWith("\""))
                			throw new NoSuchElementException();
                		tokenizer=new StringTokenizer(tmp2);
                		tagValue=tokenizer.nextToken("\"");//tag value
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -addTag \"<fileName>\" <tagType>:\"<tagValue>\"");
                		continue;
                	}
                	if(tagValue==null||fileName==null||tagType==null)
                	{
                		printError("Correct Format -addTag \"<fileName>\" <tagType>:\"<tagValue>\"");
                		continue;
                	}
                	tagValue=tagValue.trim();
                	fileName=fileName.trim();
                	tagType=tagType.trim();
                	if(tagValue.isEmpty()||fileName.isEmpty()||tagType.isEmpty())
                	{
                		printError("Correct Format -addTag \"<fileName>\" <tagType>:\"<tagValue>\"");
                		continue;
                	}
                    boolean added= false;
                    try {
                        added = control.addTag(fileName, tagType, tagValue);

                        if(added)
                        {
                            System.out.println("Added tag:");
                            System.out.println(fileName+" "+tagType+":"+tagValue);
                        }
                        else
                        {
                            System.out.println("Tag already exists for "+fileName+" "+tagType+":"+tagValue);
                        }
                    } catch (PhotoNotFoundException e) {
                        //printError(e.getMessage());
                    	System.out.println("Photo "+fileName+" does not exist");
                        continue;
                    } catch (FileNotFoundException e) {
                    	System.out.println("Photo "+fileName+" does not exist");
						continue;
					}
                }
                else if(command.compareTo("deleteTag")==0)//works
                {
                	String fileName=new String();
                	String tagValue=new String();
                	String tagType=new String();
                	try
                	{
                		String tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		fileName=tokenizer.nextToken("\"");//file name
                		tokenizer.nextToken(" ");
                		tagType=tokenizer.nextToken(" :");//tag type
                		tokenizer.nextToken("\"");
                		String tmp2=tokenizer.nextToken("\0");
                		if(!tmp2.endsWith("\""))
                			throw new NoSuchElementException();
                		tokenizer=new StringTokenizer(tmp2);
                		tagValue=tokenizer.nextToken("\"");//tag value
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -deleteTag \"<fileName>\" <tagType>:\"<tagValue>\"");
                		continue;
                	}
                	if(tagValue==null||fileName==null||tagType==null)
                	{
                		printError("Correct Format -deleteTag \"<fileName>\" <tagType>:\"<tagValue>\"");
                		continue;
                	}
                	tagValue=tagValue.trim();
                	fileName=fileName.trim();
                	tagType=tagType.trim();
                	if(tagValue.isEmpty()||fileName.isEmpty()||tagType.isEmpty())
                	{
                		printError("Correct Format -deleteTag \"<fileName>\" <tagType>:\"<tagValue>\"");
                		continue;
                	}
                    boolean deleted= false;
                    try {
                        deleted = control.deleteTag(fileName, tagType, tagValue);

                        if(deleted)
                        {
                            System.out.println("Deleted tag");
                            System.out.println(fileName+" "+tagType+":"+tagValue);
                        }
                        else
                        {
                            System.out.println("Tag does not exist for "+fileName+" "+tagType+":"+tagValue);
                        }
                    } catch (PhotoNotFoundException e) {
                        System.out.println("Photo "+fileName+" does not exist");
                        continue;
                    } catch (FileNotFoundException e) {
						System.out.println("Photo "+fileName+" does not exist");
						continue;
					}
                }
                else if(command.compareTo("listPhotoInfo")==0)//works
                {
                	String fileName=new String();
                	try
                	{
                		String tmp=tokenizer.nextToken("\"");
                		if(tmp.compareTo(" ")!=0)
                			throw new NoSuchElementException();
                		String tmp2=tokenizer.nextToken("\0");
                		if(!tmp2.endsWith("\""))
                			throw new NoSuchElementException();
                		tokenizer=new StringTokenizer(tmp2);
                		fileName=tokenizer.nextToken("\"");//file name
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -listPhotoInfo \"<fileName>\"");
                		continue;
                	}
                	if(fileName==null)
                		{
                			printError("Correct Format -listPhotoInfo \"<fileName>\"");
                			continue;
                		}
                	fileName=fileName.trim();
                	if(fileName.isEmpty())
                		{
                			printError("Correct Format -listPhotoInfo \"<fileName>\"");
                			continue;
                		}

					try {

                    Photo temp = control.getPhotoByFileName(fileName);
                    List<String> albumNames = control.listsPhotoAlbumNamesInfo(fileName);
                	if(temp!=null&&albumNames!=null)
                	{
                		System.out.println("Photo file name: " + fileName);
                        System.out.print("Album: ");
                        if(!albumNames.isEmpty())
                        {
                        	for(int i=0;i<albumNames.size()-1;i++)
                        		{
                        			System.out.print("\""+albumNames.get(i)+"\", ");
                        		}
                        	System.out.print("\""+albumNames.get(albumNames.size()-1)+"\"");
                        }
                        System.out.println();
                        System.out.println("Date: "+Utils.formatDate(temp.getDate()));//does not work
                        System.out.println("Caption: "+temp.getCaption());
                        System.out.println("Tags:");
                        List<Tag> tags=temp.getTags();
                        for(int i=0;i<tags.size();i++)
                        	System.out.println(tags.get(i).toString());
                	}
                	else
                		System.out.println("Photo "+fileName+" does not exist");

					}  catch (FileNotFoundException e) {
						System.out.println("Photo "+fileName+" does not exist");
						continue;
					}
				}
                else if(command.compareTo("getPhotosByDate")==0)
                {
                	String startDate=new String();
                	String endDate=new String();
                	try
                	{
                		startDate=tokenizer.nextToken(" ");//file name
                		endDate=tokenizer.nextToken(" ");
                		if(tokenizer.hasMoreTokens())
                			throw new NoSuchElementException();
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -getPhotosByDate <start date> <end date>");
                		continue;
                	}
                	if(startDate==null||endDate==null)
                	{
                		printError("Could not get photos by date.");
                		continue;
                	}
                	startDate=startDate.trim();
                	endDate=endDate.trim();
                	if(startDate.isEmpty()||endDate.isEmpty())
                	{
                		printError("Start date and end date cannot be empty.");
                		continue;
                	}
                    List<Photo> photos= null;
                    try 
                    {
                        photos = control.getPhotosByDate(startDate, endDate);

                        if(photos==null)
                        {
                        	printError("Could not get photos by date.");
                        	continue;
                        }
                        System.out.println("Photos for user "+user.getID()+" in range "+startDate+" to "+endDate);
                        if(!photos.isEmpty())
                        {
                            for(int i=0;i<photos.size();i++)
                            {
                                System.out.print(photos.get(i).getCaption()+" - Album: ");
                                List<String> albumNames = control.listsPhotoAlbumNamesInfo(photos.get(i).getFileName());
                                for(int n=0;n<albumNames.size()-1;n++)
                                {
                                    System.out.print(albumNames.get(n)+", ");
                                }
                                System.out.print(albumNames.get(albumNames.size()-1));
                                System.out.println(" - Date:"+Utils.formatDate(photos.get(i).getDate()));
                            }
                        }
                    } 
                    catch (ParseException e) 
                    {
                        printError(e.getMessage());
                        continue;
                    } catch (FileNotFoundException e) {
						printError(e.getMessage());
						continue;
					} catch (IllegalArgumentException e) {
						printError(e.getMessage());
						continue;
					}
                }
                else if(command.compareTo("getPhotosByTag")==0)
                {
                	String searchString=new String();
                	try
                	{
                		searchString=tokenizer.nextToken("\0");
                	}
                	catch(NoSuchElementException e)
                	{
                		printError("Correct Format -getPhotosByTag [<tagType>:]\"<tagValue>\" [,[<tagType>:]\"<tagValue>\"]... ");
                		continue;
                	}
                	tokenizer=new StringTokenizer(searchString);
                	List<String> tags=new ArrayList<String>();
                	while(tokenizer.hasMoreTokens())//this breaks up the tokens into tags(with or without values)
                	{
                		tags.add(tokenizer.nextToken(" ,"));
                	}
                	if(tags.isEmpty())
                	{
                		printError("Correct Format -getPhotosByTag [<tagType>:]\"<tagValue>\" [,[<tagType>:]\"<tagValue>\"]... ");
                		continue;
                		
                	}
                	try{
                	for(int i=0;i<tags.size();i++)//removes the quotes around tag value
                	{
                		if(!tags.get(i).endsWith("\""))
                		{
                			throw new IllegalArgumentException();
                		}
                		if(!tags.get(i).contains(":"))
                		{
                			if(!tags.get(i).startsWith("\""))
                				throw new IllegalArgumentException(); 
                		}
                		String temp=tags.get(i);
                		tags.remove(i);
                		StringTokenizer tok=new StringTokenizer(temp,"\"");
                		ArrayList<String> args=new ArrayList<String>();
                		while(tok.hasMoreTokens())
                		{
                			args.add(tok.nextToken());
                		}
                		if(args.size()==2)
                		{
                			tags.add(i, args.get(0).concat(args.get(1)));
                		}
                		else if(args.size()==1)
                		{
                			if(args.get(0).endsWith(":"))
                				throw new IllegalArgumentException();
                			tags.add(0,args.get(0));
                		}
                		else
                		{
                			printError("getPhotosByTag. Could Not Parse Arguments.");
                			continue;
                		}
                	}
                	} catch(IllegalArgumentException e)
                	{
                		printError("Correct Format -getPhotosByTag [<tagType>:]\"<tagValue>\" [,[<tagType>:]\"<tagValue>\"]... ");
            			continue;
                	}
                	
					try {
						List<Photo> photos=control.getPhotosByTag(tags);
	                	if(photos==null)
	                	{
	                		printError("getPhotosByTag. Could not retrieve photos.");
	                		continue;
	                	}
						System.out.println("Photos for user " + user.getID() + " with tags " + searchString + ": ");
						for (int i = 0; i < photos.size(); i++) {
							System.out.print(photos.get(i).getCaption() + " - Album:");
							List<String> albumNames = control.listsPhotoAlbumNamesInfo(photos.get(i).getFileName());
							if (!albumNames.isEmpty()) {
								for (int n = 0; n < albumNames.size() - 1; n++) {
									System.out.print(albumNames.get(n) + ", ");
								}
								System.out.print(albumNames.get(albumNames.size() - 1));
							}
							System.out.println(" - Date: " + Utils.formatDate(photos.get(i).getDate()));
						}
					} catch (FileNotFoundException e) {
						printError(e.getMessage());
						continue;
					} catch (IllegalArgumentException e) {
						printError(e.getMessage());
						continue;
					}
                }
                else if(command.compareTo("logout")==0)//works
                {
                	if(tokenizer.hasMoreTokens())
                	{
                		printError("Corect Format -logout");
                		continue;
                	}
                	control.logout();
                    break;
                }
                else
                {
                	printError("Unknown Command");
                }
            }

        } else {
            System.out.println("user " + ID + " does not exist");
        }

	}
	private static void printError(String error)
	{
		System.out.println("Error: " + error);
	}
 }
