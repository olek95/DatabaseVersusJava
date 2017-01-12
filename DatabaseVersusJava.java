package databaseversusjava;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
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
import javax.swing.JOptionPane;

public class DatabaseVersusJava extends Application {
    private static String login, password, url;
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
        showLoginInformationDialog();
        fillTable();
        launch(args);
    }
    private static void fillTable(){ 
        Random rand = new Random();
        if(!(new File("Skrypt.sql").exists())){
            for(int i = 0; i < 1000000; i++){
                StringBuilder sb = new StringBuilder("INSERT INTO Dane VALUES("); 
                sb.append(rand.nextLong()).append(", ").append(rand.nextLong()).append(");\r\n");
                try{
                    Files.write(Paths.get("Skrypt.sql"), sb.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }catch(IOException e){
                    Logger.getLogger(DatabaseVersusJava.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        try(Connection conn = DriverManager.getConnection(url, login, password)){
            if(!conn.getMetaData().getTables(null, null, "Dane", null).next()){
                Statement stat = conn.createStatement();
                stat.executeUpdate("CREATE TABLE Dane(id BIGINT, data BIGINT)");
            }
        }catch(SQLException e){
            Logger.getLogger(DatabaseVersusJava.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private static void showLoginInformationDialog(){
        login = JOptionPane.showInputDialog("Podaj login: ");
        if(login == null) System.exit(0);
        password = JOptionPane.showInputDialog("Podaj hasło: ");
        if(password == null) System.exit(0);
        url = JOptionPane.showInputDialog("Podaj URL (np. jdbc:mysql://localhost:3306/databaseversusjava): ");
        if(url == null) System.exit(0);
    }
    /**
     * Zwraca nazwę użytkownika. 
     * @return nazwa użytkownika
     */
    public static String getLogin(){
        return login;
    }
    /**
     * Zwraca hasło użytkownika.
     * @return hasło użytkownika.
     */
    public static String getPassword(){
        return password;
    }
    /**
     * Zwraca adres URL.
     * @return adres URL
     */
    public static String getURL(){
        return url;
    }
}
