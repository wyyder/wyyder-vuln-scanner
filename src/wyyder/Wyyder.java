package wyyder;

import java.io.PrintWriter;

import burp.IBurpExtenderCallbacks;
import wyyder.ui.WyyderHostHeader;
import wyyder.ui.WyyderPanel;
import wyyder.ui.WyyderUniqueReqRes;

public class Wyyder {

	public static IBurpExtenderCallbacks callback;
	private static PrintWriter stdout, stderr;
	public static WyyderPanel panel;
	public static WyyderUniqueReqRes uniqueReqResListener;
	public static WyyderHostHeader hostHeaderListener;

	public Wyyder(IBurpExtenderCallbacks callback) {
		Wyyder.callback = callback;

		stdout = new PrintWriter(callback.getStdout(), true);
		stderr = new PrintWriter(callback.getStderr(), true);

		// Wyyder UI
		panel = new WyyderPanel();

		// Register http listener
		uniqueReqResListener = new WyyderUniqueReqRes();
		hostHeaderListener = new WyyderHostHeader();

		callback.registerHttpListener(uniqueReqResListener);
		callback.registerHttpListener(hostHeaderListener);

		callback.addSuiteTab(panel);
	}

	public static void log(String value) {
		stdout.println(value);
	}

	public static void error(String value) {
		stderr.println(value);
	}
}
