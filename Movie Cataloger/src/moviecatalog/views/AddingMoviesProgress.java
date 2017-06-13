/*Author: Shubham Mathur
 * Project: MovieCataloger
 * Description:This Dialog window shows Progress bar and adds movies found into catalog.
 * 
 * */
package moviecatalog.views;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import moviecatalog.common.Tools;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Toolkit;
import java.awt.Color;

public class AddingMoviesProgress extends JDialog {

	private JPanel contentPanel;
	private JProgressBar pgrsbr;
	private JLabel lblmovies;
	private JLabel lblprogress;
	private DefaultTableModel model;

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

	/**
	 * Create the dialog.
	 */
	public AddingMoviesProgress(DefaultTableModel model) {
		this.model = model;
		initComponents();
		addingmovies();
	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	private void initComponents() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(AddingMoviesProgress.class.getResource("/moviecatalog/resources/icon.png")));
		setTitle("Adding movies to catalog");
		setBounds(100, 100, 450, 150);
		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		pgrsbr = new JProgressBar();
		pgrsbr.setOpaque(true);
		pgrsbr.setBackground(Color.WHITE);

		lblmovies = new JLabel("Adding movies to catalog");
		lblmovies.setBackground(Color.WHITE);

		lblprogress = new JLabel("");
		lblprogress.setBackground(Color.WHITE);
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
	private void addingmovies() {
		updateProgressbar obj = new updateProgressbar(this);
		obj.setDaemon(true);
		obj.start();
	}
}

//////////////////////////////////////////////////////////////////////////////////
//// This class sub classes Thread class and works in background
//////////////////////////////////////////////////////////////////////////////////
class updateProgressbar extends Thread{
	AddingMoviesProgress obj;
	public updateProgressbar(AddingMoviesProgress obj) {//For updating Progress bar reference is needed of calling object
		this.obj=obj;
	}
	//Updating database and progress bar and status
	public void run(){
		Connection c;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+Tools.getDBNAME());
			String filefullpath;
			String[] movieinfo;
			List<String> languages;
			System.out.println("Opened database successfully");
			PreparedStatement instmt1 = c.prepareStatement("insert or ignore into LocalInfo values(?,?,?,?,?,?,?,?,?)");
			PreparedStatement instmt2 = c.prepareStatement("insert or ignore into LanguageInfo values(?,?)");
			int i = 0, n = obj.getModel().getRowCount();
			obj.getPgrsbr().setMaximum(n);//setting maximum of Progress Bar

			try {
				for (int counter = 0; counter < n; counter++) {
					if ((boolean) obj.getModel().getValueAt(counter, 0)) {
						filefullpath=(String) obj.getModel().getValueAt(counter, 1);
						movieinfo = Tools.getMovieDetails(filefullpath);	//getting meta data from video file 
						languages=Tools.getMovieLanguages(filefullpath); 	//getting languages from video file						
						instmt1.setString(8, "0");
						instmt1.setInt(9, 0);
						for (int j = 0; j < 7; j++)
							instmt1.setString(j + 1, movieinfo[j]);		
						instmt2.setString(1, filefullpath);
						if(languages.isEmpty())
							languages.add("N/A");	
						for(String language:languages)
						{
							instmt2.setString(2, language);
							instmt2.executeUpdate();		//Updating LanguageInfo
						}
						i += instmt1.executeUpdate();		//Updating LocalInfo				
						obj.setPgrsbr(obj.getPgrsbr().getValue()+1);						
						obj.setLblprogress(i+" Movies added");						
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,e);
			}
			System.out.println(i + " records inserted");
			c.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,e);
			System.exit(0);
		}
		obj.dispose();

	}
}