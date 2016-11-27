package databaseversusjava;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DatabaseVersusJavaFXMLController implements Initializable {
    private String algorithm;
    @FXML
    private Button exitButton, startButton;
    @FXML
    private TextArea databaseTextArea, javaTextArea;
    @FXML
    private TextField databaseTimeTextField, javaTimeTextField;
    @FXML
    private void sortAction(ActionEvent event) {
        //algorithm = null;
        //showAlgorithmChoiceDialog();
        //if(algorithm != null){
            //databaseTextArea.setText("Trwa sortowanie...");
            //javaTextArea.setText("Trwa sortowanie...");
            //try{
            //    Thread.sleep(20);
            //}catch(InterruptedException e){
            //    Logger.getLogger(DatabaseVersusJavaFXMLController.class.getName()).log(Level.SEVERE, null, e);
            //}
            //Platform.runLater(new SQLSorting(this));
            //Platform.runLater(new JavaSorting(this, algorithm));
            //Thread t = new Thread(new SQLSorting(this));
            //t.setDaemon(true);
            //t.start();
            //Platform.runLater(new SQLSorting(databaseTextArea.textProperty(), databaseTimeTextField.textProperty()));
            Thread t = new Thread(new SQLSorting(databaseTextArea.textProperty(), databaseTimeTextField.textProperty()));
            t.start();
            
       // }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    private void showAlgorithmChoiceDialog(){
        ArrayList<String> algorithms = new ArrayList();
        algorithms.add("Scalanie");
        algorithms.add("Bąbelkowe (z modyfikacją)");
        algorithms.add("Przez wybór");
        algorithms.add("Przez wstawianie");
        ChoiceDialog<String> algorithmDialog = new ChoiceDialog("Scalanie", algorithms);
        algorithmDialog.setTitle("Algorytmy");
        algorithmDialog.setHeaderText("Wybierz algorytm dla sortowania przez Javę");
        Optional<String> result = algorithmDialog.showAndWait();
        result.ifPresent(choice -> algorithm = choice);
    }
}
