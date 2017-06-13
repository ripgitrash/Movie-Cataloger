/*Author: Rishab Garg, Shubham Mathur 
 * Project: MovieCataloger
 * Description:This Dialog window shows movies categorized in genres, it plots pie chart genre wise.
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import moviecatalog.common.Tools;
import moviecatalog.views.MainWindow;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.AttributedString;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JCheckBox;
import java.awt.Color;

public class AnalyseGenre extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable gentble;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTable movietbl;
	private JLabel lblChart;
	private JPanel buttonPane;
	private JLabel labelRating;
	private MainWindow mainref;
	private JCheckBox chckbxShowPieChart;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public AnalyseGenre(MainWindow ref) {
		mainref=ref;
		initcomponents();
		createChart();
		createEvents();
	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////

	private void initcomponents() {
		setTitle("Categorize & Anaylze By Genre");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(AnalyseLanguage.class.getResource("/moviecatalog/resources/icon.png")));
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
			
			chckbxShowPieChart = new JCheckBox("Show Pie Chart");
			chckbxShowPieChart.setBackground(Color.WHITE);
			chckbxShowPieChart.setSelected(true);
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.TRAILING)
					.addGroup(Alignment.LEADING, gl_buttonPane.createSequentialGroup()
						.addComponent(chckbxShowPieChart)
						.addGap(205)
						.addComponent(labelRating)
						.addContainerGap(684, Short.MAX_VALUE))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_buttonPane.createSequentialGroup()
								.addGap(5)
								.addComponent(labelRating))
							.addComponent(chckbxShowPieChart))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
			buttonPane.setLayout(gl_buttonPane);
		}

		lblChart = new JLabel("");
		lblChart.setOpaque(true);
		lblChart.setBackground(Color.WHITE);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addComponent(lblChart,
				GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblChart,
				GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE));
		panel.setLayout(gl_panel);
		movietbl = new JTable();
		movietbl.setRowHeight(25);
		movietbl.setAutoCreateRowSorter(true);
		movietbl.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Movies" }) {
			boolean[] columnEditables = new boolean[] { false };
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		movietbl.getColumnModel().getColumn(0).setPreferredWidth(618);
		scrollPane_1.setViewportView(movietbl);
		{
			gentble = new JTable();
			gentble.setRowHeight(25);
			gentble.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Genre" }) {
				boolean[] columnEditables = new boolean[] { false };

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			gentble.getColumnModel().getColumn(0).setPreferredWidth(618);
			scrollPane.setViewportView(gentble);
		}
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("106px"),
				FormSpecs.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("270px"),
				FormSpecs.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("592px"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("427px"),
				FormSpecs.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("33px"),}));
		contentPanel.add(buttonPane, "2, 4, 5, 1, fill, fill");
		contentPanel.add(scrollPane, "2, 2, fill, top");
		contentPanel.add(scrollPane_1, "4, 2, fill, top");
		contentPanel.add(panel, "6, 2, left, top");
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//// This function populates the Genre Table with distinct Genres and plots the pie chart
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void createChart() {
		DefaultTableModel genmodel = (DefaultTableModel) gentble.getModel();
		Connection c;
		Statement stmt;
		ResultSet resGenre, resCount = null;
		PreparedStatement pst;
		DefaultPieDataset dataset = new DefaultPieDataset();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			stmt = c.createStatement();
			String sqlGenre = "select DISTINCT(Genre) from GenreInfo order by Genre";
			resGenre = stmt.executeQuery(sqlGenre);
			genmodel.setRowCount(0);
			while (resGenre.next()) {
				String genre = resGenre.getString("Genre");
				String sqlCount = "select COUNT(IMDBID) from GenreInfo where Genre=?";
				pst = c.prepareStatement(sqlCount);
				pst.setString(1, genre);
				resCount = pst.executeQuery();
				int count = Integer.parseInt(resCount.getString("count(IMDBID)"));// Parses the total count of movies within one Genre to integer
				dataset.setValue(genre, new Integer(count));// sets the dataset values for chart plotting
				genmodel.addRow(new Object[] { genre });
			}
			resGenre.close();
			resCount.close();
			stmt.close();
			c.close();
			JFreeChart chart = ChartFactory.createPieChart3D("Genre Statistical Analysis", dataset, true, true, false);
			final PiePlot3D plot = (PiePlot3D) chart.getPlot();
			plot.setStartAngle(270);
			plot.setForegroundAlpha(0.60f); // Chart is plotted with desired measurements
			plot.setInteriorGap(0.02);
			plot.setDarkerSides(true);
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}")); // label with percent
			plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}-{1}")); //label with count
			int width = 560;
			int height = 370;
			File pieChart = new File("Charts//Pie_Chart_Genre.jpeg");
			ChartUtilities.saveChartAsJPEG(pieChart, chart, width, height);// stores plotted chart in Charts Folder
			lblChart.setIcon(Tools.scaleImage(new ImageIcon("Charts//Pie_Chart_Genre.jpeg"), 581, 414));// display pie chart
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//// This function populates the Movies Table for the selected index of genre
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void showMovies(String genre) {
		DefaultTableModel moviemodel = (DefaultTableModel) movietbl.getModel();
		moviemodel.setRowCount(0);
		Connection c;
		ResultSet res=null;
		PreparedStatement stmt=null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			String sql = "select Title from LocalInfo where FileFullPath in(select FileFullPath from IMDBInfo where IMDBID in(select IMDBID from GenreInfo where Genre=?))";
			stmt = c.prepareStatement(sql);
			stmt.setString(1, genre);
			res = stmt.executeQuery();
			while (res.next()) { // Fetching the Title of selected path movies and adding them to table
				String name = res.getString("Title");
				moviemodel.addRow(new Object[] { name });
			}
			res.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	//////////////////////////////////////////////////////////////
	

	private void createEvents() {

		// Selecting a particular genre range
		gentble.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (gentble.getSelectedRow() >= 0 && gentble.getSelectedColumn() == 0) {
					String genSelected = gentble.getValueAt(gentble.getSelectedRow(), 0).toString();
					showMovies(genSelected);// the clicked year range is passed to show its movies

				}
			}
		});
		// Showing IMDB Rating of Movie and other data in main window 
		movietbl.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (movietbl.getSelectedRow() >= 0 && movietbl.getSelectedColumn() == 0) {
					String movieSelected = movietbl.getValueAt(movietbl.getSelectedRow(), 0).toString();
					labelRating.setText("IMDB Rating "+Tools.getRating(movieSelected));
					mainref.loadPanelMovieDetailsData(movieSelected);
				}
			}
		});

		// play the movie
		movietbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					try {

						Desktop.getDesktop().open(new File(
								Tools.getFullPath(movietbl.getValueAt(movietbl.getSelectedRow(), 0).toString()))); // Open movie on double click
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
					setBounds(0, 43, 420, 520);
				}
			}
		});
		
	}
}
