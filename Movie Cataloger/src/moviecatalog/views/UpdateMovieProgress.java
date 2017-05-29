/*Author: Shubham Mathur
 * Project: MovieCataloger
 * Description:This Dialog window shows progress of updating movie's IMDB information  
 * 
 * */
package moviecatalog.views;

import java.awt.BorderLayout;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.omertron.imdbapi.ImdbApi;
import com.omertron.imdbapi.model.ImdbCast;
import com.omertron.imdbapi.model.ImdbMovieDetails;

import moviecatalog.common.Tools;
import java.awt.Toolkit;

public class UpdateMovieProgress extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JProgressBar pgrsbr;
	private JLabel lblmovies;
	private JLabel lblprogress;

	private DefaultTableModel model;

	private boolean filter;

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public JProgressBar getPgrsbr() {
		return pgrsbr;
	}

	public void setPgrsbr(int i) {
		this.pgrsbr.setValue(i);
	}

	public JLabel getLblprogress() {
		return lblprogress;
	}

	public void setLblprogress(String text) {
		this.lblprogress.setText(text);
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public void setModel(DefaultTableModel model) {
		this.model = model;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the dialog.
	 */
	public UpdateMovieProgress(DefaultTableModel tblmodel, boolean filter) {
		
		this.model = tblmodel;
		this.filter = filter;
		initComponents();
		updatingMovies();
	}
	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	private void initComponents() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(UpdateMovieProgress.class.getResource("/moviecatalog/resources/icon.png")));
		setTitle("Updating movies information");
		setBounds(100, 100, 450, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		pgrsbr = new JProgressBar();

		lblmovies = new JLabel("Updating movies information");

		lblprogress = new JLabel("");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPanel.createSequentialGroup().addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(pgrsbr,
								GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
						.addGroup(gl_contentPanel.createSequentialGroup().addGap(146).addComponent(lblmovies))
						.addGroup(gl_contentPanel.createSequentialGroup().addGap(182).addComponent(lblprogress)))
						.addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup().addGap(20).addComponent(lblmovies)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblprogress)
						.addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE).addComponent(pgrsbr,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(23)));
		contentPanel.setLayout(gl_contentPanel);

	}
	//Creating a new daemon thread to update database and progress bar
	private void updatingMovies() {
		updateMovieProgressbar obj = new updateMovieProgressbar(this);
		obj.setDaemon(true);
		obj.start();
	}

}

//////////////////////////////////////////////////////////////////////////////////
////This class sub classes Thread class and works in background
//////////////////////////////////////////////////////////////////////////////////

class updateMovieProgressbar extends Thread {
	UpdateMovieProgress obj;
	private String filefullpath;

	public updateMovieProgressbar(UpdateMovieProgress obj) {
		//For updating Progress bar reference is needed of calling object
		this.obj = obj;

	}
	//Updating database and progress bar and status
	public void run() {
		int i = 0, n = obj.getModel().getRowCount();
		obj.getPgrsbr().setMaximum(n);
		for (int counter = 0; counter < n; counter++) {
			if ((boolean) obj.getModel().getValueAt(counter, 0)) {
				String moviename = (String) obj.getModel().getValueAt(counter, 1);
				filefullpath = Tools.getFullPath(moviename);
				if (obj.isFilter()) {
					String pattern = "(.*?)(dvd|hindi|eng|1080p|720|[0-9]{4}).*";  //removing year and few more keywords as aggressive filtering
					Pattern r;
					Matcher m;
					r = Pattern.compile(pattern);
					m = r.matcher(moviename);
					if (m.find())// if match was found then store it
						moviename = m.group(1);
					moviename=moviename.replace(" and ", " & "); 
				}
			try {
					String imdbid=Tools.searchOneMovie(moviename);
					if(!imdbid.isEmpty())
					{
						updateIMDBInfo(imdbid);	//Updating IMDBinfo
						obj.setPgrsbr(obj.getPgrsbr().getValue() + 1); //Progress bar updated
						obj.setLblprogress(++i + " Movies updated");	// Movies updated count updated
					}
					
				} catch (Exception e) {
				}
			

			}
		}
		obj.dispose();
		JOptionPane.showMessageDialog(null, i+" Movies Updated!");

	}
	
	//////////////////////////////////////////////////////////////////////////////////
	////Updates Databases
	//////////////////////////////////////////////////////////////////////////////////

	protected void updateIMDBInfo(String IMDBid) {

		Connection c;
		try {
			System.out.println("Update Invoked");
			ImdbApi imdbApi = new ImdbApi();
			ImdbMovieDetails movie =imdbApi.getFullDetails(IMDBid);			
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			System.out.println("Opened database successfully");
			ImageIcon icon = null;
			try{if (!movie.getImage().getUrl().isEmpty()) {//get poster image file and saves it if available.
				icon = new ImageIcon(ImageIO.read(new URL(movie.getImage().getUrl())));
				BufferedImage bimg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g = bimg.createGraphics();
				icon.paintIcon(null, g, 0, 0);
				g.dispose();
				File outputfile = new File("Posters\\" + IMDBid + ".jpg");
				ImageIO.write(bimg, "jpg", outputfile);
			}
			}catch(IIOException e1){}
			PreparedStatement instmt1;
			PreparedStatement instmt2;
			PreparedStatement instmt3;
			PreparedStatement instmt4;
			PreparedStatement instmt5;
			instmt1 = c.prepareStatement(
					"insert or ignore into IMDBInfo(FileFullPath,Director,Year,Country,Rating,IMDBRating,Plot,IMDBID) values(?,?,?,?,?,?,?,?)");
			instmt3 = c.prepareStatement("insert or ignore into ActorInfo(Actor,IMDBID) values(?,?)");
			instmt4 = c.prepareStatement("insert or ignore into GenreInfo(Genre,IMDBID) values(?,?)");
			instmt2 = c.prepareStatement("update LocalInfo set Title=? where FileFullPath=?");


			instmt1.setString(1, filefullpath);
			instmt1.setString(2, movie.getDirectors().get(0).getPerson().getName());
			instmt1.setString(3, movie.getYear()+"");
			instmt1.setString(4, "N/A");
			instmt1.setString(5, movie.getCertificate().get("certificate"));
			instmt1.setString(6, movie.getRating() + "");
			
			if(imdbApi.getTitlePlot(IMDBid).isEmpty())
				instmt1.setString(7, movie.getBestPlot().getOutline());
			else
				instmt1.setString(7, imdbApi.getTitlePlot(IMDBid).get(0).getText());
			instmt1.setString(8, IMDBid);

			//IMDBInfo updated
			try {
				instmt1.executeUpdate();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,e);
			}
			//LocalInfo updated
			instmt2.setString(1, movie.getTitle());
			instmt2.setString(2, filefullpath);

			try {
				instmt2.executeUpdate();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,e);
			}

			//ActorInfo updated
			instmt3.setString(2, IMDBid);
			for(ImdbCast ob:movie.getCast())
			{
				instmt3.setString(1, ob.getPerson().getName());
				try {
					instmt3.executeUpdate();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,e);
				}
			}
			
			//GenreInfo updated
			instmt4.setString(2, IMDBid);
			for (String x : movie.getGenres()) {
				instmt4.setString(1, x);
				try {
					instmt4.executeUpdate();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,e);
				}
			}
			//LanguageInfo updated
			/*if (Tools.getLanguage(filefullpath).isEmpty())//update language if not exists in database
			{
				String Languages[]=movie.getLanguage().split(",");
				instmt5 = c.prepareStatement("insert or ignore into LanguageInfo(Language,FileFullPath) values(?,?)");
				for(String x:Languages)
				{
					instmt5.setString(1, x.trim());
					instmt5.setString(2, filefullpath);
					try {
						instmt5.executeUpdate();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,e);
					}					
				}
			}*/

			c.close();

		} catch (Exception e) {
			//JOptionPane.showMessageDialog(null,e);

		}

	}

}