package com.C.Client;

import com.C.GUI.LoginGUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        //创建客户端
        Socket s = new Socket("localhost", 10087);
        OutputStream os = s.getOutputStream();
        InputStream is = s.getInputStream();
        LoginGUI loginGUI = new LoginGUI();
        loginGUI.setIs(is);
        loginGUI.setOs(os);

    }
}
