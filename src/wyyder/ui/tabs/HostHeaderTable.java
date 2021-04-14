package wyyder.ui.tabs;

import javax.swing.JTable;

import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IMessageEditor;
import burp.IMessageEditorController;
import wyyder.Wyyder;
import wyyder.WyyderHttpMessage;

public class HostHeaderTable extends JTable implements IMessageEditorController {

	private static final long serialVersionUID = 1L;

	private HostHeaderTableModel uniqueTableModel;
	private IMessageEditor requestViewer;
	private IMessageEditor responseViewer;
	private IHttpRequestResponse currentlyDisplayedItem;

	HostHeaderTable(HostHeaderTableModel uniqueTableModel) {
		super(uniqueTableModel);

		this.uniqueTableModel = uniqueTableModel;
		this.requestViewer = Wyyder.callback.createMessageEditor(this, false);
		this.responseViewer = Wyyder.callback.createMessageEditor(this, false);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getColumnModel().getColumn(0).setPreferredWidth(20);
		getColumnModel().getColumn(1).setPreferredWidth(50);
		getColumnModel().getColumn(2).setPreferredWidth(150);
		getColumnModel().getColumn(3).setPreferredWidth(40);
		getColumnModel().getColumn(4).setPreferredWidth(60);
		getColumnModel().getColumn(5).setPreferredWidth(250);
		getColumnModel().getColumn(6).setPreferredWidth(100);
		getColumnModel().getColumn(7).setPreferredWidth(40);
		setAutoCreateRowSorter(true);
	}

	@Override
	public byte[] getRequest() {
		return currentlyDisplayedItem.getRequest();
	}

	@Override
	public byte[] getResponse() {
		return currentlyDisplayedItem.getResponse();
	}

	@Override
	public IHttpService getHttpService() {
		return currentlyDisplayedItem.getHttpService();
	}

	@Override
	public void changeSelection(int row, int col, boolean toggle, boolean extend) {
		WyyderHttpMessage message = uniqueTableModel.getWyyderHttpMessages().get(convertRowIndexToModel(row));
		requestViewer.setMessage(message.getRequest(), true);
		responseViewer.setMessage(message.getResponse(), false);
		currentlyDisplayedItem = message.getRequestResponse();
		super.changeSelection(row, col, toggle, extend);
	}

	IMessageEditor getRequestViewer() {
		return requestViewer;
	}

	IMessageEditor getResponseViewer() {
		return responseViewer;
	}

}