package com.handsome.nosnhoj.impl.Paint.PaintProgramNode;

import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.handsome.nosnhoj.impl.Paint.paintconfig;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;

public class PaintProgramContribution implements ProgramNodeContribution{

	private final ProgramAPIProvider apiProvider;
	private final PaintProgramView view;
	private final DataModel model;
	private final UndoRedoManager undoRedoManager;
	
	private KeyboardInputFactory numInput;
	
	private final String xml_var;
	
	private static final String KEY_INPUT_DEG = "input_deg";
	private static final String DEF_INPUT_DEG = "0";
	
	private static final String KEY_SPEED = "key_speed";
	private static final String DEF_SPEED = "100";
	
	/*========================================================================================
     * Constructor
     * ======================================================================================*/
	public PaintProgramContribution(ProgramAPIProvider apiProvider, PaintProgramView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
		
		numInput = this.apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		xml_var = apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class).GetXmlRpcVariable();
	}
	
	/*========================================================================================
     * Get and sets
     * ======================================================================================*/
	private void SetAngle(String in) {
		model.set(KEY_INPUT_DEG, in);
	}
	private String GetAngle() {
		return model.get(KEY_INPUT_DEG, DEF_INPUT_DEG);
	}
	
	private void SetMotorSpeed(String in) {
		model.set(KEY_SPEED, in);
	}
	private String GetMotorSpeed() {
		return model.get(KEY_SPEED, DEF_SPEED);
	}
	
	/*========================================================================================
     * Keyinput Listener
     * ======================================================================================*/
	public KeyboardNumberInput<Integer> GetKeyInput(){
		KeyboardNumberInput<Integer> in = numInput.createIntegerKeypadInput();
		in.setInitialValue((int) 0);
		return in;
	}
	
	public KeyboardInputCallback<Integer> GetInputCallback(){
		return new KeyboardInputCallback<Integer>() {
			@Override
			public void onOk(Integer value) {
				if (value>=paintconfig.max_ang) value = paintconfig.max_ang;
				else if (value<=-paintconfig.max_ang) value = -paintconfig.max_ang;
				SetAngle(Integer.toString(value));
				view.SetDegrees(Integer.toString(value));
			}
		};
	}
	
	/*========================================================================================
     * Other Listener events
     * ======================================================================================*/
	//TODO if want to add test command button.
	
	
	/*========================================================================================
     * Main Overrides
     * ======================================================================================*/
	@Override
	public void openView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getTitle() {
		return "paint";
	}

	@Override
	public boolean isDefined() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void generateScript(ScriptWriter writer) {
		writer.assign("test_assign", "14");	//temp filler
	}

}
