package wyyder.ui.tabs;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import wyyder.Wyyder;

public class UniqueReqResTab extends JPanel {

	private static final long serialVersionUID = 13255L;
	private UniqueReqResTableModel uniqueReqResTableModel;

	public UniqueReqResTab() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		uniqueReqResTableModel = new UniqueReqResTableModel();
		UniqueReqResTable uniqueReqResTable = new UniqueReqResTable(uniqueReqResTableModel);
		JScrollPane uniqueReqResScrollPane = new JScrollPane(uniqueReqResTable);
		mainPane.setLeftComponent(uniqueReqResScrollPane);

		// top control panel
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JButton startButton = new JButton("Start");
		controlPanel.add(startButton);
		JButton stopButton = new JButton("Stop");
		controlPanel.add(stopButton);
		JButton clearButton = new JButton("Clear");
		stopButton.setEnabled(false);
		controlPanel.add(clearButton);

		startButton.addActionListener(e -> {
			Wyyder.uniqueReqResListener.setUniqueReqResRunning(true);
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
		});

		stopButton.setEnabled(false);
		stopButton.addActionListener(e -> {
			Wyyder.uniqueReqResListener.setUniqueReqResRunning(false);
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
		});

		clearButton.addActionListener(e -> {
			getUniqueReqResTableModel().getWyyderHttpMessages().clear();
			Wyyder.uniqueReqResListener.getReqCaptured().clear();
			Wyyder.panel.getUniqueReqResTab().getUniqueReqResTableModel().fireTableDataChanged();
		});

		controlPanel.setAlignmentX(0);
		add(controlPanel);

		JSplitPane reqResPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JScrollPane reqPane = new JScrollPane(uniqueReqResTable.getRequestViewer().getComponent());
		reqPane.setName("Request");
		reqResPane.setRightComponent(reqPane);

		JScrollPane resPane = new JScrollPane(uniqueReqResTable.getResponseViewer().getComponent());
		resPane.setName("Response");
		reqResPane.setLeftComponent(resPane);

		mainPane.setRightComponent(reqResPane);

		add(mainPane);
		Wyyder.callback.customizeUiComponent(this);
	}

	public UniqueReqResTableModel getUniqueReqResTableModel() {
		return uniqueReqResTableModel;
	}

}
