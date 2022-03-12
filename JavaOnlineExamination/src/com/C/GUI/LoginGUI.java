package com.C.GUI;

import com.Public.pojo.TestPaper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LoginGUI implements ActionListener {
    JFrame frame;
    JPanel panel1, panel2, panel3;
    JLabel label1, label2, theme, information;
    JTextField name, id;
    JButton login, reset;
    private String nameText;
    private String idText;
    private OutputStream os;
    private InputStream is;
    private StudentGUI studentGUI = null;


    public LoginGUI() {
        frame = new JFrame("学生登录端");
        frame.setLayout(new GridLayout(5, 1));
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        theme = new JLabel(" Java 在 线 考 试 系 统 ", JLabel.CENTER);
        label1 = new JLabel("用户名");
        name = new JTextField(12);
        label2 = new JLabel("  学  号");
        id = new JTextField(12);
        information = new JLabel("用户不存在！", JLabel.CENTER);
        login = new JButton("登录");
        reset = new JButton("重置");
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel1.add(label1);
        panel1.add(name);
        panel2.add(label2);
        panel2.add(id);
        panel3.add(login);
        panel3.add(reset);
        frame.add(theme);
        frame.add(information);
        information.setVisible(false);
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.addActionListener(this);
        reset.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        //登录
        if (e.getSource() == login) {
            nameText = this.name.getText();
            idText = this.id.getText();
            try {
                if (login()) {
                    //打开新的窗口
                    studentGUI = new StudentGUI(idText, nameText, getPaper());
                    studentGUI.setIs(is);
                    studentGUI.setOs(os);
                    //销毁当前窗口
                    this.frame.dispose();
                } else {
                    //显示错误信息
                    information.setVisible(true);
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        //重置
        if (e.getSource() == reset) {
            this.name.setText("");
            this.id.setText("");
            information.setVisible(false);
        }
    }

    /**
     * 登录
     *
     * @throws IOException
     */
    public boolean login() throws IOException {
//        String idText = loginGUI.getStudent().getId();
//        String nameText = loginGUI.getStudent().getName();
//        System.out.println(idText);
//        System.out.println(nameText);
        if (nameText != null && idText != null) {
//            System.out.println("login-" + idText + "-" + nameText);
            os.write(("login-" + idText + "-" + nameText).getBytes(StandardCharsets.UTF_8));
            os.flush();
            byte[] bys = new byte[10];
            int len;
            while ((len = is.read(bys)) != -1) {
                String flag = new String(bys, 0, len);
                if (flag.startsWith("login")) {
                    return "login-true".equals(flag);
                }
            }
        }
        return false;
    }

    /**
     * 获取试卷
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public TestPaper getPaper() throws ClassNotFoundException, IOException {
        ObjectInputStream ois = new ObjectInputStream(is);
        return (TestPaper) ois.readObject();
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }
}
