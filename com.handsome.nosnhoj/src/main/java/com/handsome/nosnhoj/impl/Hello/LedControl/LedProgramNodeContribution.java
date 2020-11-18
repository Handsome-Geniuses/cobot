package com.handsome.nosnhoj.impl.Hello.LedControl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;

public class LedProgramNodeContribution implements ProgramNodeContribution{
	private final ProgramAPIProvider apiProvider;
	private final LedProgramNodeView view;
	private final DataModel model;
	private final UndoRedoManager undoRedoManager;
	
	private static final String STATE_KEY = "state";
	private static final boolean[] DEFAULT_STATE = {false, false, false};

	private static final String FLASH_KEY = "flash";
	private static final boolean DEFAULT_FLASH = false;

	private static int NodeNum=0;
	private int ThisNum=0;
	
	public LedProgramNodeContribution(ProgramAPIProvider apiProvider, LedProgramNodeView view,
			DataModel model) {
		// TODO Auto-generated constructor stub
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
		this.ThisNum = NodeNum;
		NodeNum++;
	}
	
	public void FlashChange(final boolean state) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(FLASH_KEY, state);
			}
		});
	}
	
	public void CheckChange(final boolean states[]) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(STATE_KEY, states);
			}
		});
	}
	
	public boolean[] GetStates() {
		return model.get(STATE_KEY, DEFAULT_STATE);
	}
	
	public boolean GetFlash() {
		return model.get(FLASH_KEY, DEFAULT_FLASH);
	}

	@Override
	public void openView() {
		view.SetChecks(GetStates());
		view.SetFlash(GetFlash());
	}
 
	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		String out = "led_";
		boolean states[] = GetStates();
		if(states[0]==true) out = out+"r";
		if(states[1]==true) out = out+"g";
		if(states[2]==true) out = out+"b";
		if(GetFlash()==true) out=out+"f";
		return out;
//		return "led_"+Integer.toString(ThisNum);
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		int pins[] = view.GetPins();
		boolean states[] = GetStates();
		for (int i = 0; i < pins.length; i++) {
			writer.appendLine("set_standard_digital_out("+pins[i]+", "+(states[i]==true ? "True":"False")+")");
		}
	}

}
