/*Author: Shubham Mathur
 * Project: MovieCataloger
 * Description:This Dialog window lets user update IMDB information of selected movies.
 * */
package moviecatalog.views;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import moviecatalog.common.Tools;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Toolkit;
import java.awt.Color;

public class BatchUpdate extends JDialog {

	private JPanel contentPanel;
	private JScrollPane slptblmovie;
	private JTable tblmovie;
	private JButton btnUpdateSelected;
	private JButton btnUpdateSingleMovie;
	private JButton btnUpdateByFiltering;
	private JLabel lblStatus;
	private JLabel lblMovieSelected;
	private int selectcount = 0;

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
	public BatchUpdate() {
	
		initComponents();
		getmoviesinlist();
		createEvents();

	}
	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	private void initComponents() {
		setTitle("Batch Update movies from Internet");
		setIconImage(Toolkit.getDefaultToolkit().getImage(BatchUpdate.class.getResource("/moviecatalog/resources/icon.png")));
		setBounds(100, 100, 516, 370);
		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		slptblmovie = new JScrollPane();

		btnUpdateSingleMovie = new JButton("Update Single Movie");

		btnUpdateSelected = new JButton("Update Selected");

		btnUpdateByFiltering = new JButton("Update by filtering moviename");

		lblStatus = new JLabel("");
		lblStatus.setBackground(Color.WHITE);

		lblMovieSelected = new JLabel("");
		lblMovieSelected.setBackground(Color.WHITE);

		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(slptblmovie, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
				.addGroup(gl_contentPanel.createSequentialGroup().addComponent(btnUpdateSelected).addGap(33)
						.addComponent(btnUpdateSingleMovie)
						.addPreferredGap(ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
						.addComponent(btnUpdateByFiltering))
				.addGroup(gl_contentPanel.createSequentialGroup().addComponent(lblStatus)
						.addPreferredGap(ComponentPlacement.RELATED, 321, Short.MAX_VALUE)
						.addComponent(lblMovieSelected)));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel
				.createSequentialGroup()
				.addComponent(slptblmovie, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE).addComponent(btnUpdateSelected)
						.addComponent(btnUpdateByFiltering).addComponent(btnUpdateSingleMovie))
				.addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_contentPanel
						.createParallelGroup(Alignment.BASELINE).addComponent(lblStatus).addComponent(lblMovieSelected))
				.addContainerGap(60, Short.MAX_VALUE)));

		tblmovie = new JTable();
		tblmovie.setAutoCreateRowSorter(true);
		tblmovie.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tblmovie.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblmovie.setShowHorizontalLines(false);
		tblmovie.setShowGrid(false);

		tblmovie.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "", "Movie Name" }) {
			Class[] columnTypes = new Class[] { Boolean.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblmovie.getColumnModel().getColumn(0).setPreferredWidth(19);
		tblmovie.getColumnModel().getColumn(1).setPreferredWidth(455);
		tblmovie.setRowHeight(22);
		slptblmovie.setViewportView(tblmovie);
		contentPanel.setLayout(gl_contentPanel);

	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method loads those movies list which do not have IMDB information
	//////////////////////////////////////////////////////////////////////////////////

	private void getmoviesinlist() {

		DefaultTableModel tblmodel = (DefaultTableModel) tblmovie.getModel();
		tblmodel.setRowCount(0);
		Connection c;
		PreparedStatement stmt;
		ResultSet res;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + Tools.getDBNAME());
			//Selects those files which are there in LocalInfo but not in IMDBInfo
			String sql = "select Title from LocalInfo where FileFullPath not in ( select FileFullPath from IMDBInfo)";   
			stmt = c.prepareStatement(sql);
			res = stmt.executeQuery();
			while (res.next())
				tblmodel.addRow(new Object[] { true, "" + res.getString("Title") });	//Adding movies to list
			c.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		lblStatus.setText("Movies Found: " + tblmodel.getRowCount());
		selectcount = tblmodel.getRowCount();
		lblMovieSelected.setText("Movies Selected: " + selectcount);

	}
	
	/////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	/////////////////////////////////////////////////////////////
	private void createEvents() {

		//It changes selected movies count
		DefaultTableModel model = (DefaultTableModel) tblmovie.getModel();
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent tme) {
				if (tme.getType() == TableModelEvent.UPDATE && tme.getColumn() == 0) {
					if ((boolean) model.getValueAt(tme.getFirstRow(), 0))
						lblMovieSelected.setText("Movies Selected: " + (++selectcount));
					else
						lblMovieSelected.setText("Movies Selected: " + (--selectcount));
				}
			}
		});
		
		//Update marked movies
		btnUpdateSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Tools.netIsAvailable()) {
					UpdateMovieProgress obj = new UpdateMovieProgress((DefaultTableModel) tblmovie.getModel(), false);
					obj.setModal(true);
					obj.setVisible(true);
					obj.dispose();
					getmoviesinlist();
				} else
					JOptionPane.showMessageDialog(null, "Please connect to Internet to reterive data.");
			}
		});

		//Update selected movie 
		btnUpdateSingleMovie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Tools.netIsAvailable()) {
					String title = (String) tblmovie.getValueAt(tblmovie.getSelectedRow(), 1);
					String filefullpath = Tools.getFullPath(title);
					UpdateMovieDetails obj = new UpdateMovieDetails(filefullpath);
					obj.setModal(true);
					obj.setVisible(true);
					obj.dispose();
					getmoviesinlist();
				} else
					JOptionPane.showMessageDialog(null, "Please connect to Internet to reterive data.");
			}
		});
		
		//More aggressive filters applied and update movies 
		btnUpdateByFiltering.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Tools.netIsAvailable()) {
					UpdateMovieProgress obj = new UpdateMovieProgress((DefaultTableModel) tblmovie.getModel(), true);
					obj.setModal(true);
					obj.setVisible(true);
					obj.dispose();
					getmoviesinlist();
				} else
					JOptionPane.showMessageDialog(null, "Please connect to Internet to reterive data.");
			}
		});

	}
	
	
}
