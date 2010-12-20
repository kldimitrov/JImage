package jimageviewer;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(View.ID,  false, IPageLayout.LEFT, 0.88f, editorArea);
		layout.addStandaloneView(NavigationView.ID, false, IPageLayout.RIGHT, 0.12f, editorArea);
		
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
	}
	static void dispose(){
		dispose();
	}
}
