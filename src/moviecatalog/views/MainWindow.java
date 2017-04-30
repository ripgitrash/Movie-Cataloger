package moviecatalog.views;
import java.awt.Desktop;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import moviecatalog.common.Tools;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow extends JFrame {

	//All the Swing Components declaration
	private JPanel contentPane;
	private JTable tblmovie;
	private JScrollPane slptable;
	private JMenuItem mntmExit;
	private JButton btnScanFolders;
	private JButton btnPlay;
	private JButton btnOpenContiningFolder;
	private JButton btnUpdateMovieDetails;
	private JButton btnRemoveMovieFrom;
	private JLabel lblTotalMovies;
	private JLabel lblPoster;
	private JPanel pnlMovieDetails;
	private JPanel pnlLocalInfo;
	private JFileChooser chooser;
    private JLabel lblFileFormat;
	private JLabel dataMediaType;
	private JLabel lblFilSize;
	private JLabel dataFileSize;
	private JLabel lblResolution;
	private JLabel lblBitRate;
	private JLabel lblDuration;
	private JLabel dataResolution;
	private JLabel dataBitRate;
	private JLabel dataDuration;
	private JLabel lblLanguage;
	private JLabel dataTitle;
	private JPanel pnlIMDB;
	private JLabel lblYear;
	private JLabel lblImdbRating;
	private JLabel lblMpaa;
	private JLabel lblCountry;
	private JLabel lblDirector;
	private JLabel lblGenre;
	private JLabel lblActor;
	private JLabel lblPlot;
	private JLabel dataYear;
	private JLabel dataIMDBRating;
	private JLabel dataRating;
	private JLabel dataCountry;
	private JLabel dataDirector;
	private JScrollPane slpPlot;
	private JLabel dataActor;
	private JLabel dataGenre;
	private JTextArea txtAreaPlot;
	private JLabel lblMyRating;
	private JLabel dataMyRating;
	private JScrollPane slpdataLanguage;
	private JTextArea dataAreaLanguage;
	private JButton btnBatchUpdate;
	private JLabel lblMovieNameSearch;
	private JTextField txtSearch;
	private TableRowSorter<TableModel> rowSorter;
	private JPanel pnlTitle;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnViewBy;
	private JMenuItem mntmByRating;
	private JMenuItem mntmByLanguage;
	private JMenuItem mntmByYear;
	private JMenu mnHelp;
	private JMenuItem mntmAboutUs;
	private JButton btnSelectDestination;
	private JLabel lblWatched;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());// Default theme to application
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.getStackTrace()[0].getLineNumber();
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		
		Tools.createTables();
		initComponents();
		createEvents();
	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	private void initComponents() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/moviecatalog/resources/icon.png")));
		setTitle("Movie Cataloger");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);// by default maximized
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 5, 1250, 720);// (xpos,ypos,width,height)

		/////////////////////////////////////////////////////////////////////////////
		// Menu and its components
		/////////////////////////////////////////////////////////////////////////////

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mnViewBy = new JMenu("Analyze Collection");
		menuBar.add(mnViewBy);
		
		mntmByLanguage = new JMenuItem("By Language");
	    mnViewBy.add(mntmByLanguage);
		
		mntmByYear = new JMenuItem("By Year");
		mnViewBy.add(mntmByYear);
		
		mntmByRating = new JMenuItem("By Genre");
		mnViewBy.add(mntmByRating);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmAboutUs = new JMenuItem("About Us");
		mnHelp.add(mntmAboutUs);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		/////////////////////////////////////////////////////////////////////////////
		// Loading Buttons , labels, text boxes on Main Window frame and their properties
		///////////////////////////////////////////////////////////////////////////// 

		slptable = new JScrollPane();

		btnScanFolders = new JButton("Scan Folders");
		btnScanFolders.setToolTipText("Click to scan folders for movie");

		btnPlay = new JButton("Play");
		btnPlay.setToolTipText("Play the movie");

		btnOpenContiningFolder = new JButton("Open Contining Folder");
		btnOpenContiningFolder.setToolTipText("Open folder containing selected movie");

		btnUpdateMovieDetails = new JButton("Update Movie details");
		btnUpdateMovieDetails.setToolTipText("Select a movie from the list and Update its details from Internt");

		btnRemoveMovieFrom = new JButton("Remove Movie");
		btnRemoveMovieFrom.setToolTipText("Remove Movie from Catalogue");

		lblTotalMovies = new JLabel("Total Movies:0");

		pnlMovieDetails = new JPanel();
		pnlMovieDetails.setVisible(false);

		btnBatchUpdate = new JButton("Batch Update");
		btnBatchUpdate.setToolTipText("Update many movies data at once");

		lblMovieNameSearch = new JLabel("Movie name to search");

		txtSearch = new JTextField();
		txtSearch.setToolTipText("Enter Movie Name to search");
		txtSearch.setColumns(10);
		btnSelectDestination = new JButton("Select Destination And Copy");
		
		lblWatched = new JLabel("");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblTotalMovies)
							.addGap(213)
							.addComponent(lblWatched))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblMovieNameSearch)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(txtSearch, 250, 250, 250))
								.addComponent(slptable, GroupLayout.PREFERRED_SIZE, 391, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(pnlMovieDetails, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnScanFolders)
									.addGap(40)
									.addComponent(btnUpdateMovieDetails)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnBatchUpdate)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnRemoveMovieFrom)
									.addGap(18)
									.addComponent(btnPlay)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnOpenContiningFolder)
									.addGap(17)
									.addComponent(btnSelectDestination)))))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblMovieNameSearch)
							.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnScanFolders)
							.addComponent(btnUpdateMovieDetails)
							.addComponent(btnBatchUpdate)
							.addComponent(btnRemoveMovieFrom)
							.addComponent(btnPlay)
							.addComponent(btnOpenContiningFolder))
						.addComponent(btnSelectDestination))
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(slptable, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
						.addComponent(pnlMovieDetails, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTotalMovies)
						.addComponent(lblWatched)))
		);

		/////////////////////////////////////////////////////////////////////////////
		// Side panel of movie information and its components and properties
		/////////////////////////////////////////////////////////////////////////////
		lblPoster = new JLabel("");
		lblPoster.setToolTipText("Click to view Poster");

		pnlLocalInfo = new JPanel();

		pnlTitle = new JPanel();

		GroupLayout gl_pnlMovieDetails = new GroupLayout(pnlMovieDetails);
		gl_pnlMovieDetails.setHorizontalGroup(gl_pnlMovieDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlMovieDetails.createSequentialGroup()
						.addGroup(gl_pnlMovieDetails.createParallelGroup(Alignment.LEADING)
								.addComponent(pnlLocalInfo, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPoster, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(pnlTitle, GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)));
		gl_pnlMovieDetails.setVerticalGroup(gl_pnlMovieDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlMovieDetails.createSequentialGroup().addGroup(gl_pnlMovieDetails
						.createParallelGroup(Alignment.LEADING)
						.addComponent(pnlTitle, GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
						.addGroup(gl_pnlMovieDetails.createSequentialGroup()
								.addComponent(lblPoster, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(pnlLocalInfo,
										GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(40, Short.MAX_VALUE)));
		pnlTitle.setLayout(
				new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
						new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								RowSpec.decode("default:grow"), }));

		dataTitle = new JLabel("");
		dataTitle.setToolTipText("Click to view movie on IMDB");
		dataTitle.setForeground(new Color(0, 0, 255));

		dataTitle.setFont(new Font("Garamond", Font.BOLD, 25));
		pnlTitle.add(dataTitle, "2, 2");

		pnlIMDB = new JPanel();
		pnlTitle.add(pnlIMDB, "2, 6, fill, fill");
		pnlIMDB.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(19dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(20dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(22dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(21dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(19dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(18dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(21dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:max(23dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(93dlu;default):grow"), }));

		lblYear = new JLabel("Year");
		lblYear.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlIMDB.add(lblYear, "4, 4, right, default");

		dataYear = new JLabel("");
		dataYear.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlIMDB.add(dataYear, "6, 4");

		lblImdbRating = new JLabel("IMDB Rating");
		lblImdbRating.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlIMDB.add(lblImdbRating, "4, 6, right, default");

		dataIMDBRating = new JLabel("");
		dataIMDBRating.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlIMDB.add(dataIMDBRating, "6, 6");

		lblMpaa = new JLabel("MPAA rating");
		lblMpaa.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlIMDB.add(lblMpaa, "4, 8, right, default");

		dataRating = new JLabel("");
		dataRating.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlIMDB.add(dataRating, "6, 8");

		lblCountry = new JLabel("Country");
		lblCountry.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlIMDB.add(lblCountry, "4, 10, right, default");

		dataCountry = new JLabel("");
		dataCountry.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlIMDB.add(dataCountry, "6, 10");

		lblDirector = new JLabel("Director");
		lblDirector.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlIMDB.add(lblDirector, "4, 12, right, default");

		dataDirector = new JLabel("");
		dataDirector.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlIMDB.add(dataDirector, "6, 12");

		lblGenre = new JLabel("Genre");
		lblGenre.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlIMDB.add(lblGenre, "4, 14, right, default");

		dataGenre = new JLabel("");
		dataGenre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlIMDB.add(dataGenre, "6, 14");

		lblMyRating = new JLabel("My Rating");
		lblMyRating.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlIMDB.add(lblMyRating, "4, 16, right, default");

		dataMyRating = new JLabel("");
		dataMyRating.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlIMDB.add(dataMyRating, "6, 16");

		lblActor = new JLabel("Actors");
		lblActor.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlIMDB.add(lblActor, "4, 18, right, default");

		dataActor = new JLabel("");
		dataActor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlIMDB.add(dataActor, "6, 18");

		lblPlot = new JLabel("Plot");
		lblPlot.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblPlot.setAlignmentY(Component.TOP_ALIGNMENT);
		lblPlot.setVerticalTextPosition(SwingConstants.TOP);
		lblPlot.setVerticalAlignment(SwingConstants.TOP);
		lblPlot.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlIMDB.add(lblPlot, "4, 20, right, top");

		slpPlot = new JScrollPane();
		slpPlot.setBorder(null);
		pnlIMDB.add(slpPlot, "6, 20, fill, fill");

		txtAreaPlot = new JTextArea();
		txtAreaPlot.setDisabledTextColor(Color.BLACK);
		txtAreaPlot.setLineWrap(true);
		txtAreaPlot.setBorder(null);
		txtAreaPlot.setFocusTraversalKeysEnabled(false);
		txtAreaPlot.setFocusable(false);
		txtAreaPlot.setOpaque(false);
		txtAreaPlot.setWrapStyleWord(true);
		txtAreaPlot.setEnabled(false);
		txtAreaPlot.setEditable(false);
		txtAreaPlot.setFont(new Font("Tahoma", Font.PLAIN, 14));
		slpPlot.setViewportView(txtAreaPlot);
		pnlLocalInfo.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(92dlu;default):grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:max(41dlu;default):grow"), }));

		lblFileFormat = new JLabel("File Format");
		lblFileFormat.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlLocalInfo.add(lblFileFormat, "2, 2");

		dataMediaType = new JLabel("");
		dataMediaType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLocalInfo.add(dataMediaType, "4, 2");

		lblFilSize = new JLabel("File Size");
		lblFilSize.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlLocalInfo.add(lblFilSize, "2, 4");

		dataFileSize = new JLabel("");
		dataFileSize.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLocalInfo.add(dataFileSize, "4, 4");

		lblResolution = new JLabel("Resolution");
		lblResolution.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlLocalInfo.add(lblResolution, "2, 6");

		dataResolution = new JLabel("");
		dataResolution.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLocalInfo.add(dataResolution, "4, 6");

		lblBitRate = new JLabel("Bit Rate");
		lblBitRate.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlLocalInfo.add(lblBitRate, "2, 8");

		dataBitRate = new JLabel("");
		dataBitRate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLocalInfo.add(dataBitRate, "4, 8");

		lblDuration = new JLabel("Duration");
		lblDuration.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlLocalInfo.add(lblDuration, "2, 10, default, fill");

		dataDuration = new JLabel("");
		dataDuration.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLocalInfo.add(dataDuration, "4, 10");

		lblLanguage = new JLabel("Language");
		lblLanguage.setFont(new Font("Tahoma", Font.BOLD, 13));
		pnlLocalInfo.add(lblLanguage, "2, 12");

		slpdataLanguage = new JScrollPane();
		slpdataLanguage.setBorder(null);
		pnlLocalInfo.add(slpdataLanguage, "4, 12, fill, fill");

		dataAreaLanguage = new JTextArea();
		dataAreaLanguage.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dataAreaLanguage.setOpaque(false);
		dataAreaLanguage.setLineWrap(true);
		dataAreaLanguage.setWrapStyleWord(true);
		dataAreaLanguage.setEnabled(false);
		dataAreaLanguage.setEditable(false);
		dataAreaLanguage.setDisabledTextColor(Color.BLACK);
		dataAreaLanguage.setBorder(null);
		slpdataLanguage.setViewportView(dataAreaLanguage);
		pnlMovieDetails.setLayout(gl_pnlMovieDetails);

		/////////////////////////////////////////////////////////////////////////////
		// Main Movie Table and its properties
		/////////////////////////////////////////////////////////////////////////////
		tblmovie = new JTable();
		tblmovie.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblmovie.setModel(
				new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Copy", "Movie Name", "My Rating", "Watched"
			}
		) {
			Class[] columnTypes = new Class[] {
				Boolean.class, String.class, Integer.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				true, false, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblmovie.getColumnModel().getColumn(0).setPreferredWidth(39);
		tblmovie.getColumnModel().getColumn(1).setPreferredWidth(215);
		tblmovie.getColumnModel().getColumn(2).setPreferredWidth(58);
		tblmovie.getColumnModel().getColumn(3).setPreferredWidth(77);
		tblmovie.setAutoCreateRowSorter(true);
		tblmovie.setRowHeight(25);
		loadtable();// load data into table
		setUpMyRatingColumn(); // Adding combo box to table cell
		tblmovie.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblmovie.setFont(new Font("Calibri", Font.PLAIN, 17));
		DefaultTableCellRenderer renderer1 = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
		renderer1.setToolTipText("Double click to play movie");
		renderer2.setHorizontalAlignment(JLabel.CENTER); // Center align text
		renderer2.setToolTipText("Click to change your rating.");
		rowSorter = new TableRowSorter<>(tblmovie.getModel());
		tblmovie.setRowSorter(rowSorter);
		tblmovie.changeSelection(0, 0, false, false);
		if (!Tools.isDBEmpty())
			loadPanelMovieDetailsData();
		slptable.setViewportView(tblmovie);

		contentPane.setLayout(gl_contentPane);
	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method adds rating combo box to table
	//////////////////////////////////////////////////////////////////////////////////

	public void setUpMyRatingColumn() {

		// Set up the editor for the my rating cells.

		TableColumn myratingcolumn = tblmovie.getColumnModel().getColumn(2);

		JComboBox comboBox = new JComboBox();
		for (int i = 0; i <= 10; i++)
			comboBox.addItem(i);

		myratingcolumn.setCellEditor(new DefaultCellEditor(comboBox));

	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method loads data from database to table
	//////////////////////////////////////////////////////////////////////////////////
	private void loadtable() {
		Connection c;
		Statement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			stmt = c.createStatement();
			int counter=0;
			String sql = "select Title,MyRating,Watched from LocalInfo order by Title";
			ResultSet res = stmt.executeQuery(sql);
			DefaultTableModel tblmodel = (DefaultTableModel) tblmovie.getModel();
			tblmodel.setRowCount(0);
			while (res.next()) {
				int myrating = Integer
						.parseInt(res.getString("MyRating").isEmpty() == true ? "0" : res.getString("MyRating"));
				boolean watched = res.getBoolean("Watched");
				String moviename = res.getString("Title");
				tblmodel.addRow(new Object[] { false,moviename, myrating, watched });
			}
			res.close();
			stmt.close();
			c.close();

			lblTotalMovies.setText("Total Movies:" + tblmodel.getRowCount());
			for(int i=0;i<tblmodel.getRowCount();i++)
			{
				if((boolean) tblmodel.getValueAt(i, 3)){
					counter++;
				    		}
			}
		   lblWatched.setText("Movies Watched:" + counter);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	//////////////////////////////////////////////////////////////////////////////////
	//// Movies information is loaded into Panel
	//////////////////////////////////////////////////////////////////////////////////
	protected void loadPanelMovieDetailsData() {
		pnlLocalInfo.setVisible(true);
		pnlMovieDetails.setVisible(true);
		String title = (String) tblmovie.getValueAt(tblmovie.getSelectedRow(), 1);
		String filefullpath = Tools.getFullPath(title);
		String IMDBID = Tools.getIMDBID(title);
		File file = new File("Posters\\" + IMDBID + ".jpg");
		if (file.exists())//checking if poster exists then setting that poster or default one.
			lblPoster.setIcon(Tools.scaleImage(new ImageIcon(file.getPath()), 190, 270));
		else
			lblPoster.setIcon(Tools.scaleImage(new ImageIcon("Posters\\Default.jpg"), 190, 270));
		Connection c;
		PreparedStatement stmt;
		ResultSet res;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			String sql = "select * from LocalInfo where FileFullPath=?";
			stmt = c.prepareStatement(sql);
			stmt.setString(1, filefullpath);
			//Loading LocalInfo data to panel if exists
			res = stmt.executeQuery();
			if (res.isBeforeFirst()) {
				dataMediaType.setText(res.getString("MediaType"));
				dataFileSize.setText(res.getString("FileSize"));
				dataResolution.setText(res.getString("Resolution"));
				dataBitRate.setText(res.getString("BitRate"));
				dataDuration.setText(res.getString("Duration"));
				dataAreaLanguage.setText(Tools.getLanguage(filefullpath));
				dataTitle.setText(res.getString("Title"));
			}
			sql = "select * from IMDBInfo where FileFullPath=?";
			stmt.clearBatch();
			stmt = c.prepareStatement(sql);
			stmt.setString(1, filefullpath);

			res = stmt.executeQuery();
			//Loading IMDBInfo data to panel if exists
			if (!res.isBeforeFirst())// no results, set empty values
			{
				dataYear.setText("");
				dataIMDBRating.setText("");
				dataRating.setText("");
				dataCountry.setText("");
				dataDirector.setText("");
				dataGenre.setText("");
				dataActor.setText("");
				txtAreaPlot.setText("");
			} else { //fetch values from database and set
				dataYear.setText(res.getString("Year"));
				dataIMDBRating.setText(res.getString("IMDBRating"));
				dataRating.setText(res.getString("Rating"));
				dataCountry.setText(res.getString("Country"));
				dataDirector.setText(res.getString("Director"));
				dataGenre.setText(Tools.getGenre(IMDBID));
				dataActor.setText(Tools.getActor(IMDBID));
				txtAreaPlot.setText(res.getString("Plot"));
			}
			dataMyRating.setText("" + tblmovie.getValueAt(tblmovie.getSelectedRow(), 2));
			c.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}	
	/////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	/////////////////////////////////////////////////////////////
	private void createEvents() {
	
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ret = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?");
				if (ret == JOptionPane.YES_OPTION) {
					// Elegantly shut down program
					// Save any work into file or database, etc.
					System.exit(0);
				}
			}
		});
		
		lblPoster.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				String imdbid = Tools.getIMDBID(tblmovie.getValueAt(tblmovie.getSelectedRow(), 1) + "");
				if (!imdbid.isEmpty())
					try {
						Desktop.getDesktop().open(new File("Posters\\" + imdbid + ".jpg")); //Open Poster image.
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				else {
					JOptionPane.showMessageDialog(null, "Please update movie details first!");
				}
			}
			//changing mouse cursor to hand on mouse hover
			@Override
			public void mouseEntered(MouseEvent e) {
				lblPoster.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		//Opening Web Browser and opening IMDB page of that movie
		dataTitle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				String imdbid = Tools.getIMDBID(tblmovie.getValueAt(tblmovie.getSelectedRow(), 1) + "");
				if (!imdbid.isEmpty())
					try {
						Desktop.getDesktop().browse(new URI("http://www.imdb.com/title/" + imdbid + "/"));  //Open link in browser
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				else {
					JOptionPane.showMessageDialog(null, "Please update movie details first!");
				}
			}
			//changing mouse cursor to hand on mouse hover
			@Override
			public void mouseEntered(MouseEvent e) {
				dataTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});

		//Playing movie by double click
		tblmovie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					try {

						Desktop.getDesktop().open(new File(
								Tools.getFullPath(tblmovie.getValueAt(tblmovie.getSelectedRow(), 1).toString()))); //Open movie on double click
					} catch (IOException e1) {

						e1.printStackTrace();
					}

				}
			}
		});

		//Searching table and filtering results
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				warn();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				warn();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				warn();
			}

			private void warn() {
				String text = txtSearch.getText();

				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}

			}
		});
		
		//Scanning folders for new movies
		btnScanFolders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ScanFolders scf = new ScanFolders();
				scf.setModal(true);
				scf.setVisible(true);
				loadtable();
				if (!Tools.isDBEmpty()) {
					tblmovie.changeSelection(0, 0, false, false);
					loadPanelMovieDetailsData();
				}

			}
		});

		// Show Movie Details on Selection
		tblmovie.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (tblmovie.getSelectedRow() >= 0 && tblmovie.getSelectedColumn() == 1) {
					loadPanelMovieDetailsData();
				}
			}
		});

		TableModel model = tblmovie.getModel();
		model.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent tme) {
				if (tme.getType() == TableModelEvent.UPDATE && tme.getColumn() == 3) {// If watched check box value is changed then update it 
					Connection c;
					PreparedStatement stmt;
					try {
						Class.forName("org.sqlite.JDBC");
						c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
						String sql = "update LocalInfo set Watched=? where Title=?";
						String title = (String) model.getValueAt(tme.getFirstRow(), 1);//  Getting movie name
						stmt = c.prepareStatement(sql);
						int ans = ((boolean) model.getValueAt(tme.getFirstRow(), tme.getColumn())) ? 1 : 0;//Getting check box value and converting it to int
						stmt.setInt(1, ans);
						stmt.setString(2, title);
						stmt.executeUpdate();
						c.close();
						loadtable();
						loadPanelMovieDetailsData();// showing new contents of that movie in panel 
						pnlMovieDetails.setVisible(true);

					} catch (Exception e) {
						
					}

				} else if (tme.getType() == TableModelEvent.UPDATE && tme.getColumn() == 2) { //If new rating is selected then update it
					Connection c;
					PreparedStatement stmt;
					try {
						Class.forName("org.sqlite.JDBC");
						c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
						System.out.println("Listener invoked");
						String sql = "update LocalInfo set MyRating=? where Title=?";
						String title = (String) model.getValueAt(tme.getFirstRow(), 1);//getting selected movie name
						stmt = c.prepareStatement(sql);
						String myrating = String.valueOf(model.getValueAt(tme.getFirstRow(), 2)); //getting new rating value
						stmt.setString(1, myrating);
						stmt.setString(2, title);
						stmt.executeUpdate();//database updated
						c.close();
						loadPanelMovieDetailsData();//new contents shown in panel

					} catch (Exception e) {
						
						JOptionPane.showMessageDialog(null, e);
					}
				}
			}
		});

		//Remove selected movie from catalog
		btnRemoveMovieFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblmovie.getSelectedRow() >= 0) {
					int ret = JOptionPane.showConfirmDialog(null, "Do you want to remove "
							+ tblmovie.getValueAt(tblmovie.getSelectedRow(), 1) + " from Catalog ?");
					if (ret == JOptionPane.YES_OPTION) {
						Connection c;
						PreparedStatement instmt;
						try {
							Class.forName("org.sqlite.JDBC");
							c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
							String filefullpath, IMDBid,
									title = (String) tblmovie.getValueAt(tblmovie.getSelectedRow(), 1);
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
							JOptionPane.showMessageDialog(null, "Movie removed From Catalog!");
							loadtable();	//Loading movies table
							tblmovie.changeSelection(0, 0, false, false);// selecting first row
							loadPanelMovieDetailsData(); 
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, e);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please Select One Movie!");
				}

			}
		});
		//Play selected movie 
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (tblmovie.getSelectedRow() >= 0)
					try {

						Desktop.getDesktop().open(new File(
								Tools.getFullPath(tblmovie.getValueAt(tblmovie.getSelectedRow(), 1).toString())));
					} catch (IOException e) {

						e.printStackTrace();
					}
				else
					JOptionPane.showMessageDialog(null, "Please select any movie from the list first!!!");

			}
		});
		//Showing that movie in windows explorer, its folder.
		btnOpenContiningFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblmovie.getSelectedRow() >= 0)
					try {
						String fullpath = Tools
								.getFullPath(tblmovie.getValueAt(tblmovie.getSelectedRow(), 1).toString());
						Runtime.getRuntime().exec("explorer.exe /select," + fullpath); //Opening Windows Explorer and selecting that file
					} catch (IOException e) {

						e.printStackTrace();
					}
				else
					JOptionPane.showMessageDialog(null, "Please select any movie from the list first!!!");

			}
		});
		//Updating single selected movie from IMDB
		btnUpdateMovieDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tblmovie.getSelectedRow() >= 0) {
					int row = tblmovie.getSelectedRow();
					String moviename = tblmovie.getValueAt(tblmovie.getSelectedRow(), 1).toString();//get selected movie name
					String filefullpath = Tools.getFullPath(moviename);// get full path of movie
					UpdateMovieDetails obj = new UpdateMovieDetails(filefullpath); //update that movie
					obj.setModal(true);
					obj.setVisible(true);
					loadtable(); // load table
					tblmovie.changeSelection(row, 0, false, false);
					loadPanelMovieDetailsData();
				} else
					JOptionPane.showMessageDialog(null, "Please select any movie from the list first!!!");
			}
		});
		//Update all movies data from IMDB
		btnBatchUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BatchUpdate obj = new BatchUpdate();
				obj.setModal(true);
				obj.setVisible(true);
				loadtable();
				tblmovie.changeSelection(0, 0, false, false);
				loadPanelMovieDetailsData();
			}
		});
		//Analyzing movie collection by Language
		mntmByLanguage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				AnalyseLanguage asl=new AnalyseLanguage();
				asl.setVisible(true);
			 }
		});
		//Analyzing movie collection by Genre
		mntmByRating.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AnalyseGenre asg=new AnalyseGenre();
				asg.setVisible(true);
				
			}
		});
		//Analyzing movie collection by Year
		mntmByYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AnalyseYear asy=new AnalyseYear();
				asy.setVisible(true);
				
			}
		});
		//About Us
		mntmAboutUs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutUs abs=new AboutUs();
				abs.setModal(true);
				abs.setVisible(true);
				abs.dispose();
            	
			}
		});
		//Selecting Destination path for Batch Copy and Invoking the copy function
		btnSelectDestination.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path=null;
				chooser = new JFileChooser();	//For selecting folder
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select Destination For Copy");
				chooser.setApproveButtonText("Select Folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                  path=chooser.getSelectedFile().toString();
                  BatchCopy bc=new BatchCopy((DefaultTableModel) tblmovie.getModel(),path);//:p
	              bc.setVisible(true);
	                
                } else {
                	int n=tblmovie.getModel().getRowCount();  
                	for(int i=0;i<n;i++)
        			{
        				if((boolean) tblmovie.getModel().getValueAt(i, 0))
        			         tblmovie.getModel().setValueAt(false, i, 0);    //Clearing the Checked boxes
				
					 }
			}
				}});
	}
}
