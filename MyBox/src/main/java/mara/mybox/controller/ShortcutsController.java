package mara.mybox.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import mara.mybox.data.StringTable;
import mara.mybox.tools.HtmlTools;
import static mara.mybox.value.AppVariables.logger;
import static mara.mybox.value.AppVariables.message;

/**
 * @Author Mara
 * @CreateDate 2018-6-20
 * @Description
 * @License Apache License Version 2.0
 */
public class ShortcutsController extends HtmlViewerController {

    @FXML
    protected HBox iccBox;
    @FXML
    protected ComboBox<String> indexSelector;

    public ShortcutsController() {
        baseTitle = message("Shortcuts");

    }

    @Override
    public void initializeNext() {
        try {
            displayHtml();
        } catch (Exception e) {

        }
    }

    public void key(String key1, String key2, String action, String alt) {
        List<String> row = new ArrayList<>();
        row.addAll(Arrays.asList(key1, key2, action, alt));
        table.add(row);
    }

    @Override
    public void displayHtml() {
        try {
            if (table == null) {
                table();
            }
            html = HtmlTools.html(message("Shortcuts"), htmlStyle, StringTable.tableDiv(table));
            webView.getEngine().loadContent​(html);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public void table() {
        try {
            List<String> names = new ArrayList<>();
            names.addAll(Arrays.asList(message("FunctionKey"), message("KeyCombination"),
                    message("Action"), message("PossibleAlternative"))
            );
            table = new StringTable(names, message("Shortcuts"));

            key("F1", "", message("Start") + ", " + message("OK") + ", " + message("Set") + ", " + message("Query"), "CTRL+e / ALT+e, CTRL+q / ALT+q");
            key("F2", "", message("Save"), "CTRL+s / ALT+s");
            key("F3", "", message("Recover") + ", " + message("Export"), "CTRL+r / ALT+r, CTRL+e / ALT+e ");
            key("F4", "", message("CloseStage"), "");
            key("F5", "", message("RefreshStage"), "");
            key("F6", "", message("ClosePopup"), "");
            key("F11", "", message("SaveAs"), "CTRL+f / ALT+f");
            key("F12", "", message("MoreControls"), "CTRL+m / ALT+m");
            key("DELETE", "", message("Delete"), "CTRL+d / ALT+d");
            key("PAGE_UP", "", message("Previous"), "ALT+PAGE_UP");
            key("PAGE_DOWN", "", message("Next"), "ALT+PAGE_DOWN");
            key("ESCAPE", "", message("Cancel"), "");

            key("CTRL", "e", message("Start") + "," + message("OK") + ", " + message("Set") + ", " + message("Export"), "F1 / ALT+e");
            key("CTRL", "c", message("Copy"), "ALT+c");
            key("CTRL", "v", message("Paste"), "ALT+v");
            key("CTRL", "z", message("Undo"), "ALT+z");
            key("CTRL", "y", message("Redo"), "ALT+y");
            key("CTRL", "d", message("Delete"), "DELETE / ALT+d");
            key("CTRL", "x", message("Crop"), "ALT+x");
            key("CTRL", "s", message("Save"), "F2 / ALT+s");
            key("CTRL", "f", message("SaveAs"), "F3 / ALT+f");
            key("CTRL", "r", message("Recover") + "," + message("Clear"), "F11 / ALT+r");
            key("CTRL", "m", message("MoreControls"), "F12 / ALT+m");
            key("CTRL", "n", message("Create"), "");
            key("CTRL", "a", message("SelectAll"), "ALT+a");
            key("CTRL", "g", message("Clear"), "ALT+g");
            key("CTRL", "p", message("Pop"), "ALT+p");
            key("CTRL", "q", message("Query"), "ALT+q");
            key("CTRL", "1", message("OriginalSize") + " / " + message("First"), "");
            key("CTRL", "2", message("PaneSize") + " / " + message("Previous"), "");
            key("CTRL", "3", message("ZoomIn") + " / " + message("Next"), "");
            key("CTRL", "4", message("ZoomOut") + " / " + message("Last"), "");
            key("CTRL", "q", message("Replace"), "");
            key("CTRL", "w", message("ReplaceAll"), "");
            key("CTRL", "-", message("DecreaseFontSize"), "");
            key("CTRL", "=", message("IncreaseFontSize"), "");

            key("ALT", "1", message("Set"), "F1");
            key("ALT", "2", message("Increase"), "");
            key("ALT", "3", message("Decrease"), "");
            key("ALT", "4", message("Filter"), "");
            key("ALT", "5", message("Invert"), "");
            key("ALT", "PAGE_UP", message("Previous"), "PAGE_UP");
            key("ALT", "PAGE_DOWN", message("Next"), "PAGE_DOWN");
            key("ALT", "HOME", message("First"), "");
            key("ALT", "END", message("Last"), "");

            key("s / S", "", message("Play") + "/" + message("Pause"), "");
            key("q / Q", "", message("Stop"), "");
            key("m / M", "", message("Mute") + "/" + message("Sound"), "");
            key("f / F", "", message("FullScreen"), "");

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

}
