/*Author: Shubham Mathur
 * Project: MovieCataloger
 * Description:This Dialog window shows list of movies found on IMDB for a given movie name and allows user to select one. 
 * */

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
import com.omertron.imdbapi.ImdbApi;
import com.omertron.imdbapi.ImdbException;
import com.omertron.imdbapi.model.ImdbMovieDetails;
import com.omertron.imdbapi.search.SearchObject;

import moviecatalog.common.Tools;
import javax.swing.JScrollPane;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
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
	private int poster=0;

	public int getPoster() {
		return poster;
	}

	public void setPoster(int poster) {
		this.poster = poster;
	}

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
	
	//Delete poster images which are not required
	private void deletetempfiles(int index)
	{
		while(poster>=0)
		{
			File file = new File(poster+".jpg");
			if (file.exists() && poster!=index)
				file.delete();
			poster--;
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
				int index=tblmovie.getSelectedRow();
				if(index>=0)
				{
					IMDBId=(String)model.getValueAt(tblmovie.getSelectedRow(),0);
					deletetempfiles(index);//delete other temporary poster images except the selected 
					File file = new File(index+".jpg");
					if (file.exists())
						poster=index;
					else
						poster=-1; //if selected one does not have poster 
					dispose();
				}
				else
					JOptionPane.showMessageDialog(null, "Please select one movie from the list.");
				
			}
		});
		//Exit the dialog box
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletetempfiles(-1);
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
	private int temp=0;
	public MyThread(SearchMovieInfo obj) {
		this.obj=obj;
	}
	//create an image file for each result
	public void writeimage(URL imgurl) throws Exception
	{
		ImageIcon icon = new ImageIcon(ImageIO.read(imgurl));
		BufferedImage bimg = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bimg.createGraphics();
		icon.paintIcon(null, g, 0,0);
		g.dispose();
		File outputfile= new File((temp++)+".jpg");
		ImageIO.write(bimg, "jpg", outputfile);
	}
	public void run()
	{
		obj.getLblStatus().setText("Searching.....");
		try {
		
			//New IMDBAPI code added
			String title,url,imdbid;
			ImdbApi imdbApi = new ImdbApi();
			DefaultTableModel tblmodel = (DefaultTableModel) obj.getTblmovie().getModel();
			Map<String, List<SearchObject>> searchResultMap =null;
			try{				
				searchResultMap = imdbApi.getSearch(obj.getMoviename().trim());
			}catch (ImdbException e) {
				JOptionPane.showMessageDialog(null,"No movie found");
				e.printStackTrace();
				obj.dispose();
			}
			List<SearchObject> result = searchResultMap.get("Search results");
			if(searchResultMap.isEmpty())
				JOptionPane.showMessageDialog(null,"No movie found");
			for (SearchObject so : result) {
				try{
				if (so instanceof ImdbMovieDetails && ((ImdbMovieDetails) so).getType().equals("feature")) {
					if(!so.getImage().getUrl().isEmpty())
					{						
						if(obj.getYear()!=0)
							if(obj.getYear()!=((ImdbMovieDetails) so).getYear())
								continue;
						imdbid=((ImdbMovieDetails) so).getImdbId();
						title=((ImdbMovieDetails) so).getTitle() +"("+((ImdbMovieDetails) so).getYear()+")";
						url=so.getImage().getUrl();
						writeimage(new URL(url.trim()));//create image and then show						
						tblmodel.addRow(new Object[]{imdbid,Tools.scaleImage(new ImageIcon((temp-1)+".jpg"),140,185),title});						
					}
					if(so.getImage().getUrl().isEmpty() )
					{
						if(obj.getYear()!=0)
							if(obj.getYear()!=((ImdbMovieDetails) so).getYear())
								continue;
						imdbid=((ImdbMovieDetails) so).getImdbId();
						title=((ImdbMovieDetails) so).getTitle() +"("+((ImdbMovieDetails) so).getYear()+")";
						temp++;
						tblmodel.addRow(new Object[]{imdbid,Tools.scaleImage(new ImageIcon("Posters\\Default.jpg"),140,185),title});
					}
				
				}
				}
				catch(NullPointerException e)
				{}
				catch(IIOException e2)
				{}
				obj.setPoster(temp);
			}
			
			//OLD OMDB CODE
			
			/*List<SearchResult> search ;
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
				
			}*/
			obj.getLblStatus().setText("Search Completed");
		}
		catch (Exception e) {
			
			JOptionPane.showMessageDialog(null,e);
		}
	}
}
