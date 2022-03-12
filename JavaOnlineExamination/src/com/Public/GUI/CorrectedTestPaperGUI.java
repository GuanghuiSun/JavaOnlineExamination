package com.Public.GUI;

import com.Public.pojo.TestPaper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class CorrectedTestPaperGUI {
    JFrame frame;
    JLabel staticIdLabel, staticNameLabel, staticScoreLabel, itemTwoLabel,itemOneLabel;
    JLabel idLabel, nameLabel, scoreLabel, questionLabel;
    ButtonGroup buttonGroup;
    JPanel overallPanel,optionPanel,informationPanel;
    JScrollPane scrollPane;
//    Font f;
    private final String id;
    private final String name;
    private final TestPaper testPaper;
    private final String[] answer;
    private final String[] detailScores;

    public CorrectedTestPaperGUI(String id, String name, TestPaper testPaper, String[] answer, String[] detailScores){
        this.id = id;
        this.name = name;
        this.testPaper = testPaper;
        this.answer = answer;//文件中的所有信息
        this.detailScores = detailScores;//只有成绩

        //计算总分
        double score = 0;
        for(String s :detailScores){
            score += Double.parseDouble(s);
        }
        //处理试卷和答案
        //客观题
        Map<String, String> firstItem = testPaper.getFirstItem();
        Set<String> item1Questions = firstItem.keySet();
        //主观题
        ArrayList<String> secondItem = testPaper.getSecondItem();
        //题号——也是答案数组对应的位置
        int index =  1;
//        f = new Font("黑体", Font.PLAIN, 18);
        frame = new JFrame("查看试卷");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 900);
        frame.setLocationRelativeTo(null);
        //总的面板
        overallPanel = new JPanel(new GridLayout(0, 1));
        //个人信息面板
        {
            staticIdLabel = new JLabel("学号");
            idLabel = new JLabel(id);
            staticNameLabel = new JLabel("                           姓名");
            nameLabel = new JLabel(name);
            staticScoreLabel = new JLabel("                                得分");
            scoreLabel = new JLabel(String.valueOf(score));
            informationPanel = new JPanel(new FlowLayout());
            informationPanel.setPreferredSize(new Dimension(0, 50));
//        informationPanel.setSize(800,50);
//            informationPanel.setBorder(BorderFactory.createLineBorder(Color.red, 3));
            informationPanel.add(staticIdLabel);
            informationPanel.add(idLabel);
            informationPanel.add(staticNameLabel);
            informationPanel.add(nameLabel);
            informationPanel.add(staticScoreLabel);
            informationPanel.add(scoreLabel);
            overallPanel.add(informationPanel);
        }
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
                    switch (answer[index]){
                        case "A":
                        case "a":
                            a.setSelected(true);
                            break;
                        case "B":
                        case "b":
                            b.setSelected(true);
                            break;
                        case "C":
                        case "c":
                            c.setSelected(true);
                            break;
                        case "D":
                        case "d":
                            d.setSelected(true);
                            break;
                    }
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
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    panel.setPreferredSize(new Dimension(0, 20));
//                panel.setBorder(BorderFactory.createLineBorder(Color.red,2));
                    JLabel label = new JLabel("得分");
//                label.setHorizontalAlignment(JLabel.RIGHT);
                    JTextField textField = new JTextField(5);
//                textField.setHorizontalAlignment(JTextField.RIGHT);
                    textField.setPreferredSize(new Dimension(50, 20));
                    textField.setText(detailScores[index - 1]);
                    textField.setEditable(false);
                    panel.add(label);
                    panel.add(textField);
                    optionPanel.setSize(new Dimension(0, 20));
                    optionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    overallPanel.add(optionPanel);
                    overallPanel.add(panel);
//                    optionPanel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                }
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
//                questionLabel.setFont(f);
                questionLabel.setPreferredSize(new Dimension(0, 20));
//                questionLabel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                overallPanel.add(questionLabel);
                JTextArea item2Answer = new JTextArea(4, 0);
//                item2Answer.setFont(f);
                item2Answer.setText(answer[index]);
                item2Answer.setEditable(false);
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                panel.setPreferredSize(new Dimension(0, 20));
//                panel.setBorder(BorderFactory.createLineBorder(Color.red,2));
                JLabel label = new JLabel("得分");
//                label.setHorizontalAlignment(JLabel.RIGHT);
                JTextField textField = new JTextField(5);
//                textField.setHorizontalAlignment(JTextField.RIGHT);
                textField.setPreferredSize(new Dimension(50, 20));
                textField.setText(detailScores[index - 1]);
                textField.setEditable(false);
                panel.add(label);
                panel.add(textField);
                overallPanel.add(item2Answer);
                overallPanel.add(panel);
                index++;
            }
        }

        scrollPane = new JScrollPane(overallPanel);
        frame.add(scrollPane);
        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
