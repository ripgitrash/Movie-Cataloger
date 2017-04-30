package moviecatalog.views;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.omdbapi.MovieType;
import com.omdbapi.Omdb;
import com.omdbapi.OmdbConnectionErrorException;
import com.omdbapi.OmdbMovieNotFoundException;
import com.omdbapi.SearchResult;
import moviecatalog.common.Tools;
import javax.swing.JScrollPane;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.UIManager;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class SearchMovieInfo extends JDialog {
	private JButton btnSelect;
	private JButton btnCancel;
	private JScrollPane scrollPane;
	private String moviename;
	private int year=0;
	private JTable tblmovie;
	private JLabel lblStatus;
	private String IMDBId="0";

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getIMDBId() {
		return IMDBId;
	}

	public void setIMDBId(String iMDBId) {
		IMDBId = iMDBId;
	}

	public JTable getTblmovie() {
		return tblmovie;
	}

	public void setTblmovie(JTable tblmovie) {
		this.tblmovie = tblmovie;
	}
	public JLabel getLblStatus() {
		return lblStatus;
	}

	public void setLblStatus(JLabel lblStatus) {
		this.lblStatus = lblStatus;
	}

	public String getMoviename() {
		return moviename;
	}

	public void setMoviename(String moviename) {
		this.moviename = moviename;
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
	public SearchMovieInfo(String moviename,String year) {
		
		this.moviename=moviename; //initialize movie name from the calling Method. 
		if(!year.isEmpty())
			this.year=Integer.parseInt(year); //Initializing year for search by year method
		initComponents();
		searchIMDB();		
		createEvents();
		
	}
	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	private void initComponents() {		
		setIconImage(Toolkit.getDefaultToolkit().getImage(SearchMovieInfo.class.getResource("/moviecatalog/resources/icon.png")));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Select a movie title");
		setBounds(100, 100, 560, 400);
		
		btnSelect = new JButton("Select");
		
		
		btnCancel = new JButton("Cancel");
		
		
		scrollPane = new JScrollPane();
		
		lblStatus = new JLabel("");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblStatus, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
							.addComponent(btnSelect)
							.addGap(108)
							.addComponent(btnCancel)
							.addGap(150))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSelect)
						.addComponent(btnCancel)
						.addComponent(lblStatus))
					.addContainerGap())
		);
		
		tblmovie = new JTable();
		tblmovie.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblmovie.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblmovie.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "IMDb ID", "Poster", "Title" }) {
					Class[] columnTypes = new Class[] { String.class, ImageIcon.class, String.class };

					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					};

					boolean[] columnEditables = new boolean[] { false, false, false };

					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
		tblmovie.getColumnModel().getColumn(0).setPreferredWidth(70);
		tblmovie.getColumnModel().getColumn(1).setPreferredWidth(140);
		tblmovie.getColumnModel().getColumn(2).setPreferredWidth(320);
		((DefaultTableCellRenderer)tblmovie.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		DefaultTableCellRenderer renderer= new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);	//For making text in center
		tblmovie.getColumnModel().getColumn(2).setCellRenderer(renderer);
		tblmovie.setAutoCreateRowSorter(true);
		tblmovie.setRowHeight(190);
		scrollPane.setViewportView(tblmovie);
		
		getContentPane().setLayout(groupLayout);
		
	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method spawns a daemon thread to find movies and add results in table 
	//////////////////////////////////////////////////////////////////////////////////
	private void searchIMDB() {
		if(Tools.netIsAvailable())
		{			
			MyThread thread=new MyThread(this);
			thread.setDaemon(true);
			thread.start();			
		}
		else
		{	
			JOptionPane.showMessageDialog(null, "Check Your Internet Connection.");
			dispose();			
		
		}
		
	}
	//////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	//////////////////////////////////////////////////////////////
	private void createEvents() {
		
		//Save IMDBID and exit the dialog box
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model=(DefaultTableModel)tblmovie.getModel();
				if(tblmovie.getSelectedRow()>=0)
				{
					IMDBId=(String)model.getValueAt(tblmovie.getSelectedRow(),0);
					dispose();
				}
				else
					JOptionPane.showMessageDialog(null, "Please select one movie from the list.");
				
			}
		});
		//Exit the dialog box
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}

//////////////////////////////////////////////////////////////////////////////////
////This class sub classes Thread class and works in background
//////////////////////////////////////////////////////////////////////////////////
class MyThread extends Thread{
	private SearchMovieInfo obj;
	public MyThread(SearchMovieInfo obj) {
		this.obj=obj;
	}
	public void run()
	{
		obj.getLblStatus().setText("Searching.....");
		try {
			DefaultTableModel tblmodel = (DefaultTableModel) obj.getTblmovie().getModel();
			List<SearchResult> search ;
			if(obj.getYear()!=0) //if year is there search by year otherwise don't use year.
				search= new Omdb().year(obj.getYear()).type(MovieType.movie).search(obj.getMoviename().trim());
			else
				search = new Omdb().type(MovieType.movie).search(obj.getMoviename().trim());
			for(SearchResult x:search)	//Add the search results into table
			{
				if(x.getPoster().equals("N/A"))
					tblmodel.addRow(new Object[]{x.getImdbID(),Tools.scaleImage(new ImageIcon("Posters\\Default.jpg"),140,185),x.getTitle()+" ("+x.getYear()+")"});
				else
					tblmodel.addRow(new Object[]{x.getImdbID(),Tools.scaleImage(new ImageIcon(ImageIO.read(new URL(x.getPoster().trim()))),140,185),x.getTitle()+" ("+x.getYear()+")"});
				
			}
			obj.getLblStatus().setText("Search Completed");
		}
		catch(OmdbMovieNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
			obj.dispose();
			
		}	
		catch(OmdbConnectionErrorException e1)
		{
			JOptionPane.showMessageDialog(null, e1.getMessage());
			obj.dispose();
			
		}
		catch (Exception e) {
			
			JOptionPane.showMessageDialog(null,e);
		}
	}
}
