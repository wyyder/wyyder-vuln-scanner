package wyyder.ui.tabs;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class SettingsTab extends JPanel {

	private static final long serialVersionUID = 13258L;

	public SettingsTab() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	}

}
