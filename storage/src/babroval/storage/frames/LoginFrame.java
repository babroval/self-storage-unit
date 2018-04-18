package babroval.storage.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JLabel labelLogin, labelPassword;
	private JTextField tfLogin;
	private JPasswordField tfPassword;
	private JButton mainView;
	private JComboBox<String> comboOrder;
	private String[] select = { "select payment:", "RENT", "ELECTRICITY" };

	public LoginFrame() {
		setSize(260, 175);
		setTitle("Login");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
		action();
		setVisible(true);
		setResizable(false);
	}

	private void initComponents() {

		panel = new JPanel();
		labelLogin = new JLabel("Login");
		labelPassword = new JLabel("Password");
		tfLogin = new JTextField(20);
		tfPassword = new JPasswordField(20);
		mainView = new JButton("MAIN VIEW");
		comboOrder = new JComboBox<String>(select);

		panel.add(labelLogin);
		panel.add(tfLogin);
		panel.add(labelPassword);
		panel.add(tfPassword);
		panel.add(mainView);
		panel.add(comboOrder);

		add(panel);
	}

	private void action() {

		comboOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!tfLogin.getText().equals(" ")) {
					if (comboOrder.getSelectedIndex() == 1) {

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								new RentFrame();
							}
						});
						dispose();
					}
					if (comboOrder.getSelectedIndex() == 2) {
						
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								new ElectricFrame();
							}
						});
						dispose();
					}
				} else {
					JOptionPane.showMessageDialog(panel, "incorrect login", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		mainView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {

				if (("").equals(tfLogin.getText())) {
					char[] pass = tfPassword.getPassword();
					String res = "";
					for (int i = 0; i < pass.length; i++) {
						res += pass[i];
					}
					if (res.equals("")) {

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								new AdminFrame();
							}
						});

						dispose();
					} else {
						JOptionPane.showMessageDialog(panel, "incorrect password", "", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(panel, "incorrect login", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

	}
}
