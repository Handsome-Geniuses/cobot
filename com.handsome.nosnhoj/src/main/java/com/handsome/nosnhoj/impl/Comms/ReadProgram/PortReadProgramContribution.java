package com.handsome.nosnhoj.impl.Comms.ReadProgram;

import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class PortReadProgramContribution implements ProgramNodeContribution{
	
	private final String VAR_READ_STRING_VARIABLE;
	
	public PortReadProgramContribution(ProgramAPIProvider apiProvider) {
		this.VAR_READ_STRING_VARIABLE = apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class).GetVarReadString();
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
		return "GetString";
	}

	@Override
	public boolean isDefined() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.appendLine("while(True):");
			writer.appendLine(VAR_READ_STRING_VARIABLE+"=dae_ard.get_message()");	//TODO replace dae_ard with the installation variable
//			writer.writeChildren();
			writer.appendLine("if("+VAR_READ_STRING_VARIABLE+"!=\"~\""+"):");
				writer.writeChildren();
			writer.appendLine("end");
//		writer.appendLine("popup(dae_ard.read_message(), blocking=True)");
		writer.appendLine("end");
	}

}
