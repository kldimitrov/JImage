package jimageviewer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart {
	
	public View() {
	}
	public static final String ID = "JImageViewer.view";
	public static SWTImageCanvas picture;
	public static IActionBars bars;
	public void createPartControl(Composite parent) {
		picture=new SWTImageCanvas(parent);
		bars=getViewSite().getActionBars();
	}

	public void setFocus() {
		picture.setFocus();
	}
	public void dispose(){
		picture.dispose();
		super.dispose();
	}
	}
