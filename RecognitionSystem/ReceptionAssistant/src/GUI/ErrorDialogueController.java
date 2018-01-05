/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class for informing the user of an error
 *
 * @author Derek Mason
 */
public class ErrorDialogueController implements Initializable {
    
    @FXML
    private Label errorTextLabel;
    @FXML
    private Button okayButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorTextLabel.setText("There was an error processing your request. "
                + "Please make sure all values are in the format specified and "
                + "none of the required fields are blank.");
        errorTextLabel.setWrapText(true);
    }    
    
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) okayButton.getScene().getWindow();
        stage.close();
    }
    
}
