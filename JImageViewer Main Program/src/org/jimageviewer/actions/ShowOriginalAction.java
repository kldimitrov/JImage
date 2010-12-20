package org.jimageviewer.actions;

import jimageviewer.ICommandIds;
import jimageviewer.SWTImageCanvas;
import jimageviewer.View;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class ShowOriginalAction extends Action {
    private final IWorkbenchWindow window;

    public ShowOriginalAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
       // setImageDescriptor(jimageviewer.JImageViewerPlugin.getImageDescriptor("/icons/sample3.gif"));
    }

    public void run() {
    	SWTImageCanvas picture=View.picture;
    if(window!=null){
    	if(picture!=null){
    		picture.showOriginal();
    		}
    	}	
    } 

}
