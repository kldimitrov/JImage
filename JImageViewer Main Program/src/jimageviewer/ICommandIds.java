package jimageviewer;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

    public static final String CMD_OPEN = "JImageViewer.open";
    public static final String CMD_OPEN_MESSAGE = "JImageViewer.openMessage";
    public static final String CMD_SAVE="JImageViewer.saveImage";    
    public static final String SHC_FILTER="JImageViewer.FilterScheme";

    public static final String CMD_UNDO="JImageViewer.undo";
    public static final String CMD_ZOOMIN="JImageViewer.zoomIn";
}
