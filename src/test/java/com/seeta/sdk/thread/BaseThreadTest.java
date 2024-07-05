package com.seeta.sdk.thread;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;


/**
 * 单个对象测试
 */
public class BaseThreadTest {


    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_GPU);
    }

    public static void main(String[] args) {
//创建多线程对象
        MyThread mt1 = new MyThread();
        MyThread mt2 = new MyThread();
        MyThread mt3 = new MyThread();


        //调用start()方法，其内部调用了run()方法，实现了多线程
        //在这里直接调用run()方法，不能实现多线程
        mt1.start();
        mt2.start();
        mt3.start();

    }

    /**
     * 获取特征数组
     *
     * @author YaoCai Lin
     * @time 2020年7月15日 下午12:10:56
     */
    private static void extract() {
        FaceDetector detector = null;
        FaceLandmarker faceLandmarker = null;
        FaceRecognizer faceRecognizer = null;
        try {
            detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_GPU));
            faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_GPU));
            faceRecognizer = new FaceRecognizer(new SeetaModelSetting(FileConstant.face_recognizer, SeetaDevice.SEETA_DEVICE_GPU));

            String fileName = FileConstant.TEST_PICT;
            String fileName2 = "D:\\data\\train\\images\\face\\me\\01.png";
            SeetaImageData image1 = SeetafaceUtil.toSeetaImageData(fileName);
            SeetaRect[] detects1 = detector.Detect(image1);
            float[] features1 = new float[faceRecognizer.GetExtractFeatureSize()];
            SeetaPointF[] pointFS1 = new SeetaPointF[5];
            int[] masks1 = new int[5];
            faceLandmarker.mark(image1, detects1[0], pointFS1, masks1);
            faceRecognizer.Extract(image1, pointFS1, features1);

            SeetaImageData image2 = SeetafaceUtil.toSeetaImageData(fileName2);
            SeetaRect[] detects2 = detector.Detect(image2);
            float[] features2 = new float[faceRecognizer.GetExtractFeatureSize()];
            SeetaPointF[] pointFS2 = new SeetaPointF[5];
            int[] masks2 = new int[5];
            faceLandmarker.mark(image2, detects2[0], pointFS2, masks2);
            faceRecognizer.Extract(image2, pointFS2, features2);
            if (features1 != null && features2 != null) {
                float calculateSimilarity = faceRecognizer.CalculateSimilarity(features1, features2);
                System.out.printf("相似度:%f\n", calculateSimilarity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    static class MyThread extends Thread {
        public void run() {
            for (int x = 0; x < 1000; x++) {
                extract();
            }
        }
    }

}
