package mara.mybox.fxml;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import mara.mybox.data.DoubleCircle;
import mara.mybox.data.DoubleEllipse;
import mara.mybox.data.DoublePenLines;
import mara.mybox.data.DoublePolygon;
import mara.mybox.data.DoublePolyline;
import mara.mybox.data.DoubleRectangle;
import mara.mybox.data.DoubleShape;
import mara.mybox.image.ImageBlend;
import mara.mybox.image.PixelBlend.ImagesBlendMode;
import mara.mybox.image.ImageBlend.ImagesRelativeLocation;
import mara.mybox.image.ImageConvert;
import mara.mybox.image.ImageScope;
import mara.mybox.data.ImageCombine;
import mara.mybox.data.ImageInformation;
import static mara.mybox.value.AppVaribles.logger;

/**
 * @Author Mara
 * @CreateDate 2018-6-11 11:19:42
 * @Description
 * @License Apache License Version 2.0
 */
public class ImageManufacture {

    public class ImageManufactureType {

        public static final int Brighter = 0;
        public static final int Darker = 1;
        public static final int Gray = 2;
        public static final int Invert = 3;
        public static final int Saturate = 4;
        public static final int Desaturate = 5;
    }

    public static java.awt.Color toAwtColor(Color color) {
        java.awt.Color newColor = new java.awt.Color((int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                (int) (color.getOpacity() * 255));
        return newColor;
    }

    public static Image createImage(int width, int height, Color color) {
        WritableImage newImage = new WritableImage(width, height);
        PixelWriter pixelWriter = newImage.getPixelWriter();
        for (int y = 0; y < newImage.getHeight(); y++) {
            for (int x = 0; x < newImage.getWidth(); x++) {
                pixelWriter.setColor(x, y, color);
            }
        }
        return newImage;
    }

    public static Image manufactureImage(Image image, int manuType) {
        PixelReader pixelReader = image.getPixelReader();
        WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = newImage.getPixelWriter();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                switch (manuType) {
                    case ImageManufactureType.Brighter:
                        color = color.brighter();
                        break;
                    case ImageManufactureType.Darker:
                        color = color.darker();
                        break;
                    case ImageManufactureType.Gray:
                        color = color.grayscale();
                        break;
                    case ImageManufactureType.Invert:
                        color = color.invert();
                        break;
                    case ImageManufactureType.Saturate:
                        color = color.saturate();
                        break;
                    case ImageManufactureType.Desaturate:
                        color = color.desaturate();
                        break;
                    default:
                        break;
                }
                pixelWriter.setColor(x, y, color);
            }
        }
        return newImage;
    }

    // https://stackoverflow.com/questions/19548363/image-saved-in-javafx-as-jpg-is-pink-toned
    public static BufferedImage getBufferedImage(Image image) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        return source;
    }

    public static BufferedImage checkAlpha(Image image, String format) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.checkAlpha(source, format);
        return target;
    }

    public static Image clearAlpha(Image image) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.clearAlpha(source);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image scaleImage(Image image, float scale) {
        int targetW = (int) Math.round(image.getWidth() * scale);
        int targetH = (int) Math.round(image.getHeight() * scale);
        return scaleImage(image, targetW, targetH);
    }

    public static Image scaleImage(Image image, int width, int height) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.scaleImage(source, width, height);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image scaleImage(Image image, int width) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.scaleImageWidthKeep(source, width);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image scaleImage(Image image, int width, int height,
            boolean keepRatio, int keepType) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.scaleImage(source, width, height, keepRatio, keepType);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image addText(Image image, String textString,
            java.awt.Font font, Color color, int x, int y,
            float transparent, int shadow, int angle,
            boolean isOutline, boolean isVertical) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.addText(source, textString,
                font, ImageManufacture.toAwtColor(color), x, y, transparent,
                shadow, angle, isOutline, isVertical);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    // This way may fail for big image
    public static Image addTextFx(Image image, String textString,
            Font font, Color color, int x, int y, float transparent, int shadow) {
        try {
            Group group = new Group();

            Text text = new Text(x, y, textString);
            text.setFill(color);
            text.setFont(font);
            if (shadow > 0) {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setOffsetX(shadow);
                dropShadow.setOffsetY(shadow);
                text.setEffect(dropShadow);
            }

            group.getChildren().add(text);

            Blend blend = new Blend(BlendMode.SRC_OVER);
            blend.setBottomInput(new ImageInput(image));
            blend.setOpacity(1.0 - transparent);
            group.setEffect(blend);

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = group.snapshot(parameters, null);
            return newImage;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    public static Image addArc(Image image, int arc, Color bgColor) {
        if (image == null || arc <= 0) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.addArc(source, arc, ImageManufacture.toAwtColor(bgColor));
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    // This way may fail for big image
    public static Image addArcFx(Image image, int arc, Color bgColor) {
        try {
            if (image == null || arc <= 0) {
                return null;
            }
            Group group = new Group();
            double imageWidth = image.getWidth(), imageHeight = image.getHeight();
            Scene scene = new Scene(group);

            ImageView view = new ImageView(image);
            view.setPreserveRatio(true);
            view.setFitWidth(imageWidth);
            view.setFitHeight(imageHeight);

            Rectangle clip = new Rectangle(imageWidth, imageHeight);
            clip.setArcWidth(arc);
            clip.setArcHeight(arc);
            view.setClip(clip);

            group.getChildren().add(view);

            Blend blend = new Blend(BlendMode.SRC_OVER);
            blend.setBottomInput(new ColorInput(0, 0, imageWidth, imageHeight, bgColor));
            group.setEffect(blend);

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = group.snapshot(parameters, null);
            return newImage;

        } catch (Exception e) {
//            logger.error(e.toString());
            return null;
        }

    }

    // This way may fail for big image
    public static Image addArcFx2(Image image, int arc, Color bgColor) {
        try {
            if (image == null || arc <= 0) {
                return null;
            }

            double imageWidth = image.getWidth(), imageHeight = image.getHeight();

            final Canvas canvas = new Canvas(imageWidth, imageHeight);
            final GraphicsContext g = canvas.getGraphicsContext2D();
            g.setGlobalBlendMode(BlendMode.ADD);
            g.setFill(bgColor);
            g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            g.drawImage(image, 0, 0);

            Rectangle clip = new Rectangle(imageWidth, imageHeight);
            clip.setArcWidth(arc);
            clip.setArcHeight(arc);
            canvas.setClip(clip);
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = canvas.snapshot(parameters, null);
            return newImage;

        } catch (Exception e) {
//            logger.error(e.toString());
            return null;
        }

    }

    public static Image addShadowAlpha(Image image, int shadow, Color color) {
        if (image == null || shadow <= 0) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.addShadowAlpha(source, shadow, ImageManufacture.toAwtColor(color));
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image addShadowNoAlpha(Image image, int shadow, Color color) {
        if (image == null || shadow <= 0) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.addShadowNoAlpha(source, shadow, ImageManufacture.toAwtColor(color));
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    // This way may fail for big image
    public static Image addShadowFx(Image image, int shadow, Color color) {
        try {
            if (image == null || shadow <= 0) {
                return null;
            }
            double imageWidth = image.getWidth(), imageHeight = image.getHeight();
            Group group = new Group();
            Scene s = new Scene(group);

            ImageView view = new ImageView(image);
            view.setPreserveRatio(true);
            view.setFitWidth(imageWidth);
            view.setFitHeight(imageHeight);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(shadow);
            dropShadow.setOffsetY(shadow);
            dropShadow.setColor(color);
            view.setEffect(dropShadow);

            group.getChildren().add(view);

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = group.snapshot(parameters, null);
            return newImage;
        } catch (Exception e) {
//            logger.error(e.toString());
            return null;
        }

    }

    public static Image blurMarginsAlpha(Image image, int blurWidth,
            boolean blurTop, boolean blurBottom, boolean blurLeft, boolean blurRight) {
        if (image == null || blurWidth <= 0) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.blurMarginsAlpha(source, blurWidth,
                blurTop, blurBottom, blurLeft, blurRight);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image blurMarginsNoAlpha(Image image, int blurWidth,
            boolean blurTop, boolean blurBottom, boolean blurLeft, boolean blurRight) {
        if (image == null || blurWidth <= 0) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.blurMarginsNoAlpha(source, blurWidth,
                blurTop, blurBottom, blurLeft, blurRight);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image cropOutsideFx(Image image, DoubleShape shape, Color bgColor) {
        try {
            if (image == null || shape == null || !shape.isValid()
                    || bgColor == null) {
                return image;
            }
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            DoubleRectangle bound = shape.getBound();

            int x1 = (int) Math.round(Math.max(0, bound.getSmallX()));
            int y1 = (int) Math.round(Math.max(0, bound.getSmallY()));
            if (x1 >= width || y1 >= height) {
                return image;
            }
            int x2 = (int) Math.round(Math.min(width - 1, bound.getBigX()));
            int y2 = (int) Math.round(Math.min(height - 1, bound.getBigY()));
            int w = x2 - x1 + 1;
            int h = y2 - y1 + 1;
            PixelReader pixelReader = image.getPixelReader();
            WritableImage newImage = new WritableImage(w, h);
            PixelWriter pixelWriter = newImage.getPixelWriter();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (shape.include(x1 + x, y1 + y)) {
                        pixelWriter.setColor(x, y, pixelReader.getColor(x1 + x, y1 + y));
                    } else {
                        pixelWriter.setColor(x, y, bgColor);
                    }
                }
            }
            return newImage;
        } catch (Exception e) {
            logger.debug(e.toString());
            return image;
        }
    }

    public static Image cropOutsideFx(Image image, DoubleRectangle rect) {
        return cropOutsideFx(image, rect, Color.WHITE);
    }

    public static Image cropOutsideFx(Image image, double x1, double y1, double x2, double y2) {
        return cropOutsideFx(image, new DoubleRectangle(x1, y1, x2, y2), Color.WHITE);
    }

    public static Image cropInsideFx(Image image, DoubleShape shape, Color bgColor) {
        if (image == null || shape == null || !shape.isValid()
                || bgColor == null) {
            return image;
        }
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage newImage = new WritableImage(width, height);
        PixelWriter pixelWriter = newImage.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (shape.include(x, y)) {
                    pixelWriter.setColor(x, y, bgColor);
                } else {
                    pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
                }
            }
        }
        return newImage;
    }

    public static Image indicateSplit(Image image,
            List<Integer> rows, List<Integer> cols,
            Color lineColor, int lineWidth, boolean showSize, double scale) {
        if (rows == null || cols == null
                || rows.size() < 2 || cols.size() < 2) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageScope.indicateSplit(source, rows, cols,
                ImageManufacture.toAwtColor(lineColor), lineWidth, showSize, scale);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image drawRectangle(Image image,
            DoubleRectangle rect, Color strokeColor, int strokeWidth,
            int arcWidth, boolean dotted, boolean isFill, Color fillColor) {
        if (rect == null || strokeColor == null) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.drawRectangle(source, rect,
                ImageManufacture.toAwtColor(strokeColor), strokeWidth, arcWidth, dotted,
                isFill, ImageManufacture.toAwtColor(fillColor));
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image drawCircle(Image image,
            DoubleCircle circle, Color strokeColor, int strokeWidth,
            boolean dotted, boolean isFill, Color fillColor) {
        if (circle == null || strokeColor == null) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.drawCircle(source, circle,
                ImageManufacture.toAwtColor(strokeColor), strokeWidth, dotted,
                isFill, ImageManufacture.toAwtColor(fillColor));
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image drawEllipse(Image image,
            DoubleEllipse ellipse, Color strokeColor, int strokeWidth,
            boolean dotted, boolean isFill, Color fillColor) {
        if (ellipse == null || strokeColor == null) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.drawEllipse(source, ellipse,
                ImageManufacture.toAwtColor(strokeColor), strokeWidth, dotted,
                isFill, ImageManufacture.toAwtColor(fillColor));
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image drawPolygon(Image image,
            DoublePolygon polygon, Color strokeColor, int strokeWidth,
            boolean dotted, boolean isFill, Color fillColor) {
        if (polygon == null || strokeColor == null) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.drawPolygon(source, polygon,
                ImageManufacture.toAwtColor(strokeColor), strokeWidth, dotted,
                isFill, ImageManufacture.toAwtColor(fillColor));
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image drawPolyline(Image image,
            DoublePolyline polyline, Color strokeColor, int strokeWidth,
            boolean dotted) {
        if (polyline == null || strokeColor == null) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.drawPolyline(source, polyline,
                ImageManufacture.toAwtColor(strokeColor), strokeWidth, dotted);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image drawLines(Image image,
            DoublePolyline polyline, Color strokeColor, int strokeWidth,
            boolean dotted) {
        if (polyline == null || strokeColor == null) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.drawLines(source, polyline,
                ImageManufacture.toAwtColor(strokeColor), strokeWidth, dotted);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image drawLines(Image image,
            DoublePenLines penData, Color strokeColor, int strokeWidth,
            boolean dotted) {
        if (penData == null || strokeColor == null) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.drawLines(source, penData,
                ImageManufacture.toAwtColor(strokeColor), strokeWidth, dotted);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    // This way may fail for big image
    public static Image indicateSplitFx(Image image,
            List<Integer> rows, List<Integer> cols,
            Color lineColor, int lineWidth, boolean showSize) {
        try {
            if (rows == null || cols == null) {
                return image;
            }
            Group group = new Group();
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            int row;
            for (int i = 0; i < rows.size(); i++) {
                row = rows.get(i);
                if (row <= 0 || row >= height - 1) {
                    continue;
                }
                Line rowLine = new Line(0, row, width, row);
                rowLine.setStroke(lineColor);
                rowLine.setStrokeWidth(lineWidth);
                group.getChildren().add(rowLine);
            }
            int col;
            for (int i = 0; i < cols.size(); i++) {
                col = cols.get(i);
                if (col <= 0 || col >= width - 1) {
                    continue;
                }
                Line colLine = new Line(col, 0, col, height);
                colLine.setStroke(lineColor);
                colLine.setStrokeWidth(lineWidth);
                group.getChildren().add(colLine);
            }

            if (showSize) {
                for (int i = 0; i < rows.size() - 1; i++) {
                    int h = rows.get(i + 1) - rows.get(i) + 1;
                    for (int j = 0; j < cols.size() - 1; j++) {
                        int w = cols.get(j + 1) - cols.get(j) + 1;
                        Text text = new Text();
                        text.setX(cols.get(j) + w / 3);
                        text.setY(rows.get(i) + h / 3);
                        text.setFill(lineColor);
                        text.setText(w + "x" + h);
                        text.setFont(new javafx.scene.text.Font(lineWidth * 3.0));
                        group.getChildren().add(text);
                    }
                }
            }

            Blend blend = new Blend(BlendMode.SRC_OVER);
            blend.setBottomInput(new ImageInput(image));
            group.setEffect(blend);

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = group.snapshot(parameters, null);

            return newImage;
        } catch (Exception e) {
//            logger.error(e.toString());
            return null;
        }
    }

    // This way may fail for big image
    public static Image combineImagesColumnFx(List<Image> images,
            Color bgColor, int interval, int Margin) {
        try {
            if (images == null || images.isEmpty()) {
                return null;
            }
            Group group = new Group();

            int x = Margin, y = Margin, width = 0, height = 0;
            for (int i = 0; i < images.size(); i++) {
                Image image = images.get(i);
                ImageView view = new ImageView(image);
                view.setPreserveRatio(true);
                view.setFitWidth(image.getWidth());
                view.setFitHeight(image.getHeight());
                view.setX(x);
                view.setY(y);
                group.getChildren().add(view);

                x = Margin;
                y += image.getHeight() + interval;

                if (image.getWidth() > width) {
                    width = (int) image.getWidth();
                }
            }

            width += 2 * Margin;
            height = y + Margin - interval;
            Blend blend = new Blend(BlendMode.SRC_OVER);
            blend.setBottomInput(new ColorInput(0, 0, width, height, bgColor));
            group.setEffect(blend);

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = group.snapshot(parameters, null);

            return newImage;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    public static Image combineSingleColumn(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        BufferedImage target = ImageConvert.combineSingleColumn(images);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    // This way may fail for big image
    public static Image combineSingleColumnFx(ImageCombine imageCombine,
            List<ImageInformation> images) {
        if (imageCombine == null || images == null) {
            return null;
        }
        try {
            Group group = new Group();

            double x = imageCombine.getMarginsValue(), y = imageCombine.getMarginsValue(), imageWidth, imageHeight;
            double totalWidth = 0, totalHeight = 0, maxWidth = 0, minWidth = Integer.MAX_VALUE;
            int sizeType = imageCombine.getSizeType();
            if (sizeType == ImageCombine.CombineSizeType.AlignAsBigger) {
                for (ImageInformation image : images) {
                    imageWidth = (int) image.getWidth();
                    if (imageWidth > maxWidth) {
                        maxWidth = imageWidth;
                    }
                }
            }
            if (sizeType == ImageCombine.CombineSizeType.AlignAsSmaller) {
                for (ImageInformation image : images) {
                    imageWidth = (int) image.getWidth();
                    if (imageWidth < minWidth) {
                        minWidth = imageWidth;
                    }
                }
            }
            for (int i = 0; i < images.size(); i++) {
                ImageInformation imageInfo = images.get(i);
                Image image = imageInfo.getImage();
                ImageView view = new ImageView(image);
                view.setPreserveRatio(true);
                view.setX(x);
                view.setY(y);
                if (sizeType == ImageCombine.CombineSizeType.KeepSize
                        || sizeType == ImageCombine.CombineSizeType.TotalWidth || sizeType == ImageCombine.CombineSizeType.TotalHeight) {
                    view.setFitWidth(image.getWidth());
                } else if (sizeType == ImageCombine.CombineSizeType.EachWidth) {
                    view.setFitWidth(imageCombine.getEachWidthValue());
                } else if (sizeType == ImageCombine.CombineSizeType.EachHeight) {
                    view.setFitHeight(imageCombine.getEachHeightValue());
                } else if (sizeType == ImageCombine.CombineSizeType.AlignAsBigger) {
                    view.setFitWidth(maxWidth);
                } else if (sizeType == ImageCombine.CombineSizeType.AlignAsSmaller) {
                    view.setFitWidth(minWidth);
                }
                imageWidth = view.getBoundsInParent().getWidth();
                imageHeight = view.getBoundsInParent().getHeight();
//                logger.debug(imageWidth + " " + imageHeight);
                group.getChildren().add(view);

                x = imageCombine.getMarginsValue();
                y += imageHeight + imageCombine.getIntervalValue();

                if (imageWidth > totalWidth) {
                    totalWidth = imageWidth;
                }
            }

            totalWidth += 2 * imageCombine.getMarginsValue();
            totalHeight = y + imageCombine.getMarginsValue() - imageCombine.getIntervalValue();
            Blend blend = new Blend(BlendMode.SRC_OVER);
            blend.setBottomInput(new ColorInput(0, 0, totalWidth, totalHeight, imageCombine.getBgColor()));
            group.setEffect(blend);

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = group.snapshot(parameters, null);
            return newImage;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    // This way may fail for big image
    public static Image combineSingleRowFx(ImageCombine imageCombine,
            List<ImageInformation> images, boolean isPart) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        try {
            Group group = new Group();
            int x = imageCombine.getMarginsValue(), y = imageCombine.getMarginsValue(), imageWidth, imageHeight;
            int totalWidth = 0, totalHeight = 0, maxHeight = 0, minHeight = Integer.MAX_VALUE;
            if (isPart) {
                y = 0;
            }
            int sizeType = imageCombine.getSizeType();
            if (sizeType == ImageCombine.CombineSizeType.AlignAsBigger) {
                for (ImageInformation image : images) {
                    imageHeight = (int) image.getHeight();
                    if (imageHeight > maxHeight) {
                        maxHeight = imageHeight;
                    }
                }
            }
            if (sizeType == ImageCombine.CombineSizeType.AlignAsSmaller) {
                for (ImageInformation image : images) {
                    imageHeight = (int) image.getHeight();
                    if (imageHeight < minHeight) {
                        minHeight = imageHeight;
                    }
                }
            }
            for (int i = 0; i < images.size(); i++) {
                ImageInformation imageInfo = images.get(i);
                Image image = imageInfo.getImage();
                ImageView view = new ImageView(image);
                view.setPreserveRatio(true);
                view.setX(x);
                view.setY(y);
                if (sizeType == ImageCombine.CombineSizeType.KeepSize
                        || sizeType == ImageCombine.CombineSizeType.TotalWidth || sizeType == ImageCombine.CombineSizeType.TotalHeight) {
                    view.setFitWidth(image.getWidth());
                } else if (sizeType == ImageCombine.CombineSizeType.EachWidth) {
                    view.setFitWidth(imageCombine.getEachWidthValue());
                } else if (sizeType == ImageCombine.CombineSizeType.EachHeight) {
                    view.setFitHeight(imageCombine.getEachHeightValue());
                    logger.debug("EachHeight");
                } else if (sizeType == ImageCombine.CombineSizeType.AlignAsBigger) {
                    view.setFitHeight(maxHeight);
                } else if (sizeType == ImageCombine.CombineSizeType.AlignAsSmaller) {
                    view.setFitHeight(minHeight);
                }
                imageWidth = (int) view.getBoundsInParent().getWidth();
                imageHeight = (int) view.getBoundsInParent().getHeight();
//                logger.debug(imageWidth + " " + imageHeight);
                group.getChildren().add(view);

                x += imageWidth + imageCombine.getIntervalValue();

                if (imageHeight > totalHeight) {
                    totalHeight = imageHeight;
                }
            }

            totalWidth = x + imageCombine.getMarginsValue() - imageCombine.getIntervalValue();
            if (!isPart) {
                totalHeight += 2 * imageCombine.getMarginsValue();
            }
            Blend blend = new Blend(BlendMode.SRC_OVER);
            blend.setBottomInput(new ColorInput(0, 0, totalWidth, totalHeight, imageCombine.getBgColor()));
            group.setEffect(blend);

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = group.snapshot(parameters, null);

            return newImage;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    // This way may fail for big image
    public static Image combineSingleColumnFx(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        try {
            Group group = new Group();

            double x = 0, y = 0, imageWidth, imageHeight;
            double totalWidth = 0, totalHeight = 0;

            for (Image theImage : images) {
                ImageView view = new ImageView(theImage);
                imageWidth = theImage.getWidth();
                imageHeight = theImage.getHeight();

                view.setPreserveRatio(true);
                view.setX(x);
                view.setY(y);
                view.setFitWidth(imageWidth);
                view.setFitHeight(imageHeight);

                group.getChildren().add(view);
                y += imageHeight;

                if (imageWidth > totalWidth) {
                    totalWidth = imageWidth;
                }
            }
            totalHeight = y;
            Blend blend = new Blend(BlendMode.SRC_OVER);
            blend.setBottomInput(new ColorInput(0, 0, totalWidth, totalHeight, Color.TRANSPARENT));
            group.setEffect(blend);

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = group.snapshot(parameters, null);
            return newImage;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    public static Image blendImages(Image foreImage, Image backImage,
            ImagesRelativeLocation location, int x, int y,
            boolean intersectOnly, ImagesBlendMode blendMode, float opacity) {
        if (foreImage == null || backImage == null || blendMode == null) {
            return null;
        }
        BufferedImage source1 = SwingFXUtils.fromFXImage(foreImage, null);
        BufferedImage source2 = SwingFXUtils.fromFXImage(backImage, null);
        BufferedImage target = ImageBlend.blendImages(source1, source2,
                location, x, y, intersectOnly, blendMode, opacity);
        if (target == null) {
            target = source1;
        }
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image blendImages(Image foreImage, Image backImage,
            int x, int y, ImagesBlendMode blendMode, float opacity) {
        if (foreImage == null || backImage == null || blendMode == null) {
            return null;
        }
        BufferedImage source1 = SwingFXUtils.fromFXImage(foreImage, null);
        BufferedImage source2 = SwingFXUtils.fromFXImage(backImage, null);
        BufferedImage target = ImageBlend.blendImages(source1, source2,
                x, y, blendMode, opacity);
        if (target == null) {
            target = source1;
        }
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static boolean isImageSame(Image imageA, Image imageB) {
        if (imageA == null || imageB == null
                || imageA.getWidth() != imageB.getWidth()
                || imageA.getHeight() != imageB.getHeight()) {
            return false;
        }
        PixelReader readA = imageA.getPixelReader();
        PixelReader readB = imageB.getPixelReader();
        for (int y = 0; y < imageA.getHeight(); y++) {
            for (int x = 0; x < imageA.getWidth(); x++) {
                if (!readA.getColor(x, y).equals(readB.getColor(x, y))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int calculateColorDistance2(Color color1, Color color2) {
        double redDiff = (color1.getRed() - color2.getRed()) * 255;
        double greenDiff = (color1.getGreen() - color2.getGreen()) * 255;
        double blueDiff = (color1.getBlue() - color2.getBlue()) * 255;
        int v = (int) Math.round(2 * redDiff * redDiff
                + 4 * greenDiff * greenDiff
                + 3 * blueDiff * blueDiff);
        return v;
    }

    // distance2 = Math.pow(distance, 2)
    // distance: 0-255
    public static boolean isColorMatch2(Color color1, Color color2, int distance2) {
        if (color1.equals(color2)) {
            return true;
        } else if (distance2 == 0) {
            return false;
        }
        return calculateColorDistance2(color1, color2) <= distance2;
    }

    public static Image indicateRectangle(Image image,
            Color color, int lineWidth, DoubleRectangle rect) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = mara.mybox.image.ImageScope.indicateRectangle(source,
                ImageManufacture.toAwtColor(color), lineWidth, rect);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image indicateCircle(Image image,
            Color color, int lineWidth, DoubleCircle circle) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = mara.mybox.image.ImageScope.indicateCircle(source,
                ImageManufacture.toAwtColor(color), lineWidth, circle);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image indicateEllipse(Image image,
            Color color, int lineWidth, DoubleEllipse ellipse) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = mara.mybox.image.ImageScope.indicateEllipse(source,
                ImageManufacture.toAwtColor(color), lineWidth, ellipse);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image cutMarginsByWidth(Image image, int MarginWidth,
            boolean cutTop, boolean cutBottom, boolean cutLeft, boolean cutRight) {

        try {
            int imageWidth = (int) image.getWidth();
            int imageHeight = (int) image.getHeight();

            int top = 0, bottom = imageHeight - 1, left = 0, right = imageWidth - 1;
            if (cutTop) {
                top = MarginWidth;
            }
            if (cutBottom) {
                bottom -= MarginWidth;
            }
            if (cutLeft) {
                left = MarginWidth;
            }
            if (cutRight) {
                right -= MarginWidth;
            }
            return cropOutsideFx(image, left, top, right, bottom);
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }

    }

    public static Image cutMarginsByColor(Image image, Color mColor, int colorDistance,
            boolean cutTop, boolean cutBottom, boolean cutLeft, boolean cutRight) {

        try {
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            PixelReader pixelReader = image.getPixelReader();
//            logger.debug(width + " " + height);

            int top = 0, bottom = height - 1, left = 0, right = width - 1;
            int distance2 = colorDistance * colorDistance;
            if (cutTop) {
                for (int j = 0; j < height; j++) {
                    boolean notMatch = false;
                    for (int i = 0; i < width; i++) {
                        if (!ImageManufacture.isColorMatch2(pixelReader.getColor(i, j), mColor, distance2)) {
//                            logger.debug("notMatch: " + i + " " + j + " " + pixelReader.getColor(i, j));
                            notMatch = true;
                            break;
                        }
                    }
                    if (notMatch) {
                        top = j;
                        break;
                    }
                }
            }
//            logger.debug("top: " + top);
            if (top < 0) {
                return null;
            }
            if (cutBottom) {
                for (int j = height - 1; j >= 0; j--) {
                    boolean notMatch = false;
                    for (int i = 0; i < width; i++) {
                        if (!ImageManufacture.isColorMatch2(pixelReader.getColor(i, j), mColor, distance2)) {
                            notMatch = true;
                            break;
                        }
                    }
                    if (notMatch) {
                        bottom = j;
                        break;
                    }
                }
            }
//            logger.debug("bottom: " + bottom);
            if (bottom < 0) {
                return null;
            }
            if (cutLeft) {
                for (int i = 0; i < width; i++) {
                    boolean notMatch = false;
                    for (int j = 0; j < height; j++) {
                        if (!ImageManufacture.isColorMatch2(pixelReader.getColor(i, j), mColor, distance2)) {
                            notMatch = true;
                            break;
                        }
                    }
                    if (notMatch) {
                        left = i;
                        break;
                    }
                }
            }
//            logger.debug("left: " + left);
            if (left < 0) {
                return null;
            }
            if (cutRight) {
                for (int i = width - 1; i >= 0; i--) {
                    boolean notMatch = false;
                    for (int j = 0; j < height; j++) {
                        if (!ImageManufacture.isColorMatch2(pixelReader.getColor(i, j), mColor, distance2)) {
                            notMatch = true;
                            break;
                        }
                    }
                    if (notMatch) {
                        right = i;
                        break;
                    }
                }
            }
//            logger.debug("right: " + right);
            if (right < 0) {
                return null;
            }

//            logger.debug(left + " " + top + " " + right + " " + bottom);
            return cropOutsideFx(image, left, top, right, bottom);

        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }

    }

    public static Image addMarginsFx(Image image, Color color, int MarginWidth,
            boolean addTop, boolean addBottom, boolean addLeft, boolean addRight) {
        try {
            if (image == null || MarginWidth <= 0) {
                return image;
            }
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            int totalWidth = width, totalHegiht = height;
            int x1 = 0, y1 = 0, x2 = width, y2 = height;
            if (addLeft) {
                totalWidth += MarginWidth;
                x1 = MarginWidth;
                x2 = width + MarginWidth;
            }
            if (addRight) {
                totalWidth += MarginWidth;
            }
            if (addTop) {
                totalHegiht += MarginWidth;
                y1 = MarginWidth;
                y2 = height + MarginWidth;
            }
            if (addBottom) {
                totalHegiht += MarginWidth;
            }
//            logger.debug(width + "  " + totalWidth);

            PixelReader pixelReader = image.getPixelReader();
            WritableImage newImage = new WritableImage(totalWidth, totalHegiht);
            PixelWriter pixelWriter = newImage.getPixelWriter();
            pixelWriter.setPixels(x1, y1, width, height, pixelReader, 0, 0);

//            logger.debug(x1 + "  " + y1);
//            logger.debug(totalWidth + "  " + totalHegiht);
            if (addLeft) {
                for (int x = 0; x < MarginWidth; x++) {
                    for (int y = 0; y < totalHegiht; y++) {
                        pixelWriter.setColor(x, y, color);
                    }
                }
            }
            if (addRight) {
                for (int x = totalWidth - 1; x > totalWidth - MarginWidth - 1; x--) {
                    for (int y = 0; y < totalHegiht; y++) {
                        pixelWriter.setColor(x, y, color);
                    }
                }
            }
            if (addTop) {
                for (int y = 0; y < MarginWidth; y++) {
                    for (int x = 0; x < totalWidth; x++) {
                        pixelWriter.setColor(x, y, color);
                    }
                }
            }
            if (addBottom) {
                for (int y = totalHegiht - 1; y > totalHegiht - MarginWidth - 1; y--) {
                    for (int x = 0; x < totalWidth; x++) {
                        pixelWriter.setColor(x, y, color);
                    }
                }
            }

            return newImage;

        } catch (Exception e) {
            logger.error(e.toString());
            return image;
        }

    }

    public static Image dragMarginsFx(Image image, Color color, DoubleRectangle rect) {
        try {
            if (image == null || rect == null) {
                return image;
            }
            int iwidth = (int) image.getWidth();
            int iheight = (int) image.getHeight();

            int rwidth = (int) rect.getWidth();
            int rheight = (int) rect.getHeight();
            int rx = (int) rect.getSmallX();
            int ry = (int) rect.getSmallY();
            PixelReader pixelReader = image.getPixelReader();
            WritableImage newImage = new WritableImage(rwidth, rheight);
            PixelWriter pixelWriter = newImage.getPixelWriter();
            int ix, iy;
            for (int j = 0; j < rheight; j++) {
                for (int i = 0; i < rwidth; i++) {
                    ix = i + rx;
                    iy = j + ry;
                    if (ix >= 0 && ix < iwidth && iy >= 0 && iy < iheight) {
                        pixelWriter.setColor(i, j, pixelReader.getColor(ix, iy));
                    } else {
                        pixelWriter.setColor(i, j, color);
                    }
                }
            }

            return newImage;

        } catch (Exception e) {
            logger.error(e.toString());
            return image;
        }

    }

    // This way may fail for big image
    public static Image addMarginsFx2(Image image, Color color, int MarginWidth,
            boolean addTop, boolean addBottom, boolean addLeft, boolean addRight) {
        try {
            if (image == null || MarginWidth <= 0) {
                return image;
            }
            Group group = new Group();
            double imageWidth = image.getWidth(), imageHeight = image.getHeight();
            double totalWidth = image.getWidth(), totalHeight = image.getHeight();
            ImageView view = new ImageView(image);
            view.setPreserveRatio(true);
            view.setFitWidth(imageWidth);
            view.setFitHeight(imageHeight);
            if (addLeft) {
                view.setX(MarginWidth);
                totalWidth += MarginWidth;
            } else {
                view.setX(0);
            }
            if (addTop) {
                view.setY(MarginWidth);
                totalHeight += MarginWidth;
            } else {
                view.setY(0);
            }
            if (addBottom) {
                totalHeight += MarginWidth;
            }
            if (addRight) {
                totalWidth += MarginWidth;
            }
            group.getChildren().add(view);

            Blend blend = new Blend(BlendMode.SRC_OVER);
            blend.setBottomInput(new ColorInput(0, 0, totalWidth, totalHeight, color));
            group.setEffect(blend);

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage newImage = group.snapshot(parameters, null);
            return newImage;

        } catch (Exception e) {
            logger.error(e.toString());
            return image;
        }

    }

    public static Image rotateImage(Image image, int angle) {

        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.rotateImage(source, angle);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image horizontalImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage newImage = new WritableImage(width, height);
        PixelWriter pixelWriter = newImage.getPixelWriter();

        for (int j = 0; j < height; j++) {
            int l = 0, r = width - 1;
            while (l <= r) {
                Color cl = pixelReader.getColor(l, j);
                Color cr = pixelReader.getColor(r, j);
                pixelWriter.setColor(l, j, cr);
                pixelWriter.setColor(r, j, cl);
                l++;
                r--;
            }
        }
        return newImage;
    }

    public static Image verticalImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage newImage = new WritableImage(width, height);
        PixelWriter pixelWriter = newImage.getPixelWriter();
        for (int i = 0; i < width; i++) {
            int t = 0, b = height - 1;
            while (t <= b) {
                Color ct = pixelReader.getColor(i, t);
                Color cb = pixelReader.getColor(i, b);
                pixelWriter.setColor(i, t, cb);
                pixelWriter.setColor(i, b, ct);
                t++;
                b--;
            }
        }
        return newImage;
    }

    public static Image shearImage(Image image, float shearX, float shearY) {
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage target = ImageConvert.shearImage(source, shearX, shearY);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

    public static Image makeMosaic(Image image, DoubleShape shape, int size,
            boolean isMosaic, boolean isExcluded) {
        if (!shape.isValid()) {
            return image;
        }
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage newImage = new WritableImage(w, h);
        PixelWriter pixelWriter = newImage.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (isExcluded) {
                    if (shape.include(x, y)) {
                        pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
                    } else {
                        if (isMosaic) {
                            int mx = Math.max(0, Math.min(w - 1, x - x % size));
                            int my = Math.max(0, Math.min(h - 1, y - y % size));
                            pixelWriter.setColor(x, y, pixelReader.getColor(mx, my));
                        } else {
                            int fx = Math.max(0, Math.min(w - 1, x - new Random().nextInt(size)));
                            int fy = Math.max(0, Math.min(h - 1, y - new Random().nextInt(size)));
                            pixelWriter.setColor(x, y, pixelReader.getColor(fx, fy));
                        }
                    }
                } else {
                    if (shape.include(x, y)) {
                        if (isMosaic) {
                            int mx = Math.max(0, Math.min(w - 1, x - x % size));
                            int my = Math.max(0, Math.min(h - 1, y - y % size));
                            pixelWriter.setColor(x, y, pixelReader.getColor(mx, my));
                        } else {
                            int fx = Math.max(0, Math.min(w - 1, x - new Random().nextInt(size)));
                            int fy = Math.max(0, Math.min(h - 1, y - new Random().nextInt(size)));
                            pixelWriter.setColor(x, y, pixelReader.getColor(fx, fy));
                        }
                    } else {
                        pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
                    }
                }
            }

        }
        return newImage;
    }

    public static Image addPicture(Image image, Image picture, int x, int y, int w, int h,
            boolean keepRatio, float transparent) {
        if (image == null) {
            return null;
        }
        if (picture == null) {
            return image;
        }
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);
        BufferedImage pic = SwingFXUtils.fromFXImage(picture, null);
        BufferedImage target = ImageConvert.addPicture(source, pic, x, y, w, h, keepRatio, transparent);
        Image newImage = SwingFXUtils.toFXImage(target, null);
        return newImage;
    }

}