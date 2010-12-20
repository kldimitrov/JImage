package org.jimageviewer.actions;

import jimageviewer.ICommandIds;
import jimageviewer.View;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class ZoomAction extends Action {
	private final IWorkbenchWindow window;
	private boolean in;
	public ZoomAction(IWorkbenchWindow window, String label, boolean in) {
		this.window = window;
		this.in=in;
		setText(label);
		setActionDefinitionId(ICommandIds.CMD_ZOOMIN);
		setId(ICommandIds.CMD_ZOOMIN);
        // The id is used to refer to the action in a menu or toolbar
	
        // Associate the action with a pre-defined command, to allow key bindings.
		
		
	}
	
	public void run() {
		if(window != null) {	
			if(in==true){
				
				View.picture.zoomIn();
				View.bars.getStatusLineManager().setMessage("Zoom In Successful");
			}
			else if(in==false)
				View.picture.zoomOut();
			View.bars.getStatusLineManager().setMessage("Zoom Out Successful");
		}
	}


}
