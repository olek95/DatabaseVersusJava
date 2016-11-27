package databaseversusjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SQLSorting implements Runnable{
    //private DatabaseVersusJavaFXMLController controller;
    //public SQLSorting(DatabaseVersusJavaFXMLController cotroller){
    //    this.controller = cotroller;
    //}
    private StringProperty textAreaTextProperty, textFieldTextProperty;
    public SQLSorting(StringProperty textAreaTextProperty, StringProperty textFieldTextProperty){
        this.textAreaTextProperty = textAreaTextProperty;
        this.textFieldTextProperty = textFieldTextProperty;
    }
    public void run(){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseversusjava", "olek", "haslo12345")){
            Statement stat = conn.createStatement();
            long before = System.currentTimeMillis();
            ResultSet rs = stat.executeQuery("SELECT * FROM Dane ORDER BY id, data DESC");
            long after = System.currentTimeMillis();
            StringBuffer sb = new StringBuffer("");
            while(rs.next()){
                sb.append(rs.getLong("id")).append(" ").append(rs.getLong("data")).append("\n");
            }
            textAreaTextProperty.set(sb.toString());
            textFieldTextProperty.set((double)(after - before)/1000 + " s");
        }catch(SQLException e){
            Logger.getLogger(DatabaseVersusJavaFXMLController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
