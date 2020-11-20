package com.handsome.nosnhoj.impl.Comms.ReadProgram;

import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class PortReadProgramContribution implements ProgramNodeContribution{
	
	private final String VAR_READ_STRING_VARIABLE;
	private final String XML_RPC_VARIABLE;
	private final CommsInstallationContribution comms;
	
	public PortReadProgramContribution(ProgramAPIProvider apiProvider) {
		this.comms = apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class);
		this.VAR_READ_STRING_VARIABLE = this.comms.GetVarReadString();
		this.XML_RPC_VARIABLE = this.comms.GetXmlRpcVariable();
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
		return "Message Handler";
	}

	@Override
	public boolean isDefined() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.appendLine(XML_RPC_VARIABLE+".msg_dump()");
		writer.appendLine(XML_RPC_VARIABLE+".get_message()");
		writer.appendLine("while(True):");
			writer.appendLine(VAR_READ_STRING_VARIABLE+"="+XML_RPC_VARIABLE+".get_message()");
//			writer.writeChildren();
			writer.appendLine("if("+VAR_READ_STRING_VARIABLE+"!=\"~\""+"):");
				writer.writeChildren();
			writer.appendLine("end");
//		writer.appendLine("popup(dae_ard.read_message(), blocking=True)");
		writer.appendLine("end"); 
	}

}
