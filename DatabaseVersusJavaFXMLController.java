package databaseversusjava;

import databaseversusjava.JavaSorting.SortTypes;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Klasa <code>DatabaseVersusJavaFXMLController</code> reprezentuje sterowanie 
 * oknem programu porównującego sortowanie za pomocą SQL ze sortowaniem w Javie. 
 * Okno zawiera przyciski do włączenia sortowania, do wyjścia z programu oraz 
 * posiada pola i obszary tekstowe dla każdego języka. Użytkownik ma możliwość
 * wyboru algorytmu sortowania. Sortowania dla SQL i dla Javy odbywają się w 
 * osobnych wątkach. 
 * @author AleksanderSklorz
 */
public class DatabaseVersusJavaFXMLController implements Initializable {
    private SortTypes algorithm;
    @FXML
    private Button exitButton, startButton;
    @FXML
    private TextArea databaseTextArea, javaTextArea;
    @FXML
    private TextField databaseTimeTextField, javaTimeTextField;
    @FXML
    private void sortAction(ActionEvent event) {
        algorithm = null;
        showAlgorithmChoiceDialog();
        if(algorithm != null){
            databaseTextArea.setText("Trwa sortowanie...");
            javaTextArea.setText("Trwa sortowanie...");
            new Thread(new SQLSorting(databaseTextArea.textProperty(), databaseTimeTextField.textProperty())).start();
            new Thread(new JavaSorting(javaTextArea.textProperty(), javaTimeTextField.textProperty(), algorithm)).start();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        exitButton.setOnAction(event -> {
            System.exit(0);
        });
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
        result.ifPresent(choice -> {
            if(choice.equals("Scalanie")) algorithm = SortTypes.MERGE; 
            else if(choice.equals("Bąbelkowe (z modyfikacją)")) algorithm = SortTypes.BUBBLE;
            else if(choice.equals("Przez wybór")) algorithm = SortTypes.SELECTION;
            algorithm = SortTypes.INSERTION;
        });
    }
}
