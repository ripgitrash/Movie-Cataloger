package moviecatalog.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import moviecatalog.common.Tools;

import java.awt.Toolkit;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Window.Type;
import java.awt.Point;

public class AboutUs extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel panel;

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

	public AboutUs() {
			initcomponents();
	}
//////////////////////////////////////////////////////////////////////////////////
//// This method contains all of the code for
//// creating and initializing components.
//////////////////////////////////////////////////////////////////////////////////

     private void initcomponents() {
    	 setTitle("About Us");
 		setIconImage(Toolkit.getDefaultToolkit().getImage(AboutUs.class.getResource("/moviecatalog/resources/icon.png")));
 		
 		JPanel panel_1 = new JPanel();
 		GroupLayout groupLayout = new GroupLayout(getContentPane());
 		groupLayout.setHorizontalGroup(
 			groupLayout.createParallelGroup(Alignment.LEADING)
 				.addGroup(groupLayout.createSequentialGroup()
 					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 589, Short.MAX_VALUE)
 					.addContainerGap())
 		);
 		groupLayout.setVerticalGroup(
 			groupLayout.createParallelGroup(Alignment.LEADING)
 				.addGroup(groupLayout.createSequentialGroup()
 					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 361, GroupLayout.PREFERRED_SIZE)
 					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
 		);
 		
 		JLabel lblAbout = new JLabel("");
 		lblAbout.setSize(new Dimension(598, 300));
 		panel_1.add(lblAbout);
 		getContentPane().setLayout(groupLayout);
 	    lblAbout.setIcon(new ImageIcon(AboutUs.class.getResource("/moviecatalog/resources/about.jpg")));// display
 	   setLocation(new Point(400, 180));
		setResizable(false);
		setType(Type.POPUP);
		setSize(new Dimension(580, 380));
		setPreferredSize(new Dimension(600, 400));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		
	}
}
