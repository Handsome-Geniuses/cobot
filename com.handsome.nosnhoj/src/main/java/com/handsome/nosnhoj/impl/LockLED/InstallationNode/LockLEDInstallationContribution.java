package com.handsome.nosnhoj.impl.LockLED.InstallationNode;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waypointnode.VariablePositionWaypointNodeConfig.VariableDefinition;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.variable.GlobalVariable;
import com.ur.urcap.api.domain.variable.Variable;
import com.ur.urcap.api.domain.variable.VariableFactory;
import com.ur.urcap.api.domain.variable.Variable.Type;

public class LockLEDInstallationContribution implements InstallationNodeContribution{

	private final InstallationAPIProvider apiProvider;
	private final LockLEDInstallationView view;
	private final DataModel model;
	
	private final GlobalVariable g_FlashStringVar;
	private final GlobalVariable g_FlashEnableVar;
	
	public LockLEDInstallationContribution(InstallationAPIProvider apiProvider,
			LockLEDInstallationView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		

		this.g_FlashStringVar = new GlobalVariable() {
			
			@Override
			public Type getType() {
				return Type.GLOBAL;
			}
			
			@Override
			public String getDisplayName() {
				return "flash_string_command";
			}
		};
		this.g_FlashEnableVar = new GlobalVariable() {
			
			@Override
			public Type getType() {
				return Type.GLOBAL;
			}
			
			@Override
			public String getDisplayName() {
				return "flash_enable";
			}
		};
	}
	
	public String GetFlashStringVariable() {
		return g_FlashStringVar.getDisplayName();
	}
	public String GetFlashEnableVariable() {
		return g_FlashEnableVar.getDisplayName();
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
//		writer.appendLine("global "+FlashStringVariable);
//		writer.appendLine("global "+OnDurationVariable);
//		writer.appendLine("global "+OffDurationVariable);
//		writer.appendLine("global "+FlashEnableVariable);
//		writer.appendLine("global "+FLASH_THREAD_HANDLE);
//		writer.defineThread(THREAD_FUNCTION_NAME);
		
		
//		writer.appendLine("thread ");
//		writer.assign(FlashStringVariable, "\"\"");
//		writer.assign(OnDurationVariable, Float.toString(DEF_ON_DURATION));
//		writer.assign(OffDurationVariable, Float.toString(DEF_OFF_DURATION));
		
	}

}
