package burp;

import java.net.URL;

//
// Hold each request
//

class LogEntry {

	final int tool;
	final IHttpRequestResponsePersisted requestResponse;
	final URL url;

	LogEntry(int tool, IHttpRequestResponsePersisted requestResponse, URL url) {
		this.tool = tool;
		this.requestResponse = requestResponse;
		this.url = url;
	}
}
