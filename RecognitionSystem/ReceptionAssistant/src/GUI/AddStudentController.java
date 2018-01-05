package GUI;

import Database.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class for adding or editing student information
 *
 * @author Derek Mason
 */
public class AddStudentController implements Initializable {

    private static Student student;
    private static Visit visit;
    private static boolean filled;
    private boolean isNew;

    @FXML
    private TextField nameField;
    @FXML
    private TextField IDField;
    @FXML
    private ChoiceBox programBox;
    @FXML
    private ChoiceBox genderBox;
    @FXML
    private ChoiceBox visitReasonBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Button okayButton;
    @FXML
    private AnchorPane errorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isNew = true;
        programBox.setItems(FXCollections.observableArrayList("MISM", "MSIT", "MSPPM"));
        genderBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        programBox.setValue("MISM");
        genderBox.setValue("Male");
        student = MainScreenController.getStudent();
        if (student != null) {
            nameField.setText(student.getName());
            IDField.setText("" + student.getID());
            IDField.setDisable(true);
            programBox.setValue(student.getProgram());
            genderBox.setValue(student.getGender());
            isNew = false;
        }
        filled = false;
        visitReasonBox.setItems(FXCollections.observableArrayList(
                "General Question", "Fee Payment", "Collecting Mail", "Admissions", "Assignments"));
        visitReasonBox.setValue("General Question");
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        filled = false;
        stage.close();
    }

    @FXML
    private void handleOk(ActionEvent event) {
        Stage temp;
        boolean isInt = false;
        try {
            Integer.parseInt(IDField.getText());
            if (isNew) {
                Student objStudent = Driver.getStudentFromDB(IDField.getText());
                if (objStudent.getName() == null) {
                    isInt = true;
                }
            } else {
                isInt = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (nameField.getText().equals("") || IDField.getText().equals("") || !isInt) {
            try {
                temp = new Stage();
                errorPane = (AnchorPane) FXMLLoader.load(getClass().getResource(
                        "/GUI/ErrorDialogue.fxml"));
                Scene scene = new Scene(errorPane);
                temp.setScene(scene);
                temp.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (isNew) {
                student = new Student();
            }
            student.setName(nameField.getText());
            student.setID(Integer.parseInt(IDField.getText()));
            student.setProgram((String) programBox.getValue());
            student.setGender((String) genderBox.getValue());
            try {
                if (isNew) {
                    student.insert();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            visit = student.createVisit((String) visitReasonBox.getValue());
            filled = true;
            Stage stage = (Stage) okayButton.getScene().getWindow();
            stage.close();
        }
    }

    public static Student getStudent() {
        return student;
    }

    public static Visit getVisit() {
        return visit;
    }

    public static boolean isFilled() {
        return filled;
    }
}
