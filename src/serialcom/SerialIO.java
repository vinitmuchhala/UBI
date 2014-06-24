package serialcom;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import javax.comm.*;
public class SerialIO {
    String wantedPortName="CNCB0";
    Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
    CommPortIdentifier portId = null;  // will be set if port found
    PrintStream    os = null;
    InputStream in=null;
    SerialPort port = null;
    int indexOf;
    ServerSocket serverSocket = null;
    Socket socket = null;
    DataInputStream dataInputStream = null;
    DataOutputStream dataOutputStream = null;
    boolean containsThrottle;
    boolean containsFuelType;
    boolean containsFuelLevel;
    boolean containsSpeed;
    String mode=null;
    int[] speed={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    String fuelType;
    int[] fuelLevel={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    int[] throttle={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    String formattedResponse="CORRUPTDATA";
    String avgSpeed,maxThrottle,lFuelLevel;
    private byte[] readBuffer;
    int i=0;
    
    private static int numValue;
    private static int numValue2;
   
    private boolean containsRPM;
    private boolean containsEngineRunTime;
    private String formattedResponse2;
   
    public SerialIO() throws IOException
    {
        while (portIdentifiers.hasMoreElements()){
            CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
            if(pid.getPortType() == CommPortIdentifier.PORT_SERIAL &&
            pid.getName().equals(wantedPortName)) 
            {
                portId = pid;
                break;
            }
        }
        if(portId == null)
        {
            System.err.println("Could not find serial port " + wantedPortName);
            System.exit(1);
        }
        try {
            port = (SerialPort) portId.open(
                "name", // Name of the application asking for the port 
                1000   // Wait max. 10 sec. to acquire port
            );
        } catch(PortInUseException e) {
            System.err.println("Port already in use: " + e);
           // System.exit(1);
        }
        try{
            port.setSerialPortParams(
                9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

            port.notifyOnDataAvailable(true);
        }
        catch(UnsupportedCommOperationException e){
            System.err.println(e);  
        }
        try {
            in = port.getInputStream();
        } catch (IOException e) {
            System.err.println("Can't open input stream: write-only");
  
        }
        os = new PrintStream(port.getOutputStream(), true);
     
        
        serverSocket = new ServerSocket(1234);
        System.out.println("Listening :8888");
        try {
            socket = serverSocket.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
           
            
    
        
    
   } catch (IOException e) {
   }
    }
    public String Comm(String command) throws IOException
    {
        String response;
        os.print(command+"\r\n");
        readBuffer = new byte[64];
        port.notifyOnDataAvailable(true);
        sleep(200);
        in.read(readBuffer);
        mode=command.substring(2);
        //String response1=new String(readBuffer);
        response=new String(readBuffer);
        
        return response;
    }
    public String ATComm(String command) throws IOException
    {
        String response;
        os.print("AT"+command+"\r");
        readBuffer = new byte[16];
        port.notifyOnDataAvailable(true);
        int numBytes = in.read(readBuffer);
        response=new String(readBuffer);
        try{
            Thread.sleep(1000);
         }
         catch(InterruptedException e){
         }
        return response;
    }
    public void sleep(int milli)
    {
        try{
            Thread.sleep(milli);
         }
         catch(InterruptedException e){
         }
       
    }
    public double formatResult(String pid,String response){
        switch (pid) {
            case "1F":
                indexOf=response.indexOf(pid);
                response=response.substring(indexOf);
                numValue=Integer.parseInt(response.substring(3, 5),16);
                numValue2=Integer.parseInt(response.substring(6, 8),16);
                return numValue*256+numValue2;
            case "0C":
                indexOf=response.indexOf(pid);
                response=response.substring(indexOf);
                numValue=Integer.parseInt(response.substring(3, 5),16);
                numValue2=Integer.parseInt(response.substring(6, 8),16);
                return (numValue*256+numValue2)/4;
            case "11":
                indexOf=response.indexOf(pid);
                response=response.substring(indexOf);
                numValue=Integer.parseInt(response.substring(3, 5),16);
                return numValue/2.55;
            case "0D":
                indexOf=response.indexOf(pid);
                response=response.substring(indexOf);
                numValue=Integer.parseInt(response.substring(3, 5),16);
                return numValue;
            case "04":
                indexOf=response.indexOf(pid);
                response=response.substring(indexOf);
                numValue=Integer.parseInt(response.substring(3, 5),16);
                return numValue/2.55;
        }
        return 0;

    }
    public String AccValue(){
        try{
            dataOutputStream.writeUTF("ACCEL");
            return dataInputStream.readUTF();
        }catch(IOException | NumberFormatException e){
            return null;
        }
        
    }
}
