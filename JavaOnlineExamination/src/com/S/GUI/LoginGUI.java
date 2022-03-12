package com.S.GUI;



import com.S.Server.TCPServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class LoginGUI {
    JFrame frame;
    JPanel panel1, panel2;
    JLabel label1, theme, information;
    JPasswordField password;
    JButton login, reset;
    private String passwordText;

    public LoginGUI() {
        frame = new JFrame("教师登录端");
        frame.setLayout(new GridLayout(5, 1));
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        theme = new JLabel(" Java 在 线 考 试 系 统 ", JLabel.CENTER);
        label1 = new JLabel("请输入指定的密码：");
        password = new JPasswordField(12);
        information = new JLabel("密码错误！", JLabel.CENTER);//提示信息
        login = new JButton("登录");
        reset = new JButton("重置");
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel1.add(label1);
        panel1.add(password);
        panel2.add(login);
        panel2.add(reset);
        frame.add(theme);
        frame.add(information);
        information.setVisible(false);
        frame.add(panel1);
        frame.add(panel2);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //登录
                if (e.getSource() == login) {
                    passwordText = password.getText();
                    if ("88888888".equals(passwordText)) {
                        //销毁当前窗口
                        frame.dispose();
                        //开启服务器端
                        Thread t = new Thread(new TCPServer());
                        t.start();
                    } else {
                        //显示错误信息
                        information.setVisible(true);
                    }
                }
            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //重置
                if (e.getSource() == reset) {
                    password.setText("");
                    information.setVisible(false);
                }
            }
        });
    }


    public static void main(String[] args) {
        new LoginGUI();
    }

}
