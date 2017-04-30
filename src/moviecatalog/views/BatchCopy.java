package moviecatalog.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import moviecatalog.common.Tools;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;

public class BatchCopy extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JProgressBar prgbar;
	private JLabel lblCopy;
	

	private DefaultTableModel model;
	private JLabel lblupdate;
	private String dest;

	public JProgressBar getPrgbar() {
		return prgbar;
	}

	public void setPrgbar(int i) {
		this.prgbar.setValue(i);
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public JLabel getLblCopy() {
		return lblCopy;
	}

	public void setLblCopy(JLabel lblCopy) {
		this.lblCopy = lblCopy;
	}

	public JLabel getLblupdtae() {
		return lblupdate;
	}

	public void setLblupdtae(String string) {
		this.lblupdate.setText(string);
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public void setModel(DefaultTableModel model) {
		this.model = model;
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}

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
	public BatchCopy(DefaultTableModel tblmodel, String dest) {
		this.dest = dest;
		this.model = tblmodel;
		initcomponents();
		copyMovies();
	}
	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////

	private void initcomponents() {
		setTitle("Batch Copy");
		setBounds(100, 100, 450, 163);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(BatchCopy.class.getResource("/moviecatalog/resources/icon.png")));

		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 433, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		prgbar = new JProgressBar();

		lblCopy = new JLabel("Copying Movies");

		lblupdate = new JLabel("");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(180, Short.MAX_VALUE)
					.addComponent(lblCopy)
					.addGap(178))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(100)
					.addComponent(prgbar, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(109, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addGap(200)
					.addComponent(lblupdate)
					.addContainerGap(200, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(35)
					.addComponent(lblCopy)
					.addGap(18)
					.addComponent(prgbar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblupdate)
					.addContainerGap(17, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);

	}
	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for copying the file
	//////////////////////////////////////////////////////////////////////////////////

	private void copyMovies() {
		BatchCopyProgress obj = new BatchCopyProgress(this);
		obj.setDaemon(true);
		obj.start();
	}

	class BatchCopyProgress extends Thread {
		BatchCopy obj;
		private String filefullpath;

		public BatchCopyProgress(BatchCopy obj) {
			// For updating Progress bar reference is needed of calling object
			this.obj = obj;

		}

		// Updating progress bar and status
		public void run() {
			int counter=0,i=0,j=0;
			int n=obj.getModel().getRowCount();
        	for(i=0;i<n;i++)
			{
				if((boolean) obj.getModel().getValueAt(i, 0)){
					counter++;
				    		}
			}
			obj.getPrgbar().setMaximum(counter);
			for (i = 0; i < counter; i++) {
				for (j = 0; j < n; j++) {
					boolean chk = (boolean) (obj.getModel().getValueAt(j, 0));
					if (chk) {
						String moviename = (String) obj.getModel().getValueAt(j, 1);
						filefullpath = Tools.getFullPath(moviename);
						Path source = Paths.get(filefullpath);
						String name = source.getFileName().toString();
						Path destn = Paths.get(dest + name);
						CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
								StandardCopyOption.COPY_ATTRIBUTES };// Maintains User Privilage
						try {
							Files.copy(source, destn, options);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						obj.setPrgbar(obj.getPrgbar().getValue() + 1); // Progress
						// bar
						// updated
						obj.setLblupdtae((i+1)+"/"+counter + " Movies copied");// Movies
						// copied
						// count
						// updated
						obj.getModel().setValueAt(false, j, 0);  //Clearing the Checked boxes one by one
						break;
					}
				}

			}
			
			obj.dispose();
			JOptionPane.showMessageDialog(null, i + " Movies Copied!");

		}
	}
}
