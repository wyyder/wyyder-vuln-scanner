package wyyder.ui.tabs;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import wyyder.WyyderHttpMessage;

public class UniqueReqResTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static final ArrayList<WyyderHttpMessage> messages = new ArrayList<>();

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public int getRowCount() {
		return messages.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return row + 1;
		case 1:
			return messages.get(row).getTool();
		case 2:
			return messages.get(row).getHost();
		case 3:
			return messages.get(row).getPort();
		case 4:
			return messages.get(row).getMethod();
		case 5:
			return messages.get(row).getUrl();
		case 6:
			return messages.get(row).getStatusCode();
		case 7:
			return messages.get(row).getHostIP();
		default:
			return "";
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "#";
		case 1:
			return "Tool";
		case 2:
			return "Host";
		case 3:
			return "Port";
		case 4:
			return "Method";
		case 5:
			return "URL";
		case 6:
			return "Status";
		case 7:
			return "IP";
		default:
			return "";
		}
	}

	public ArrayList<WyyderHttpMessage> getWyyderHttpMessages() {
		return messages;
	}
}