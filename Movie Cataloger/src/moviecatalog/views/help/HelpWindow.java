/*Author:  Shubham Mathur 
 * Project: MovieCataloger
 * Description:This Dialog window shows help screenshots to set up catalog.
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
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class HelpWindow extends JFrame {

	private JPanel contentPane;
	private JLabel lblImage;
	private JButton btnNext;
	private JButton btnPrevious;
	private int helpcount=1; 

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
	public HelpWindow() {
		initComponents();
		createEvents();
	}

	//////////////////////////////////////////////////////////////////////////////////
	//// This method contains all of the code for
	//// creating and initializing components.
	//////////////////////////////////////////////////////////////////////////////////
	private void initComponents() {
		setTitle("Setup your Catalog\r\n");
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(HelpWindow.class.getResource("/moviecatalog/resources/icon.png")));
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
				new ImageIcon(HelpWindow.class.getResource("/moviecatalog/resources/Help/1.png")), 1175, 610));
		btnNext = new JButton("Next");

		btnPrevious = new JButton("Previous");

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap(524, Short.MAX_VALUE)
						.addComponent(btnPrevious).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnNext)
						.addGap(516))
				.addComponent(lblImage, GroupLayout.DEFAULT_SIZE, 1174, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(lblImage, GroupLayout.PREFERRED_SIZE, 619, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnNext)
								.addComponent(btnPrevious))));
		contentPane.setLayout(gl_contentPane);

	}
	
	//////////////////////////////////////////////////////////////
	//// This method contains all of the code for creating events.
	//////////////////////////////////////////////////////////////
	private void createEvents() {
		
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				helpcount=(helpcount-1)%9;
				helpcount=helpcount==0?1:helpcount;
				ImageIcon img=new ImageIcon(HelpWindow.class.getResource("/moviecatalog/resources/Help/"+helpcount+".png"));
				int height=img.getIconHeight()<610?img.getIconHeight():610;
				int width=img.getIconWidth()<1175?img.getIconWidth():1175;	
				lblImage.setIcon(Tools.scaleImage((img), width,height));
			}
		});
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				helpcount=(helpcount+1)%9;
				helpcount=helpcount==0?1:helpcount;
				ImageIcon img=new ImageIcon(HelpWindow.class.getResource("/moviecatalog/resources/Help/"+helpcount+".png"));
				int height=img.getIconHeight()<610?img.getIconHeight():610;
				int width=img.getIconWidth()<1175?img.getIconWidth():1175;
				lblImage.setIcon(Tools.scaleImage((img), width,height));
			}
		});
		
	}
}
