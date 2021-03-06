package babroval.storage.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import babroval.storage.model.Electric;
import babroval.storage.model.Storage;
import babroval.storage.model.User;
import babroval.storage.service.ElectricServiceImpl;
import babroval.storage.service.Service;
import babroval.storage.service.StorageServiceImpl;
import babroval.storage.service.UserServiceImpl;
import babroval.storage.util.DateUtil;
import babroval.storage.view.ElectricView;

public class ElectricController {

	private ElectricView view = new ElectricView();
	private Electric electric = new Electric();
	private Service<Electric> electricService = new ElectricServiceImpl<>();
	private Service<Storage> storageService = new StorageServiceImpl<>();
	private Service<User> userService = new UserServiceImpl<>();

	public ElectricController() {
		initView();
	}

	public void initView() {
		try {
			List<String> allStoragesNumbers = new ArrayList<String>();

			allStoragesNumbers = storageService.getAllNumbers();

			for (String storageNum : allStoragesNumbers) {
				view.getComboNum().addItem(storageNum);
			}
		} catch (Exception e) {
			view.getComboNum().setSelectedIndex(0);
			resetFrame();
			JOptionPane.showMessageDialog(view.getPanel(), "database fault", "", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void initController() {

		view.getComboSelect().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (view.getComboSelect().getSelectedIndex() == 1) {

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

							RentController controller = new RentController();
							controller.initController();
						}
					});
					view.dispose();
				}

				if (view.getComboSelect().getSelectedIndex() == 2) {

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

							LoginController controller = new LoginController();
							controller.initController();
						}
					});
					view.dispose();
				}
			}
		});

		view.getComboNum().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

				updateFrame();
			}
		});

		view.getCalculate().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					Integer indicationLastPaid = electric.getMeter_paid();
					Integer indication = new Integer(view.getTfIndication().getText());
					BigDecimal tariff = new BigDecimal(view.getTfTariff().getText());

					BigDecimal sum = electricService.getSum(indicationLastPaid, indication, tariff);
					electric.setTariff(tariff);
					electric.setMeter_paid(indication);
					electric.setSum(sum);

					view.getTfSum().setText(sum.toString());
					view.getTfInf().setEnabled(true);
					view.getEnter().setEnabled(true);
					view.getTfTariff().setEnabled(false);
					view.getTfTariff().setEnabled(false);
					view.getTfIndication().setEnabled(false);
					view.getCalculate().setEnabled(false);
					view.getCancel().setEnabled(true);

					view.getPanel().updateUI();

				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(view.getPanel(),
							"enter the right price per kilowatt-hour and the right indication", "",
							JOptionPane.ERROR_MESSAGE);
					updateFrame();

				} catch (Exception e) {
					view.getComboNum().setSelectedIndex(0);
					resetFrame();
					JOptionPane.showMessageDialog(view.getPanel(), "database fault", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		view.getEnter().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

				try {

					if (view.getComboNum().getSelectedIndex() == 0) {
						throw new NumberFormatException("e");
					}

					electricService.insert(new Electric(
							storageService.getIdByStorageNumber(String.valueOf(view.getComboNum().getSelectedItem())),
							DateUtil.stringToDate(view.getTfDate().getText(), "dd-MM-yyyy"), electric.getTariff(),
							electric.getMeter_paid(), electric.getSum(), view.getTfInf().getText()));

					view.getComboNum().setSelectedIndex(0);
					resetFrame();
					JOptionPane.showMessageDialog(view.getPanel(), "The payment has been successfully included",
							"Message", JOptionPane.INFORMATION_MESSAGE);

				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(view.getPanel(), "Select storage and electric power indication", "",
							JOptionPane.ERROR_MESSAGE);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(view.getPanel(), "database fault", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		view.getCancel().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

				updateFrame();
			}
		});

	}

	private void updateFrame() {

		if (view.getComboNum().getSelectedIndex() == 0) {
			resetFrame();
		} else {
			resetFrame();
			try {
				String userName = userService
						.getNameByStorageNumber(String.valueOf(view.getComboNum().getSelectedItem()));
				view.getTfName().setText(userName);

				electric = electricService
						.getLastPaidByStorageNumber(String.valueOf(view.getComboNum().getSelectedItem()));
				view.getTfIndicationLastPaid().setText(String.valueOf(electric.getMeter_paid()));
				view.getTfTariff().setText(electric.getTariff().toString());

				view.getTfTariff().setEnabled(true);
				view.getTfIndication().setEnabled(true);
				view.getCalculate().setEnabled(true);
			} catch (Exception e) {
				view.getComboNum().setSelectedIndex(0);
				resetFrame();
				JOptionPane.showMessageDialog(view.getPanel(), "database fault", "", JOptionPane.ERROR_MESSAGE);
			}
		}
		view.getPanel().updateUI();
	}

	private void resetFrame() {

		view.getTfName().setText("");
		view.getTfIndicationLastPaid().setText("");
		view.getTfTariff().setText("");
		view.getTfTariff().setEnabled(false);
		view.getTfIndication().setText("");
		view.getTfIndication().setEnabled(false);
		view.getCalculate().setEnabled(false);
		view.getTfSum().setText("");
		view.getTfSum().setEnabled(false);
		view.getTfInf().setText("");
		view.getTfInf().setEnabled(false);
		view.getEnter().setEnabled(false);
		view.getCancel().setEnabled(false);
	}

}
