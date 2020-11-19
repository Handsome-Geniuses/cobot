package com.handsome.nosnhoj.impl.Comms.ReadProgram;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class PortReadProgramContribution implements ProgramNodeContribution{

	public PortReadProgramContribution() {
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
	public String getTitle() {
		return "read()";
	}

	@Override
	public boolean isDefined() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.appendLine("while(True):");
		writer.appendLine("popup(dae_ard.read_message(), blocking=True)");
		writer.appendLine("end");
	}

}
