/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FaceRecognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
//import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
// import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;
// import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_core.Mat;

import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;




public class Recognition {
    
    public int recognize(){
        //BufferedImage image1
        String trainingDir = "/Users/Apple/Desktop/test";
        //Mat testImage = bufferedImageToMat(image1);

        Mat testImage = imread("/Users/Apple/Desktop/temp/temp.png", CV_LOAD_IMAGE_GRAYSCALE);

                
        File root = new File(trainingDir);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        
        
        File[] imageFiles = root.listFiles(imgFilter);

        MatVector images = new MatVector(imageFiles.length);

        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        int counter = 0;
        

        for (File image : imageFiles) {
            Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

            int label = Integer.parseInt(image.getName().split("\\-")[0]);

            images.put(counter, img);

            labelsBuf.put(counter, label);

            counter++;
        }

        FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        faceRecognizer.setThreshold(1500);
//        FaceRecognizer faceRecognizer2 = createEigenFaceRecognizer();
//        faceRecognizer2.setThreshold(1000);
//        FaceRecognizer faceRecognizer3 = createLBPHFaceRecognizer();
//        faceRecognizer3.setThreshold(1000);

        faceRecognizer.train(images, labels);
//        faceRecognizer2.train(images, labels);
//        faceRecognizer3.train(images, labels);

        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        faceRecognizer.predict(testImage, label, confidence);
        int predictedLabel = label.get(0);
        
//        IntPointer label2 = new IntPointer(1);
//        DoublePointer confidence2 = new DoublePointer(1);
//        faceRecognizer2.predict(testImage, label2, confidence2);
//        int predictedLabel2 = label2.get(0);
//        
//        IntPointer label3 = new IntPointer(1);
//        DoublePointer confidence3 = new DoublePointer(1);
//        faceRecognizer3.predict(testImage, label3, confidence3);
//        int predictedLabel3 = label3.get(0);
//
//        
//        System.out.println("Predicted label: " + predictedLabel);
//        System.out.println("Predicted label: " + predictedLabel2);
//        System.out.println("Predicted label: " + predictedLabel3);

        return predictedLabel;
    }
    
    public Mat bufferedImageToMat(BufferedImage bi) {
        OpenCVFrameConverter.ToMat cv = new OpenCVFrameConverter.ToMat();
        return cv.convertToMat(new Java2DFrameConverter().convert(bi)); 
    }
    
    
}
