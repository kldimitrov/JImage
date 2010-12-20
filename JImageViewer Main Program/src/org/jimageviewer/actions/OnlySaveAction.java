package org.jimageviewer.actions;

import jimageviewer.ICommandIds;
import jimageviewer.View;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class OnlySaveAction extends Action {
	private final IWorkbenchWindow window;
	public OnlySaveAction(IWorkbenchWindow window, String label) {
		this.window = window;
		setText(label);
        // The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN);
        // Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(ICommandIds.CMD_SAVE);
	}
	
	public void run() {
		if(window != null) {	
				View.picture.saveImage(null);
		}
	}


}
