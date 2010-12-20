package jimageviewer;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.jimageviewer.actions.BrightnessAction;
import org.jimageviewer.actions.FilterAction;
import org.jimageviewer.actions.FlipImageAction;
import org.jimageviewer.actions.MoveBGAction;
import org.jimageviewer.actions.MoveRBAction;
import org.jimageviewer.actions.MoveRGAction;
import org.jimageviewer.actions.OnlySaveAction;
import org.jimageviewer.actions.OpenImageAction;
import org.jimageviewer.actions.QuitAction;
import org.jimageviewer.actions.RGBAction;
import org.jimageviewer.actions.RotateImageAction;
import org.jimageviewer.actions.SaveImageAction;
import org.jimageviewer.actions.TransparenceAction;
import org.jimageviewer.actions.UndoFilterAction;
import org.jimageviewer.actions.ZoomAction;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
	private OnlySaveAction saveAction;
	private SaveImageAction saveAsAction;
    private IWorkbenchAction aboutAction;
    private OpenImageAction openAction;
    private QuitAction quitAction;
    private MoveBGAction moveBGAction;
    private MoveRBAction moveRBAction;
    private MoveRGAction moveRGAction;
    private RGBAction rgbAction;
    private RotateImageAction rotateLeftAction;
    private RotateImageAction rotateRightAction;
    private RotateImageAction rotateDownAction;
    private FilterAction blurAction;
    private FilterAction sharpenAction;
    private FilterAction edgeDetectAction;
    private FilterAction grayScaleAction;
    private BrightnessAction brightnessAction;
    private FilterAction embossAction;
    private FilterAction negativeAction;
    private FilterAction prewittAction;
    private FilterAction highPassAction;
    private TransparenceAction transparenceAction;
    private FlipImageAction flipHorizontalAction;
    private FlipImageAction flipVerticalAction;
    
    private ZoomAction zoomInAction;
    private ZoomAction zoomOutAction;
    
    private UndoFilterAction undoAction;
    // private ShowOriginalAction showOriginalAction;
    
    private IWorkbenchAction helpAction;
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.
       
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        
        saveAction=new OnlySaveAction(window,"Save");
        register(saveAction);
        
        openAction = new OpenImageAction(window, "Open Image", View.ID);
        register(openAction);
        
        moveBGAction = new MoveBGAction("Blue and Green", window);
        register(moveBGAction);
        
        moveRBAction=new MoveRBAction("Red and Blue",window);
        register(moveRBAction);
        
        moveRGAction=new MoveRGAction("Red and Green",window);
        register(moveRGAction);
        
        rgbAction=new RGBAction("RGB Control",window);
        register(rgbAction);
        
        quitAction=new QuitAction("Quit",window);
        register(quitAction);
        
        undoAction=new UndoFilterAction("Undo", window,true);
        register(undoAction);
              
        flipHorizontalAction=new FlipImageAction("Flip Horizontal",window, true);
        register(flipHorizontalAction);
        
        flipVerticalAction=new FlipImageAction("Flip Vertical",window, false);
        register(flipVerticalAction);
        
        rotateRightAction=new RotateImageAction("270 degrees", window, SWT.RIGHT);
        register(rotateRightAction);
        
        rotateDownAction=new RotateImageAction("180 degrees",window,SWT.DOWN);
        register(rotateDownAction);
        
        rotateLeftAction=new RotateImageAction("90 degrees",window,SWT.LEFT);
        register(rotateLeftAction);
       
        saveAsAction=new SaveImageAction(window,"Save As");
        register(saveAsAction);
        
        blurAction=new FilterAction("Blur Image",window,1);
        register(blurAction);
        
        prewittAction=new FilterAction("Prewitt",window,10);
        register(prewittAction);
        
        highPassAction=new FilterAction("High pass",window,9);
        register(highPassAction);
        
        sharpenAction=new FilterAction("Sharpen Image",window,2);
        register(sharpenAction);
        
        edgeDetectAction=new FilterAction("Edge Detect",window,3);
        register(edgeDetectAction);
        
        grayScaleAction=new FilterAction("GrayScale",window,4);
        register(grayScaleAction);
        
        brightnessAction=new BrightnessAction("Control Brightness",window);
        register(brightnessAction);
        
        embossAction=new FilterAction("Emboss",window,7);
        register(embossAction);
        
        negativeAction=new FilterAction("Negative",window,8);
        register(negativeAction);
        
        transparenceAction=new TransparenceAction(window,"Transparence");
        register(transparenceAction);
        
        zoomInAction=new ZoomAction(window,"Zoom In",true);
        register(zoomInAction);
        
        zoomOutAction=new ZoomAction(window,"Zoom Out",false);
        register(zoomOutAction);
        
        helpAction=ActionFactory.HELP_CONTENTS.create(window);
        register(helpAction);
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager editMenu=new MenuManager("&Edit");
        MenuManager filterMenu=new MenuManager("&Filters");
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
                
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(editMenu);
        menuBar.add(filterMenu);
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        
        // File
        fileMenu.add(openAction);
        fileMenu.add(saveAction);
        fileMenu.add(saveAsAction);
        fileMenu.add(new Separator());
        fileMenu.add(quitAction);
        
        //Effects
        
        editMenu.add(undoAction);
        editMenu.add(new Separator());
        editMenu.add(zoomInAction);
        editMenu.add(zoomOutAction);
       // effectMenu.add(new GroupMarker("zoom"));
        editMenu.add(new Separator());
        editMenu.add(flipHorizontalAction);
        editMenu.add(flipVerticalAction);
        editMenu.add(new Separator());
        	MenuManager Rotate=new MenuManager("&Rotate to","rotate");
        	Rotate.add(rotateLeftAction);
        	Rotate.add(rotateDownAction);
        	Rotate.add(rotateRightAction);        
       editMenu.add(Rotate);
       editMenu.add(new Separator());
        
        	MenuManager RGB=new MenuManager("&Change Colors", "change");
        	RGB.add(moveBGAction);
        	RGB.add(moveRBAction);
        	RGB.add(moveRGAction);
        	RGB.add(rgbAction);
        	
        editMenu.add(RGB);
        editMenu.add(new Separator());
        editMenu.add(brightnessAction);
        editMenu.add(new Separator());
        filterMenu.add(blurAction);
        filterMenu.add(sharpenAction);
        filterMenu.add(edgeDetectAction);
        filterMenu.add(grayScaleAction);
        filterMenu.add(negativeAction);
        filterMenu.add(embossAction);
        filterMenu.add(highPassAction);
        filterMenu.add(prewittAction);
        filterMenu.add(new Separator());
        filterMenu.add(new GroupMarker("aaa"));
        editMenu.add(new GroupMarker("filters"));
        
        // Help
        helpMenu.add(aboutAction);
        helpMenu.add(helpAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        toolbar.add(openAction);
        toolbar.add(new Separator());
        toolbar.add(new GroupMarker("actions"));
        toolbar.add(new Separator());
        toolbar.add(new GroupMarker("other"));
    }


	public void fillTrayItem(MenuManager trayMenu) {
		trayMenu.add(aboutAction);
		trayMenu.add(quitAction);
		
	}
}
