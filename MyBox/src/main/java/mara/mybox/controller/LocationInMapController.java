package mara.mybox.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mara.mybox.data.GeographyCode;
import mara.mybox.db.TableGeographyCode;
import mara.mybox.fxml.FxmlControl;
import mara.mybox.tools.DoubleTools;
import mara.mybox.tools.GeographyCodeTools;
import mara.mybox.tools.LocationTools;
import mara.mybox.value.AppVariables;
import static mara.mybox.value.AppVariables.logger;
import static mara.mybox.value.AppVariables.message;
import mara.mybox.value.CommonValues;

/**
 * @Author Mara
 * @CreateDate 2020-1-20
 * @License Apache License Version 2.0
 */
public class LocationInMapController extends GeographyCodeMapController {

    protected BaseController consumer;
    protected GeographyCode geographyCode;

    @FXML
    protected TextField locateInput;
    @FXML
    protected TextArea dataArea;
    @FXML
    protected ToggleGroup locateGroup;
    @FXML
    protected RadioButton clickMapRadio, addressRadio, coordinateRadio;
    @FXML
    protected CheckBox multipleCheck;
    @FXML
    protected Button clearCodeButton;

    public LocationInMapController() {
        baseTitle = AppVariables.message("LocationInMap");

    }

    @Override
    public void initializeNext() {
        try {
            super.initializeNext();

            mapOptionsController.optionsBox.getChildren().removeAll(mapOptionsController.dataBox);

            setButtons();

            locateGroup.selectedToggleProperty().addListener(
                    (ObservableValue<? extends Toggle> ov, Toggle oldv, Toggle newv) -> {
                        checkLocate();
                    });
            checkLocate();

            multipleCheck.selectedProperty().addListener(
                    (ObservableValue<? extends Boolean> ov, Boolean oldv, Boolean newv) -> {
                        AppVariables.setUserConfigValue(baseName + "MultiplePoints", newv);
                    });
            multipleCheck.setSelected(AppVariables.getUserConfigBoolean(baseName + "MultiplePoints", true));

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @Override
    protected void mapClicked(double longitude, double latitude) {
        setCoordinate(longitude, latitude);
    }

    @Override
    public void setCoordinate(double longitude, double latitude) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (!LocationTools.validCoordinate(longitude, latitude)) {
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (timer == null || !mapOptionsController.mapLoaded) {
                        return;
                    }
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    isSettingValues = true;
                    coordinateRadio.setSelected(true);
                    locateInput.setText(longitude + "," + latitude);
                    locateInput.setDisable(false);
                    startButton.setDisable(false);
                    isSettingValues = false;
                    startAction();
                });
            }
        }, 0, 100);
    }

    protected void checkLocate() {
        if (isSettingValues) {
            return;
        }
        if (coordinateRadio.isSelected()) {
            FxmlControl.setTooltip(locateInput, message("InputCoordinateComments"));
            locateInput.setText("117.0983,36.25551");
            locateInput.setDisable(false);
            startButton.setDisable(false);
        } else if (addressRadio.isSelected()) {
            FxmlControl.setTooltip(locateInput, message("GeographyCodeComments"));
            locateInput.setText("拙政园");
            locateInput.setDisable(false);
            startButton.setDisable(false);
        } else {
            FxmlControl.setTooltip(locateInput, message("InputCoordinateComments"));
            locateInput.setStyle(null);
            locateInput.setText("");
            locateInput.setDisable(true);
            startButton.setDisable(true);
        }
    }

    protected void setButtons() {
        boolean none = geographyCode == null;
        saveButton.setDisable(none);
        clearCodeButton.setDisable(none);
    }

    @FXML
    @Override
    public void startAction() {
        clearCodeAction();
        String value = locateInput.getText().trim();
        if (value.isBlank()) {
            return;
        }
        synchronized (this) {
            if (task != null) {
                return;
            }
            task = new SingletonTask<Void>() {

                @Override
                protected boolean handle() {
                    GeographyCode code;
                    if (coordinateRadio.isSelected()) {
                        try {
                            String[] values = value.split(",");
                            double longitude = DoubleTools.scale6(Double.valueOf(values[0].trim()));
                            double latitude = DoubleTools.scale6(Double.valueOf(values[1].trim()));
                            code = GeographyCodeTools.geoCode(mapOptionsController.coordinateSystem,
                                    longitude, latitude, true);
                        } catch (Exception e) {
                            logger.error(e.toString());
                            return false;
                        }
                    } else {
                        code = GeographyCodeTools.geoCode(mapOptionsController.coordinateSystem, value);
                    }
                    if (code == null) {
                        return false;
                    }
                    geographyCode = code;
                    return true;
                }

                @Override
                protected void whenSucceeded() {
                    if (geographyCodes == null) {
                        geographyCodes = new ArrayList<>();
                    } else if (!multipleCheck.isSelected()) {
                        geographyCodes.clear();
                    }
                    dataArea.setText(geographyCode.info("\n"));
                    setButtons();
                    try {
                        geographyCodes.add((GeographyCode) geographyCode.clone());
                        drawPoints();
                    } catch (Exception e) {
                    }
                    if (consumer != null) {
                        consumer.setGeographyCode(geographyCode);
                        consumer.getMyStage().toFront();
                        if (saveCloseCheck.isSelected()) {
                            closeStage();
                        }
                    }
                }
            };
            openHandlingStage(task, Modality.WINDOW_MODAL);
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }

    }

    @FXML
    public void clearCodeAction() {
        geographyCode = null;
        dataArea.clear();
        setButtons();
    }

    @FXML
    @Override
    public void clearAction() {
        if (mapOptionsController.mapLoaded) {
            webEngine.executeScript("clearMap();");
        }
        geographyCodes.clear();
        clearCodeAction();
    }

    @FXML
    @Override
    public void saveAction() {
        if (geographyCode == null) {
            return;
        }
        GeographyCode code = GeographyCodeTools.encode(geographyCode);
        if (code != null) {
            geographyCode = code;
        } else {
            if (!TableGeographyCode.write(geographyCode)) {
                popFailed();
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(getBaseTitle());
        alert.setContentText(AppVariables.message("Saved"));
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        ButtonType buttonEdit = new ButtonType(AppVariables.message("Edit"));
        ButtonType buttonClose = new ButtonType(AppVariables.message("Close"));
        alert.getButtonTypes().setAll(buttonEdit, buttonClose);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != buttonEdit) {
            return;
        }
        GeographyCodeEditController controller
                = (GeographyCodeEditController) openStage(CommonValues.GeographyCodeEditFxml);
        controller.setGeographyCode(geographyCode);
        controller.getMyStage().toFront();

    }

}
