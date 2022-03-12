package com.C.GUI;

import com.C.GUI.TestPaperGUI.TestPaperGUI;
import com.Public.GUI.CorrectedTestPaperGUI;
import com.Public.pojo.TestPaper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class StudentGUI {
    JFrame frame;
    JButton startAnswer, checkScore, checkTestPaper, exitSystem;
    JLabel information;
    JPanel panel1, panel2, panel3, panel4;
    private final String id;
    private final String name;
    private TestPaper testPaper;
    private OutputStream os;
    private InputStream is;

    public StudentGUI(String id, String name, TestPaper testPaper) {
        this.id = id;
        this.name = name;
        this.testPaper = testPaper;
        frame = new JFrame("学生端");
        frame.setSize(500, 300);
        frame.setLayout(new GridLayout(5, 1));
        frame.setLocationRelativeTo(null);
        information = new JLabel(id + "      " + name);
        information.setHorizontalAlignment(SwingConstants.CENTER);
        startAnswer = new JButton("开始答题");
        checkScore = new JButton("查看成绩");
        checkTestPaper = new JButton("查看已作答试卷");
        exitSystem = new JButton("退出系统");
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        panel1.add(startAnswer);
        panel2.add(checkScore);
        panel3.add(checkTestPaper);
        panel4.add(exitSystem);
        frame.add(information);
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        frame.add(panel4);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == startAnswer) {
                    TestPaperGUI testPaperGUI = new TestPaperGUI(id, name, testPaper);
                    testPaperGUI.setIs(is);
                    testPaperGUI.setOs(os);
                }
            }
        });//开始答题
        checkScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == checkScore) {
                    try {
                        new CheckScoreGUI(getScores());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });//查看成绩
        checkTestPaper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] answer = getAnswer(id, name);//文件中的所有信息
                    int item1 = testPaper.getFirstItem().size();
                    int item2 = testPaper.getSecondItem().size();
                    String[] detailScores = getDetailScores(id, item1 + item2);
                    if(answer!=null && detailScores!=null) {
                        System.out.println(Arrays.toString(answer));
                        System.out.println(Arrays.toString(detailScores));
                        new CorrectedTestPaperGUI(id, name, testPaper, answer, detailScores);
                    }else{
                        JOptionPane.showMessageDialog(frame, "老师还未批改！", "提示",JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });//查看已作答试卷
        exitSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == exitSystem) {
                    try {
                        os.write(("end-" + id + "-" + name).getBytes(StandardCharsets.UTF_8));
                        System.exit(0);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });//退出系统
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    os.write(("end-" + id + "-" + name).getBytes(StandardCharsets.UTF_8));
                    System.exit(0);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取成绩
     *
     * @return
     * @throws IOException
     */
    public double[] getScores() throws IOException {
        os.write(("checkScore-" + id).getBytes(StandardCharsets.UTF_8));
        os.flush();
        double[] scores = new double[3];
        byte[] bys = new byte[1024];
        int len;
        while ((len = is.read(bys)) != -1) {
            String scoreStr = new String(bys, 0, len);
            if (scoreStr.startsWith("scores")) {
                String[] score = scoreStr.split("-");
                scores[0] = Double.parseDouble(score[1]);
                scores[1] = Double.parseDouble(score[2]);
                scores[2] = Double.parseDouble(score[3]);
                return scores;
            }
        }//scores-12.0-32.0-44.0
        return null;
    }

    /**
     * 获取答案
     *
     * @param id
     * @param name
     * @return
     * @throws IOException
     */
    public String[] getAnswer(String id, String name) throws IOException {
        os.write(("getAnswer-" + name + id).getBytes(StandardCharsets.UTF_8));
        os.flush();
        byte[] bys = new byte[1024];
        int len;
        while ((len = is.read(bys)) != -1) {
            String answerStr = new String(bys, 0, len);
            if (answerStr.startsWith("getAnswer")) {
//                System.out.println(answerStr);
                if("getAnswer-null".equals(answerStr)){
                    return null;
                }
                String[] arr = answerStr.split("-");
                String[] answer = new String[arr.length - 1];
                for (int i = 1; i < arr.length; i++) {
                    answer[i - 1] = arr[i];
                }
                return answer;
            }
        }//scores-12.0-32.0-44.0
        return null;
    }

    /**
     * 获取小题分
     *
     * @param id
     * @param sum
     * @return
     * @throws IOException
     */
    public String[] getDetailScores(String id, int sum) throws IOException {
        os.write(("getDetailScores-" + sum + "-" + id).getBytes(StandardCharsets.UTF_8));
        os.flush();
        byte[] bys = new byte[1024];
        int len;
        while ((len = is.read(bys)) != -1) {
            String dsStr = new String(bys, 0, len);
            if (dsStr.startsWith("getDetailScores")) {
//                System.out.println(dsStr);
                String[] arr = dsStr.split("-");
                String[] detailScores = new String[arr.length - 1];
                for (int i = 1; i < arr.length; i++) {
                    detailScores[i - 1] = arr[i];
                }
                return detailScores;
            }
        }//scores-12.0-32.0-44.0
        return null;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

//    public String getName() {
//        return name;
//    }
//
//    public TestPaper getTestPaper() {
//        return testPaper;
//    }
//
//    public void setTestPaper(TestPaper testPaper) {
//        this.testPaper = testPaper;
//    }
}
