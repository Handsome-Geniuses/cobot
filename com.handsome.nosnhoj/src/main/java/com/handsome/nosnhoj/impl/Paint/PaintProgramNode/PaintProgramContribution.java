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
     * TODO speed numpad 
     * ======================================================================================*/
	/*========================================================================================
     * TODO goto button
     * ======================================================================================*/
	
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
     * Script helpers
     * ======================================================================================*/
	private String SendString(String s) {
		return xml_var+".send_message(\""+s+"\")";
	}
	private String GetSpeedString() {
		return SendString("s"+Integer.toString(GetMotorSpeed())+";");
	}
	private String GetDegreeString() {
		return SendString("p"+Integer.toString(GetAngle())+";");
	}
	private String GetDumpString() {
		return xml_var+".msg_dump()";
	}
	private String GetContainsString() {
		return "while not "+xml_var+".string_contains("+xml_var+".get_message(), \"!p\"):";
//		return "while "+xml_var+".get_message() != \"!p\": sleep(0.05) end";	//xml was timing out. think sleep will fix
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
		return "paint("+Integer.toString(GetAngle())+") @"+Integer.toString(GetMotorSpeed())+"%";
	}

	@Override
	public boolean isDefined() {
		boolean connected = false;
		try {
			connected = apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class).GetXmlRpc().IsPortConnected();
		}
		catch (Exception e) {
			System.out.println("!!! paint node define failure.");
		}
		//TODO add a check to see if painter is initialized.
		return connected;
	}
	
	@Override
	public void generateScript(ScriptWriter writer) {
		//TODO complete before next action
//		writer.assign("test_assign", "14");	//temp filler
		
		//for wait, i think flow should be: set speed->dump->move->whileloop
		writer.appendLine(GetSpeedString());
		if(GetCheckBox()) {
			writer.assign("ismovedone", "False");
			writer.appendLine(GetDumpString());
			writer.appendLine(GetDegreeString());
			
			//the following 3 lines are stable but no halt if error
//			writer.appendLine(GetContainsString());
//			writer.appendLine("sleep(0.05)");
//			writer.appendLine("end");
		
			//attempt at timeout. kinda works. need to scale timeout with speed.
//			writer.appendLine("t = "+xml_var+".get_time()");
//			writer.appendLine(GetContainsString());
//			writer.appendLine("sleep(0.05)");
//			writer.appendLine("if "+xml_var+".get_time()-t>5:");
//			writer.appendLine("popup(\"ERROR: paint move timeout.\", blocking=False)");
//			writer.appendLine("halt");
//			writer.appendLine("end");
//			writer.appendLine("sleep(0.05)");
//			writer.appendLine("end");
			
			//attempt at letting arduino notify that something is up.
			writer.appendLine("msg = "+xml_var+".get_message()");
			//wait for 'p'
			writer.appendLine("while not "+xml_var+".string_contains(msg, \"p\"):");
			writer.appendLine("sleep(0.05)");
			writer.appendLine("msg = "+xml_var+".get_message()");
			writer.appendLine("end");
			//check for '?" error
			writer.appendLine("sleep(0.05)");
			writer.appendLine("if "+xml_var+".string_contains(msg, \"?\"):");
			writer.appendLine("popup(\"ERROR: paint move failed.\", blocking=False)");
			writer.appendLine("halt");
			writer.appendLine("end");
			
		}
		else {
			writer.appendLine(GetDegreeString());
		}
	}

}
