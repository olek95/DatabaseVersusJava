package databaseversusjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DatabaseVersusJava extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DatabaseVersusJavaFXML.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Porównanie sortowań Java vs SQL");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        fillTable();
        launch(args);
    }
    private static void fillTable(){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseversusjava", "olek", "haslo12345")){
            if(!conn.getMetaData().getTables(null, null, "Dane", null).next()){
                Statement stat = conn.createStatement();
                stat.executeUpdate("CREATE TABLE Dane(id BIGINT, data BIGINT)");
                PreparedStatement prepStat = conn.prepareStatement("INSERT INTO Dane VALUES(?, ?)");
                Random rand = new Random();
                //int size = rand.nextInt(4000001) + 1000000;
                for(int i = 0; i < 1000000; i++){
                    prepStat.setLong(1, rand.nextLong());
                    prepStat.setLong(2, rand.nextLong());
                    prepStat.executeUpdate();
                }
            }
        }catch(SQLException e){
            Logger.getLogger(DatabaseVersusJava.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
