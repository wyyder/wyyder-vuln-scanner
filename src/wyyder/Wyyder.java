package wyyder;

import java.io.PrintWriter;

import burp.IBurpExtenderCallbacks;
import wyyder.ui.WyyderPanel;
import wyyder.ui.WyyderUniqueReqRes;

public class Wyyder {

	public static IBurpExtenderCallbacks callback;
	private static PrintWriter stdout, stderr;
	public static WyyderPanel panel;
	public static WyyderUniqueReqRes uniqueReqResListener;
	// Settings
	public static boolean isRunning = false;
	public static int toolFilter = 0;

	public Wyyder(IBurpExtenderCallbacks callback) {
		this.callback = callback;
		stdout = new PrintWriter(callback.getStdout(), true);
		stderr = new PrintWriter(callback.getStderr(), true);

		// Wyyder UI
		panel = new WyyderPanel();

		// Register http listener
		uniqueReqResListener = new WyyderUniqueReqRes();

		callback.registerHttpListener(uniqueReqResListener);

		callback.addSuiteTab(panel);
	}

	public static void log(String value) {
		stdout.println(value);
	}

	public static void error(String value) {
		stderr.println(value);
	}
}
