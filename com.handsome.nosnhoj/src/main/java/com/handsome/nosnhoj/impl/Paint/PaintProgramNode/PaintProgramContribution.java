package com.handsome.nosnhoj.impl.Paint.PaintProgramNode;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;

public class PaintProgramContribution implements ProgramNodeContribution{

	private final ProgramAPIProvider apiProvider;
	private final PaintProgramView view;
	private final DataModel model;
	private final UndoRedoManager undoRedoManager;
	
	public PaintProgramContribution(ProgramAPIProvider apiProvider, PaintProgramView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
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
