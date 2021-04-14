package wyyder.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import burp.ITab;
import wyyder.ui.tabs.HostHeaderTab;
import wyyder.ui.tabs.SettingsTab;
import wyyder.ui.tabs.UniqueReqResTab;

public class WyyderPanel extends JPanel implements ITab {

	private static final long serialVersionUID = 13254L;

	UniqueReqResTab uniqueReqResTab;
	HostHeaderTab hostHeaderTab;
	SettingsTab settingsTab;

	public WyyderPanel() {
		setLayout(new BorderLayout());
		add(getTabs());
	}

	private JTabbedPane getTabs() {
		JTabbedPane tabs = new JTabbedPane();

		uniqueReqResTab = new UniqueReqResTab();
		hostHeaderTab = new HostHeaderTab();
		settingsTab = new SettingsTab();

		tabs.addTab("Unique", uniqueReqResTab);
		tabs.addTab("Host Header", hostHeaderTab);
		tabs.addTab("Settings", settingsTab);
		return tabs;
	}

	@Override
	public String getTabCaption() {
		return "Wyyder";
	}

	@Override
	public Component getUiComponent() {
		return this;
	}

	public UniqueReqResTab getUniqueReqResTab() {
		return uniqueReqResTab;
	}

	public HostHeaderTab getHostHeaderTab() {
		return hostHeaderTab;
	}

	public SettingsTab getSettingsTab() {
		return settingsTab;
	}
}
