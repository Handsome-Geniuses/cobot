package com.handsome.nosnhoj.impl.Paint.PaintProgramNode;

import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.handsome.nosnhoj.impl.Paint.paintconfig;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
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
	private static final Integer DEF_INPUT_DEG = 0;
	
	private static final String KEY_SPEED = "key_speed";
	private static final Integer DEF_SPEED = 100;
	
	private static final String KEY_COMPLETE = "key_complete";
	private static final Boolean DEF_COMPLETE = true;
	
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
	private void SetAngle(Integer in) {
		model.set(KEY_INPUT_DEG, in);
	}
	private Integer GetAngle() {
		return model.get(KEY_INPUT_DEG, DEF_INPUT_DEG);
	}
	
	private void SetMotorSpeed(Integer in) {
		model.set(KEY_SPEED, in);
	}
	private Integer GetMotorSpeed() {
		return model.get(KEY_SPEED, DEF_SPEED);
	}
	private void SetCheckBox(Boolean b) {
		model.set(KEY_COMPLETE, b);
	}
	private Boolean GetCheckBox() {
		return model.get(KEY_COMPLETE, DEF_COMPLETE);
	}
	
	/*========================================================================================
     * Degree numpad listener
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
				SetAngle(value);
				view.SetDegrees(value);
			}
		};
	}
	
	/*========================================================================================
     * Degree +- button listener
     * ======================================================================================*/
	public void OnDegreeChange(final Integer degree) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				SetAngle(degree);
			}
		});
	}
	
	/*========================================================================================
     * Speed slider listener
     * ======================================================================================*/
	//TODO if want to add test command button.
	public void OnSpeedChange(final Integer speed) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				SetMotorSpeed(speed);
			}
		});
	}
	
	/*========================================================================================
     * Checkbox listerner
     * ======================================================================================*/
	public void OnCheckChange(final Boolean b) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				SetCheckBox(b);
			}
		});
	}
	
	
	/*========================================================================================
     * Main Overrides
     * ======================================================================================*/
	@Override
	public void openView() {
		view.SetDegrees(GetAngle());
		view.SetSpeed(GetMotorSpeed());
		view.SetCheck(GetCheckBox());
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
		// TODO set define 
		return true;
	}
	
	@Override
	public void generateScript(ScriptWriter writer) {
		writer.assign("test_assign", "14");	//temp filler
	}

}
