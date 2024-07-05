package com.seeta.sdk.memory;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

public class MemoryTest {
    public static FaceDetector detector = null;

    public static FaceLandmarker faceLandmarker = null;
    public static FaceRecognizer faceRecognizer = null;


    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_GPU);
    }

    public static void main(String[] args) {
// 开始时间
        long stime = System.currentTimeMillis();

        try {

            detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_GPU));
            faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_GPU));
            faceRecognizer = new FaceRecognizer(new SeetaModelSetting(FileConstant.face_recognizer, SeetaDevice.SEETA_DEVICE_GPU));
            String fileName = FileConstant.TEST_PICT;
            String fileName2 = "D:\\data\\train\\images\\face\\me\\01.png";
            for (int i = 0; i < 100; i++) {
                float[] features1 = extract(fileName);
                float[] features2 = extract(fileName2);
                if (features1 != null && features2 != null) {
                    float calculateSimilarity = faceRecognizer.CalculateSimilarity(features1, features2);
                    System.out.printf("第%s个,相似度:%f\n", i, calculateSimilarity);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 结束时间
        long etime = System.currentTimeMillis();
        // 计算执行时间
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
    }

    /**
     * 获取特征数组
     *
     * @author YaoCai Lin
     * @time 2020年7月15日 下午12:10:56
     */
    private static float[] extract(String fileName) {
        SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);

        SeetaRect[] detects = detector.Detect(image);
        for (SeetaRect seetaRect : detects) {

            SeetaPointF[] pointFS = new SeetaPointF[5];
            int[] masks = new int[5];
            faceLandmarker.mark(image, seetaRect, pointFS, masks);
            float[] features = new float[faceRecognizer.GetExtractFeatureSize()];

            faceRecognizer.Extract(image, pointFS, features);
            return features;
        }
        return null;
    }

}
