/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FaceRecognition;

import GUI.MainScreenController;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author jieni
 */
public class TrainingSetController implements Initializable {

    @FXML
    private ImageView originalFrame;
    @FXML
    private Button closeButton;

    private ScheduledExecutorService timer;
    private VideoCapture capture;
    private boolean cameraActive;

    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;

    public Rect rectCrop = null;
    public Mat finalFrame;
    public int count = 1;
    
    public Timer timer2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;

        // set a fixed width for the frame
        originalFrame.setFitWidth(750);
        // preserve image ratio
        originalFrame.setPreserveRatio(true);

        this.faceCascade.load("/Users/Apple/opencv/data/haarcascades_cuda/haarcascade_frontalface_alt.xml");

        this.capture.open(0);

        // is the video stream available?
        if (this.capture.isOpened()) {

            // grab a frame every 33 ms (30 frames/sec)
            Runnable frameGrabber = new Runnable() {
                @Override
                public void run() {

                    // effectively grab and process a single frame
                    Mat frame = grabFrame();
                    // convert and show the frame
                    Image imageToShow = Utils.mat2Image(frame);
                    updateImageView(originalFrame, imageToShow);

                }
            };
            this.timer = Executors.newSingleThreadScheduledExecutor();
            this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
        } else {
            // log the error
            System.err.println("Failed to open the camera connection...");
        }

        timer2 = new Timer();
        timer2.schedule(new DateTask(), 1000, 500);
        //System.out.println("現在時間：" + new Date());
    }

    private Mat grabFrame() {
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // face detection
                    this.detectAndDisplay(frame);
                }

            } catch (Exception e) {
                // log the (full) error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    private void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        if (this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        // detect faces
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();

        finalFrame = frame;

        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
            rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
        }

        //for (int i = 0; i < facesArray.length; i++)
        //	Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    public class DateTask extends TimerTask {

        @Override
        public void run() {
            Mat imageRoi = new Mat(finalFrame, rectCrop);

            if (count <= 10) {
                Mat resizeimage = new Mat();
                Size sz = new Size(200, 200);
                Imgproc.resize(imageRoi, resizeimage, sz);
                Imgcodecs.imwrite("/Users/Apple/Desktop/test/"
                        + MainScreenController.getStudent().getID()
                        + "-test_" + count + ".png", resizeimage);
                count++;
                //System.out.println("任務時間：" + new Date());
            } else {
                closeButton.setDisable(false);
                closeButton.setText("Finished!");
            }
        }

    }

    @FXML
    private void closeWindow(ActionEvent event) {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
                this.timer2.cancel();
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
            capture.release();
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
    }
    
}
