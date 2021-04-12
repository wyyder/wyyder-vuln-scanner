package burp;

import java.awt.Component;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class BurpExtender extends AbstractTableModel implements IBurpExtender, IHttpListener, IProxyListener,
		IScannerListener, IExtensionStateListener, ITab, IMessageEditorController {

	private static final long serialVersionUID = 1L;

	private IBurpExtenderCallbacks callbacks;
	private PrintWriter stdout, stderr;
	private IExtensionHelpers helpers;

	private JSplitPane splitPane;
	private IMessageEditor requestViewer;
	private IMessageEditor responseViewer;
	private final List<LogEntry> log = new ArrayList<LogEntry>();
	private IHttpRequestResponse currentlyDisplayedItem;

	@Override
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		this.callbacks = callbacks;
		callbacks.setExtensionName("Wyyder");

		// obtain stdout object
		stdout = new PrintWriter(callbacks.getStdout(), true);
		// obtain stderr object
		stderr = new PrintWriter(callbacks.getStderr(), true);

		// obtain helpers object
		helpers = callbacks.getHelpers();

		// register ourselves as an HTTP listener
		callbacks.registerHttpListener(this);

		// register ourselves as a Proxy listener
		callbacks.registerProxyListener(this);

		// register ourselves as a Scanner listener
		callbacks.registerScannerListener(this);

		// register ourselves as an extension state listener
		callbacks.registerExtensionStateListener(this);

		// Burp's UI
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// main split pane
				splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

				// table of log entries
				Table logTable = new Table(BurpExtender.this);
				JScrollPane scrollPane = new JScrollPane(logTable);
				splitPane.setLeftComponent(scrollPane);

				// tabs with request/response viewers
				JTabbedPane tabs = new JTabbedPane();
				requestViewer = callbacks.createMessageEditor(BurpExtender.this, false);
				responseViewer = callbacks.createMessageEditor(BurpExtender.this, false);
				tabs.addTab("Request", requestViewer.getComponent());
				tabs.addTab("Response", responseViewer.getComponent());
				splitPane.setRightComponent(tabs);

				// customize our UI components
				callbacks.customizeUiComponent(splitPane);
				callbacks.customizeUiComponent(logTable);
				callbacks.customizeUiComponent(scrollPane);
				callbacks.customizeUiComponent(tabs);

				// UI added
				callbacks.addSuiteTab(BurpExtender.this);
			}
		});

		stdout.println("Wyyder burp registered sucessfully.");
	}

	@Override
	public void newScanIssue(IScanIssue issue) {
		stdout.println("New scan issue: " + issue.getIssueName());
	}

	@Override
	public void processProxyMessage(boolean messageIsRequest, IInterceptedProxyMessage message) {
		stdout.println((messageIsRequest ? "Proxy request to " : "Proxy response from ")
				+ message.getMessageInfo().getHttpService());
	}

	@Override
	public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
		// only process responses
		if (!messageIsRequest) {
			// create a new log entry with the message details
			synchronized (log) {
				int row = log.size();
				log.add(new LogEntry(toolFlag, callbacks.saveBuffersToTempFiles(messageInfo),
						helpers.analyzeRequest(messageInfo).getUrl()));
				fireTableRowsInserted(row, row);
			}
		}
	}

	//
	// IMessageEditorController
	//

	@Override
	public IHttpService getHttpService() {
		return currentlyDisplayedItem.getHttpService();
	}

	@Override
	public byte[] getRequest() {
		return currentlyDisplayedItem.getRequest();
	}

	@Override
	public byte[] getResponse() {
		return currentlyDisplayedItem.getResponse();
	}

	//
	// ITab
	//

	@Override
	public String getTabCaption() {
		return "Wyyder";
	}

	@Override
	public Component getUiComponent() {
		return splitPane;
	}

	//
	// AbstractTableModel
	//

	@Override
	public int getRowCount() {
		return log.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LogEntry logEntry = log.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return callbacks.getToolName(logEntry.tool);
		case 1:
			return logEntry.url.toString();
		default:
			return "";
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Tool";
		case 1:
			return "URL";
		default:
			return "";
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	//
	// JTable to handle cell selection
	//

	private class Table extends JTable {

		private static final long serialVersionUID = 1L;

		public Table(TableModel tableModel) {
			super(tableModel);
		}

		@Override
		public void changeSelection(int row, int col, boolean toggle, boolean extend) {
			// show the log entry for the selected row
			LogEntry logEntry = log.get(row);
			requestViewer.setMessage(logEntry.requestResponse.getRequest(), true);
			responseViewer.setMessage(logEntry.requestResponse.getResponse(), false);
			currentlyDisplayedItem = logEntry.requestResponse;

			super.changeSelection(row, col, toggle, extend);
		}
	}

	//
	// Extension
	//

	@Override
	public void extensionUnloaded() {
		stdout.println("Wyyder Extension unloaded successfully.");
	}

}