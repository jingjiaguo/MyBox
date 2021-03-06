package mara.mybox.controller;

import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import mara.mybox.controller.ImageManufactureController.ImageOperation;
import mara.mybox.data.DoubleRectangle;
import mara.mybox.fxml.FxmlColor;
import mara.mybox.fxml.FxmlControl;
import static mara.mybox.fxml.FxmlControl.badStyle;
import mara.mybox.fxml.FxmlImageManufacture;
import mara.mybox.value.AppVariables;
import static mara.mybox.value.AppVariables.logger;
import static mara.mybox.value.AppVariables.message;
import mara.mybox.value.CommonValues;

/**
 * @Author Mara
 * @CreateDate 2019-9-8
 * @License Apache License Version 2.0
 */
public class ImageManufactureRichTextController extends ImageManufactureOperationController {

    protected ImageTextController editor;
    protected int rotateAngle, marginsWidth, arc;
    protected float opacity;
    protected WebView webView;
    protected WebEngine webEngine;
    protected boolean isPreview;
    protected Group g;

    @FXML
    protected Rectangle bgRect;
    @FXML
    protected ComboBox<String> angleBox, opacityBox, marginsWidthBox, arcBox;
    @FXML
    protected Button paletteButton;
    @FXML
    protected Slider angleSlider;
    @FXML
    protected FlowPane setBox;
    @FXML
    protected HBox opBox;
    @FXML
    protected Label commentLabel;
    @FXML
    protected Button editButton;

    public ImageManufactureRichTextController() {
        baseTitle = AppVariables.message("ImageManufactureText");
        operation = ImageOperation.RichText;
    }

    @Override
    public void initControls() {
        try {
            super.initControls();
            myPane = richTextPane;

        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

    @Override
    public void initPane(ImageManufactureController parent) {
        try {
            super.initPane(parent);
            if (parent == null) {
                return;
            }

            imageController.imageLabel.setText(message("ImageRichTextTips"));

            marginsWidthBox.getItems().addAll(Arrays.asList("5", "15", "2", "8", "0", "20", "10", "30", "25", "40", "50", "4", "6"));
            marginsWidthBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String oldValue, String newValue) {
                    try {
                        int v = Integer.valueOf(newValue);
                        if (v >= 0) {
                            marginsWidth = v;
                            FxmlControl.setEditorNormal(marginsWidthBox);
                            AppVariables.setUserConfigInt("ImageTextMarginsWidth", v);
                            if (webView != null) {
                                webView.setPrefWidth(imageController.maskRectangleLine.getWidth() - 2 * marginsWidth);
                                webView.setPrefHeight(imageController.maskRectangleLine.getHeight() - 2 * marginsWidth);
                                webView.setLayoutX(imageController.maskRectangleLine.getLayoutX() + marginsWidth);
                                webView.setLayoutY(imageController.maskRectangleLine.getLayoutY() + marginsWidth);
                            }
                        } else {
                            FxmlControl.setEditorBadStyle(marginsWidthBox);
                        }
                    } catch (Exception e) {
                        logger.debug(e.toString());
                        FxmlControl.setEditorBadStyle(marginsWidthBox);
                    }
                }
            });
            marginsWidthBox.getSelectionModel().select(AppVariables.getUserConfigInt("ImageTextMarginsWidth", 15) + "");

            String c = AppVariables.getUserConfigValue("ImageRichTextBackground", Color.WHITE.toString());
            bgRect.setFill(Color.web(c));
            FxmlControl.setTooltip(bgRect, FxmlColor.colorNameDisplay((Color) bgRect.getFill()));
            imageController.maskRectangleLine.setFill(bgRect.getFill());

            arcBox.getItems().addAll(Arrays.asList("20", "10", "5", "15", "2", "8", "0", "30", "25", "40", "50", "4", "6"));
            arcBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String oldValue, String newValue) {
                    try {
                        int v = Integer.valueOf(newValue);
                        if (v >= 0) {
                            arc = v;
                            FxmlControl.setEditorNormal(arcBox);
                            AppVariables.setUserConfigInt("ImageTextArc", v);
                            imageController.maskRectangleLine.setArcWidth(v);
                            imageController.maskRectangleLine.setArcHeight(v);
                        } else {
                            FxmlControl.setEditorBadStyle(arcBox);
                        }
                    } catch (Exception e) {
                        FxmlControl.setEditorBadStyle(arcBox);
                    }
                }
            });
            arcBox.getSelectionModel().select(AppVariables.getUserConfigInt("ImageTextArc", 20) + "");

            opacityBox.getItems().addAll(Arrays.asList("0.5", "1.0", "0.3", "0.1", "0.8", "0.2", "0.9", "0.0"));
            opacityBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String oldValue, String newValue) {
                    try {
                        float f = Float.valueOf(newValue);
                        if (opacity >= 0.0f && opacity <= 1.0f) {
                            opacity = f;
                            AppVariables.setUserConfigInt("ImageTextOpacity", (int) (f * 100));
                            FxmlControl.setEditorNormal(opacityBox);
                            imageController.maskRectangleLine.setOpacity(opacity);
                        } else {
                            FxmlControl.setEditorBadStyle(opacityBox);
                        }
                    } catch (Exception e) {
                        FxmlControl.setEditorBadStyle(opacityBox);
                    }
                }
            });
            opacityBox.getSelectionModel().select((AppVariables.getUserConfigInt("ImageTextOpacity", 50) / 100f) + "");

            angleSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    webView.setRotate(newValue.intValue());
                }
            });

            angleBox.getItems().addAll(Arrays.asList("90", "180", "45", "30", "60", "15", "5", "10", "1", "75", "120", "135"));
            angleBox.setVisibleRowCount(10);
            angleBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String oldValue, String newValue) {
                    try {
                        rotateAngle = Integer.valueOf(newValue);
                        FxmlControl.setEditorNormal(angleBox);
                    } catch (Exception e) {
                        FxmlControl.setEditorBadStyle(angleBox);
                    }
                }
            });
            angleBox.getSelectionModel().select(0);

            rotateLeftButton.disableProperty().bind(
                    angleBox.getEditor().styleProperty().isEqualTo(badStyle)
            );
            rotateLeftButton.disableProperty().bind(
                    angleBox.getEditor().styleProperty().isEqualTo(badStyle)
            );
            setBox.disableProperty().bind(parent.editable.not());
            opBox.disableProperty().bind(parent.editable.not());

            editAction();
            editor.htmlEditor.setHtmlText(message("ImageTextComments"));

        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

    protected void initWebview() {
        if (webView == null) {
            webView = new WebView();
            imageController.webView = webView;
            imageController.maskPane.getChildren().add(webView);
            webEngine = webView.getEngine();
            webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
                @Override
                public void changed(ObservableValue ov, State oldState, State newState) {
                    try {
                        if (newState == State.SUCCEEDED) {
                            htmlLoaded();
                        }
                    } catch (Exception e) {
                        logger.debug(e.toString());
                    }
                }
            });
        }
        webView.setVisible(true);

        if (!imageController.maskRectangleLine.isVisible()) {
            imageController.clearValues();
            imageController.maskRectangleData = new DoubleRectangle(0, 0,
                    imageView.getImage().getWidth() / 2, imageView.getImage().getHeight() / 2);
            imageController.scope.setRectangle(imageController.maskRectangleData.cloneValues());
            imageController.setMaskRectangleLineVisible(true);
            imageController.drawMaskRectangleLineAsData();
            imageController.maskRectangleLine.setFill(bgRect.getFill());
            imageController.maskRectangleLine.setArcWidth(arc);
            imageController.maskRectangleLine.setArcHeight(arc);
            imageController.maskRectangleLine.setOpacity(opacity);
            webView.setPrefWidth(imageController.maskRectangleLine.getWidth() - 2 * marginsWidth);
            webView.setPrefHeight(imageController.maskRectangleLine.getHeight() - 2 * marginsWidth);
            webView.setLayoutX(imageController.maskRectangleLine.getLayoutX() + marginsWidth);
            webView.setLayoutY(imageController.maskRectangleLine.getLayoutY() + marginsWidth);
            angleSlider.setValue(0);
        }
    }

    @Override
    public boolean setColor(Control control, Color color) {
        if (control == null || color == null) {
            return false;
        }
        if (paletteButton.equals(control)) {
            bgRect.setFill(color);
            FxmlControl.setTooltip(bgRect, FxmlColor.colorNameDisplay(color));
            AppVariables.setUserConfigValue("ImageRichTextBackground", color.toString());
            imageController.maskRectangleLine.setFill(bgRect.getFill());
        }
        return true;
    }

    @FXML
    @Override
    public void showPalette(ActionEvent event) {
        showPalette(paletteButton, message("BackgroundColor"), true);
    }

    @FXML
    public void editAction() {
        initWebview();
        if (editor == null || !editor.getMyStage().isShowing()) {
            editor = (ImageTextController) openStage(CommonValues.ImageTextFxml);
            FxmlControl.locateRight(editor.getMyStage());
            editor.init(this);
        } else {
            editor.toFront();
        }
    }

    @FXML
    public void rotateRight() {
        webView.setRotate(webView.getRotate() - rotateAngle);
    }

    @FXML
    public void rotateLeft() {
        webView.setRotate(webView.getRotate() + 360 - rotateAngle);
    }

    @Override
    public void paneClicked(MouseEvent event) {
        if (imageController.scope == null || imageController.maskRectangleData == null) {
            return;
        }
        if (!imageController.scope.getRectangle().same(imageController.maskRectangleData)) {
            imageController.scope.setRectangle(imageController.maskRectangleData.cloneValues());
            double xWidth = marginsWidth * imageView.getBoundsInParent().getWidth() / imageView.getImage().getWidth();
            double yWidth = marginsWidth * imageView.getBoundsInParent().getHeight() / imageView.getImage().getHeight();
            webView.setPrefWidth(imageController.maskRectangleLine.getWidth() - 2 * xWidth);
            webView.setPrefHeight(imageController.maskRectangleLine.getHeight() - 2 * yWidth);
            webView.setLayoutX(imageController.maskRectangleLine.getLayoutX() + xWidth);
            webView.setLayoutY(imageController.maskRectangleLine.getLayoutY() + yWidth);
        }
    }

    public void htmlLoaded() {
        if (!parent.editable.get()) {
            return;
        }
        webEngine.executeScript("document.body.style.backgroundColor = 'rgba(0,0,0,0)';");
        webView.toFront();

    }

    // http://news.kynosarges.org/2017/02/01/javafx-snapshot-scaling/
    private static Image snap(WebView node, boolean keepScale) {
        logger.debug(Screen.getPrimary().getDpi());
        if (Screen.getPrimary().getDpi() == 96) {
            SnapshotParameters spa = new SnapshotParameters();
            spa.setFill(Color.TRANSPARENT);
            return node.snapshot(spa, null);
        }
        double scale = Screen.getPrimary().getDpi() / 96;
        WritableImage image = new WritableImage(
                (int) Math.round(node.getWidth() * scale),
                (int) Math.round(node.getHeight() * scale));
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(scale, scale));
        spa.setFill(Color.TRANSPARENT);
        spa.setDepthBuffer(true);
        Image snap = node.snapshot(spa, image);
        if (keepScale) {
            snap = FxmlImageManufacture.scaleImage(snap,
                    (int) node.getWidth(), (int) node.getHeight());
        }
        return snap;
    }

    protected void makeImage() {
        try {
            if (webView == null || webEngine == null || !webView.isVisible()) {
                return;
            }

            synchronized (this) {
                if (task != null) {
                    return;
                }
                final double rotate = webView.getRotate();
                webView.setRotate(0);
//                webEngine.executeScript("document.body.style.backgroundColor = '"
//                        + FxmlColor.rgb2css((Color) bgRect.getFill()) + "'; opacity:" + opacity);
//                webView.setOpacity(1);
//                webEngine.executeScript("document.body.style.backgroundColor = 'rgba(0,0,0,0)';");

                // http://news.kynosarges.org/2017/02/01/javafx-snapshot-scaling/
                double scale = FxmlControl.dpiScale();
                final WritableImage snap = new WritableImage(
                        (int) Math.round(webView.getWidth() * scale),
                        (int) Math.round(webView.getHeight() * scale));
                final SnapshotParameters parameters = new SnapshotParameters();
                parameters.setTransform(Transform.scale(scale, scale));
                parameters.setFill(Color.TRANSPARENT);
                webView.snapshot(parameters, snap);
                if (!parent.editable.get()) {
                    return;
                }
                task = new SingletonTask<Void>() {
                    private Image transparent, blended;

                    @Override
                    protected boolean handle() {
                        try {
                            transparent = FxmlImageManufacture.replaceColor(snap,
                                    Color.WHITE, Color.TRANSPARENT, 0);

                            blended = FxmlImageManufacture.drawHTML(imageController.imageView.getImage(),
                                    transparent, imageController.maskRectangleData,
                                    (Color) bgRect.getFill(), opacity, arc,
                                    (int) rotate, marginsWidth);

                            if (task == null || isCancelled()) {
                                return false;
                            }

                            return blended != null;
                        } catch (Exception e) {
                            logger.error(e.toString());
                            return false;
                        }
                    }

                    @Override
                    protected void whenSucceeded() {
                        if (isPreview) {
                            webView.setRotate(rotate);
//                            ImageViewerController controller1
//                                    = (ImageViewerController) openStage(CommonValues.ImageFxml);
//                            controller1.loadImage(transparent);
                            ImageViewerController controller
                                    = (ImageViewerController) openStage(CommonValues.ImageViewerFxml);
                            controller.loadImage(blended);
                            controller.setAsPopped();
                        } else {
                            parent.updateImage(ImageOperation.RichText, null, null, blended, cost);
                            webView = null;
                        }
                    }

                };
                parent.openHandlingStage(task, Modality.WINDOW_MODAL);
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            }

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @FXML
    @Override
    public void okAction() {
        if (!parent.editable.get()) {
            return;
        }
        isPreview = false;
        makeImage();
    }

    @FXML
    public void previewAction() {
        if (!parent.editable.get()) {
            return;
        }
        isPreview = true;
        makeImage();
    }

    @Override
    public void quitPane() {
        if (editor != null) {
            editor.closeStage();
        }
        imageController.clearValues();
    }

}
