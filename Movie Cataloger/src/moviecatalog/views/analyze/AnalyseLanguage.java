/*Author: Rishab Garg, Shubham Mathur 
 * Project: MovieCataloger
 * Description:This Dialog window shows movies categorized in languages, it plots pie chart language wise.
 * 
 * */
package moviecatalog.views.analyze;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Toolkit;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import moviecatalog.common.Tools;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

public class AnalyseLanguage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable langtble;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTable movietbl;
	private JButton okButton;
	private JLabel lblChart;
	private JPanel buttonPane;
	private JLabel labelRating;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	
	}

	
	public AnalyseLanguage() {
		initcomponents();
		createChart();
		createEvents();
	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////

	private void initcomponents() {
		setTitle("Anaylze By Language");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(AnalyseLanguage.class.getResource("/moviecatalog/resources/icon.png")));
		setBounds(100, 100, 1024, 520);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		scrollPane = new JScrollPane();
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setToolTipText("");

		JPanel panel = new JPanel();
		{
			buttonPane = new JPanel();
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}

			labelRating = new JLabel("");
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_buttonPane.createSequentialGroup().addGap(324).addComponent(labelRating)
							.addPreferredGap(ComponentPlacement.RELATED, 571, Short.MAX_VALUE).addComponent(okButton)
							.addGap(39)));
			gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup().addGap(5).addGroup(gl_buttonPane
							.createParallelGroup(Alignment.BASELINE).addComponent(okButton).addComponent(labelRating))
							.addContainerGap()));
			buttonPane.setLayout(gl_buttonPane);
		}
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 587, GroupLayout.PREFERRED_SIZE))
				.addComponent(buttonPane, GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel
				.createSequentialGroup()
				.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, 0, 0, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(buttonPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		lblChart = new JLabel("");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblChart,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblChart,
				GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE));
		panel.setLayout(gl_panel);

		movietbl = new JTable();
		movietbl.setRowHeight(25);
		movietbl.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Movies" }) {
			boolean[] columnEditables = new boolean[] { false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		movietbl.getColumnModel().getColumn(0).setPreferredWidth(618);
		scrollPane_1.setViewportView(movietbl);
		{
			langtble = new JTable();
			langtble.setRowHeight(25);
			langtble.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Languages" }) {
				boolean[] columnEditables = new boolean[] { false };

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			langtble.getColumnModel().getColumn(0).setPreferredWidth(796);
			scrollPane.setViewportView(langtble);
		}
		contentPanel.setLayout(gl_contentPanel);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//// This function populates the Language Table with distinct Languages and Plots The 3D Pie Chart 
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void createChart(){
		DefaultTableModel langmodel = (DefaultTableModel) langtble.getModel();
		Connection c;
		Statement stmt;
		ResultSet resLanguage,resCount = null;
		PreparedStatement pst;
		DefaultPieDataset dataset = new DefaultPieDataset();   
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			stmt = c.createStatement();
			String sql = "select DISTINCT(Language) from LanguageInfo order by Language";
			resLanguage= stmt.executeQuery(sql);
			langmodel.setRowCount(0);
			while (resLanguage.next()) {
				String lang=resLanguage.getString("Language");
				String sqlCount="select COUNT(FileFullPath) from LanguageInfo where Language=?";
				pst=c.prepareStatement(sqlCount);
				pst.setString(1,lang);
				resCount=pst.executeQuery();
				int count=Integer.parseInt(resCount.getString("count(FileFullPath)"));//Parses the total count of movies within one language to integer
				dataset.setValue(lang, new Integer(count));//sets the dataset values for chart plotting
				langmodel.addRow(new Object[] { lang });
			}
			resLanguage.close();
			resCount.close();
			stmt.close();
			c.close();
			JFreeChart chart=ChartFactory.createPieChart3D("Language Statistical Analysis", dataset,true,true,false);
			final PiePlot3D plot = ( PiePlot3D ) chart.getPlot( );
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}"));// label with percent
			plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}-{1}"));// label with count
		    plot.setStartAngle( 270 );
		    plot.setDarkerSides(true);
		    plot.setForegroundAlpha( 0.60f );             //Chart is plotted with desired measurements 
		    plot.setInteriorGap( 0.02 );     
			int width = 560;    	     
			int height = 370;
			File pieChart = new File( "Charts//Pie_Chart_Lang.jpeg" );
		    ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );//stores plotted chart in Charts Folder
		    lblChart.setIcon(Tools.scaleImage(new ImageIcon("Charts//Pie_Chart_Lang.jpeg"),581,414));//display pieplot
        	} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////This function populates the Movies Table for the selected index of Language 
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void showMovies(String lang){
	 DefaultTableModel moviemodel = (DefaultTableModel) movietbl.getModel();
	 moviemodel.setRowCount(0);
	 Connection c;
	 ResultSet res= null;
		PreparedStatement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			String sql = "select Title from LocalInfo where FileFullPath in(select FileFullPath from LanguageInfo where Language=? order by FileFullPath) order by Title";
			stmt = c.prepareStatement(sql);
			stmt.setString(1,lang);
			res = stmt.executeQuery();
		    while (res.next()) {                       //Fetching the Title of selected path movies and adding them to table
				String name=res.getString("Title");
				moviemodel.addRow(new Object[] { name });
			}
			res.close();			
			stmt.close();
			c.close();
     	} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}

   }
			
		

	
	//////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	//////////////////////////////////////////////////////////////
         // OK button
	private void createEvents(){
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
			
		});
		
		//Selecting a particlular language range
				langtble.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (langtble.getSelectedRow() >= 0 && langtble.getSelectedColumn() == 0) {
							String yearSelected=langtble.getValueAt(langtble.getSelectedRow(), 0).toString();
							showMovies(yearSelected);// the clicked year range is passed to show its movies
						
					   }
					}
				});
		// Showing IMDB Rating of Movie
				movietbl.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (movietbl.getSelectedRow() >= 0 && movietbl.getSelectedColumn() == 0) {
							String movieSelected = movietbl.getValueAt(movietbl.getSelectedRow(), 0).toString();
							String rating=Tools.getRating(movieSelected);
							 labelRating.setText("IMDB Rating "+rating);
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

		
	}
}