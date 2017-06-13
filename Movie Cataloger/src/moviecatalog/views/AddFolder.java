/*Author: Shubham Mathur
 * Project: MovieCataloger
 * Description:This Dialog window lets user add folders in list that needs to be scanned for movies.
 * 
 * */
package moviecatalog.views;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import javax.swing.UIManager;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Color;


public class AddFolder extends JDialog {

	//All the Swing Components declaration
	private JPanel contentPane;
	private JList<String> lstpath;
	private JButton btnAddFolder;
	private JButton btnRemoveFolder;
	private JButton btnSave;
	private JButton btnCancel;
	private JFileChooser chooser;
	DefaultListModel<String> lstmodel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());// Default theme to application
		} catch (Throwable e) {
			e.printStackTrace();
		}
			
	}

	/**
	 * Create the frame.
	 */
	public AddFolder() {
		initComponents();
		initpathlist();
		createEvents();
	}


	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	private void initComponents() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddFolder.class.getResource("/moviecatalog/resources/icon.png")));
		setTitle("Select the folders to scan");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		lstmodel = new DefaultListModel<>();
		lstpath = new JList<>(lstmodel);
		lstpath.setBackground(Color.WHITE);

		btnAddFolder = new JButton("Add Folder");

		btnRemoveFolder = new JButton("Remove Folder");

		btnSave = new JButton("Save");
		
		btnCancel = new JButton("Cancel");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lstpath, GroupLayout.PREFERRED_SIZE, 422, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
										.addComponent(btnAddFolder).addGap(18).addComponent(btnRemoveFolder).addGap(18)
										.addComponent(btnSave).addGap(18).addComponent(btnCancel)))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(lstpath, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnAddFolder)
								.addComponent(btnRemoveFolder).addComponent(btnSave).addComponent(btnCancel))
						.addContainerGap(15, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);

	}
	
	//////////////////////////////////////////////////////////////////////////////////
	//// This method reads a json file for folder paths and add them to list
	//////////////////////////////////////////////////////////////////////////////////

	private void initpathlist() {
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("folderlist.json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray pathlist = (JSONArray) jsonObject.get("folderpath");
			Iterator<String> iterator = pathlist.iterator();
			while (iterator.hasNext()) 
				lstmodel.addElement(iterator.next());
		} 
		catch(FileNotFoundException e)
		{}
		catch(NullPointerException e)
		{}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
		}

		
	}
	//////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	//////////////////////////////////////////////////////////////
	private void createEvents() {
		// Browse and add folder path to list
		btnAddFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooser = new JFileChooser();	//For selecting folder
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select Folder to add in list");
				chooser.setApproveButtonText("Select Folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

					lstmodel.addElement("" + chooser.getSelectedFile());
				} else {
					System.out.println("No Selection ");
				}
			}
		});
		//Remove element from list
		btnRemoveFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!lstpath.isSelectionEmpty())
					lstmodel.removeElementAt(lstpath.getSelectedIndex());
				else
					JOptionPane.showMessageDialog(null, "No element selected for removal. ");
			}
		});
		//Save folders path in an array to a JSON file 
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONObject obj = new JSONObject();
				try {
					JSONArray arrylist = new JSONArray();
					for (Object x : lstmodel.toArray())
						arrylist.add(x.toString());
					obj.put("folderpath", arrylist);
					FileWriter file = new FileWriter("folderlist.json");
					file.write(obj.toString());
					file.flush();
					dispose();
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}
		});
		//Cancel changes
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

	}

}
