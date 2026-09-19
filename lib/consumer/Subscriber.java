package lib.consumer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import com.alibaba.fastjson2.JSON;

import lib.entity.Job;
import lib.executor.Processor;

import java.awt.*;
import java.awt.TrayIcon.MessageType;


public class Subscriber {
    public static final int IAM = 0;
    public static final int LST = 1;
    public static final int SUB = 2;
    public static final int PUB = 3;

    static Socket socket = null;
    static DataInputStream dis = null;
    static DataOutputStream dos = null;
    private Processor processor = new Processor();

    public Subscriber(String[] args) {
        try {
            socket = new Socket(args[0], Integer.parseInt(args[1]));
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            String deviceId = args[2];
            wComm(dos, IAM, deviceId.getBytes(StandardCharsets.UTF_8));
            wComm(dos, SUB, args[3].getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            try {
                dis.close();
                dos.close();
                socket.close();
            } catch (Exception ex) {
            }
        }
    }

    public void consume(){
        try{    
            while (true) {
                int len = dis.readInt();
                byte[] data = new byte[len];
                dis.readFully(data);
                if (len != -1){
                    Job job = JSON.parseObject(new String(data, StandardCharsets.UTF_8), Job.class);
                    //System.out.printf("Parsed Job: uid=%d, data=%s, type=%s%n", job.getUid(), job.getData(), job.getType());
                    processor.submit(job);
                }
            }
        } catch (Exception e) {
            System.out.println("Subscriber consume error: " + e.getMessage());
        }
    }

    public static void wComm(DataOutputStream dos, int cmd, byte[] data) throws Exception {
        dos.writeByte(cmd);
        wData(dos, data);
    }

    public static void wData(DataOutputStream dos, byte[] data) throws Exception {
        int len = data.length;
        dos.writeInt(len);
        dos.write(data);
        dos.flush();
    }

    public static void alert(String tooltip, String caption, String text) {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            // Image image =
            // Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip(tooltip);
            tray.add(trayIcon);

            trayIcon.displayMessage(caption, text, MessageType.INFO);
        } catch (Exception e) {
            System.out.println("tray.msg error");
        }
    }
}
