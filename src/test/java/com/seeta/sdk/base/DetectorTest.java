package com.seeta.sdk.base;


import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 人脸检测器 测试
 */
public class DetectorTest {

    /**
     * 初始化加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    static String dirPath = "D:\\data\\train\\images\\face\\point";
    static String filePath = "D:\\data\\train\\images\\face\\point\\ce.png";

    public static void main(String[] args) {
        File dir = new File(dirPath);
        String[] imagesPath = dir.list();
        if (imagesPath == null) {
            return;
        }
        for (String path : imagesPath) {
            try {
                FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
                BufferedImage image = SeetafaceUtil.toBufferedImage(dirPath + "\\" + path);
                //照片数据
                SeetaImageData imageData = SeetafaceUtil.toSeetaImageData(image);
                //检测到的人脸坐标
                SeetaRect[] detects = detector.Detect(imageData);
                int i = 0;
                for (SeetaRect rect : detects) {
                    System.out.printf("第%s张人脸 x: %s, y: %s, width: %s, height: %s\n", i++, rect.x, rect.y, rect.width, rect.height);
                    image = SeetafaceUtil.writeRect(image, rect);
                }
                SeetafaceUtil.show("人脸检测", image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
