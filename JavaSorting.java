package databaseversusjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextArea;

public class JavaSorting implements Runnable{
    private DatabaseVersusJavaFXMLController controller;
    public JavaSorting(DatabaseVersusJavaFXMLController controller){
        this.controller = controller;
    }
    public void run(){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseversusjava", "olek", "haslo12345")){
            long[][] data = getDataFromDatabase(conn);
            long before = System.currentTimeMillis();
            Arrays.sort(data, new Comparator<long[]>(){
                public int compare(long[] r1, long[] r2){
                    int result = Long.compare(r1[0], r2[0]);
                    if(result == 0) return Long.compare(r1[1], r2[1]);
                    else return result;
                }
            });
            long after = System.currentTimeMillis();
            TextArea javaTextArea = controller.getJavaTextArea();
            javaTextArea.setText("");
            for(int i = 0; i < data.length; i++){
                for(int k = 0; k < data[i].length; k++)
                    javaTextArea.appendText(data[i][k] + " ");
                javaTextArea.appendText("\n");
            }
            controller.getJavaTimeTextField().setText((double)(after - before)/1000 + " s");
        }catch(SQLException e){
            Logger.getLogger(JavaSorting.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private long[][] getDataFromDatabase(Connection conn) throws SQLException{
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
        return data;
    }
}
