package databaseversusjava;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DatabaseVersusJavaFXMLController implements Initializable {
    @FXML
    private Button exitButton, startButton;
    @FXML
    private TextArea databaseTextArea, javaTextArea;
    @FXML
    private TextField databaseTimeTextField, javaTimeTextField;
    @FXML
    private void sortAction(ActionEvent event) {
        Platform.runLater(new SQLSorting(this));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseversusjava", "olek", "haslo12345")){
            Statement stat = conn.createStatement(); 
            ResultSet rs = stat.executeQuery("SELECT * FROM Dane");
            rs.last();
            long[][] data = new long[rs.getRow()][2];
            rs.beforeFirst();
            int i = 0;
            while(rs.next()){
                data[i][0] = rs.getLong(1);
                data[i][1] = rs.getLong(2);
                i++;
            }
        }catch(SQLException e){
            Logger.getLogger(DatabaseVersusJavaFXMLController.class.getName()).log(Level.SEVERE, null, e);
        }
        exitButton.setOnAction(event -> {
            System.exit(0);
        });
    }   
    public TextArea getDatabaseTextArea(){
        return databaseTextArea; 
    }
    public TextField getDatabaseTimeTextField(){
        return databaseTimeTextField;
    }
    public TextArea getJavaTextArea(){
        return javaTextArea;
    }
    public TextField getJavaTimeTextField(){
        return javaTimeTextField;
    }
}
