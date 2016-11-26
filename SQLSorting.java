package databaseversusjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextArea;

public class SQLSorting implements Runnable{
    private DatabaseVersusJavaFXMLController controller;
    public SQLSorting(DatabaseVersusJavaFXMLController cotroller){
        this.controller = cotroller;
    }
    public void run(){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseversusjava", "olek", "haslo12345")){
            Statement stat = conn.createStatement();
            long before = System.currentTimeMillis();
            ResultSet rs = stat.executeQuery("SELECT * FROM Dane ORDER BY id, data DESC");
            long after = System.currentTimeMillis();
            TextArea databaseTextArea = controller.getDatabaseTextArea();
            databaseTextArea.setText("");
            while(rs.next())
                databaseTextArea.appendText(rs.getLong("id") + " " + rs.getLong("data") +"\n");
            controller.getDatabaseTimeTextField().setText((double)(after - before)/1000 + " s");
        }catch(SQLException e){
            Logger.getLogger(DatabaseVersusJavaFXMLController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
