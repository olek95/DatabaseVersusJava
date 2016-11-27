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
    private String algorithm;
    public JavaSorting(DatabaseVersusJavaFXMLController controller, String algorithm){
        this.controller = controller;
        this.algorithm = algorithm;
    }
    public void run(){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseversusjava", "olek", "haslo12345")){
            long[][] data = getDataFromDatabase(conn);
            long time=0;
            if(algorithm.equals("Scalanie")) time = mergeSort(data);
            else if(algorithm.equals("Bąbelkowe (z modyfikacją)")) time = bubbleSort(data);
            else if(algorithm.equals("Przez wybór")) time = selectionSort(data);
            else if(algorithm.equals("Przez wstawianie")) time = insertionSort(data);
            TextArea javaTextArea = controller.getJavaTextArea();
            javaTextArea.setText("");
            for(int i = 0; i < data.length; i++){
                for(int k = 0; k < data[i].length; k++)
                    javaTextArea.appendText(data[i][k] + " ");
                javaTextArea.appendText("\n");
            }
            controller.getJavaTimeTextField().setText((double)time/1000 + " s");
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
    private long mergeSort(long[][] data){
        long before = System.currentTimeMillis();
        Arrays.sort(data, new Comparator<long[]>(){ 
            public int compare(long[] r1, long[] r2){
                int result = Long.compare(r1[0], r2[0]);
                if(result == 0) return Long.compare(r1[1], r2[1]);
                else return result;
            }
        });
        return System.currentTimeMillis() - before;
    }
    private long bubbleSort(long[][] data){
        long before = System.currentTimeMillis();
        boolean swap = true; 
        for(int i = 0; i < data.length && swap; i++){
            swap = false;
            for(int k = 0; k < data.length - 1; k++){
                if(data[k][0] > data[k+1][0]){
                    long x = data[k][0], y = data[k][1];
                    data[k][0] = data[k+1][0];
                    data[k][0] = data[k+1][1];
                    data[k+1][0] = x;
                    data[k+1][1] = y;
                    swap = true;
                }else if(data[k][0] == data[k+1][0])
                    if(data[k][1] > data[k+1][1]){
                        long temp = data[k][1];
                        data[k][1] = data[k+1][1];
                        data[k+1][1] = temp;
                        swap = true;
                    }
            }
        }
        return System.currentTimeMillis() - before;
    }
    private long selectionSort(long[][] data){
        long before = System.currentTimeMillis();
        int min;
        long x, y;
        for(int i = 0; i < data.length; i++){
            min = i;
            for(int k = i + 1; k < data.length; k++)
                if(data[k][0] < data[min][0] || (data[k][0] == data[min][0] && data[k][1] < data[min][1]))
                    min = k;
            x = data[min][0];
            y = data[min][1];
            data[min][0] = data[i][0];
            data[min][1] = data[i][1];
            data[i][0] = x;
            data[i][1] = y;
        }
        return System.currentTimeMillis() - before;
    }
    private long insertionSort(long[][] data){
        long before = System.currentTimeMillis();
        int k;
        long[] pair = new long[2];
        for(int i = 1; i < data.length; i++){
            k = i;
            pair[0] = data[i][0];
            pair[1] = data[i][1];
            while(k > 0 && (data[k-1][0] > pair[0] || data[k-1][0] == pair[0] && data[k-1][1] > pair[1])){
                data[k][0] = data[k-1][0];
                data[k][1] = data[k-1][1];
                k--;
            }
            data[k][0] = pair[0];
            data[k][1] = pair[1];
        }
        return System.currentTimeMillis() - before;
    }
}
