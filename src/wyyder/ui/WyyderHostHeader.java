package wyyder.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import burp.IHttpListener;
import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import wyyder.Wyyder;
import wyyder.WyyderHttpMessage;

public class WyyderHostHeader implements IHttpListener {

	private HashMap<String, Boolean> reqCaptured = new HashMap<>();

	private boolean hostHeaderRunning = false;

	public HashMap<String, Boolean> getReqCaptured() {
		return reqCaptured;
	}

	public boolean isHostHeaderRunning() {
		return hostHeaderRunning;
	}

	public void setHostHeaderRunning(boolean hostHeaderRunning) {
		this.hostHeaderRunning = hostHeaderRunning;
	}

	@Override
	public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
		if (hostHeaderRunning) {
			IRequestInfo iReq = Wyyder.callback.getHelpers().analyzeRequest(messageInfo);
			if (Wyyder.callback.isInScope(iReq.getUrl())) {
				String id = iReq.getMethod() + iReq.getUrl();
				if (messageIsRequest) {
					Wyyder.log("Host Header Request : " + id);
					if (!reqCaptured.containsKey(id)) {

						reqCaptured.put(id, true);
						byte[] rawRequest = messageInfo.getRequest();

						List<String> headers = iReq.getHeaders();
						for (int h = 0; h < headers.size(); h++) {
							if (headers.get(h).startsWith("Host:")) {
								headers.set(h, "Host: wyyder.xyz");
								break;
							}
						}

						byte message[] = Wyyder.callback.getHelpers().buildHttpMessage(headers,
								Arrays.copyOfRange(rawRequest, iReq.getBodyOffset(), rawRequest.length));
						IHttpRequestResponse resp = Wyyder.callback.makeHttpRequest(messageInfo.getHttpService(),
								message);
						Wyyder.log("Host Header Performed : " + id);
					}
				} else {
					if (reqCaptured.containsKey(id)) {
						Wyyder.log("Host Header Response : " + id);
						String tool = "Host Header";
						synchronized (Wyyder.panel.getHostHeaderTab().getHostHeaderTableModel()
								.getWyyderHttpMessages()) {
							int row = Wyyder.panel.getHostHeaderTab().getHostHeaderTableModel().getWyyderHttpMessages()
									.size();
							addHostHeaderMessages(tool, messageInfo, row);
						}
					} else {
						Wyyder.log("Host Header Response Not in list : " + id);
					}
				}
			}
		}
	}

	private void addHostHeaderMessages(String tool, IHttpRequestResponse messageInfo, int row) {
		Wyyder.panel.getHostHeaderTab().getHostHeaderTableModel().getWyyderHttpMessages()
				.add(new WyyderHttpMessage(tool, Wyyder.callback.saveBuffersToTempFiles(messageInfo)));
		Wyyder.panel.getHostHeaderTab().getHostHeaderTableModel().fireTableRowsInserted(row, row);
	}

}
