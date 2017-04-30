package moviecatalog.views;

import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.omdbapi.Movie;
import com.omdbapi.Omdb;
import moviecatalog.common.Tools;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;
import java.awt.Toolkit;

public class UpdateMovieDetails extends JDialog {
	
	private String filefullpath;
	private String IMDBid="0";
	private String OldIMDBid="0";
	
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtTitle;
	private JTextField txtDirector;
	private JTextField txtYear;
	private JTextField txtLanguage;
	private JTextField txtCountry;
	private JTextField txtRating;
	private JTextField txtIMDB;
	private JCheckBox chckbxWatched;
	private JSpinner spnMyRating;
	private JTextArea txtAreaActors;
	private JTextArea txtAreaPlot;
	private JLabel lblposter;
	private JButton btnSearch;
	private JScrollPane slpGenre;
	private JTextArea txtAreaGenre;
	private JScrollPane slpPlot;
	private JScrollPane slpActors;
	private JLabel lblSummary;
	private JLabel lblGenre;
	private JLabel lblTitle;
	private JLabel lblDirector;
	private JLabel lblYear;
	private JLabel lblLanguage;
	private JLabel lblCountry;
	private JLabel lblRating;
	private JLabel lblImdbRating;
	private JLabel lblMyRating;
	private JLabel lblActors;
	private JButton btnSave;
	private JButton btnCancel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public UpdateMovieDetails(String filefullpath) {
		
		this.filefullpath=filefullpath;
		
		initComponents();
		loadMovieDetails();		
		createEvents();
	}
	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	
	private void initComponents() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(UpdateMovieDetails.class.getResource("/moviecatalog/resources/icon.png")));
		setTitle("Update Movie Details");
			
		setBounds(100, 100, 750, 533);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		lblTitle = new JLabel("Title");
		
		lblDirector = new JLabel("Director");
		
		lblYear = new JLabel("Year");
		
		lblLanguage = new JLabel("Language");
		
		lblCountry = new JLabel("Country");
		
		lblRating = new JLabel("Rating");
		
		lblImdbRating = new JLabel("IMDB rating");
		
		lblMyRating = new JLabel("My Rating");
		
		lblActors = new JLabel("Actors");
		
		txtTitle = new JTextField();
		txtTitle.setColumns(10);
		
				
		txtDirector = new JTextField();
		txtDirector.setEditable(false);
		txtDirector.setColumns(10);
		
		txtYear = new JTextField();
		txtYear.setEditable(false);
		txtYear.setColumns(10);
		
		txtLanguage = new JTextField();
		txtLanguage.setEditable(false);
		txtLanguage.setColumns(10);
		
		txtCountry = new JTextField();
		txtCountry.setEditable(false);
		txtCountry.setColumns(10);
		
		txtRating = new JTextField();
		txtRating.setEditable(false);
		txtRating.setColumns(10);
		
		txtIMDB = new JTextField();
		txtIMDB.setEditable(false);
		txtIMDB.setColumns(10);
		
		
		chckbxWatched = new JCheckBox("Watched");
		
		
		spnMyRating = new JSpinner();
		spnMyRating.setModel(new SpinnerNumberModel(0, 0, 10, 1));
		
		
		lblSummary = new JLabel("Plot Summary");
		
		
		lblGenre = new JLabel("Genre");
		
		btnSearch = new JButton("Search");
		
		lblposter = new JLabel("");
		
		slpGenre = new JScrollPane();
		slpGenre.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		slpPlot = new JScrollPane();
		slpPlot.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		slpPlot.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		slpActors = new JScrollPane();
		slpActors.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		btnSave = new JButton("Save");
		
		
		btnCancel = new JButton("Cancel");
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPanel.createSequentialGroup()
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(lblTitle, Alignment.LEADING)
												.addComponent(lblDirector, Alignment.LEADING)
												.addComponent(lblYear, Alignment.LEADING)
												.addComponent(lblLanguage, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
											.addComponent(lblCountry, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
										.addGap(28))
									.addGroup(gl_contentPanel.createSequentialGroup()
										.addComponent(lblRating)
										.addGap(44)))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblImdbRating)
										.addComponent(lblMyRating)
										.addComponent(lblActors))
									.addGap(18)))
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
									.addComponent(slpActors, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
									.addGroup(gl_contentPanel.createSequentialGroup()
										.addComponent(spnMyRating, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
										.addComponent(chckbxWatched))
									.addComponent(txtIMDB, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
									.addComponent(txtTitle, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
									.addComponent(txtDirector)
									.addComponent(txtYear)
									.addComponent(txtLanguage)
									.addComponent(txtCountry)
									.addComponent(txtRating)))
							.addGap(22)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblSummary, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblGenre))
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(27)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(slpPlot, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
										.addComponent(slpGenre, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(96)
									.addComponent(lblposter, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)))
							.addGap(24))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(btnSave)
							.addGap(18)
							.addComponent(btnCancel)
							.addGap(60))))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTitle)
								.addComponent(txtTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSearch))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDirector)
								.addComponent(txtDirector, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(24)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblYear)
								.addComponent(txtYear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblLanguage)
								.addComponent(txtLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCountry)
								.addComponent(txtCountry, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(lblposter, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblRating)
								.addComponent(txtRating, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblGenre))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblImdbRating)
								.addComponent(txtIMDB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblMyRating)
								.addComponent(spnMyRating, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxWatched)))
						.addComponent(slpGenre, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(slpPlot, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(slpActors, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblSummary))
						.addComponent(lblActors))
					.addPreferredGap(ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		
		txtAreaActors = new JTextArea();
		txtAreaActors.setEditable(false);
		slpActors.setViewportView(txtAreaActors);
		txtAreaActors.setLineWrap(true);
		txtAreaActors.setWrapStyleWord(true);
		
		
		txtAreaPlot = new JTextArea();
		txtAreaPlot.setWrapStyleWord(true);
		txtAreaPlot.setEditable(false);
		slpPlot.setViewportView(txtAreaPlot);
		txtAreaPlot.setLineWrap(true);
		
		txtAreaGenre = new JTextArea();
		txtAreaGenre.setWrapStyleWord(true);
		txtAreaGenre.setEditable(false);
		txtAreaGenre.setLineWrap(true);
		slpGenre.setViewportView(txtAreaGenre);
		contentPanel.setLayout(gl_contentPanel);
		
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//// It searches for movie information in database and load the Dialog Components with the data
	///////////////////////////////////////////////////////////////////////////////////////////////////	
	private void loadMovieDetails() {
		Connection c;
		try {
			System.out.println("Loading invoked");
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
				System.out.println("Opened database successfully");
				PreparedStatement instmt1=c.prepareStatement("select * from LocalInfo where FileFullPath=?");
				PreparedStatement instmt2=c.prepareStatement("select * from IMDBinfo where FileFullPath=?");				
				
				instmt1.setString(1, filefullpath);
				instmt2.setString(1, filefullpath);
				ResultSet res=instmt1.executeQuery();
				if(res.isBeforeFirst() )    // if res is non empty
				{
					txtTitle.setText(res.getString("Title"));					
					spnMyRating.setValue(Integer.parseInt(res.getString("MyRating")));
					chckbxWatched.setSelected(res.getInt("Watched")==1?true:false);					
				}				
				res=instmt2.executeQuery();
				if(res.isBeforeFirst() )    // if res is non empty
				{
					txtDirector.setText(res.getString("Director"));
					txtYear.setText(res.getString("Year"));
					txtCountry.setText(res.getString("Country"));
					txtRating.setText(res.getString("Rating"));
					txtIMDB.setText(res.getString("IMDBRating"));
					txtAreaPlot.setText(res.getString("Plot"));
					IMDBid=res.getString("IMDBID");
					OldIMDBid=res.getString("IMDBID");
				}
				
				txtLanguage.setText(Tools.getLanguage(filefullpath));				
				txtAreaGenre.setText(Tools.getGenre(IMDBid));				
				txtAreaActors.setText(Tools.getActor(IMDBid));
			
				File file=new File("Posters\\"+IMDBid+".jpg");
				if(file.exists())
					lblposter.setIcon(Tools.scaleImage(new ImageIcon(file.getPath()),150, 200));
				else	
					lblposter.setIcon(Tools.scaleImage(new ImageIcon("Posters\\Default.jpg"),150, 200));
				c.close();				
			}
		
		catch(Exception e)
			{
				
				JOptionPane.showMessageDialog(null,e);
			}		
	}	
	//////////////////////////////////////////////////////////////////////
	//// It update Dialog components by searching IMDB for updated IMDBID
	//////////////////////////////////////////////////////////////////////
	private void loaddetails() {
		try {
			if(!IMDBid.equals("0"))
			{
				Movie movie=new Omdb().fullPlot().getById(IMDBid);
				txtTitle.setText(movie.getTitle());
				txtDirector.setText(movie.getDirector());
				txtYear.setText(movie.getYear());
				txtLanguage.setText(movie.getLanguage());
				txtCountry.setText(movie.getCountry());
				txtRating.setText(movie.getRated());
				txtIMDB.setText(movie.getImdbRating()+"");
				txtAreaActors.setText(movie.getActors().toString());
				txtAreaPlot.setText(movie.getPlot());
				File file=new File("Posters\\"+IMDBid+".jpg");
				if(file.exists())
					file.delete();
				if(!movie.getPoster().equals("N/A"))
					lblposter.setIcon(Tools.scaleImage(new ImageIcon(ImageIO.read(movie.getPosterURL())),150,200));
				txtAreaGenre.setText(movie.getGenres().toString());
			}
			
		} 
		catch (Exception e) {
		
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	
		
	}
	//////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	//////////////////////////////////////////////////////////////
	private void createEvents() {
		
		//Search and update components.
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SearchMovieInfo obj=new SearchMovieInfo(txtTitle.getText(),txtYear.getText());
				IMDBid="0";
				obj.setModal(true);
				obj.setVisible(true);
				if(!obj.getIMDBId().equals("0"))
				{
					txtYear.setEditable(false);
					IMDBid=obj.getIMDBId();
				}
				else
				{
					txtYear.setEditable(true);
					JOptionPane.showMessageDialog(null, "Please enter correct movie name or enter year to find movie information online.");
				}
				loaddetails();
			}			
		});
		
		//Cancel Button	
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		//Save work and exit.
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateIMDBInfo();
				dispose();			
			}
		});
		
		
	}
	
	//////////////////////////////////////////////////////////////
	//// This method updates Database and posters.
	//////////////////////////////////////////////////////////////

	protected void updateIMDBInfo() {
		Connection c;
		try {
			System.out.println("Update Invoked");
			
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			System.out.println("Opened database successfully");
			
			Movie movie=new Omdb().getById(IMDBid);
			ImageIcon icon=null;
			if(!movie.getPoster().equals("N/A"))
			{
				icon=new ImageIcon(ImageIO.read(movie.getPosterURL()));
				BufferedImage bimg = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics g = bimg.createGraphics();
				icon.paintIcon(null, g, 0,0);
				g.dispose();
				File outputfile= new File("Posters\\"+IMDBid+".jpg");
				ImageIO.write(bimg, "jpg", outputfile);
			}
			
			PreparedStatement instmt = c.prepareStatement("select * from IMDBInfo where FileFullPath=?");
			PreparedStatement instmt1;
			PreparedStatement instmt2;
			PreparedStatement instmt3;
			PreparedStatement instmt4;
			PreparedStatement instmt5;
			instmt.setString(1, filefullpath);
			ResultSet res=instmt.executeQuery();
			if(!res.isBeforeFirst() ) //Insert if not exists
			{
				System.out.println("insertion invoked");
				instmt1 = c.prepareStatement("insert or ignore into IMDBInfo(IMDBID,Director,Year,Country,Rating,IMDBRating,Plot,FileFullPath) values(?,?,?,?,?,?,?,?)");
				instmt3 = c.prepareStatement("insert or ignore into ActorInfo(Actor,IMDBID) values(?,?)");
				instmt4 = c.prepareStatement("insert or ignore into GenreInfo(Genre,IMDBID) values(?,?)");
			}
			else	//Update if it is there
			{
				System.out.println("updation invoked");
				if(IMDBid.equals(res.getString("IMDBID"))) // If old and updated IMDBID are same then all other data will be same, so skip update.
				{	
					res.close();
					c.close();
					return;
				}
				instmt1 = c.prepareStatement("update IMDBInfo set IMDBID=?,Director =?, Year =?, Country =?,Rating =?,IMDBRating =?,Plot =? where FileFullPath=?");

				// Removing incorrect data from GenreInfo
				instmt = c.prepareStatement("delete from GenreInfo where IMDBID=?");
				instmt.setString(1, IMDBid);
				instmt.execute();

				// Removing incorrect data from ActorInfo
				instmt = c.prepareStatement("delete from ActorInfo where IMDBID=?");
				instmt.setString(1, IMDBid);
				instmt.execute();
				
				//Inserting new values
				instmt3 = c.prepareStatement("insert or ignore into ActorInfo(Actor,IMDBID) values(?,?)");
				instmt4 = c.prepareStatement("insert or ignore into GenreInfo(Genre,IMDBID) values(?,?)");
			}
			
			instmt2 = c.prepareStatement("update LocalInfo set Title=?,MyRating=?, Watched=? where FileFullPath=?");
			instmt5=c.prepareStatement("update LanguageInfo set Language=? where FileFullPath=?");
			
			instmt1.setString(1, IMDBid);			
			instmt1.setString(2, txtDirector.getText());
			instmt1.setString(3, txtYear.getText());
			instmt1.setString(4, txtCountry.getText());
			instmt1.setString(5, txtRating.getText());
			instmt1.setString(6, txtIMDB.getText());
			instmt1.setString(7, txtAreaPlot.getText());
			instmt1.setString(8, filefullpath);
			
			//IMDBInfo updated
			try{
			
				instmt1.executeUpdate();
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null,e);
			}
			
			instmt2.setString(1, txtTitle.getText());
			instmt2.setString(2, spnMyRating.getValue().toString());
			instmt2.setInt(3, (chckbxWatched.isSelected()) ? 1 : 0);
			instmt2.setString(4, filefullpath);
			
			//LocalInfo updated
			try{			
				instmt2.executeUpdate();
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null,e);
			}
			
			//ActorInfo updated
			instmt3.setString(2, IMDBid);
			for(String x:movie.getActors())
			{
				instmt3.setString(1, x);
				try{
					
					instmt3.executeUpdate();
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null,e);
				}
			}				
			
			//GenreInfo updated
			instmt4.setString(2, IMDBid);
			for(String x:movie.getGenres())
			{
				instmt4.setString(1, x);
				try{
					
					instmt4.executeUpdate();
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null,e);
				}
			}
			
			if(!IMDBid.equals(OldIMDBid))	//Update Movie Information only after IMDBID is changed
			{
				if(Tools.getMovieLanguages(filefullpath).isEmpty())
				{
					String Languages[]=movie.getLanguage().split(","); //getting languages from IMDB and saving it in database
					if(Tools.getLanguage(filefullpath).isEmpty())
						instmt5 = c.prepareStatement(
								"insert or ignore into LanguageInfo(Language,FileFullPath) values(?,?)");
					else
						instmt5 = c.prepareStatement("update LanguageInfo set Language=? where FileFullPath=?");
					for (String x : Languages) {
						instmt5.setString(1, x.trim());
						instmt5.setString(2, filefullpath);
						try {
							instmt5.executeUpdate();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			c.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);

		}

	}

}
