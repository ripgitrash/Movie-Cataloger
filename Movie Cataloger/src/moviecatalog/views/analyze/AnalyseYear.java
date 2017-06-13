/*Author: Rishab Garg, Shubham Mathur 
 * Project: MovieCataloger
 * Description:This Dialog window shows movies categorized in years, it plots pie chart year wise.
 * 
 * */
package moviecatalog.views.analyze;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import moviecatalog.common.Tools;
import moviecatalog.views.MainWindow;
import java.awt.Color;

public class AnalyseYear extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable yeartble;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTable movietbl;
	private JLabel lblChart;
	private JPanel buttonPane;
	private JLabel labelRating;
	private JLabel lblYear;
	private MainWindow mainref;
	private JCheckBox chckbxShowPieChart;
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	
	}

	
	public AnalyseYear(MainWindow ref) {
		mainref=ref;
		initcomponents();
		createChart();
		createEvents();
	}
		
	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////

	private void initcomponents(){
		setTitle("Categorize & Anaylze By Year");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AnalyseLanguage.class.getResource("/moviecatalog/resources/icon.png")));
		setBounds(0, 43, 1024, 520);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		scrollPane = new JScrollPane();
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setToolTipText("");
		
		JPanel panel = new JPanel();
		{
			buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			
			labelRating = new JLabel("");
			labelRating.setBackground(Color.WHITE);
			
			lblYear = new JLabel("");
			lblYear.setBackground(Color.WHITE);
			
			chckbxShowPieChart = new JCheckBox("Show Pie Chart");
			chckbxShowPieChart.setBackground(Color.WHITE);
			chckbxShowPieChart.setSelected(true);
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addComponent(chckbxShowPieChart)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_buttonPane.createSequentialGroup()
								.addGap(18)
								.addComponent(lblYear))
							.addGroup(gl_buttonPane.createSequentialGroup()
								.addGap(224)
								.addComponent(labelRating)))
						.addContainerGap(645, Short.MAX_VALUE))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(chckbxShowPieChart)
							.addComponent(lblYear)
							.addComponent(labelRating))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
		
		lblChart = new JLabel("");
		lblChart.setOpaque(true);
		lblChart.setBackground(Color.WHITE);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(lblChart, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(lblChart, GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
		);
		panel.setLayout(gl_panel);
		
		movietbl = new JTable();
		movietbl.setRowHeight(25);
		movietbl.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Movies"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		movietbl.getColumnModel().getColumn(0).setPreferredWidth(618);
		scrollPane_1.setViewportView(movietbl);
		{
			yeartble = new JTable();
			yeartble.setRowHeight(25);
			yeartble.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Year Range"
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			yeartble.getColumnModel().getColumn(0).setPreferredWidth(612);
			scrollPane.setViewportView(yeartble);
		}
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("106px"),
				FormSpecs.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("289px"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("587px"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("427px"),
				FormSpecs.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("35px"),}));
		contentPanel.add(scrollPane, "2, 2, fill, top");
		contentPanel.add(scrollPane_1, "4, 2, fill, top");
		contentPanel.add(panel, "6, 2, fill, top");
		contentPanel.add(buttonPane, "2, 4, 5, 1, left, top");
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//// This function populates the Genre Table with distinct Genres and Plots The 3D Pie Chart 
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void createChart(){
		DefaultTableModel yearmodel = (DefaultTableModel) yeartble.getModel();
		Connection c;
		Statement stmt;
		Date d=new Date();
		int yerMin=0,yerCurrent=d.getYear();
		yerCurrent+=1900;                                         //Gets the current year
		ResultSet resYear,resCount = null;
		PreparedStatement pst;
		DefaultPieDataset dataset = new DefaultPieDataset();   
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			stmt = c.createStatement();
			String sql = "select min(Year) from IMDBInfo";
			resYear= stmt.executeQuery(sql);
			yearmodel.setRowCount(0);
			while(resYear.next()){
			yerMin=Integer.parseInt(resYear.getString(1));
			}
			int yerTemp=yerMin-(yerMin%5);	//Changing to lower limit of 5, like 1942 to 1940, 1946 to 1945
			while(yerTemp<=yerCurrent){                            //Iterate from min year to current year
				String from=yerTemp+"";                           // Min year for each iteration
				String to=(yerTemp+=5)+"";                        //Gap of 5 year from min
				String finl=from+"-"+to;				
				String sql1="select COUNT(FileFullPath) from IMDBInfo where Year >= ? and Year <?"; 
				pst=c.prepareStatement(sql1);
				pst.setString(1,from);
				pst.setString(2,to);
				resCount=pst.executeQuery();
				int count=Integer.parseInt(resCount.getString("COUNT(FileFullPath)"));	//Parses the total count of movies within one Genre to integer
				if(count!=0)
				{
					yearmodel.addRow(new Object[] { finl });
					dataset.setValue(finl, new Integer(count));//sets the dataset values for chart plotting
				}					
				
			}
			resYear.close();
			resCount.close();
			stmt.close();
			c.close();
			JFreeChart chart=ChartFactory.createPieChart3D("Year Statistical Analysis", dataset,true,true,false);
			final PiePlot3D plot = ( PiePlot3D ) chart.getPlot( );
			plot.setStartAngle(90);             
		    plot.setForegroundAlpha( 0.60f );             //Chart is plotted with desired measurements 
		    plot.setInteriorGap( 0.02 );
		    plot.setDarkerSides(true);
		    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}"));
			plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({1})"));
			int width = 560;    	     
			int height = 370;
			File pieChart = new File( "Charts//Pie_Chart_Year.jpeg" );
		    ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );//stores plotted chart in Charts Folder
		    lblChart.setIcon(Tools.scaleImage(new ImageIcon("Charts//Pie_Chart_Year.jpeg"),581,414));//display pieplot
        	} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////This function populates the Movies Table for the selected index of Language 
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void showMovies(String year){
	 DefaultTableModel moviemodel = (DefaultTableModel) movietbl.getModel();
	 String from=year.substring(0,4);                  //Getting from year
	 String to=year.substring(5,9);                    //Getting to year
	 moviemodel.setRowCount(0);
	 Connection c;
	 ResultSet resPath,resTitle = null;
		PreparedStatement stmtTitle,stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			String sql1 = "select FileFullPath from IMDBInfo where Year >= ? and Year <?";
			stmt = c.prepareStatement(sql1);
			stmt.setString(1,from);
			stmt.setString(2,to);
			resPath= stmt.executeQuery();
		   while (resPath.next()) {                       //Fetching the Title of selected path movies and adding them to table
				String path=resPath.getString("FileFullPath");
				String sql = "select Title from LocalInfo where FileFullPath=?";
				stmtTitle = c.prepareStatement(sql);
				stmtTitle.setString(1,path);
				resTitle = stmtTitle.executeQuery();
				String name=resTitle.getString("Title");
				moviemodel.addRow(new Object[] { name });
			}
			resTitle.close();
			resPath.close();
			stmt.close();
			c.close();
     	} catch (Exception e) {
		}

   }
	
	//////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	//////////////////////////////////////////////////////////////
         
	private void createEvents(){
		
		//Selecting a particular year range
		yeartble.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (yeartble.getSelectedRow() >= 0 && yeartble.getSelectedColumn() == 0) {
					String yearSelected=yeartble.getValueAt(yeartble.getSelectedRow(), 0).toString();
					showMovies(yearSelected);// the clicked year range is passed to show its movies
				
			   }
			}
		});
		
		// Showing IMDB Rating of Movie and other data in main window 
		movietbl.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (movietbl.getSelectedRow() >= 0 && movietbl.getSelectedColumn() == 0) {
					String movieSelected = movietbl.getValueAt(movietbl.getSelectedRow(), 0).toString();
					lblYear.setText("Year "+Tools.getYear(movietbl.getValueAt(movietbl.getSelectedRow(), 0).toString()));
					labelRating.setText("IMDB Rating "+Tools.getRating(movieSelected));
					mainref.loadPanelMovieDetailsData(movieSelected);
				}
			}
		});

		//play the movie
		movietbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					try {

						Desktop.getDesktop().open(new File(
								Tools.getFullPath(movietbl.getValueAt(movietbl.getSelectedRow(), 0).toString()))); //Open movie on double click
					} catch (IOException e1) {

						e1.printStackTrace();
					}

				}
			}
		});
		
		//Toggle Pie Chart
		chckbxShowPieChart.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (chckbxShowPieChart.isSelected()) {
					setBounds(0, 43, 1024, 520);

				} else {
					setBounds(0, 43, 435, 520);
				}
			}
		});
		
		
	}
}
