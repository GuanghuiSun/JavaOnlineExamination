package com.S.GUI.TestPaperGUI;

import com.Public.pojo.TestPaper;
import com.S.Service.Impl.TeacherServiceImpl;
import com.S.Service.TeacherService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class CorrectTestPaperGUI {
    JFrame frame;
    JLabel itemTwoLabel;
    JLabel questionLabel;
    JButton submit;
    JPanel overallPanel;
    JScrollPane scrollPane;
    Font f;
    private final String id;
    private final String name;
    private final TestPaper testPaper;
    private final String[] answer;
    private final File file;
    private double[] secondItemScores;
    private TeacherService teacherService;

    /**
     * 构造
     *
     * @param id
     * @param name
     * @param testPaper
     * @param answer    姓名+学号  + 答案
     * @param file      学生本地答案文件
     */
    public CorrectTestPaperGUI(String id, String name, TestPaper testPaper, String[] answer, File file) {
        this.id = id;
        this.name = name;
        this.testPaper = testPaper;
        this.answer = answer;
        this.file = file;
        teacherService = new TeacherServiceImpl();

        //处理试卷和答案
        //客观题
        Map<String, String> firstItem = testPaper.getFirstItem();
        Set<String> item1Questions = firstItem.keySet();
        //主观题
        ArrayList<String> secondItem = testPaper.getSecondItem();
        //题号——也是答案数组对应的位置
        int index = firstItem.size() + 1;
        //分数
        secondItemScores = new double[secondItem.size()];
        f = new Font("黑体", Font.PLAIN, 18);
        frame = new JFrame("批改试卷");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 900);
        frame.setLocationRelativeTo(null);
        //总的面板
        overallPanel = new JPanel(new GridLayout(0, 1));
        //题目信息面板
        //添加主观题信息
        {
            itemTwoLabel = new JLabel("二、主观题(每小题10分)");
//        itemOneLabel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
            overallPanel.add(itemTwoLabel);
            for (String s : secondItem) {
                questionLabel = new JLabel(index + "." + s);
                questionLabel.setFont(f);
                questionLabel.setPreferredSize(new Dimension(0, 20));
//                questionLabel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                overallPanel.add(questionLabel);
                JTextArea item2Answer = new JTextArea(4, 0);
                item2Answer.setFont(f);
                item2Answer.setText(answer[index]);
                item2Answer.setEditable(false);
                int finalIndex = index - firstItem.size() - 1;
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                panel.setPreferredSize(new Dimension(0, 20));
//                panel.setBorder(BorderFactory.createLineBorder(Color.red,2));
                JLabel label = new JLabel("得分");
//                label.setHorizontalAlignment(JLabel.RIGHT);
                JTextField textField = new JTextField(5);
//                textField.setHorizontalAlignment(JTextField.RIGHT);
                textField.setPreferredSize(new Dimension(50, 20));
                textField.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        if (textField.getText() != null && !textField.getText().equals(""))
                            secondItemScores[finalIndex] = Double.parseDouble(textField.getText());
                    }
                });
                panel.add(label);
                panel.add(textField);
                overallPanel.add(item2Answer);
                overallPanel.add(panel);
                index++;
            }
        }

        submit = new JButton("提交");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == submit) {
                    Object[] option = {"提交", "取消"};
                    int choice = JOptionPane.showOptionDialog(frame, "是否确认提交？", "提交", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
                    if (choice == 0) {
//                        批改完成动作
                        //更改文件名字为corrected,并转移文件夹
                        teacherService.replaceFile(file);
                        //录入主观题小题分
                        teacherService.inputSecondItemScores(id, firstItem.size() + 1, secondItemScores);
                        //录入主观题分数
                        teacherService.saveSecondItemScore(secondItemScores,id);
                        //计算总分
                        teacherService.sumScore(id);
                        //关闭窗口
                        frame.dispose();
                    }
                }
            }
        });
        overallPanel.add(submit);
        scrollPane = new JScrollPane(overallPanel);
        frame.add(scrollPane);
        frame.setVisible(true);
//        frame.setFont(f);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
