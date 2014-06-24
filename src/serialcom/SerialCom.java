package serialcom;
import eu.hansolo.steelseries.gauges.Linear;
import eu.hansolo.steelseries.gauges.Radial;
import eu.hansolo.steelseries.gauges.Radial2Top;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.ColorDef;
import eu.hansolo.steelseries.tools.GaugeType;
import eu.hansolo.steelseries.tools.PointerType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
public class SerialCom implements ActionListener{
    private static String response;
    private static double rating;
    private static String mode;
    final private static String ENGINE_RUN_TIME="011F";
    final private static String SPEED="010D";
    final private static String THROTTLE="0111";
    final private static String RPM="010C";
    final private static String ENGINE_LOAD="0104";
    private static  int indexOf;
    private static Statement pstm;
    JPanel dem;
    private static double dX;
    private static double dY;
    static String message;
    private static int index=0;
    private static Connection connect;
    private static double turn;
    private static String update;
    private static double engineLoad;
    private static double speed;
    private static double rpm;
    private static double throttle;
    private static double timeSinceEngStart;
    private static String deltaX;
    private static String deltaY;
    private static double speedArray[]=new double[60];
    private static double rpmArray[]=new double[60];
    private static double turnArray[]=new double[60];
    private static double engineLoadArray[]=new double[60];
    private static double throttleArray[]=new double[60];
    private static double deltaXArray[]=new double[60];
    private static double deltaYArray[]=new double[60];
    public static void main(String[] args) throws IOException {
        rating=0;
        SwingUtilities.invokeLater(new Runnable() {
                   
                @Override
                
            public void run() {
                createAndShowGUI();
                
            }

            
        });

        SerialIO connection=new SerialIO();
        try{
            
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            connect =DriverManager.getConnection("jdbc:odbc:access");
            pstm=connect.createStatement();
        }catch(ClassNotFoundException | SQLException e){
        }
        
        
        System.out.println(connection.ATComm("E0"));
        
   
        
        
        
        while(true)
        {
                message=connection.AccValue();
                if(message == null){
                    Rashness start1=new Rashness();
                    System.exit(1);
                }
                //System.out.println(message);
                response=connection.Comm(ENGINE_RUN_TIME);
                timeSinceEngStart=connection.formatResult("1F",response);
                message=connection.AccValue();
                if(message == null){
                    Rashness start1=new Rashness();
                    System.exit(1);
                }
                deltaX=message.substring(0,message.indexOf(","));
                deltaY=message.substring(message.indexOf(",")+1);
                if(deltaX.equals("0.0")&&deltaY.equals("0.0")){
                    turn=0.0;
                }else{
                    turn=(Math.atan(Math.abs(Double.parseDouble(deltaX))/Double.parseDouble(deltaY)))*180/3.14;
                }
                response=connection.Comm(SPEED);
                System.out.println(response);
                speed=connection.formatResult("0D",response);
                message=connection.AccValue();
                if(message == null){
                    Rashness start1=new Rashness();
                    System.exit(1);
                }
                deltaX=message.substring(0,message.indexOf(","));
                deltaY=message.substring(message.indexOf(",")+1);
                if(deltaX.equals("0.0")&&deltaY.equals("0.0")){
                    turn=0.0;
                }else{
                    turn=(Math.atan(Math.abs(Double.parseDouble(deltaX))/Double.parseDouble(deltaY)))*180/3.14;
                }
                response=connection.Comm(RPM);
                System.out.println(response);
                rpm=connection.formatResult("0C",response);
                message=connection.AccValue();
                if(message == null){
                    Rashness start1=new Rashness();
                    System.exit(1);
                }
                deltaX=message.substring(0,message.indexOf(","));
                deltaY=message.substring(message.indexOf(",")+1);
                if(deltaX.equals("0.0")&&deltaY.equals("0.0")){
                    turn=0.0;
                }else{
                    turn=(Math.atan(Math.abs(Double.parseDouble(deltaX))/Double.parseDouble(deltaY)))*180/3.14;
                }
                response=connection.Comm(THROTTLE);
                throttle=connection.formatResult("11",response);
                message=connection.AccValue();
                if(message == null){
                    Rashness start1=new Rashness();
                    System.exit(1);
                }
                deltaX=message.substring(0,message.indexOf(","));
                deltaY=message.substring(message.indexOf(",")+1);
                if(deltaX.equals("0.0")&&deltaY.equals("0.0")){
                    turn=0.0;
                }else{
                    turn=(Math.atan(Math.abs(Double.parseDouble(deltaX))/Double.parseDouble(deltaY)))*180/3.14;
                }
                response=connection.Comm(ENGINE_LOAD);
                engineLoad=connection.formatResult("04",response);
                message=connection.AccValue();
                if(message == null){
                    
                    System.exit(1);
                }
                deltaX=message.substring(0,message.indexOf(","));
                deltaY=message.substring(message.indexOf(",")+1);
                if(deltaX.equals("0.0")&&deltaY.equals("0.0")){
                    turn=0.0;
                }else{
                    turn=(Math.atan(Math.abs(Double.parseDouble(deltaX))/Double.parseDouble(deltaY)))*180/3.14;
                }//deltaX="0";
                //deltaY="0";
                //turn=0;
                System.out.println(deltaX+","+deltaY);
                
                try{
                    speedArray[index]=speed;
                    turnArray[index]=turn;
                    throttleArray[index]=throttle;
                    engineLoadArray[index]=engineLoad;
                    rpmArray[index]=rpm;
                    deltaXArray[index]=Double.parseDouble(deltaX);
                    deltaYArray[index]=Double.parseDouble(deltaY);
                    index++;
                    response=connection.Comm(ENGINE_RUN_TIME);
                    timeSinceEngStart=connection.formatResult("1F",response);
                    if(index==59){
                        speed=0;
                        rpm=0;
                        throttle=0;
                        engineLoad=0;
                        dX=0;
                        dY=0;
                        turn=0;
                        for(int i=0;i<60;i++){
                            speed+=speedArray[i];
                            rpm+=rpmArray[i];
                            throttle+=throttleArray[i];
                            engineLoad+=engineLoadArray[i];
                            dX+=deltaXArray[i];
                            dY+=deltaYArray[i];
                            turn+=turnArray[i];
                            
                        }
                        speed=speed/60;
                        deltaX=(dX/60)+"";
                        deltaY=(dY/60)+"";
                        rpm=rpm/60;
                        throttle=throttle/60;
                        engineLoad=engineLoad/60;
                        turn=turn/60;
                        update="insert into OBD values("+timeSinceEngStart+",'"+speed+"','"+throttle+"','"+engineLoad+"','"+rpm+"','"+deltaX+"','"+deltaY+"','"+turn+"')";
                        pstm.execute(update);
                        index=0;
                        Rashness start=new Rashness();
                        rating=start.rating;
                    }
                        
                }
                catch(NumberFormatException | SQLException e){
                    connection.port.close();
                    System.exit(1);
                }
                }    
    }
            public static void createAndShowGUI() {
                JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("[=] OBD UBI [=]");

        //Create and set up the content pane.
        SerialCom demo = new SerialCom();
        frame.setContentPane(demo.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(280, 190);
        frame.setVisible(true);
            }
    private JButton redButton;
    private JLabel redLabel;
    private JLabel redLabel1;
    private JLabel redLabel2;
    private JLabel redLabel3;
    private JLabel redLabel4;
    private JLabel redLabel5;
    
    private static JTextField jText;
    private static JTextField jText1;
    private static JTextField jText2;
    private static JTextField jText3;
    private static JTextField jText4;
    private static JTextField jText5;
    private static Radial radial17;
    private static Radial radial18;
    private static Radial radial19;
    private static Linear linear17;
    private static Linear linear18;
    private static Linear linear19;
    private static Radial2Top radial6;
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==redButton){
            displayTimer.start();
            
                
        }
    }

    private JPanel createContentPane() {
    JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);
        dem=new JPanel();
        dem.setLayout(null);
       
        dem.setLocation(50, 50);
        dem.setSize(1000, 1000);
        totalGUI.add(dem);
        
        jText=new JTextField();
        jText.setLocation(100,0);
        jText.setSize(100, 30);
        //dem.add(jText);
        
        redLabel = new JLabel("Speed");
        redLabel.setLocation(0, 0);
        redLabel.setSize(120, 30);
        redLabel.setHorizontalAlignment(0);
        
        //dem.add(redLabel);
        
        jText1=new JTextField();
        jText1.setLocation(100,50);
        jText1.setSize(100, 30);
        //dem.add(jText1);
        
        redLabel1 = new JLabel("Acceleration");
        redLabel1.setLocation(0, 50);
        redLabel1.setSize(120, 30);
        redLabel1.setHorizontalAlignment(0);
        
        //dem.add(redLabel1);
        
        jText2=new JTextField();
        jText2.setLocation(450,0);
        jText2.setSize(100, 30);
        //dem.add(jText2);
        
        redLabel2 = new JLabel("Throttle");
        redLabel2.setLocation(300, 0);
        redLabel2.setSize(120, 30);
        redLabel2.setHorizontalAlignment(0);
        
        //dem.add(redLabel2);
        jText3=new JTextField();
        jText3.setLocation(450,50);
        jText3.setSize(100, 30);
        //dem.add(jText3);
        
        redLabel3 = new JLabel("Time Since Engine Start");
        redLabel3.setLocation(300, 50);
        redLabel3.setSize(150, 30);
        redLabel3.setHorizontalAlignment(0);
        
        //dem.add(redLabel3);
        jText4=new JTextField();
        jText4.setLocation(100,100);
        jText4.setSize(100, 30);
        //dem.add(jText4);
        
        redLabel4 = new JLabel("RPM");
        redLabel4.setLocation(0, 100);
        redLabel4.setSize(120, 30);
        redLabel4.setHorizontalAlignment(0);
        
        //dem.add(redLabel4);
        jText5=new JTextField();
        jText5.setLocation(450,100);
        jText5.setSize(100, 30);
        //dem.add(jText5);
        
        redLabel5 = new JLabel("Turn");
        redLabel5.setLocation(300, 100);
        redLabel5.setSize(120, 30);
        redLabel5.setHorizontalAlignment(0);
        //dem.add(redLabel5);
        
        radial17 = new Radial();
        
        radial17.setName("Engine Load"); // NOI18N
        radial17.setGaugeType(GaugeType.TYPE4);
        radial17.setTitle("Engine Load");
        radial17.setUnitString("%");
        radial17.setBackgroundColor(BackgroundColor.STAINLESS);
        radial17.setPointerType(PointerType.TYPE2);
        radial17.setPointerColor(ColorDef.RED);
        radial17.setLocation(200, 150);
        radial17.setSize(100, 100);
        radial17.setMaxValue(100);
        
        dem.add(radial17);
        radial18 = new Radial();
        radial18.setTitle("RPM");
        radial18.setUnitString("X*1000");
        radial18.setName("RPM"); // NOI18N
        radial18.setGaugeType(GaugeType.TYPE4);
        radial18.setBackgroundColor(BackgroundColor.STAINLESS);
        radial18.setPointerType(PointerType.TYPE2);
        radial18.setPointerColor(ColorDef.RED);
        radial18.setLocation(500, 150);
        radial18.setSize(100, 100);
        radial18.setMaxValue(8);
        
        dem.add(radial18);
        
        radial19 = new Radial();
        radial19.setTitle("Speed");
        radial19.setUnitString("Km/Hr");
        radial19.setName("Speed"); // NOI18N
        radial19.setGaugeType(GaugeType.TYPE4);
        radial19.setBackgroundColor(BackgroundColor.STAINLESS);
        radial19.setPointerType(PointerType.TYPE2);
        radial19.setPointerColor(ColorDef.RED);
        radial19.setLocation(350, 300);
        radial19.setSize(100, 100);
        radial19.setMaxValue(255);
        
        dem.add(radial19);
       
        linear17=new Linear();
        linear17.setName("Throttle");
        linear17.setMaxValue(100);
        linear17.setAlignmentX(0);
        linear17.setLocation(0,0);
        linear17.setTitle("Throttle");
        linear17.setUnitString("%");
        linear17.setSize(100,500);
        dem.add(linear17);
        
        linear18=new Linear();
        linear18.setName("Acceleration");
        linear18.setMaxValue(100);
        linear18.setAlignmentX(0);
        linear18.setLocation(800,0);
        linear18.setSize(100,500);
        linear18.setTitle("Acceleration");
        linear18.setUnitString("m/s");
        dem.add(linear18);

        linear19=new Linear();
        linear19.setName("Rating");
        linear19.setMaxValue(10);
        linear19.setAlignmentY(0);
        linear19.setLocation(150,525);
        linear19.setSize(600,100);
        linear19.setTitle("Rating");
        //linear18.setUnitString("");
        dem.add(linear19);

        radial6=new Radial2Top();
        radial6.setTitle("Turn");
        radial6.setUnitString("Degree");
        radial6.setName("Turn"); // NOI18N
        radial6.setGaugeType(GaugeType.TYPE4);
        radial6.setBackgroundColor(BackgroundColor.SATIN_GRAY);
        radial6.setPointerType(PointerType.TYPE2);
        radial6.setPointerColor(ColorDef.RED);
        radial6.setLocation(350, 0);
        radial6.setSize(100, 100);
        radial6.setMaxValue(90);
        dem.add(radial6);
        
        redButton = new JButton("Start");
        redButton.setLocation(400, 150);
        redButton.setSize(120, 30);
        redButton.addActionListener(this);
        dem.add(redButton);
        return totalGUI;
    }
    static Timer displayTimer = new Timer(200,  new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            jText.setText(speed+"");
            radial19.setValue(speed);
            radial17.setValue(engineLoad);
            radial18.setValue(rpm/1000);
            linear17.setValue(throttle);
            radial6.setValue(turn);
            linear18.setValue(Double.parseDouble(deltaY));
            jText2.setText(throttle+"");
            jText3.setText(timeSinceEngStart+"");
            //jText1.setText(deltaY+"");
            jText4.setText(rpm/1000+"");
            linear19.setValue(rating); 
        }
    });
    
}
