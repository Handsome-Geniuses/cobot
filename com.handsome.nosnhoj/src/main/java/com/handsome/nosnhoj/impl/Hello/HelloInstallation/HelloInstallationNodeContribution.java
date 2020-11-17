package com.handsome.nosnhoj.impl.Hello.HelloInstallation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class HelloInstallationNodeContribution implements InstallationNodeContribution{
	
	//installation provider. can use custom style instead.
	private final InstallationAPIProvider apiProvider; 
	
	//installation node class
	private final HelloInstallationNodeView view;
	
	//data model
	private DataModel model;
	
	public HelloInstallationNodeContribution(InstallationAPIProvider apiProvider, HelloInstallationNodeView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
	}
	
	@Override
	public void openView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void generateScript(ScriptWriter writer) {
		// TODO Auto-generated method stub
		writer.assign("test_assign", "14");	//filler. feel like i need something here.
	}

}
