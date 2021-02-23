package com.handsome.nosnhoj.impl.Comms.SendProgram;

import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class CommsProgramContribution implements ProgramNodeContribution{

	private final ProgramAPIProvider apiProvider;
	private final CommsProgramView view;
	private final DataModel model;
	
	private static final String KEY_INPUT_MSG = "input_msg";
	private static final String DEF_INPUT_MSG = "";
	
	private KeyboardInputFactory keyboardInputFactory;
	
	public CommsProgramContribution(ProgramAPIProvider apiProvider, CommsProgramView view, DataModel model) {
		this.apiProvider = apiProvider;
		keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.view = view;
		this.model = model;
	}
	
	private CommsInstallationContribution GetInstallationContribution() {
		return apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class);
	}
	
	private void SetUserInput(String in) {
		model.set(KEY_INPUT_MSG, in);
	}
	
	private String GetUserInput() {
		return model.get(KEY_INPUT_MSG, DEF_INPUT_MSG);
	}
	
	public void OnSendClick() {
		try {
			GetInstallationContribution().GetXmlRpc().SendMessage(GetUserInput());
		} catch (Exception e) {
			System.out.println(">>>>> Failed to send message");
		}
	}
	
	public KeyboardTextInput GetInputTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(GetUserInput());
		return keyboardTextInput;
	}
	
	public KeyboardInputCallback<String> GetInputCallback(){
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				SetUserInput(value);
				view.SetTextField(value);
			}
		};
	}
	
	@Override
	public void openView() {
		view.SetTextField(GetUserInput());
	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		//TODO thinking changing to what is actually sent 
		return "send("+GetUserInput()+")";
	}

	@Override
	public boolean isDefined() {
		boolean connected = false;
		try {
			connected = GetInstallationContribution().GetXmlRpc().IsPortConnected();
		} 
		catch (Exception e) {
			System.out.println("!!!attempted send node define failed");
		}
		return GetInstallationContribution().isDefined() && connected;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// TODO Auto-generated method stub
//		writer.assign("test_assign123", "14");	//filler. feel like i need something here.
		writer.assign("msg_to_send","\""+GetUserInput()+"\"");
//		writer.assign("msg_to_send", GetInstallationContribution().GetUserInput());
		writer.appendLine(GetInstallationContribution().GetXmlRpcVariable()+".send_message(msg_to_send)");
	}

}
