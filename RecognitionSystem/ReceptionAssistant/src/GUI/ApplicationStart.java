package GUI;

import Database.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

/**
 * Launches the application
 *
 * @author Derek Mason
 */
public class ApplicationStart extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        try {
            //create tables if they don't exist
            Driver.createTable();
            
            /*
            Announcement objAnn= new Announcement();
            
            objAnn.setAnnouncement("Java Lab will be held in classroom 2");
            objAnn.setProgram("MISM");
            objAnn.insert();
            
            //create announcements
             Announcement objAnn2= new Announcement();
            objAnn2.setAnnouncement("Resume workshop for MSIT students will be held in UCL");
            objAnn2.setProgram("MSIT");
            objAnn2.insert();
            
             Announcement objAnn3= new Announcement();
            objAnn3.setAnnouncement("Policy workshop for MSPPM students will be held in classroom 5");
            objAnn3.setProgram("MSPPM");
            objAnn3.insert(); */
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } 

        Parent root = FXMLLoader.load(getClass().getResource("/GUI/MainScreen.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show(); 
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
    
}
