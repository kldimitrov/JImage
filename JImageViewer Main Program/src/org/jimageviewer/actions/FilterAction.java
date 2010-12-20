package org.jimageviewer.actions;

import jimageviewer.ICommandIds;
import jimageviewer.View;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class FilterAction extends Action {
    private final IWorkbenchWindow window;
    int filter;
    public FilterAction(String text, IWorkbenchWindow window,int filter) {
        super(text);
        this.window = window;
        this.filter=filter;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
       // setImageDescriptor(jimageviewer.JImageViewerPlugin.getImageDescriptor("/icons/sample3.gif"));
    }

    public void run() {
    if(window!=null){
    	if(View.picture!=null){
    		switch (filter){
    						case 1:	View.picture.blurFilter();
    								break;
    						case 2: View.picture.sharpenFilter();
    								break;
    						case 3: View.picture.edgeDetectFilter();
    								break;
    						case 4: View.picture.grayScaleFilter();
    								break;
    						case 5: View.picture.brightnessFilter(1.25f);
    								break;
    						case 6: View.picture.brightnessFilter(0.75f);
    								break;
    						case 7: View.picture.embossFilter();
    								break;
    						case 8: View.picture.negativeFilter();
    								break;
    						case 9: View.picture.highPass();
    								break;
    						case 10: View.picture.prewitt();
    								break;
    						case 11: View.picture.motionFilter();
    								 break;
    						default: break;
    		}
    		}
    	}	
    } 

}
