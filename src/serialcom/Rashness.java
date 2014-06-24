/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serialcom;
import java.sql.*;
/**
 *
 * @author Vinit
 */
public class Rashness {
    double rating=0;
    public Rashness(){
         Connection connect;
        Statement pstm=null;
        String update;
        double speed=0.0;
        ResultSet rs;
        float time=0;
        float distanceDriven;
        float throttlePosition=(float) 0.0;
        
        int noOfBrakes=7;
        int noOfHarshTurns=9;
        String weather="sunny";
        
        int SpeedRating = 0;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
        int HarshBrakesRating = 0;
        int HarshTurnRating = 0;
        int DistanceRating;
        int EngineLoadRating;
        try{
            
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            connect =DriverManager.getConnection("jdbc:odbc:access");
            pstm=connect.createStatement();
        }catch(ClassNotFoundException | SQLException e){
        }
        try{
            update="select avg(Speed) from OBD ";
            rs=pstm.executeQuery(update);
            while(rs.next()){
                speed=rs.getFloat(1);
            }
            update="select max(Time) from OBD ";
            rs=pstm.executeQuery(update);
            while(rs.next()){
                time=rs.getInt(1)/3600;
            }
            update="select avg(Throttle) from OBD ";
            rs=pstm.executeQuery(update);
            while(rs.next()){
                throttlePosition=rs.getFloat(1);
            }
        }
        catch(SQLException e){
        }
        distanceDriven=(float) (speed*time);
        if ("sunny".equals(weather) && speed == 60) {
            SpeedRating=10;
        }
        else if("sunny".equals(weather) && (speed>60 && speed<=70)) {
            SpeedRating=8;
        }
        else if("sunny".equals(weather) && (speed>70 && speed<=80)) {
            SpeedRating=7;
        }
        else if("sunny".equals(weather) && (speed>80 && speed<=100)) {
            SpeedRating=6;
        }
        else if("sunny".equals(weather) && speed>100) {
            SpeedRating=5;
        }
        
        else if ("rainy".equals(weather) && speed == 45) {
            SpeedRating=10;
        }
        else if("rainy".equals(weather) && (speed>45 && speed<=55)) {
            SpeedRating=8;
        }
        else if("rainy".equals(weather) && (speed>55 && speed<=65)) {
            SpeedRating=7;
        }
        else if("rainy".equals(weather) && (speed>65 && speed<=75)) {
            SpeedRating=6;
        }
        else if("rainy".equals(weather) && speed>75) {
            SpeedRating=5;
        }
        
        if(noOfBrakes>=5 && noOfBrakes<=7) {
            HarshBrakesRating=9;
        }
        else if(noOfBrakes>7 && noOfBrakes<=10) {
            HarshBrakesRating=8;
        }
        else if(noOfBrakes>10 && noOfBrakes<=12) {
            HarshBrakesRating=7;
        }
        else if(noOfBrakes>12 && noOfBrakes<=15) {
            HarshBrakesRating=6;
        }
        else if(noOfBrakes>15) {
            HarshBrakesRating=5;
        }
        
        if(noOfHarshTurns>=3 && noOfHarshTurns<=5) {
            HarshTurnRating=9;
        }
        else if(noOfHarshTurns>5 && noOfHarshTurns<=8) {
            HarshTurnRating=8;
        }
        else if(noOfHarshTurns>8 && noOfHarshTurns<=10) {
            HarshTurnRating=7;
        }
        else if(noOfHarshTurns>10 && noOfHarshTurns<=13) {
            HarshTurnRating=6;
        }
        else if(noOfHarshTurns>13) {
            HarshTurnRating=5;
        }
        
        
        if (distanceDriven>=0 && distanceDriven<30) {
            DistanceRating=10;
        }
        else if (distanceDriven>=30 && distanceDriven<40) {
            DistanceRating=9;
        }
        else if (distanceDriven>=40 && distanceDriven<50) {
            DistanceRating=8;
        }
        else if (distanceDriven>=50 && distanceDriven<60) {
            DistanceRating=7;
        }
        else if (distanceDriven>=60 && distanceDriven<70) {
            DistanceRating=6;
        }
        else {
            DistanceRating=5;
        }
        System.out.println("Speed "+speed);
        System.out.println("Throttle "+throttlePosition);
        System.out.println("Time "+time);
        System.out.println("Distance "+distanceDriven);
        
    System.out.println("SpeedRating "+SpeedRating);
    System.out.println("HarshBrakesRating "+HarshBrakesRating);
    System.out.println("HarshTurnRating "+HarshTurnRating);
    System.out.println("DistanceRating "+DistanceRating);
    rating=(SpeedRating+HarshBrakesRating+HarshTurnRating+DistanceRating)/4;
    System.out.println("Final Rating "+rating);
    }
    
}
