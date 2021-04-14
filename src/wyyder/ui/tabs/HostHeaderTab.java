package wyyder.ui.tabs;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import wyyder.Wyyder;

public class HostHeaderTab extends JPanel {

	private static final long serialVersionUID = 132574L;
	private HostHeaderTableModel hostHeaderTableModel;

	public HostHeaderTab() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		hostHeaderTableModel = new HostHeaderTableModel();
		HostHeaderTable hostHeaderTable = new HostHeaderTable(hostHeaderTableModel);
		JScrollPane hostHeaderScrollPane = new JScrollPane(hostHeaderTable);
		mainPane.setLeftComponent(hostHeaderScrollPane);

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
			Wyyder.hostHeaderListener.setHostHeaderRunning(true);
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
		});

		stopButton.setEnabled(false);
		stopButton.addActionListener(e -> {
			Wyyder.hostHeaderListener.setHostHeaderRunning(false);
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
		});

		clearButton.addActionListener(e -> {
			getHostHeaderTableModel().getWyyderHttpMessages().clear();
			Wyyder.hostHeaderListener.getReqCaptured().clear();
			Wyyder.panel.getHostHeaderTab().getHostHeaderTableModel().fireTableDataChanged();
		});

		controlPanel.setAlignmentX(0);
		add(controlPanel);

		JSplitPane reqResPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JScrollPane reqPane = new JScrollPane(hostHeaderTable.getRequestViewer().getComponent());
		reqPane.setName("Request");
		reqResPane.setRightComponent(reqPane);

		JScrollPane resPane = new JScrollPane(hostHeaderTable.getResponseViewer().getComponent());
		resPane.setName("Response");
		reqResPane.setLeftComponent(resPane);

		mainPane.setRightComponent(reqResPane);

		add(mainPane);
		Wyyder.callback.customizeUiComponent(this);
	}

	public HostHeaderTableModel getHostHeaderTableModel() {
		return hostHeaderTableModel;
	}

}
