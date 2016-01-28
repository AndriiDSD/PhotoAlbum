All testing was done in eclipse. The program saves serialization data in PhotoAlbum01/data . All serializtion
is done in a file called users.ser which is created when the first user is created. If PhotoAlbum01/data folder does not exist it will be created.
The program accepts files from anywhere on the system. 

Graphical View:
The main class is GuiView and it is contained in cs213.photoAlbum.guiwiew package. This package contains
all gui related classes. When the program launches the login window shows up. The user can log in as admin
to see the list of existing users and to create/delete new users. If the users logs in with and existing 
user id then the user will be taken to interactive mode and a list of user albums will show up. Then the
user can delete, create, rename. Also, the user can search for photos and open an album. If the user clicks on search then the user is taken to search mode where the user can search for photos by tags and by a date range. After a search the user can create a new album from search results.
When the user opens an album the photos in that album are dispayed. The user can add photos to the current album. When adding a new photo a caption is not required. If a caption is not entered it will be 'Untiteled'. If a caption is provided the old one will  be replaced for existing photos. In open album the
user can also delete or move a photo. In addition, the user can click on a thumbnail and on display photo
button to start the slide show with the clicked thumbnail. In the slide show the details of the current 
photo are displayed. Then the user can add/remove tags to a photo or recaption a photo. Also, the user can go through all the photos in the album using forward/back buttons.

--->>> Extra Credit: 
The slide show window can be resized. When the slide show window resizes the image displayed is scaled.



Simple View:
In the commands that need the file name the path to the file should be entered (relative or absolute). The
reason for this is that on the file system there can be photos with same filename but different file path. For example the user could try to add data/file.jpg and then try
to add data2/file.jpg. The commands to manipulate them would specify the file path to distinguish between them(ex. listPhotoInfo "data2/file.jpg" should be run instead 
of listPhotoInfo "file.jpg" because the file names are the same but paths are unique). Thus the commands that need <fileName> will read the path to the photo file.



User Guide with examples that expalin how each command should be run correctly:
***************************************
Interactive Mode(logged in as user ah619):

createAlbum "<name>" - creates a new album for this user
Input:createAlbum "first"
Output: - if the new album does not exist yet
created album for user ah619:
first
Output: - if the album already exists
album exists for user ah619:
first


deleteAlbum "<name>" - deletes the specified album for this user
Input:deleteAlbum "first"
Output: - if user album was deleted
deleted album from user ah619:
first
Output: - if the specified album does not exist
album does not exist for user ah619:
first


listAlbums - lists all albums for this user
Input:listAlbums
Output: - if the user has albums with photos
Albums for user ah619
first number of photos: 2, 04/25/2014-06:22:01 - 07/13/2014-22:33:08
second number of photos: 2, 05/08/2014-20:49:07 - 07/01/2014-22:01:02
third number of photos: 2, 05/08/2014-20:39:30 - 07/13/2014-22:33:08
Output: - if this user has no albums
No albums exist for user ah619


listPhotos "name" - lists all photos in the specified album
Input:listPhotos "first"
Output: - if the album has photos in it
Photos for album first:
/home/andrii/workspace/PhotoAlbum01/data/wallpaper6.jpg - 04/25/2014-06:22:01
/home/andrii/workspace/PhotoAlbum01/data/wallpaper.jpg - 07/13/2014-22:33:08
Output: - if the album does not exist
Album first does not exist


addPhoto "<fileName>" "<caption>" "<albumName>" - adds a new photo to an album. If the photo already exists in a different 
album it's caption will be retained so the <caption> can be blank. <fileName> has to be absolute or relative file path to the photo file on
this system.
Input:addPhoto "data/wallpaper5.jpg" "circuit" "first"
Output: - if the photo is not in this album
Added photo data/wallpaper5.jpg:
circuit - Album: first
Output: - if the photo is already in this album
Photo data/wallpaper5.jpg already exists in album first 
Output: - if the album does not exist
Album first does not exist 
Output: - if the file does not exist
File data/wallpaper5.jpg does not exist


movePhoto "<fileName>" "<oldAlbumName>" "<newAlbumName>" - moves the photo specified by <fileName> from old album to 
new album. <fileName> is the absolute or relative path to the photo file. 
Input:movePhoto "data/wallpaper.jpg" "first" "second"
Output: - if the photo was moves successfully
Moved photo data/wallpaper.jpg
data/wallpaper.jpg - From album first to album second
Output: - if the old album does not exist
Album first does not exist 
Output: - if the new album does not exist
Album second does not exist 
Output: - if the photo is not in old album
Photo data/wallpaper.jpg does not exist in first
Output: - if the photo is already in the new album
none
Output: if the photo file does not exist
Photo data/wallpaper.jpg does not exist



removePhoto "<fileName>" "<albumName>" - removes photo from specified album. fileName is the absolute or relative path to the photo file on this
syste.
Input:removePhoto "data/wallpaper.jpg" "first"
Output: - if the photo was removed
Removed photo:
data/wallpaper.jpg - From album first
Output: - if the photo is not in this album
Photo data/wallpaper.jpg is not in album first




addTag "<fileName>" <tagType>:"<tagValue>" - adds a new tag to a photo. The <fileName> is the absolute or relative path to file on this system.
Input:addTag "data/wallpaper.jpg" location:"mountain"
Output: - if the photo did not have this tag
Added tag:
data/wallpaper.jpg location:mountain
Output: - if this photo already has this tag
Tag already exists for data/wallpaper.jpg location:mountain
  
 

deleteTag "<fileName>" <tagType>:"<tagValue>" - removes a tag from a specified photo
Input:deleteTag "data/wallpaper.jpg" weather:"cold"
Output: - if the tag was deleted
Deleted tag:
data/wallpaper.jpg weather:cold 
Output: - if the tag does not exist
Tag does not exist for data/wallpaper.jpg weather:cold
Output: - -if the photo does not exist
Photo data/wallpaper.jpg does not exist 


listPhotoInfo "<fileName>" - lists the info of the photo specified by its file path.
Input:listPhotoInfo "data/wallpaper.jpg"
Output: - if this photo exists
Photo file name: data/wallpaper.jpg
Album: "first", "third"
Date: 07/13/2014-22:33:08
Caption: mountains
Tags:
location:mountain
Output: - ig the photo does not exist
Photo data/wallpaper.jpg does not exist


getPhotosByDate <start date> <end date> - lists the photos within given range. The dates can be in the form MM/DD/YYYY or
MM/DD/YYYY-HH:MM:SS
Input:getPhotosByDate 03/01/2014 01/01/2015
Output:
Photos for user ah619 in range 03/01/2014 to 01/01/2015
aurora borealis - Album: first - Date:04/25/2014-06:22:01
stars - Album: third - Date:05/08/2014-20:39:30
wallpaper3 - Album: second - Date:05/08/2014-20:49:07



getPhotosByTag [<tagType>:]"<tagValue>" [,[<tagType>:]"<tagValue>"]... - lists the photos that have given tags
Input:getPhotosByTag location:"unspecified" location:"mountain"
Output:
Photos for user ah619 with tags  location:"unspecified" location:"mountain": 
aurora borealis - Album:first - Date: 04/25/2014-06:22:01
stars - Album:third - Date: 05/08/2014-20:39:30
wallpaper3 - Album:second - Date: 05/08/2014-20:49:07
circuit - Album:second - Date: 07/01/2014-22:01:02
mountains - Album:first, third - Date: 07/13/2014-22:33:08




logout - logs out the current user


***************************************
Command Mode:

listusers - lists currently existing users.

Input:java cs213.photoAlbum01.simpleview.CmdView listusers 
Output:   - if there exist users
ah619
NadiiaChepurko
Output:  - if there are no users
no users exist


adduser <user id> "user name" - creates a new user if the user does not exist yet. User Id cannot contain spaces.
Input:java cs213.photoAlbum.simpleview.CmdView adduser ah619 "Andrii Hlyvko" 
Output:created user ah619 with name Andrii Hlyvko  - if the new user was added successfully 
Output:user ah619 already exists with name Andrii Hlyvko  - if a user with this ID already exists


deleteuser <user id> - deletes the specified user.
Input:java cs213.photoAlbum.simpleview.CmdView deleteuser ah619
Output:deleted user ah619  - if deleted this user
Output:user ah619 does not exist - if this user does not exist


lodin <user id>
Input:java cs213.photoAlbum.simpleview.CmdView login ah619
Output: none - if login was successful
Output:user ah619 does not exist - if the user does not exist
