/*Author:  Shubham Mathur 
 * Project: MovieCataloger
 * Description:This Dialog window shows help screenshots on how to use copy.
 * 
*/
package moviecatalog.views.help;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import moviecatalog.common.Tools;

import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.Color;

public class HelpWindowCopy extends JFrame {

	private JPanel contentPane;
	private JLabel lblImage;

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
	 * Create the frame.
	 */
	public HelpWindowCopy() {
				
		initComponents();
		
	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	private void initComponents() {
		setTitle("How To Copy\r\n");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(HelpWindowCopy.class.getResource("/moviecatalog/resources/icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 10, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		lblImage = new JLabel("");
		lblImage.setOpaque(true);
		lblImage.setBackground(Color.WHITE);
		lblImage.setAlignmentY(Component.TOP_ALIGNMENT);
		lblImage.setIcon(Tools.scaleImage(
				new ImageIcon(HelpWindowCopy.class.getResource("/moviecatalog/resources/Help/9.png")), 1175, 648));

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblImage,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addComponent(lblImage,
				Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE));
		contentPane.setLayout(gl_contentPane);

	}
}
