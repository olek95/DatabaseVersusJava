package databaseversusjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

/**
 * Obiekt klasy <code>SQLSorting</code> reprezentuje sortowanie wartości bezpośrednio 
 * w bazie danych za pomocą SQL. Wynik jest zapisywany w obszarze tekstowym, a
 * czas w polu tekstowym. 
 * @author AleksanderSklorz
 */
public class SQLSorting extends Task{
    private StringProperty textAreaProperty, textFieldProperty;
    private int linesNumber;
    private String result;
    public SQLSorting(StringProperty textAreaProperty, StringProperty textFieldProperty, int linesNumber){
        this.textAreaProperty = textAreaProperty;
        this.textFieldProperty = textFieldProperty;
        this.linesNumber = linesNumber;
    }
    protected Void call(){
        try(Connection conn = DriverManager.getConnection(DatabaseVersusJava.getURL(), DatabaseVersusJava.getLogin(), DatabaseVersusJava.getPassword())){
            Statement stat = conn.createStatement();
            long before = System.currentTimeMillis();
            ResultSet rs = stat.executeQuery("SELECT * FROM Dane ORDER BY id, data DESC");
            long after = System.currentTimeMillis();
            result = "";
            int i = 0;
            while(i < linesNumber && rs.next()){
                result += rs.getLong("id") + " " + rs.getLong("data") + "\n";
            }
            Platform.runLater(() -> {
                textAreaProperty.set(result);
                textFieldProperty.set((double)(after - before)/1000 + " s");
            });
            
        }catch(SQLException e){
            Logger.getLogger(DatabaseVersusJavaFXMLController.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}

