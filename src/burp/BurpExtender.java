package burp;

import wyyder.Wyyder;

public class BurpExtender implements IBurpExtender, IExtensionStateListener {

	public static final boolean DEBUG = true;

	private static final String NAME = "Wyyder Vuln Scanner";
	private static final String VERSION = "1.0.0";
	private static final String COMPANY = "Wyyder Inc.";
	private Wyyder wyyder;

	@Override
	public void registerExtenderCallbacks(final IBurpExtenderCallbacks callback) {
		callback.setExtensionName(NAME);

		// Register ourselves as an extension state listener
		callback.registerExtensionStateListener(this);
		wyyder = new Wyyder(callback);

		Wyyder.log("[*] Loaded Extension - " + NAME + " v" + VERSION + " (C) " + COMPANY);
	}

	@Override
	public void extensionUnloaded() {
		Wyyder.log("[*] Unloaded Extension - " + NAME + " v" + VERSION + " (C) " + COMPANY);
	}

}