/*
 * Copyright 2007-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.framework.commons;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 *
 * @author 刘镇 (suninformation@163.com) on 15/1/2 下午4:21
 * @version 1.0
 */
public class QRCodeHelper {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private BitMatrix __matrix;

    private BufferedImage __logoImage;

    private int __logoImageSize;

    private int __borderWidth;

    private Color __borderColor;

    private Color __backgroundColor;

    // 二维码的图片格式
    private String __format;

    private QRCodeHelper(BitMatrix matrix) {
        __matrix = matrix;
    }

    /**
     * @param content      二维码内容字符串
     * @param characterSet 使用的字符编码集，默认UTF-8
     * @param width        二维码图片宽度
     * @param height       二维码图片高度
     * @param margin       二维码图片边距，默认3
     * @param level        二维码容错级别
     * @return 创建二维码工具类实例对象
     * @throws WriterException 可能产生异常
     */
    public static QRCodeHelper create(String content, String characterSet, int width, int height, int margin, ErrorCorrectionLevel level) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        //内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, StringUtils.defaultIfEmpty(characterSet, "UTF-8"));
//        // 以下两行貌似没什么用...
//        hints.put(EncodeHintType.MAX_SIZE, 298);
//        hints.put(EncodeHintType.MIN_SIZE, 235);
        hints.put(EncodeHintType.MARGIN, margin <= 0 ? 3 : margin);
        //设置QR二维码的纠错级别（H为最高级别）
        if (level != null) {
            hints.put(EncodeHintType.ERROR_CORRECTION, level);
        }
        //生成二维码
        return new QRCodeHelper(new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints));
    }

    /**
     * @param content 二维码内容字符串
     * @param width   二维码图片宽度
     * @param height  二维码图片高度
     * @param level   二维码容错级别
     * @return 创建二维码工具类实例对象
     * @throws WriterException 可能产生的异常
     */
    public static QRCodeHelper create(String content, int width, int height, ErrorCorrectionLevel level) throws WriterException {
        return create(content, null, width, height, 0, level);
    }

    /**
     * @param content 二维码内容字符串
     * @param width   二维码图片宽度
     * @param height  二维码图片高度
     * @return 创建二维码工具类实例对象
     * @throws WriterException 可能产生的异常
     */
    public static QRCodeHelper create(String content, int width, int height) throws WriterException {
        return create(content, null, width, height, 0, null);
    }

    /**
     * @param format 图片格式
     * @return 设置二维码图片格式，默认PNG
     */
    public QRCodeHelper setFormat(String format) {
        __format = format;
        return this;
    }

    public QRCodeHelper setLogo(InputStream logoInputStream, int logoImageSize, int borderWidth, Color borderColor, Color backgroundColor) throws IOException {
        return setLogo(ImageIO.read(logoInputStream), logoImageSize, borderWidth, borderColor, backgroundColor);
    }

    public QRCodeHelper setLogo(File logoFile, int logoImageSize, int borderWidth, Color borderColor, Color backgroundColor) throws IOException {
        return setLogo(ImageIO.read(logoFile), logoImageSize, borderWidth, borderColor, backgroundColor);
    }

    public QRCodeHelper setLogo(URL logoUrl, int logoImageSize, int borderWidth, Color borderColor, Color backgroundColor) throws IOException {
        return setLogo(ImageIO.read(logoUrl), logoImageSize, borderWidth, borderColor, backgroundColor);
    }

    public QRCodeHelper setLogo(ImageInputStream logoImageInputStream, int logoImageSize, int borderWidth, Color borderColor, Color backgroundColor) throws IOException {
        return setLogo(ImageIO.read(logoImageInputStream), logoImageSize, borderWidth, borderColor, backgroundColor);
    }

    public QRCodeHelper setLogo(BufferedImage logoImage, int logoImageSize, int borderWidth, Color borderColor, Color backgroundColor) {
        __logoImage = logoImage;
        if (__logoImage != null) {
            __logoImageSize = logoImageSize <= 0 ? 5 : logoImageSize;
            __borderWidth = borderWidth > 0 ? borderWidth : 2;
            __borderColor = borderColor;
            __backgroundColor = backgroundColor;
        }
        return this;
    }

    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(__matrix.getWidth(), __matrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < __matrix.getWidth(); x++) {
            for (int y = 0; y < __matrix.getHeight(); y++) {
                image.setRGB(x, y, __matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        //
        if (__logoImage != null) {
            Graphics2D g = image.createGraphics();
            //
            int _logoWidth = image.getWidth() / __logoImageSize;
            int _logoHeight = image.getHeight() / __logoImageSize;
            // 按比例缩放LOGO
            if (__logoImage.getHeight() >= _logoWidth) {
                _logoHeight = (int) Math.round((__logoImage.getHeight() * _logoWidth * 1.0 / __logoImage.getWidth()));
            } else {
                _logoWidth = (int) Math.round((__logoImage.getWidth() * _logoHeight * 1.0 / __logoImage.getHeight()));
            }
            BufferedImage _resizeLogoImage = new BufferedImage(_logoWidth, _logoHeight, __logoImage.getType());
            _resizeLogoImage.getGraphics().drawImage(__logoImage, 0, 0, _logoWidth, _logoHeight, null);
            //
            int x = (image.getWidth() - _logoWidth) / 2;
            int y = (image.getHeight() - _logoHeight) / 2;
            //
            if (__backgroundColor != null) {
                Color _originColor = g.getColor();
                g.setColor(__backgroundColor);
                g.fillRect(x, y, _logoWidth, _logoHeight);
                g.setColor(_originColor);
            }
            g.drawImage(_resizeLogoImage, x, y, _logoWidth, _logoHeight, null);
            if (__borderColor != null) {
                g.setStroke(new BasicStroke(__borderWidth));
                g.setColor(__borderColor);
                g.drawRect(x, y, _logoWidth, _logoHeight);
            }
            g.dispose();
        }
        //
        return image;
    }

    /**
     * 输出二维码图片到文件
     *
     * @param file 目标文件对象
     * @throws IOException 可能产生的异常
     */
    public void writeToFile(File file) throws IOException {
        BufferedImage image = toBufferedImage();
        if (!ImageIO.write(image, StringUtils.defaultIfEmpty(__format, "png"), file)) {
            throw new IOException("Could not write an image of format " + __format + " to " + file);
        }
    }

    /**
     * 输出二维码图片到输出流
     *
     * @param stream 目标输出流对象
     * @throws IOException 可能产生的异常
     */
    public void writeToStream(OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage();
        if (!ImageIO.write(image, StringUtils.defaultIfEmpty(__format, "png"), stream)) {
            throw new IOException("Could not write an image of format " + __format);
        }
    }

}
