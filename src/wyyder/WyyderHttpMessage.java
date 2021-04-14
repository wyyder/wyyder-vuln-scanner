package wyyder;

import java.net.URL;

import burp.IHttpRequestResponsePersisted;
import burp.IRequestInfo;
import burp.IResponseInfo;

public class WyyderHttpMessage {

	private String tool;
	private String host;
	private String port;
	private String method;
	private URL url;

	private int statusCode;
	private int responseLength;

	private String hostIP;

	private IHttpRequestResponsePersisted requestResponse;
	private byte[] request;
	private byte[] response;

	public WyyderHttpMessage(String tool, IHttpRequestResponsePersisted messageInfo) {

		IRequestInfo iReq = Wyyder.callback.getHelpers().analyzeRequest(messageInfo);
		IResponseInfo iRes = Wyyder.callback.getHelpers().analyzeResponse(messageInfo.getResponse());

		this.setTool(tool);
		this.setHost(messageInfo.getHttpService().getHost());
		this.setPort(messageInfo.getHttpService().getPort() + "");
		this.setMethod(iReq.getMethod());
		this.setUrl(iReq.getUrl());

		this.setRequest(messageInfo.getRequest());
		this.setResponse(messageInfo.getResponse());
		this.setRequestResponse(messageInfo);

		this.setHostIP("");
		this.setStatusCode(iRes.getStatusCode());
		this.setResponseLength(0);
//		Wyyder.log("Wyyder Http Message Initialized");
	}

	public String getTool() {
		return tool;
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	public String getMethod() {
		return method;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public URL getUrl() {
		return url;
	}

	public String getHostIP() {
		return hostIP;
	}

	public IHttpRequestResponsePersisted getRequestResponse() {
		return requestResponse;
	}

	public byte[] getRequest() {
		return request;
	}

	public byte[] getResponse() {
		return response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public int getResponseLength() {
		return responseLength;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public void setRequestResponse(IHttpRequestResponsePersisted requestResponse) {
		this.requestResponse = requestResponse;
	}

	public void setRequest(byte[] request) {
		this.request = request;
	}

	public void setResponse(byte[] response) {
		this.response = response;
	}

	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setResponseLength(int responseLength) {
		this.responseLength = responseLength;
	}

}