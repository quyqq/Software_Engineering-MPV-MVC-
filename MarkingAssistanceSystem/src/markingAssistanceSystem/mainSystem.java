/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0 
 * @see java.sql.SQLException;
 * @see javafx.application.Application;
 * @see javafx.fxml.*;
 * @see javafx.scene.*;
 * @see javafx.stage.*;
 * @see model.StudentMarkModel;
 * @see presenter.StudentMarkPresenter;
 * @see view.MarkingAssistanceSystemController;
 * @see javax.swing.JOptionPane
 * 
 * the main system will be fired up here
 */
package markingAssistanceSystem;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import javax.swing.JOptionPane;
import model.StudentMarkModel;
import presenter.StudentMarkPresenter;
import view.MarkingAssistanceSystemController;

public class mainSystem extends Application {

    /**
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        try {
            FXMLLoader fileLoad;
            fileLoad = new FXMLLoader(getClass().getResource("/view/MarkingAssistanceSystem.fxml"));
            Parent root = fileLoad.load();
            MarkingAssistanceSystemController crl = fileLoad.getController();

            StudentMarkModel markModel = new StudentMarkModel();
            markModel.connect();
            markModel.initialise();
            StudentMarkPresenter markPresenter = new StudentMarkPresenter(markModel, markModel, crl);
            crl.bind(markPresenter);

            primaryStage.setOnCloseRequest((x) -> {
                try {
                    markPresenter.close();
                } catch (SQLException ex) {
                    //ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            });

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Assessment 2 Marking Assistance System");
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
            System.exit(1);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
