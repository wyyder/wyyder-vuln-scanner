package wyyder.ui;

import java.util.HashMap;

import burp.IHttpListener;
import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import burp.IResponseInfo;
import wyyder.Wyyder;
import wyyder.WyyderHttpMessage;

public class WyyderUniqueReqRes implements IHttpListener {

	private HashMap<String, Boolean> reqCaptured = new HashMap<>();

	private boolean uniqueReqResRunning = false;

	public HashMap<String, Boolean> getReqCaptured() {
		return reqCaptured;
	}

	public boolean isUniqueReqResRunning() {
		return uniqueReqResRunning;
	}

	public void setUniqueReqResRunning(boolean uniqueReqResRunning) {
		this.uniqueReqResRunning = uniqueReqResRunning;
	}

	@Override
	public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
		if (uniqueReqResRunning) {
			IRequestInfo iReq = Wyyder.callback.getHelpers().analyzeRequest(messageInfo);
			if (Wyyder.callback.isInScope(iReq.getUrl())) {
				if (!messageIsRequest) {
					IResponseInfo iRes = Wyyder.callback.getHelpers().analyzeResponse(messageInfo.getResponse());
					String id = iReq.getMethod() + iReq.getUrl() + iRes.getStatusCode();
					Wyyder.log("Response : " + id);

					String tool = Wyyder.callback.getToolName(toolFlag);
					if (!reqCaptured.containsKey(id)) {
						synchronized (Wyyder.panel.getUniqueReqResTab().getUniqueReqResTableModel()
								.getWyyderHttpMessages()) {
							int row = Wyyder.panel.getUniqueReqResTab().getUniqueReqResTableModel()
									.getWyyderHttpMessages().size();
							addUniqueReqResMessages(tool, messageInfo, row);
						}
						reqCaptured.put(id, true);
					}
				}
			}
		}
	}

	private void addUniqueReqResMessages(String tool, IHttpRequestResponse messageInfo, int row) {
		Wyyder.panel.getUniqueReqResTab().getUniqueReqResTableModel().getWyyderHttpMessages()
				.add(new WyyderHttpMessage(tool, Wyyder.callback.saveBuffersToTempFiles(messageInfo)));
		Wyyder.panel.getUniqueReqResTab().getUniqueReqResTableModel().fireTableRowsInserted(row, row);
	}

}
