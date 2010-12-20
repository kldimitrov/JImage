package org.jimageviewer.actions;

import jimageviewer.ICommandIds;
import jimageviewer.View;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class TransparenceAction extends Action {
	private final IWorkbenchWindow window;
	public TransparenceAction(IWorkbenchWindow window, String label) {
		this.window = window;
		setText(label);
        // The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
		
	}
	
	public void run() {
		if(window != null) {	
				View.picture.transparence();
				View.bars.getStatusLineManager().setMessage("Transparence Successful");
		}
	}


}
