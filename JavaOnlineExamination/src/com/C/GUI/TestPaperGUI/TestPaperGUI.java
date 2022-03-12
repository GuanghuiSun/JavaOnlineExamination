package com.C.GUI.TestPaperGUI;

import com.Public.pojo.TestPaper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class TestPaperGUI {
    JFrame frame;
    JLabel staticIdLabel, staticNameLabel, staticScoreLabel, itemOneLabel, itemTwoLabel;
    JLabel idLabel, nameLabel, scoreLabel, questionLabel;
    JButton submit;
    ButtonGroup buttonGroup;
    JPanel overallPanel, informationPanel, optionPanel;
    JScrollPane scrollPane;
    private final String id;
    private final String name;
    private final TestPaper testPaper;
    private String[] answer;
    private OutputStream os;
    private InputStream is;

    public TestPaperGUI(String id, String name, TestPaper testPaper) {
        this.id = id;
        this.name = name;
        this.testPaper = testPaper;
        //题号
        int index = 1;
        //题目和答案
        //客观题
        Map<String, String> firstItem = testPaper.getFirstItem();
        Set<String> item1Questions = firstItem.keySet();
        //主观题
        ArrayList<String> secondItem = testPaper.getSecondItem();
        answer = new String[item1Questions.size() + secondItem.size() + 1];
        answer[0] = name + "|" + id;

        frame = new JFrame("试题信息");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 900);
        frame.setLocationRelativeTo(null);
        //个人信息面板
        {
            staticIdLabel = new JLabel("学号");
            idLabel = new JLabel(id);
            staticNameLabel = new JLabel("                           姓名");
            nameLabel = new JLabel(name);
            staticScoreLabel = new JLabel("                                得分");
            scoreLabel = new JLabel("99");
            scoreLabel.setVisible(false);
            informationPanel = new JPanel(new FlowLayout());
            informationPanel.setPreferredSize(new Dimension(0, 50));
//        informationPanel.setSize(800,50);
//            informationPanel.setBorder(BorderFactory.createLineBorder(Color.red, 3));
            informationPanel.add(staticIdLabel);
            informationPanel.add(idLabel);
            informationPanel.add(staticNameLabel);
            informationPanel.add(nameLabel);
            informationPanel.add(staticScoreLabel);
//        informationPanel.add(scoreLabel);
        }
        //总的面板
        overallPanel = new JPanel(new GridLayout(0, 1));
        overallPanel.add(informationPanel);
        //题目信息面板
        //添加客观题试题信息
        {
            itemOneLabel = new JLabel("一、选择题(每小题3分)");
//            itemOneLabel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
            overallPanel.add(itemOneLabel);
            for (String question : item1Questions) {
                //添加题目信息
                String[] questions = question.split("-");
                for (int i = 0; i < questions.length; i++) {
                    if (i == 0) {
                        questions[i] = index + ". " + questions[i];
                    }
                    questionLabel = new JLabel(questions[i]);
                    questionLabel.setPreferredSize(new Dimension(0, 20));
//                questionLabel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                    overallPanel.add(questionLabel);
                }
                //添加选择按钮
                {
                    JRadioButton a = new JRadioButton("A", false);
                    JRadioButton b = new JRadioButton("B", false);
                    JRadioButton c = new JRadioButton("C", false);
                    JRadioButton d = new JRadioButton("D", false);
                    int finalIndex = index;
                    a.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() == a) {
                                answer[finalIndex] = "A";
//                                System.out.println(finalIndex + "-A" );
                            }
                        }
                    });
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() == b) {
                                answer[finalIndex] = "B";
//                                System.out.println(finalIndex + "-B" );
                            }
                        }
                    });
                    c.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() == c) {
                                answer[finalIndex] = "C";
//                                System.out.println(finalIndex + "-C" );
                            }
                        }
                    });
                    d.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() == d) {
                                answer[finalIndex] = "D";
//                                System.out.println(finalIndex + "-D" );
                            }
                        }
                    });
                    buttonGroup = new ButtonGroup();
                    buttonGroup.add(a);
                    buttonGroup.add(b);
                    buttonGroup.add(c);
                    buttonGroup.add(d);
                    optionPanel = new JPanel();
                    optionPanel.add(a);
                    optionPanel.add(b);
                    optionPanel.add(c);
                    optionPanel.add(d);
                    optionPanel.setSize(new Dimension(0, 20));
                    optionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//                    optionPanel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                }
                overallPanel.add(optionPanel);
                index++;
            }
        }
        //添加主观题信息
        {
            itemTwoLabel = new JLabel("二、主观题(每小题10分)");
//        itemOneLabel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
            overallPanel.add(itemTwoLabel);
            for (String s : secondItem) {
                questionLabel = new JLabel(index + "." + s);
                questionLabel.setPreferredSize(new Dimension(0, 20));
//                questionLabel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                overallPanel.add(questionLabel);
                JTextArea item2Answer = new JTextArea(4, 0);
                int finalIndex = index;
                item2Answer.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        answer[finalIndex] = item2Answer.getText();
                    }
                });
                overallPanel.add(item2Answer);
                index++;
            }
        }

        submit = new JButton("提交");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == submit) {
                    int unAnsweredNum = 0;
                    for (String s : answer) {
                        if (s == null) {
                            unAnsweredNum++;
                        }
                    }
                    Object[] option = {"提交", "取消"};
                    int choice = 0;
                    if (unAnsweredNum == 0) {
                        choice = JOptionPane.showOptionDialog(frame, "是否确认提交？", "提交", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
                    } else {
                        String message = "是否确认提交？您还有" + unAnsweredNum + "题未作答！";
                        choice = JOptionPane.showOptionDialog(frame, message, "提交", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
                    }
                    if (choice == 0) {
                        try {
                            //交卷动作
                            String str = "answered-" + Arrays.toString(answer);
                            os.write(str.getBytes(StandardCharsets.UTF_8));
                            frame.dispose();
                        }catch (IOException exception){
                            exception.printStackTrace();
                        }
                    }
                }
            }
        });
        overallPanel.add(submit);
        scrollPane = new JScrollPane(overallPanel);
        frame.add(scrollPane);
        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

//    public String[] getAnswer() {
//        return answer;
//    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }
}
