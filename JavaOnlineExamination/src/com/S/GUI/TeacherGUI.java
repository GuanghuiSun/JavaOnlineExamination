package com.S.GUI;

import com.S.Service.Impl.TeacherServiceImpl;
import com.S.Service.TeacherService;

import com.S.GUI.TestPaperGUI.CorrectTestPaperGUI;
import com.Public.GUI.CorrectedTestPaperGUI;
import com.Public.pojo.TestPaper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class TeacherGUI {
    JFrame frame;
    JPanel menuPanel;
    JTable table;
    DefaultTableModel defaultTableModel;
    JScrollPane scrollPane;
    JButton importInformation, setTestPaper, correctPapers, checkAllScore;
    JFileChooser chooser;
    File file;
    private TestPaper testPaperA, testPaperB;//AB卷
    private int count = 0;//控制查看成绩按钮
    private String[] colName;
    private int sum;
    TeacherService teacherService = new TeacherServiceImpl();

    public TeacherGUI() {
        frame = new JFrame("教师端");
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        importInformation = new JButton("上传学生信息");
        setTestPaper = new JButton("设置试题结构");
        correctPapers = new JButton("批改试卷");
        checkAllScore = new JButton("查看所有学生成绩");
        menuPanel = new JPanel();
//        menuPanel.setBorder(BorderFactory.createLineBorder(Color.red,2));
//        menuPanel.setSize(new Dimension(0,100));
//        menuPanel.setSize(0,100);
        menuPanel.add(importInformation);
        menuPanel.add(setTestPaper);
        menuPanel.add(correctPapers);
        menuPanel.add(checkAllScore);
        frame.add(menuPanel,BorderLayout.NORTH);
//        frame.add(menu);
        defaultTableModel = new DefaultTableModel();
        table = new JTable(defaultTableModel);
//        table.setEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(table);
        scrollPane.setVisible(false);
        frame.add(scrollPane);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        importInformation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (e.getActionCommand().equals("上传学生信息")) {
                        chooser = new JFileChooser("C:\\Users\\huawei\\Desktop");
                        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                            file = chooser.getSelectedFile();
                            teacherService.importInformation(file);
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });//导入信息
        setTestPaper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == setTestPaper) {
                    class getNumberGUI {
                        JFrame gframe;
                        JTextField item1, item2;
                        JLabel label1, label2;
                        JPanel p1, p2;
                        JButton submit;

                        getNumberGUI() {
                            gframe = new JFrame("设置试题结构");
                            gframe.setLayout(new GridLayout(3, 1));
                            gframe.setSize(500, 300);
                            gframe.setLocationRelativeTo(null);
                            label1 = new JLabel("客观题");
                            label2 = new JLabel("主观题");
                            item1 = new JTextField(5);
                            item2 = new JTextField(5);
                            p1 = new JPanel();
                            p2 = new JPanel();
                            p1.add(label1);
                            p1.add(item1);
                            p2.add(label2);
                            p2.add(item2);
                            submit = new JButton("提交");
                            gframe.add(p1);
                            gframe.add(p2);
                            gframe.add(submit);
                            gframe.setVisible(true);
                            submit.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    int n1 = Integer.parseInt(item1.getText());
                                    int n2 = Integer.parseInt(item2.getText());
                                    sum = n1 + n2;
                                    teacherService.createDetailDatabase(sum);
                                    setTestPaperA(teacherService.setTestPaper(n1, n2));
//                                    System.out.println(TeacherGUI.this.getTestPaperA());
                                    setTestPaperB(teacherService.setTestPaper(n1, n2));
//                                    System.out.println(TeacherGUI.this.getTestPaperB());
                                    //创建表格
                                    colName = createColName(sum);
                                    ArrayList<String[]> allDetailScores = teacherService.getAllDetailScores(sum);
//                                    System.out.println(allDetailScores);
                                    ArrayList<String[]> allScores = teacherService.getAllScores();
//                                    System.out.println(allScores);
                                    createTable(allDetailScores,allScores,getColName());

                                    gframe.dispose();
                                }
                            });
                        }
                    }
                    new getNumberGUI();
                }
            }
        });//发布试题
        correctPapers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == correctPapers) {
                    File file = teacherService.searchFile(new File("./src/com/S/Files/已提交试卷"));
                    if (file != null) {
                        String[] answers = teacherService.readFile(file);
                        //姓名+学号
                        String[] str = answers[0].split("\\|");
                        double id = Double.parseDouble(str[1]);
                        TestPaper testPaper;
                        if (id % 2 == 0) {
                            testPaper = testPaperA;
                        } else {
                            testPaper = testPaperB;
                        }
                        new CorrectTestPaperGUI(str[1], str[0], testPaper, answers, file);
                    }
                }
            }
        });//批改试卷
        checkAllScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==checkAllScore ) {
//                    System.out.println("被点击了");
                    if(count == 0) {
                        Thread t = new Thread(new TableThread(TeacherGUI.this));
                        t.start();
//                        System.out.println("开始执行");
                    }
                    if(count % 2 ==0) {
                        scrollPane.setVisible(true);
                    }else{
                        scrollPane.setVisible(false);
                    }
                    count++;
                }
            }
        });//查看所有学生成绩信息
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //table.getSelectionModel().getValueIsAdjusting() 判断鼠标点击事件 点击时为true，释放为false
                if(e.getSource() == table.getSelectionModel() && table.getSelectionModel().getValueIsAdjusting()) {
                    int index = table.getSelectedRow();
                    if(index!=-1 ) {
                        String name = (String) table.getValueAt(index, 0);
                        String id = (String) table.getValueAt(index, 1);
                        checkTestPaperById(id, name);
                    }
                }
            }
        });//查看学生试卷
    }


    /**
     * 创建表格显示小题分信息
     * @param allDetailScores
     */
    public synchronized void createTable(ArrayList<String[]> allDetailScores,ArrayList<String[]> allScores,String[] colName){
        if(allDetailScores.size()!=0&&allScores.size()!=0) {
            String[][] data = new String[allDetailScores.size()][colName.length];
            for (int i = 0; i < allDetailScores.size(); i++) {
                for (int j = 0; j < colName.length; j++) {//13
                    if (j < allDetailScores.get(0).length) {//10  0~9
                        data[i][j] = allDetailScores.get(i)[j];
                    } else {
                        data[i][j] = allScores.get(i)[j - allDetailScores.get(0).length + 2];
                    }
                }
            }
            defaultTableModel = new DefaultTableModel(data, colName);
            table.setModel(defaultTableModel);
//            System.out.println("表格创建完毕");
//        table.setVisible(true);
        }
    }

    /**
     * 创建表格列名
     * @param sum
     * @return
     */
    public String[] createColName(int sum){
        String[] colName = new String[sum + 5];//0~9 10~12
        colName[0] = "姓名";
        colName[1] = "学号";
        for (int i = 1; i <= sum; i++) {
            colName[i + 1] = "Q" + i;
        }
        colName[sum + 2] = "客观题总分";
        colName[sum + 3] = "主观题总分";
        colName[sum + 4] = "总分";
        return colName;
    }

    /**
     * 查看学生试卷
     * @param id
     * @param name
     */
    public void checkTestPaperById(String id,String name){
        String lastNumber = id.substring(id.length()-1);
        int x = Integer.parseInt(lastNumber);
        TestPaper testPaper;
        if (x % 2 == 0) {
            testPaper = testPaperA;
        } else {
            testPaper = testPaperB;
        }
        String fileName = name + id;
        File file = teacherService.searchFileByName(new File("./src/com/S/Files/已批改试卷"), fileName);
        if(file!=null) {
            String[] answer = teacherService.readFile(file);
            int sum = testPaper.getFirstItem().size() + testPaper.getSecondItem().size();
            double[] detailScoresDouble = teacherService.getDetailScoresById(id, sum);
            String[] detailScores = new String[detailScoresDouble.length];
            for (int i = 0; i < detailScoresDouble.length; i++) {
                detailScores[i] = String.valueOf(detailScoresDouble[i]);
            }
            new CorrectedTestPaperGUI(id, name, testPaper, answer, detailScores);
        }else{
            JOptionPane.showMessageDialog(frame, "该学生试卷不存在！", "提示",JOptionPane.PLAIN_MESSAGE);
        }
    }

    public TestPaper getTestPaperA() {
        return testPaperA;
    }

    public void setTestPaperA(TestPaper testPaperA) {
        this.testPaperA = testPaperA;
    }

    public void setTestPaperB(TestPaper testPaperB) {
        this.testPaperB = testPaperB;
    }

    public TestPaper getTestPaperB() {
        return testPaperB;
    }

    public int getSum(){return sum;}

    public String[] getColName(){return colName;}
}
