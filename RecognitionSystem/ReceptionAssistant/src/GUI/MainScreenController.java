package GUI;

import Database.*;
import FaceRecognition.FaceRecognitionController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class for the main application screen
 *
 * @author Derek Mason
 */
public class MainScreenController implements Initializable {

    @FXML
    private ImageView picture;
    @FXML
    private Label title;
    @FXML
    private Label menu;
    @FXML
    private Label labelDialogue;
    @FXML
    private Label labelName;
    @FXML
    private Label labelID;
    @FXML
    private Label labelProgram;
    @FXML
    private Label labelGender;
    @FXML
    private Label labelVisits;
    @FXML
    private Label labelVisitReason;
    @FXML
    private Label labelVisitDate;
    @FXML
    private Label labelAnnouncements;
    @FXML
    private GridPane grid;
    @FXML
    private Button answerYes;
    @FXML
    private Button answerNo;
    @FXML
    private Button camera;
    @FXML
    private Button reports;
    @FXML
    private Button exit;
    @FXML
    private Button returnToMenu;
    @FXML
    private Button startTrainingSet;
    @FXML
    private BorderPane takePicture;
    @FXML
    private Image imageFile;
    @FXML
    private AnchorPane reportShow;
    @FXML
    private AnchorPane addStudent;
    @FXML
    private BorderPane borderPane;

    private static Student student;
    private static Visit visit;
    private boolean newStudentRecord;

    /**
     * Takes action on the start of the stage.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newStudentRecord = false;
        picture.setFitHeight(200);
        picture.setFitWidth(200);
        picture.setPreserveRatio(true);

    }

    /**
     * Begins the process to check the student currently in the camera view,
     * then takes action based on whether the student is already in the database
     * or a new record should be added.
     *
     * @param event clicking the check student button
     */
    @FXML
    private void checkStudent(ActionEvent event) {

        //Hide the main menu while other functions occur
        menu.setVisible(false);
        camera.setVisible(false);
        reports.setVisible(false);
        exit.setVisible(false);

        try {
            Stage temp = new Stage();
            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource(
                    "/FaceRecognition/FaceRecognitionPane.fxml"));
            Scene scene = new Scene(borderPane, 800, 600);
            temp.setScene(scene);
            temp.showAndWait();

            //The detection controller determines whether it is a new student
            if (FaceRecognitionController.checkStudent() != -1) {
                newStudentRecord = false;
                //Pull the information for this student from the database using
                //the ID and place it into a Student object
                student = Driver.getStudentFromDB("" + FaceRecognitionController.checkStudent());
                imageFile = new Image("file:/Users/Apple/Desktop/test/"
                        + student.getID() + "-test_1.png");
                // /Users/Apple/Desktop/test/1-test_1.png
                picture.setImage(imageFile);
                //Since the student is already present, ask to update the info
                labelDialogue.setText("Here is the information on this student."
                        + " Update it?");
                answerYes.setVisible(true);
                answerNo.setVisible(true);
                labelDialogue.setVisible(true);
                picture.setVisible(true);
                grid.setVisible(true);
                labelName.setText("Name: " + student.getName());
                labelID.setText("Student ID: " + student.getID());
                labelProgram.setText("Program: " + student.getProgram());
                labelGender.setText("Gender: " + student.getGender());
                labelVisitReason.setText("Reason for last visit: "
                        + student.getStudentVisits().getReason());
                labelVisits.setText("Number of visits by this student: "
                        + student.getVisitCount());
                ArrayList<Announcement> announce = student.getAnnouncements();
                String announceLabel = "Announcements: ";
                for (Announcement ann : announce) {
                    announceLabel = announceLabel + ann.toString() + "\n";
                }
                labelAnnouncements.setText(announceLabel);
                labelVisitDate.setText("Date of last visit: "
                        + student.getStudentVisits().getSqlDate().toString());
            } else {
                //If the student is not already present, ask to create a new record
                labelDialogue.setText("This student does not have a record. "
                        + "Enter a new record for this student?");
                labelDialogue.setVisible(true);
                answerYes.setVisible(true);
                answerNo.setVisible(true);
                newStudentRecord = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open a new window to show a report on the visits that have been logged in
     * the database, searching by date and category.
     *
     * @param event
     */
    @FXML
    private void showReport(ActionEvent event) {
        //Hide the main menu while other functions occur
        menu.setVisible(false);
        camera.setVisible(false);
        reports.setVisible(false);
        exit.setVisible(false);

        //Open the new window
        try {
            Stage temp = new Stage();
            reportShow = (AnchorPane) FXMLLoader.load(getClass().getResource(
                    "/GUI/ShowReport.fxml"));
            Scene scene = new Scene(reportShow);
            temp.setScene(scene);
            temp.showAndWait();

            //Once the window is closed, show the main menu once more
            menu.setVisible(true);
            camera.setVisible(true);
            reports.setVisible(true);
            exit.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exit the program.
     *
     * @param event
     */
    @FXML
    private void exitProgram(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Takes action when the user decides to add or update a student.
     *
     * @param event
     */
    @FXML
    private void answerYes(ActionEvent event) {
        //Hide the previous options
        answerYes.setVisible(false);
        answerNo.setVisible(false);

        //Open a new window to take user input for student information
        try {
            Stage temp = new Stage();
            addStudent = (AnchorPane) FXMLLoader.load(getClass().getResource(
                    "/GUI/AddStudentDialog.fxml"));
            Scene scene = new Scene(addStudent);
            temp.setScene(scene);
            temp.showAndWait();

            //If the user input some information, take action
            if (AddStudentController.isFilled()) {
                //Get the student and visit objects with the new information
                student = AddStudentController.getStudent();
                visit = AddStudentController.getVisit();
                //Update the labels with this information
                labelName.setText("Name: " + student.getName());
                labelID.setText("Student ID: " + student.getID());
                labelProgram.setText("Program: " + student.getProgram());
                labelGender.setText("Gender: " + student.getGender());
                labelVisitReason.setText("Reason for last visit: " + visit.getReason());
                labelVisits.setText("Number of visits by this student: " + student.getVisitCount());
                ArrayList<Announcement> announce = student.getAnnouncements();
                String announceLabel = "Announcements: ";
                for (Announcement ann : announce) {
                    announceLabel = announceLabel + ann.toString() + "\n";
                }
                labelVisitDate.setText("Date of last visit: "
                        + student.getStudentVisits().getSqlDate().toString());
                labelAnnouncements.setText(announceLabel);
                grid.setVisible(true);
                picture.setVisible(true);
                if (newStudentRecord) {
                    labelDialogue.setText("Record created. Press OK to create"
                            + " the training set of images for this student. "
                            + "This will take approximately one minute. When "
                            + "finished, the word 'Finished!' will appear.");
                    startTrainingSet.setVisible(true);
                } else {
                    labelDialogue.setText("Record updated. Press OK to return to main menu.");
                    returnToMenu.setVisible(true);
                }
                //If the user canceled the add or edit operation, return to menu
            } else {
                labelDialogue.setText("Cancelled. Press OK to return to main menu.");
                returnToMenu.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param event
     */
    @FXML
    private void answerNo(ActionEvent event) {
        answerYes.setVisible(false);
        answerNo.setVisible(false);
        labelDialogue.setText("Press OK to return to main menu.");
        returnToMenu.setVisible(true);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void returnToMenu(ActionEvent event) {
        menu.setVisible(true);
        camera.setVisible(true);
        reports.setVisible(true);
        exit.setVisible(true);
        returnToMenu.setVisible(false);
        labelDialogue.setVisible(false);
        grid.setVisible(false);
        picture.setVisible(false);
        student = null;
    }

    /**
     *
     * @return
     */
    public static Student getStudent() {
        return student;
    }

    @FXML
    private void beginTrainingSet(ActionEvent event) {
        try {
            Stage temp = new Stage();
            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource(
                    "/FaceRecognition/TrainingSetPane.fxml"));
            Scene scene = new Scene(borderPane, 800, 600);
            temp.setScene(scene);
            temp.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        labelDialogue.setText("Pictures captured. Press OK to return to main menu.");
        imageFile = new Image("file:/Users/Apple/Desktop/test/"
                + student.getID() + "-test_1.png");
        picture.setImage(imageFile);
        labelDialogue.setVisible(true);
        startTrainingSet.setVisible(false);
        returnToMenu.setVisible(true);
    }
}
