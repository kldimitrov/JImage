package jimageviewer;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
	private TrayItem trayItem;
	private Image trayImage;
	private IWorkbenchWindow window;
	public ApplicationActionBarAdvisor actionBarAdvisor;

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(600, 400));
        configurer.setShowCoolBar(true);
        configurer.setShowMenuBar(true);
        configurer.setShowStatusLine(true);
        //getWindowConfigurer().setShellStyle(SWT.NO_BACKGROUND);
        
    }
    @Override
    public void postWindowOpen() {
    	super.postWindowOpen();
    	IStatusLineManager statusLine=getWindowConfigurer()
    		.getActionBarConfigurer().getStatusLineManager();
    	statusLine.setMessage("Welcome!!!");
    	//Working with system tray problem
    	window=getWindowConfigurer().getWindow();
    	trayItem=initTaskItem(window);
    	if(trayItem!=null){
    		createMinimize();
    		hookPopupMenu(window);
    	}
    }
    private void hookPopupMenu(final IWorkbenchWindow window){
    	trayItem.addListener(SWT.MenuDetect, new Listener(){

			public void handleEvent(Event event) {
				MenuManager trayMenu=new MenuManager();
				Menu menu=trayMenu.createContextMenu(window.getShell());
				actionBarAdvisor.fillTrayItem(trayMenu);
				menu.setVisible(true);				
			}
    		
    	});
    }
    
    private void createMinimize(){
    	window.getShell().addShellListener(new ShellAdapter(){
    	public void shellIconified(ShellEvent e){
    		window.getShell().setVisible(false);
    	}
    	});
    	trayItem.addListener(SWT.DefaultSelection, new Listener(){
    		public void handleEvent(Event event){
    			Shell shell=window.getShell();
    			if(!shell.isVisible()){
    				shell.setVisible(true);
    				window.getShell().setMinimized(false);
    			}
    		}
    	});
    }
    private TrayItem initTaskItem(IWorkbenchWindow window){
    	final Tray tray=window.getShell().getDisplay().getSystemTray();
    	TrayItem trayItem=new TrayItem(tray,SWT.NONE);
    	trayImage=AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, "/icons/sample2.gif").createImage();
    	trayItem.setImage(trayImage);
    	trayItem.setToolTipText("JImageViewer");
    	return trayItem;
    	
    }
    
}
