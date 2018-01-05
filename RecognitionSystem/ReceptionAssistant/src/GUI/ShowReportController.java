package GUI;

import java.net.URL;
import Database.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class for the reports on student visits
 *
 * @author Derek Mason
 */
public class ShowReportController implements Initializable {

    @FXML
    private Button exitReport;
    @FXML
    private Button createTableReport;
    @FXML
    private Button createChartReport;
    @FXML
    private Label display;
    @FXML
    private TextField startDay;
    @FXML
    private TextField startMonth;
    @FXML
    private TextField startYear;
    @FXML
    private TextField endDay;
    @FXML
    private TextField endMonth;
    @FXML
    private TextField endYear;
    @FXML
    private ChoiceBox genderBox;
    @FXML
    private ChoiceBox programBox;
    @FXML
    private ChoiceBox reasonBox;
    @FXML
    private ScrollPane displayResultsPane;
    @FXML
    private RadioButton tableRadio;
    @FXML
    private RadioButton graphRadio;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis = new CategoryAxis();
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    @FXML
    private AnchorPane errorPane;

    private ObservableList<String> reasonList = FXCollections.observableArrayList();
    private XYChart.Series series;
    private ToggleGroup toggle;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        genderBox.setItems(FXCollections.observableArrayList("Any", "Male", "Female"));
        programBox.setItems(FXCollections.observableArrayList("Any", "MISM", "MSIT", "MSPPM"));
        reasonBox.setItems(FXCollections.observableArrayList("Any",
                "General Question", "Fee Payment", "Collecting Mail", "Admissions", "Assignments"));
        reasonBox.setValue("Any");
        genderBox.setValue("Any");
        programBox.setValue("Any");
        reasonList.addAll("General Question", "Fee Payment", "Collecting Mail",
                "Admissions", "Assignments");
        toggle = new ToggleGroup();
        graphRadio.setToggleGroup(toggle);
        tableRadio.setToggleGroup(toggle);
        tableRadio.setSelected(true);
        xAxis.setLabel("Reason for visit");
        xAxis.setCategories(reasonList);
        yAxis.setLabel("Number of students");
    }

    @FXML
    private void closeTheWindow(ActionEvent event) {
        Stage stage = (Stage) exitReport.getScene().getWindow();
        stage.close();
    }

    @FXML
    private Text t;

    @FXML
    private void updateDisplayTable(ActionEvent event) {
        try {
            String gender = (String) genderBox.getValue();
            String program = (String) programBox.getValue();
            String reason = (String) reasonBox.getValue();
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fromD = (Date) sdf.parse(startDay.getText() + "/" + startMonth.getText() + "/" + startYear.getText());
            Date toD = (Date) sdf.parse(endDay.getText() + "/" + endMonth.getText() + "/" + endYear.getText());

            java.sql.Date fromDate = new java.sql.Date(fromD.getTime());
            java.sql.Date toDate = new java.sql.Date(toD.getTime());

            ArrayList<Visit> visitList = Driver.getVisitReportFromDB(fromDate, toDate, gender, program, reason);
            String header = String.format("%-20s %-10s %-10s %-40s %n",
                    "Name", "Gender", "Program", "Reason for Visit");
            String table = "";
            for (Visit v : visitList) {
                table = table + String.format("%-20s %-10s %-10s %-40s %n",
                        v.getStudentName(), v.getStudentGender(), v.getStudentProgram(), v.getReason());
            }
            t = new Text(header + table);
            t.setFont(Font.font("Courier New"));
            
            //t.wrappingWidthProperty().bind(displayResultsPane.getScene().widthProperty());
            //displayResultsPane.setFitToWidth(true);
            displayResultsPane.setContent(t);
        } catch (Exception e) {
            e.printStackTrace();
            Stage temp = new Stage();
            try {
                errorPane = (AnchorPane) FXMLLoader.load(getClass().getResource(
                        "/GUI/ErrorDialogue.fxml"));
                Scene scene = new Scene(errorPane);
                temp.setScene(scene);
                temp.showAndWait();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @FXML
    private void updateDisplayChart(ActionEvent event) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fromD = (Date) sdf.parse(startDay.getText() + "/" + startMonth.getText() + "/" + startYear.getText());
            Date toD = (Date) sdf.parse(endDay.getText() + "/" + endMonth.getText() + "/" + endYear.getText());

            java.sql.Date fromDate = new java.sql.Date(fromD.getTime());
            java.sql.Date toDate = new java.sql.Date(toD.getTime());
            
            int reason1 = Driver.getCountReportFromDB(fromDate, toDate, "General Question");
            int reason2 = Driver.getCountReportFromDB(fromDate, toDate, "Fee Payment");
            int reason3 = Driver.getCountReportFromDB(fromDate, toDate, "Collecting Mail");
            int reason4 = Driver.getCountReportFromDB(fromDate, toDate, "Admissions");
            int reason5 = Driver.getCountReportFromDB(fromDate, toDate, "Assignments");

            series = new XYChart.Series();
            series.getData().add(new XYChart.Data("General Question", reason1));
            series.getData().add(new XYChart.Data("Fee Payment", reason2));
            series.getData().add(new XYChart.Data("Collecting Mail", reason3));
            series.getData().add(new XYChart.Data("Admissions", reason4));
            series.getData().add(new XYChart.Data("Assignments", reason5));
            barChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
            Stage temp = new Stage();
            try {
                errorPane = (AnchorPane) FXMLLoader.load(getClass().getResource(
                        "/GUI/ErrorDialogue.fxml"));
                Scene scene = new Scene(errorPane);
                temp.setScene(scene);
                temp.showAndWait();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @FXML
    private void toggleToTable(ActionEvent event) {
        reasonBox.setDisable(false);
        genderBox.setDisable(false);
        programBox.setDisable(false);
        createTableReport.setVisible(true);
        createChartReport.setVisible(false);
        displayResultsPane.setVisible(true);
        barChart.setVisible(false);
    }

    @FXML
    private void toggleToChart(ActionEvent event) {
        reasonBox.setDisable(true);
        genderBox.setDisable(true);
        programBox.setDisable(true);
        createTableReport.setVisible(false);
        createChartReport.setVisible(true);
        displayResultsPane.setVisible(false);
        barChart.setVisible(true);
    }
}
