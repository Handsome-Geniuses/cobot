package com.handsome.nosnhoj.impl.Comms.Daemon;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.parser.StringParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CommsXmlRpc {

	private final XmlRpcClient client;

	public CommsXmlRpc(String host, int port) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setEnabledForExtensions(true);
		try {
			config.setServerURL(new URL("http://" + host + ":" + port + "/RPC2"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		config.setConnectionTimeout(1000); //1s
		client = new XmlRpcClient();
		client.setConfig(config);
	}
	
	public boolean IsPortConnected() throws XmlRpcException, Exception{
		Object result = client.execute("port_check", new ArrayList<String>());
		String msg = processString(result);
		return msg.contains("open");
	}

	public String PortOpen() throws XmlRpcException, Exception{
		Object result = client.execute("port_open", new ArrayList<String>());
		return processString(result);
	}
	
	public String PortOpen(String port, String rate) throws XmlRpcException, Exception{
		ArrayList<String> args = new ArrayList<String>();
		args.add(port);
		args.add(rate);
		Object result = client.execute("port_open", args);
		return processString(result);
		
	}
	
	public String PortClose() throws XmlRpcException, Exception{
		Object result = client.execute("port_close", new ArrayList<String>());
		return processString(result);
	}
	
	public String GetPorts() throws XmlRpcException, Exception{
		Object result = client.execute("get_ports", new ArrayList<String>());
		return processString(result);
	}
	
	public String SendMessage(String msg) throws XmlRpcException, Exception {
		ArrayList<String> args = new ArrayList<String>();
		args.add(msg);
		Object result = client.execute("send_message", args);
		return processString(result);
	}
	
	public String PortRead() throws XmlRpcException, Exception {
		Object result = client.execute("get_message", new ArrayList<String>());
		return processString(result);
	}
	
	public String PortDump() throws XmlRpcException, Exception{
		Object result = client.execute("msg_dump", new ArrayList<String>());
		return processString(result);
	}

	private String processString(Object response) throws Exception {
		if (response instanceof String) {
			return (String) response;
		} 
		else {
			throw new Exception();
		}
	}
	
}

