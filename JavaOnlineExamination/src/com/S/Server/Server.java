package com.S.Server;


import com.S.Service.Impl.TeacherServiceImpl;
import com.S.Service.TeacherService;
import com.S.GUI.TeacherGUI;
import com.Public.pojo.TestPaper;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Server extends Thread {
    static final TeacherService teaService = new TeacherServiceImpl();
    static TeacherGUI teacherGUI;
    static TestPaper testPaperA;
    static TestPaper testPaperB;
    private final Socket socket;
    private InputStream is;
    private OutputStream os;

    public Server(Socket socket,TeacherGUI teacherUI) {
        this.socket = socket;
        teacherGUI = teacherUI;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            if (getTestPaper()) {
                checkLogin();
            }
            byte[] bys = new byte[1024];
            int len;
            while (true) {
                if ((len = is.read(bys)) != -1) {
                    String command = new String(bys, 0, len);
                    System.out.println(command);
                    //查成绩
                    if (command.startsWith("checkScore")) {
                        String id = command.substring(11);
                        checkScore(id);
                    }
                    //提交答案
                    else if (command.startsWith("answered")) {
                        //  0.answered   1.学号+答案
                        String[] str = command.split("-");
                        //  0.学号  1~。。答案  [[120201370607, B, null, null, null, null, null, null, null]]
                        String[] answerArray = str[1].split(", ");
                        //切割工作  0.学号  1~。。答案  [zz|120201370607, B, null, null, null, null, null, null, null]
                        answerArray[0] = answerArray[0].substring(1);
                        answerArray[answerArray.length - 1] = answerArray[answerArray.length - 1].substring(0, answerArray[answerArray.length - 1].length() - 1);
                        //自动评判客观题  得到成绩数组     将客观题成绩录入小题分数据库
                        int[] firstItemScores = judgeItem1(answerArray);
                        //保存已作答学生答案到本地
                        saveTestPaper(answerArray);
                        //将客观题分数写入数据库中
                        // 0.姓名  1.学号
                        String[] information = answerArray[0].split("\\|");
                        teaService.saveFirstItemScore(firstItemScores, information[1]);
//                        System.out.println(answerArray[0] + "-" + Arrays.toString(firstItemScores));
                    }
                    //查询答案
                    else if (command.startsWith("getAnswer-")) {
                        //0.getAnswer 1.姓名学号
                        String[] str = command.split("-");
                        //查询并发送答案
                        searchAnswer(str[1], new File("./src/com/S/Files/已批改试卷"));
                    }
                    //查询小题分
                    else if (command.startsWith("getDetailScores-")) {
                        //0.getAnswer 1.题目数量 2.学号
                        String[] str = command.split("-");
                        //查询并发送
                        checkDetailScores(str[2], Integer.parseInt(str[1]));
                    } else if (command.startsWith("end")) {
                        //0.end 1.学号  2.姓名
                        String[] str = command.split("-");
//                        System.out.println(str[1] +"-" +str[2] + "已退出");
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 登录验证并发送试卷
     *
     * @throws IOException
     */
    public void checkLogin() throws IOException {
        byte[] bys = new byte[1024];
        int len;
        while ((len = is.read(bys)) != -1) {
            String data = new String(bys, 0, len);
//            System.out.println(data);
            if (data.startsWith("login")) {
//              0.login   1.学号  2.姓名
                String[] str = data.split("-");
                if (teaService.login(str[2], str[1])) {
                    System.out.println(str[1] + "-" + str[2] + "登陆成功");
                    os.write("login-true".getBytes(StandardCharsets.UTF_8));
                    //对象序列化流传试卷
                    TestPaper paper = sendPaper(str[1]);
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    oos.writeObject(paper);
                    os.flush();
                    break;
                } else {
                    os.write("login-false".getBytes(StandardCharsets.UTF_8));
                    os.flush();
                }
            }
        }
    }

    /**
     * 获得完整试卷
     */
    public boolean getTestPaper() throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
            if ((teacherGUI.getTestPaperA() != null) && (teacherGUI.getTestPaperB() != null)) {
                //向客户端发送试卷
//            TestPaper[] tps = new TestPaper[2];
                testPaperA = teacherGUI.getTestPaperA();
                testPaperB = teacherGUI.getTestPaperB();
//                System.out.println(testPaperA);
//                System.out.println(testPaperB);
                return true;
            }
        }
    }

    /**
     * 发送每个登陆的用户试卷
     * 如果学号最后一位为偶数则发A卷
     * 最后一位为奇数则发B卷
     */
    public TestPaper sendPaper(String id) {
//        System.out.println(id);
        //截取最后一位
        String lastNumber = id.substring(id.length()-1);
        int x = Integer.parseInt(lastNumber);
//        System.out.println(x);
        if (x % 2 == 0) {
            return testPaperA;
        } else {
            return testPaperB;
        }
    }

    /**
     * 查询成绩
     *
     * @param id
     */
    public void checkScore(String id) throws IOException {
        double[] scores = teaService.checkScore(id);
//        System.out.println(Arrays.toString(Arrays.stream(scores).toArray()));
        byte[] bys = new byte[24];
        StringBuilder scoreStr = new StringBuilder("scores");
        for (double score : scores) {
            scoreStr.append("-").append(score);
        }
//        System.out.println(scoreStr);
        bys = (new String(scoreStr)).getBytes();
//        System.out.println(Arrays.toString(bys));
        os.write(bys);
        os.flush();
    }

    /**
     * 系统评判客观题
     *
     * @param answerArray
     * @return
     */
    public int[] judgeItem1(String[] answerArray) {
        ArrayList<String> answered = new ArrayList<>();//学生答案
        ArrayList<String> answer = new ArrayList<>();//标准答案
        // 0.姓名  1.学号
        String[] str = answerArray[0].split("\\|");
//        System.out.println(Arrays.toString(answerArray));
//        System.out.println(Arrays.toString(str));
        String lastNumber = str[1].substring(str[1].length()-1);
        int x = Integer.parseInt(lastNumber);
        for (int i = 1; i < answerArray.length; i++) {
            answered.add(answerArray[i]);
        }
        Map<String, String> firstItem;
        if (x % 2 == 0) {
            firstItem = testPaperA.getFirstItem();
            Set<String> keySet = firstItem.keySet();
            for (String key : keySet) {
                answer.add(firstItem.get(key));
            }
        } else {
            firstItem = testPaperB.getFirstItem();
            Set<String> keySet = firstItem.keySet();
            for (String key : keySet) {
                answer.add(firstItem.get(key));
            }
        }
//        System.out.println(answer);
        //传进去的是纯答案
        return teaService.AutoJudgingFirstItem(str[1], answered, answer);
    }

    /**
     * 保存已作答试卷
     *
     * @param answerArray
     */
    public void saveTestPaper(String[] answerArray) {
        FileWriter fw = null;
        try {
            // 0.姓名  1.学号
            String[] str = answerArray[0].split("\\|");
            String id = str[1];
            String name = str[0];
//            String name = stuService.selectById(String.valueOf(id));
            fw = new FileWriter(new File("./src/com/S/Files/已提交试卷/uncorrected-" + name + id + ".txt"));
            for (String answer : answerArray) {
                fw.write(answer);
                fw.write('\n');
                fw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fw!=null){
                try{
                    fw.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 查询并发送答案，文件中所有信息
     *
     * @param fileName 模糊的文件名信息
     * @param srcFolders 保存答案文件夹
     * @return
     */
    public void searchAnswer(String fileName, File srcFolders) throws IOException {
        File file = teaService.searchFileByName(srcFolders, fileName);
        if (file != null) {
            String[] answers = teaService.readFile(file);
            StringBuilder answer = new StringBuilder("getAnswer");
            for (String s : answers) {
                answer.append("-").append(s);
            }
            byte[] bys = (new String(answer)).getBytes();
            os.write(bys);
            os.flush();
        }else{
            String str = "getAnswer-null";
            byte[] bys = str.getBytes();
            os.write(bys);
            os.flush();
        }
    }

    /**
     * 查询并发送小题分
     *
     * @param id 学号
     * @param sum 题目总数
     */
    public void checkDetailScores(String id, int sum) throws IOException {
        double[] detailScores = teaService.getDetailScoresById(id, sum);
        StringBuilder scoreStr = new StringBuilder("getDetailScores");
        for (double score : detailScores) {
            scoreStr.append("-").append(score);
        }
//        System.out.println(scoreStr);
        byte[] bys = (new String(scoreStr)).getBytes();
        os.write(bys);
        os.flush();
    }

}
