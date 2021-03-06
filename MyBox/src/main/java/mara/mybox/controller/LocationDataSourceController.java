package mara.mybox.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.CheckBoxTreeItem;
import mara.mybox.data.Dataset;
import mara.mybox.fxml.ConditionNode;
import static mara.mybox.value.AppVariables.logger;
import static mara.mybox.value.AppVariables.message;

/**
 * @Author Mara
 * @CreateDate 2020-7-7
 * @License Apache License Version 2.0
 */
public class LocationDataSourceController extends ConditionTreeController {

    protected List<Dataset> datasets;

    public LocationDataSourceController() {
    }

    @Override
    public void initializeNext() {
        try {
            super.initializeNext();
            List<String> s = new ArrayList();
            s.add(message("Dataset"));
            treeView.setSelectedTitles(s);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public void loadTree(List<Dataset> datasets) {
        this.datasets = datasets;
        loadTree();
    }

    @Override
    public void loadTree() {
        if (datasets == null || datasets.isEmpty()) {
            return;
        }
        try {
            CheckBoxTreeItem<ConditionNode> allItem = new CheckBoxTreeItem(
                    ConditionNode.create(message("Dataset"))
                            .setTitle(message("Dataset"))
                            .setCondition("")
            );
            allItem.setExpanded(true);
            treeView.setRoot(allItem);

            for (Dataset dataset : datasets) {
                String name = dataset.getDataSet();
                CheckBoxTreeItem<ConditionNode> datasetItem = new CheckBoxTreeItem(
                        ConditionNode.create(name)
                                .setTitle(name)
                                .setCondition(" datasetid=" + dataset.getId())
                );
                datasetItem.setExpanded(true);
                allItem.getChildren().add(datasetItem);
            }
            treeView.setSelection();
        } catch (Exception e) {
            logger.debug(e.toString());
        }
    }

}
