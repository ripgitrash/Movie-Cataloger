/*Author: Shubham Mathur
 * Project: MovieCataloger
 * Description:Back-end methods to retrieve data from tables, video files, and other supporting methods.
 * 
 * */

package moviecatalog.common;

import java.awt.Image;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.omertron.imdbapi.ImdbApi;
import com.omertron.imdbapi.ImdbException;
import com.omertron.imdbapi.model.ImdbMovieDetails;
import com.omertron.imdbapi.search.SearchObject;

import moviecatalog.common.*;
import moviecatalog.views.ScanFolders;

/**
 * @author Shubham Mathur
 *
 */
public class Tools {
	/**
	 * Returns String array containing video information 
	 * @param 
	 * @return String array
	 * 
	 * 0:Get approximate Title from file name
	 * 1:Full file path
	 * 2:Media Type
	 * 3:Total Runtime of the video file
	 * 4:Total File size
	 * 5:Resolution
	 * 6:Bit Rate
	 */
	public static String[] getMovieDetails(String FileName) {
		String []map=new String[7]; // A String array to store movie meta data 
		File file = new File(FileName);
		MediaInfo info = new MediaInfo();   //Using MediaInfo to get details
		info.Open(FileName);	//Opening file for MediaInfo
		map[0]=getName(file.getName().toString());  											
		map[1]=FileName;  
		map[2]=info.Get(MediaInfo.StreamKind.General, 0, "Format", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name); 
		map[3]= getDuration(info.Get(MediaInfo.StreamKind.General, 0, "Duration", MediaInfo.InfoKind.Text,
				MediaInfo.InfoKind.Name));
		map[4]= getFileSize(info.Get(MediaInfo.StreamKind.General, 0, "FileSize", MediaInfo.InfoKind.Text,
				MediaInfo.InfoKind.Name));
		map[5]=	info.Get(MediaInfo.StreamKind.Video, 0, "Width", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name)
						+ " X " + info.Get(MediaInfo.StreamKind.Video, 0, "Height", MediaInfo.InfoKind.Text,
								MediaInfo.InfoKind.Name);
		map[6]= getBitRate(info.Get(MediaInfo.StreamKind.General, 0, "OverallBitRate",
				MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name)); 
		return map;
		
	}
	
	/**
	 * Returns a List of languages from a video file.
	 * @param FileName
	 * @return List<String>
	 */
	public static List<String> getMovieLanguages(String FileName) {
		List<String> map = new ArrayList<String>(); // A String List to store Languages
		String temp;
		int i=0;
		File file = new File(FileName);
		MediaInfo info = new MediaInfo();   //Using MediaInfo to get details
		info.Open(FileName);	//Opening file for MediaInfo
		while(true)
		{
			temp= (new Locale(info.Get(MediaInfo.StreamKind.Audio, i++, "Language", MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name)).getDisplayLanguage());
			if(!temp.isEmpty())
				map.add(temp);
			else
				break;
		}
		return map;
	}
	
	/**
	 * @param
	 * @return Movie Size in MB
	 */
	public static long getMovieSize(String FileName) {
		File file = new File(FileName);
		return file.length()/(1024*1024);	
		
	}
	
	/**
	 *Extracting movie name from file name using regular expression 
	 * @param line
	 * @return
	 */
	public static String getName(String line) {
		line=line.substring(0, line.length()-3);
		line = line.replace('.', ' ').replace('_', ' ').toLowerCase();  // removing . and _ . converting to lower case
		/*pattern 1 : groups string into two groups, first one contains the desired words, second group contains any other words matching the expression
		 * group 2 checks for keywords, year
		 * pattern 2 : groups string into two groups, first one contains the desired words second one words in the brackets
		 * */
		String[] pattern = {
				"(.*?)(english|hindi|720p|1080p|dvdrip|hdrip|brip|bdrip|bluray|hdtv|x264|hdts|retail|xvid| cd[0-9]|dvdscr|brrip|divx|[\\[\\{\\(]{1}|([0-9]{4})(?=^\\w)).*",
				"(.*?)\\(.*\\)(.*)" };// for splitting into words like
										// BackToTheFuturePart2_eng
		Pattern r;
		Matcher m;
		for (int i = 0; i < 2; i++) {
			// Create a Pattern object
			r = Pattern.compile(pattern[i]);
			// Now create matcher object.
			m = r.matcher(line);
			if (m.find())// if match was found then store it
				line = m.group(1);
		}
		return line;
	}
	
	/**
	 *converting millisecond to hour minute and seconds. 
	 * @param Duration
	 * @return
	 */
	public static String getDuration(String Duration) {
		long duration = Long.parseLong(Duration);
		int h = (int) ((duration / 1000) / 3600);  // converted to hour
		int m = (int) (((duration / 1000) / 60) % 60); //converted to minutes and stores only under 60
		int s = (int) ((duration / 1000) % 60); //converted to seconds and stores only under 60
		Duration = String.format("%02d:%02d:%02d", h, m, s);
		return Duration;
	}
	
	/**
	 *Converting bytes to readable format 
	 * @param FileSize
	 * @return
	 */
	public static String getFileSize(String FileSize) {
		long size = Long.parseLong(FileSize);
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));  //taking log base to 1024 to get total digits to restrict into a range of MB GB etc 
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];  //converted to format and symbol concatenated.
	}
	/**
	 *converting bps to readable format 
	 * @param BitRate
	 * @return
	 */
	public static String getBitRate(String BitRate) {
		long size = Long.parseLong(BitRate);
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B/s", "kb/s", "mb/s" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1000));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1000, digitGroups)) + " " + units[digitGroups];
	}
	/** 
	 * @return Returns Database name
	 */
	public static String getDBNAME()
	{
		JSONParser parser = new JSONParser();
        try {
        		if(new File("Settings.json").exists())//Checking if settings file exists
        		{
        			Object obj = parser.parse(new FileReader("Settings.json"));
        			JSONObject jsonObject = (JSONObject) obj;
        			String temp=jsonObject.containsKey("DBNAME")?(String)jsonObject.get("DBNAME"):"MovieCatalog.db"; 
            		return temp;
        		}
        		else
        		{
        			FileWriter file;
        			try {
        				JSONObject obj=new JSONObject();
        				obj.put("DBNAME","MovieCatalog.db"); //create file and write database name
        				file = new FileWriter("Settings.json");
        				file.write(obj.toString());
        				file.flush();
        			} catch (Exception e) {
        				
        				e.printStackTrace();
        			}        			
        		}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "MovieCatalog.db";
	}
	/**
	 * Return FileFullPath of movie from database file 
	 * @param moviename
	 * @return
	 */
	public static String getFullPath(String moviename)
	{
		
		Connection c;
		PreparedStatement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			String sql = "select FileFullPath from LocalInfo where Title=?";
			stmt = c.prepareStatement(sql);
			stmt.setString(1, moviename);
			ResultSet res=stmt.executeQuery();
			if(res.isBeforeFirst() )//if result set is non empty
			{
				String temp=res.getString("FileFullPath");
				c.close();
				return temp;
			}			

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,e);
		}
		return null;
	}
	/**
	 * Return IMDBID of movie from database file
	 * @param moviename
	 * @return
	 */
	public static String getIMDBID(String moviename)
	{
		
		Connection c;
		PreparedStatement stmt;
		try {
			String filefullpath=getFullPath(moviename);
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			String sql = "select IMDBID from IMDBInfo where FileFullPath=?";		
			stmt = c.prepareStatement(sql);
			stmt.setString(1, filefullpath);
			ResultSet res=stmt.executeQuery();
			if(res.isBeforeFirst() )
			{
				
				String temp= res.getString("IMDBID");
				c.close();
				return temp;
			}		
			

		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(null,e);
		}
		return "";
	}
	/**
	 *Return Languages in space separated form from database file 
	 * @param filefullpath
	 * @return
	 */
	public static String getLanguage(String filefullpath)
	{
		
		Connection c;
		PreparedStatement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			String sql = "select * from LanguageInfo where FileFullPath=?";		
			stmt = c.prepareStatement(sql);
			stmt.setString(1, filefullpath);
			ResultSet res=stmt.executeQuery();
			String temp="";
			while(res.next())
			{
				
				temp=res.getString("Language")+" "+temp;
				
			}		
			c.close();
			return temp;
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(null,e);
		}
		return null;
	}
	/**
	 *Return Actors in , separated form from database file 
	 * @param IMDBID
	 * @return
	 */
	public static String getActor(String IMDBID)
	{
		
		Connection c;
		PreparedStatement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			String sql = "select * from ActorInfo where IMDBID=?";		
			stmt = c.prepareStatement(sql);
			stmt.setString(1, IMDBID);
			ResultSet res=stmt.executeQuery();
			String temp="";
			while(res.next())
			{
				
				temp=res.getString("Actor")+", "+temp;
				
			}		
			if(!temp.equals(""))
				temp=temp.substring(0, temp.length()-2);//remove trailing ,
			c.close();
			return temp;
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(null,e);
		}
		return null;
	}
	/**
	 *Return Genre in space separated form from database file 
	 * @param IMDBID
	 * @return
	 */
	public static String getGenre(String IMDBID)
	{
		
		Connection c;
		PreparedStatement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			String sql = "select * from GenreInfo where IMDBID=?";		
			stmt = c.prepareStatement(sql);
			stmt.setString(1, IMDBID);
			ResultSet res=stmt.executeQuery();
			String temp="";
			while(res.next())
			{
				
				temp=res.getString("Genre")+" "+temp;
				
			}		
			c.close();
			return temp;
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(null,e);
		}
		return null;
	}

	/**
	 * Checks if movie exists in catalog
	 * @param filefullpath
	 * @return
	 */
	public static boolean isMovieinCatalogue(String filefullpath)
	{
		
		Connection c;
		PreparedStatement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			String sql = "select * from LocalInfo where FileFullPath=?";		
			stmt = c.prepareStatement(sql);
			stmt.setString(1, filefullpath);
			ResultSet res=stmt.executeQuery();
			c.close();
			if(res.isBeforeFirst())	
				return true;			
			
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(null,e);
		}
		return false;
	}	
	/**
	 * Resize image to width and height
	 * @param img
	 * @param w
	 * @param h
	 * @return
	 */
	public static ImageIcon scaleImage(ImageIcon img,int w, int h)
	{		
		return new ImageIcon(img.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	/**
	 * Checking if Internet is working
	 * @return
	 */
	public static boolean netIsAvailable() {
	    try {
	         URL url = new URL("http://www.google.com");
	         URLConnection conn = url.openConnection();
	        conn.connect();
	        return true;
	    } catch (MalformedURLException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	        return false;
	    }
	}
	/**
	 *Check is database is empty 
	 * @return
	 */
	public static boolean isDBEmpty()
	{
		Connection c;
		Statement stmt;
		boolean ans=true;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			System.out.println("Opened database successfully");
			stmt = c.createStatement();			
			String sql = "select * from LocalInfo";
			ResultSet res=stmt.executeQuery(sql);
			ans= !res.isBeforeFirst();			
			stmt.close();
			c.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,e);
		}
		return ans;
	}
	/**
	 * Creates table if not exists
	 */
	public static void createTables()
	{
		Connection c;
		Statement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			//LocalInfo Table Creation
			String sql = "CREATE TABLE IF NOT EXISTS LocalInfo(" + "Title TEXT," + " FileFullPath TEXT,"
					+ " MediaType TEXT," + " Duration TEXT," + " FileSize TEXT, " +  "Resolution TEXT,"
					+ "BitRate TEXT,MyRating TEXT, Watched INTEGER, UNIQUE(FileFullPath))";
			stmt.executeUpdate(sql);
			//LanguageInfo Table Creation			
			sql = "CREATE TABLE IF NOT EXISTS LanguageInfo(FileFullPath TEXT,"+ "Language TEXT, UNIQUE(FileFullPath,Language))";
			stmt.executeUpdate(sql);
			//IMDBInfo Table Creation
			sql = "CREATE TABLE IF NOT EXISTS IMDBInfo(FileFullPath TEXT,"+ " IMDBID TEXT,"+ " Director TEXT,"
					+ " Year TEXT,"+ " Country TEXT,"+ "Rating TEXT,"+ " IMDBRating TEXT,"+ " Plot TEXT,"+" UNIQUE(FileFullPath))";
			stmt.executeUpdate(sql);
			//ActorInfo Table Creation
			sql = "CREATE TABLE IF NOT EXISTS ActorInfo(IMDBID TEXT, Actor TEXT, UNIQUE(IMDBID,Actor))";
			stmt.executeUpdate(sql);
			//GenreInfo Table Creation
			sql = "CREATE TABLE IF NOT EXISTS GenreInfo( IMDBID TEXT, Genre TEXT, UNIQUE(IMDBID,Genre))";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,e);
		}
	}
	/**
	 * Returns Rating of the Given IMDB Title
	 */		
	public static String getRating(String title) {
		String rating = null;
		Connection c;
		ResultSet res = null;
		PreparedStatement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			String sql = "select IMDBRating from IMDBInfo where FileFullPath in(select FileFullPath from LocalInfo where Title=?)";
			stmt = c.prepareStatement(sql);
			stmt.setString(1, title);
			res = stmt.executeQuery();
			while (res.next()) {
				rating = (res.getString("IMDBRating"));
			}
			res.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return rating;
	}
	/**
	 * Returns Year of the Given IMDB Title
	 */	
	public static String getYear(String title) {
		String year = null;
		Connection c;
		ResultSet res = null;
		PreparedStatement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			String sql = "select Year from IMDBInfo where FileFullPath in(select FileFullPath from LocalInfo where Title=?)";
			stmt = c.prepareStatement(sql);
			stmt.setString(1, title);
			res = stmt.executeQuery();
			while (res.next()) {
				year = (res.getString("Year"));
			}
			res.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return year;
	}	
	/**
	 * Returns File size of the movie
	 */	
	public static String getSize(String title) {
		String filesize = null;
		Connection c;
		ResultSet res = null;
		PreparedStatement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			String sql = "select FileSize from LocalInfo where Title=?";
			stmt = c.prepareStatement(sql);
			stmt.setString(1, title);
			res = stmt.executeQuery();
			while (res.next()) {
				filesize = (res.getString("FileSize"));
			}
			res.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return filesize;
	}

	/**
	 * Searches movie and returns topmost result
	 * @param moviename
	 * @return
	 */
	public static String searchOneMovie(String moviename) {
		ImdbApi imdbApi = new ImdbApi();
		try {
			Map<String, List<SearchObject>> searchResultMap = imdbApi.getSearch(moviename);
			List<SearchObject> result = searchResultMap.get("Search results");
			for (SearchObject so : result) {
				try {
					if (so instanceof ImdbMovieDetails && ((ImdbMovieDetails) so).getType().equals("feature"))//filtering movies only
						if (!((ImdbMovieDetails) so).getImdbId().isEmpty()) {   //picking the first element of search result
							return ((ImdbMovieDetails) so).getImdbId();
						}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
		return "";
	}
	
	/**Removes movie from catalog
	 * @param title
	 */
	public static void removeMovie(String title)
	{
		Connection c;
		PreparedStatement instmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			String filefullpath, IMDBid;					
			filefullpath = Tools.getFullPath(title);
			IMDBid = Tools.getIMDBID(title);

			// Remove from LocalInfo
			instmt = c.prepareStatement("delete from LocalInfo where Title=?");
			instmt.setString(1, title);
			instmt.execute();

			// Remove from IMDBInfo
			instmt = c.prepareStatement("delete from IMDBInfo where IMDBID=?");
			instmt.setString(1, IMDBid);
			instmt.execute();

			// Remove from GenreInfo
			instmt = c.prepareStatement("delete from GenreInfo where IMDBID=?");
			instmt.setString(1, IMDBid);
			instmt.execute();

			// Remove from ActorInfo
			instmt = c.prepareStatement("delete from ActorInfo where IMDBID=?");
			instmt.setString(1, IMDBid);
			instmt.execute();

			// Remove from LanguageInfo
			instmt = c.prepareStatement("delete from LanguageInfo where FileFullPath=?");
			instmt.setString(1, filefullpath);
			instmt.execute();

			//RemovingPoster 
			File file = new File("Posters\\" + IMDBid + ".jpg");
			file.delete();
			c.close();
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	/**
	 * Removes invalid entries from catalog. Invalid like file moved or renamed or deleted but still exists in catalog with old data
	 */
	public static void checkInvlaidEntries() {
		Connection c;
		Statement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			ResultSet res = stmt.executeQuery("select Title,FileFullPath from LocalInfo");
			int count = 0;
			List<String> obselete = new ArrayList<String>();
			while (res.next()) {
				File file = new File(res.getString("FileFullPath"));
				if (!file.exists()) {
					obselete.add(res.getString("Title")); //Adding invalid entries into list
					count++; //counting invalid entries
				}
			}
			c.close();
			for (String title : obselete) {
				System.out.println("Removing " + title);
				removeMovie(title);// deleting movies one by one
			}
			if (count > 0)
				JOptionPane.showMessageDialog(null, "Invalid entries removed " + count);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	/**
	 * Checks in the set folders if any new movies are available
	 */
	public static void checkNew()
	{
		ScanFolders obj=new ScanFolders();//calling scanfolders to get the list of the new movies
		obj.initmovielist();// populating the list
		int	count=obj.getTblmoviepath().getModel().getRowCount();// getting total number of entries
		if(count>0)// if any file found then show option to add them to catalog
		{
			int ret = JOptionPane.showConfirmDialog(null, count+" new movies found, Do you want to add them to collection?");
			if (ret == JOptionPane.YES_OPTION) {
				obj.setVisible(true);
				obj.setModal(true);
			}
		}
		
	}
}


