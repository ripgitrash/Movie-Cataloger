# MovieCataloger

> Movie Cataloger is a cataloging and organizing software developed for personal use only.
> It helps user to maintain their movie collection efficiently and in an organized way.
> It is capable to scan and search recursively the desired folders, drives for movies and adds them to catalog.
> It is capable of retrieving movie’s name and other important meta data like, language, runtime, quality etc from the file.
> It has the ability to directly fetch movie data from the internet movie database (IMDb).
> Users can see their collection of their movies in a single window despite having their actual files located across various drives and folders. 
> Users can mark those movies which they have watched. They can also give a rating for a movie. 
> The data fetched will help users to know more about that particular movie, by showing its poster, IMDb rating, genre, year of release, plot summary, etc like details.
> It can fetch the data for multiple movies from IMDB at once, and users can update movie details of individual movie as well.
> Users can play or copy movies, find their location on the drive and can search for a particular movies in the Catalog.
> Users can view movies category wise in the catalog, can view pie chart showing analysis of their collection.

## Screenshot

<p align="center">
  <img src="https://raw.githubusercontent.com/googleknight/MovieCataloger/master/Screenshots/MainWindow.JPG" alt="Screenshot"/>
</p>

## System Requirements
* Any 64-bit Windows OS
* [Java Runtime Environment 64-bit 1.8 or higher](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)

## Instructions to import project in Eclipse
* Download the repo, extract it.
* Create a New Java Project in Eclipse.
* Right Click your project and select Import, then under General choose File System, and then browse and select the extracted folder
* In Package Explorer expand your project and then expand res folder, select all JAR files and add them to Build Path.
* Right Click your Project and go to configure build path and select Libraries tab and add Class Folder and select lib folder in project.
* Run project by MainWindow.java

## Instructions to install the latest build.
* Download the [setup](https://raw.githubusercontent.com/googleknight/MovieCataloger/master/Setup/MovieCatalogerSetup.exe) and follow on screen instructions.
* ### Do not install it in C Drive, there it will not going to work properly.

## Instructions for first time users
* Run the program, and select Scan Folders first, then select Set Folders  and set your folders which contains movies ,not tutorials or tv series.
* Then select Add Movies and wait.
* Connect to internet and select batch update and choose Update selected and wait.
* Then select Update by Filtering movie name.
* Now update rest of the movies individually, and explore the software now. Your Catalog is ready.
* For any help checkout the help menu.


## Things that needs to be done in future
* Speed up the updation of movies from IMDB in batch update.
* Make it cross platform compatible.
* Import/export database and files.
* Write JUnit files for unit testing all methods.
* Improve documentation, add more comments wherever it is required.
* Find new bugs and fix them.
* Imrove design, imporve button names, etc.

### Major contributors  [Rishab Garg](http://rishabgarg.me), [Shubham Mathur](https://shubhammathur.me/).
### [Other contributors](https://github.com/googleknight/MovieCataloger/graphs/contributors)
#### Contributors are welcomed to contribute to this project, just fork this repo and raise a pull request.
