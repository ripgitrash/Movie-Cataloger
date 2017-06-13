/*Author: Shubham Mathur
 * Project: MovieCataloger
 * Description:This Dialog window lets user find new movies in folders and add them to catalog 
 * 
 * */
package moviecatalog.views;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.UIManager;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import moviecatalog.common.Tools;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import moviecatalog.common.JCheckBoxList;
import javax.swing.ScrollPaneConstants;
import javax.swing.JCheckBox;
import java.awt.Toolkit;
import java.awt.Color;

@SuppressWarnings("serial")
public class ScanFolders extends JDialog {

	private JPanel contentPane;
	private JTable tblmoviepath;
	private JButton btnSetFolders;
	private JButton btnAddMovies;
	private JLabel lblStatus;
	private JLabel lblMoviesSelected;
	private int selectcount;
	private JCheckBoxList chklstExtension;
	DefaultListModel<JCheckBox> extmodel;
	private JScrollPane slpExtension;
	
	public JTable getTblmoviepath() {
		return tblmoviepath;
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

	public ScanFolders() {
		initComponents();
		initmovielist();
		createEvents();
	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	private void initComponents() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ScanFolders.class.getResource("/moviecatalog/resources/icon.png")));
		setTitle("Scan Folders");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 560, 430);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		selectcount=0;
		JScrollPane slpupper = new JScrollPane();
		tblmoviepath = new JTable();
		tblmoviepath.setAutoCreateRowSorter(true);
		tblmoviepath.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tblmoviepath.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblmoviepath.setShowHorizontalLines(false);
		tblmoviepath.setShowGrid(false);

		tblmoviepath.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"", "File Path"
			}
		) {
			Class[] columnTypes = new Class[] {
				Boolean.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblmoviepath.getColumnModel().getColumn(0).setPreferredWidth(19);
		tblmoviepath.getColumnModel().getColumn(1).setPreferredWidth(530);
		tblmoviepath.setRowHeight(22);
		slpupper.setViewportView(tblmoviepath);

		btnSetFolders = new JButton("Set Folders");
		btnSetFolders.setToolTipText("Select Folders to be scanned");

		btnAddMovies = new JButton("Add Movies");
		btnAddMovies.setToolTipText("Add selected movies to Catalogue");
		
		lblStatus = new JLabel("");
		lblStatus.setBackground(Color.WHITE);
		
		lblMoviesSelected = new JLabel("");
		lblMoviesSelected.setBackground(Color.WHITE);
		
		slpExtension = new JScrollPane();
		slpExtension.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(2)
							.addComponent(lblStatus)
							.addGap(18)
							.addComponent(lblMoviesSelected, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(slpExtension, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
							.addGap(57)
							.addComponent(btnSetFolders)
							.addGap(28)
							.addComponent(btnAddMovies)))
					.addContainerGap(201, Short.MAX_VALUE))
				.addComponent(slpupper, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addComponent(slpupper, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnSetFolders)
								.addComponent(btnAddMovies)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addComponent(slpExtension, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStatus)
						.addComponent(lblMoviesSelected)))
		);
		extmodel = new DefaultListModel<JCheckBox>();
		chklstExtension = new JCheckBoxList(extmodel);
		String extensions[]={"*.webm","*.mkv","*.flv"
				 ,"*.vob","*.ogv","*.ogg","*.drc","*.avi","*.mov","*.qt",
				 "*.wmv","*.yuv","*.rm","*.rmvb","*.asf","*.amv","*.mp4","*.m4v",
				 "*.m4p","*.mpg","*.mp2","*.mpeg","*.mpe","*.mpv","*.m2v","*.svi",
				 "*.3g2","*.mxf","*.roq","*.nsv","*.f4v","*.f4p","*.f4a","*.f4b"};
		Arrays.sort(extensions); //Sort array before insertion 
		for(String x:extensions)
		{
			JCheckBox ckbox=new JCheckBox(x);	//Creating check box
			ckbox.setSelected(true);	//By default true
			ckbox.addItemListener(new ItemListener() {				
				@Override
				public void itemStateChanged(ItemEvent e) {
					initmovielist();	//loads movie table				
				}
			});
			extmodel.addElement(ckbox); //Adding check box to list
		}
		
		chklstExtension.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		slpExtension.setViewportView(chklstExtension);
		
		JLabel lblSelectFileExtension = new JLabel("Select file extension to search");
		lblSelectFileExtension.setOpaque(true);
		lblSelectFileExtension.setBackground(Color.WHITE);
		slpExtension.setColumnHeaderView(lblSelectFileExtension);
		contentPane.setLayout(gl_contentPane);

	}
	
	//////////////////////////////////////////////////////////////////////////////////
	//// It scans folders given in folder list and adds movie to list
	//////////////////////////////////////////////////////////////////////////////////
	public void initmovielist() {
		JSONParser parser = new JSONParser();
		DefaultTableModel tblmodel = (DefaultTableModel) tblmoviepath.getModel();
		tblmodel.setRowCount(0);
		try {
			Object obj = parser.parse(new FileReader("folderlist.json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray pathlist = (JSONArray) jsonObject.get("folderpath");
			Iterator<String> iterator = pathlist.iterator();
			while (iterator.hasNext()) 
				getmoviesinlist("" + iterator.next());
		} 
		catch(FileNotFoundException e)
		{}
		catch(NullPointerException e)
		{}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
		}
		lblStatus.setText("Movies Found: "+tblmodel.getRowCount());
		selectcount=tblmodel.getRowCount();
		lblMoviesSelected.setText("Movies Selected: "+selectcount);

	}

	//////////////////////////////////////////////////////////////////////////////////
	//// For that particular folder path search recursively for movies and generates 
	//// text file containing movie's list. Then add them in table if its size >100 MB 
	//// and they do not exists in database. 	
	//////////////////////////////////////////////////////////////////////////////////
	private void getmoviesinlist(String path) {
		DefaultTableModel tblmodel = (DefaultTableModel) tblmoviepath.getModel();
		String cmdSyntax = "dir /s/b ";
		for(int i=0;i<extmodel.size();i++)
			if(extmodel.getElementAt(i).isSelected())
				cmdSyntax+=" "+extmodel.getElementAt(i).getLabel();  //Getting selected extensions to be searched for
		cmdSyntax+=" >movieslist.txt";
		new File(path+ "\\movieslist.txt").delete(); // delete old file
		try {
			Process p = Runtime.getRuntime().exec("cmd /c " + cmdSyntax, null, new File(path)); //Execute this command line syntax and saves in a text file in parent folder  
			p.waitFor();//Main thread waiting for P to finish
			//Reading File and adding to table
			FileReader reader = new FileReader(path + "\\movieslist.txt");
			BufferedReader scan=new BufferedReader(reader);
			String temp;
			while ((temp = scan.readLine()) != null) {
				//size>100 MB to ignore music files and video songs etc. and do not add movies already in catalog.
				if(!Tools.isMovieinCatalogue(temp) && Tools.getMovieSize(temp)>100)  
				tblmodel.addRow(new Object[] { true, "" + temp });			}
			reader.close();
			scan.close();
			new File(path+ "\\movieslist.txt").delete();//delete the text file.
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
		}

	}
	//////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	//////////////////////////////////////////////////////////////
	private void createEvents() {
		//It changes selected movies count
		DefaultTableModel model = (DefaultTableModel) tblmoviepath.getModel();
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent tme) {
				if (tme.getType() == TableModelEvent.UPDATE && tme.getColumn()==0) {
						if((boolean)model.getValueAt(tme.getFirstRow(), 0))
							lblMoviesSelected.setText("Movies Selected: "+(++selectcount));
						else
							lblMoviesSelected.setText("Movies Selected: "+(--selectcount));
				}
			}
		});
		
		//Select Folders to be scanned.
		btnSetFolders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddFolder adf = new AddFolder();
				adf.setModal(true);
				adf.setVisible(true);
				initmovielist();
			}
		});
		
		//Add movies into catalog
		btnAddMovies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddingMoviesProgress obj=new AddingMoviesProgress((DefaultTableModel) tblmoviepath.getModel());
				obj.setModal(true);
				obj.setVisible(true);
				dispose();
			}
		});

		
	}
}