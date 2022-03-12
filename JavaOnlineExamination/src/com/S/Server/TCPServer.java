package com.S.Server;

import com.S.GUI.TeacherGUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable{
    public void run(){
        ServerSocket ss = null;
        TeacherGUI teacherGUI = new TeacherGUI();
        try {
            ss = new ServerSocket(10087);
            while (true) {
                Socket s = ss.accept();
                new Server(s,teacherGUI).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
