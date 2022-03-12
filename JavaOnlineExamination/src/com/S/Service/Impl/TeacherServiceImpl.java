package com.S.Service.Impl;

import com.S.Service.TeacherService;
import com.S.Utils.AutoGeneratingTestPaper;
import com.S.Utils.CreateDatabase;
import com.S.Utils.JDBCUtils;
import com.Public.pojo.TestPaper;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class TeacherServiceImpl implements TeacherService {
    private CreateDatabase cd;

    @Override
    public void importInformation(File file) {
        cd = new CreateDatabase(file);
        cd.createStudent();
        System.out.println("上传成功");
    }

    @Override
    public void createDetailDatabase(int sum) {
        //创建小题分数据库
        cd.createDetailScore(sum);
    }

    @Override
    public TestPaper setTestPaper(int item1, int item2) {
        return AutoGeneratingTestPaper.getPaper(item1, item2);
    }

    @Override
    public File searchFile(File srcFolders) {
        if (srcFolders.isDirectory()) {
            File[] files = srcFolders.listFiles();
            //遍历文件数组
            if (files != null) {
                for (File singleFile : files) {
                    String name = singleFile.getName();
                    //判断
                    if (name.startsWith("uncorrected")) {
                        return singleFile;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public File searchFileByName(File srcFolders,String fileName){
        if (srcFolders.isDirectory()) {
            File[] files = srcFolders.listFiles();
            //遍历文件数组
            if (files != null) {
                for (File singleFile : files) {
                    String name = singleFile.getName();
                    //判断
                    if (name.contains(fileName)) {
                        return singleFile;
                    }
                }
                return null;
            }else{
                return null;
            }
        }
        return null;
    }

    @Override
    public String[] readFile(File file) {
        BufferedReader br = null;
        String[] answers = null;
        ArrayList<String> answerArr;
        try {
            br = new BufferedReader(new FileReader(file));
            answerArr = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                answerArr.add(line);
            }
            answers = answerArr.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return answers;
    }

    @Override
    public void inputSecondItemScores(String id, int index, double[] secondItemScores) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql;
            for (int i = 0; i < secondItemScores.length; i++) {
                String colName = "Q" + index;
                sql = "update detailScore set " + colName + " = ? where id = ?";
                ps = connection.prepareStatement(sql);
                ps.setDouble(1, secondItemScores[i]);
                ps.setString(2, id);
                ps.executeUpdate();
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection, ps);
        }
    }

    @Override
    public void replaceFile(File file) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try{
            //复制文件
            br = new BufferedReader(new FileReader(file));
            String fileName = file.getName();
            String afterName = fileName.replaceFirst("uncorrected","corrected");
            File afterFile = new File("./src/com/S/Files/已批改试卷/" + afterName);
            bw = new BufferedWriter(new FileWriter(afterFile));
            String line;
            while((line=br.readLine())!=null){
                bw.write(line);
                bw.newLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(br!=null){
                try{
                    br.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(bw!=null){
                try{
                    bw.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
//        System.out.println(1);
        if(file.exists()){
//            System.out.println(2);
            //删除源文件
            boolean delete = file.delete();
//            System.out.println(3);
        }
    }

    @Override
    public double[] checkScore(String id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select * from student where  id = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            resultSet = ps.executeQuery();
            double[] scores = new double[3];
            while (resultSet.next()) {
                scores[0] = resultSet.getDouble("firstScore");
                scores[1] = resultSet.getDouble("secondScore");
                scores[2] = resultSet.getDouble("sumScore");
            }
            return scores;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(connection,ps,resultSet);
        }
        return null;
    }

    @Override
    public boolean login(String name, String id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try{
            connection = JDBCUtils.getConnection();
            String sql = "select * from student where name = ? and id = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1,name);
            ps.setDouble(2,Double.parseDouble(id));
            resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(connection,ps,resultSet);
        }
        return false;
    }

    @Override
    public void saveFirstItemScore(int[] firstItemScores,String id) {
        int firstItemScore = 0;
        for(int score : firstItemScores){
            firstItemScore += score;
        }
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "update student set firstScore = ? where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setDouble(1,firstItemScore);
            ps.setString(2,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(connection,ps);
        }
    }

    @Override
    public void saveSecondItemScore(double[] secondItemScores,String id) {
        double secondItemScore = 0;
        for(double score : secondItemScores){
            secondItemScore += score;
        }
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "update student set secondScore = ? where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setDouble(1,secondItemScore);
            ps.setString(2,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(connection,ps);
        }
    }

    @Override
    public void sumScore(String id){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select * from student where  id = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            resultSet = ps.executeQuery();
            double[] scores = new double[2];
            while (resultSet.next()) {
                scores[0] = resultSet.getDouble("firstScore");
                scores[1] = resultSet.getDouble("secondScore");
            }
            double sumScore = scores[0] + scores[1];
            String sql2 = "update student set sumScore = ? where id = ?";
            ps = connection.prepareStatement(sql2);
            ps.setDouble(1,sumScore);
            ps.setString(2,id);
            ps.executeUpdate();
            String sql3 = "update detailScore set sumScore = ? where id = ?";
            ps = connection.prepareStatement(sql3);
            ps.setDouble(1,sumScore);
            ps.setString(2,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(connection,ps,resultSet);
        }
    }

    @Override
    public double[] getDetailScoresById(String id,int sum) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select * from detailScore where  id = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            resultSet = ps.executeQuery();
            double[] scores = new double[sum];
            while(resultSet.next()){
                for(int i = 0; i < sum; i++){
                    scores[i] = resultSet.getDouble(i + 3);
                }
            }
            return scores;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(connection,ps,resultSet);
        }
        return null;
    }

    @Override
    public ArrayList<String[]> getAllDetailScores(int sum) {
        Connection connection = null;
        Statement s = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select * from detailScore order by sumScore DESC,name DESC";
            s = connection.createStatement();
            resultSet = s.executeQuery(sql);
            ArrayList<String[]> students = new ArrayList<>();
            String[] single;
            while(resultSet.next()){
                single = new String[sum + 2];
                single[0] = resultSet.getString("name");
                single[1] = resultSet.getString("id");
                for(int i = 2; i < single.length; i++){
                    single[i] = resultSet.getString(i+1);
                }
                students.add(single);
            }
            return students;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(connection,s,resultSet);
        }
        return null;
    }

    @Override
    public ArrayList<String[]> getAllScores() {
        Connection connection = null;
        Statement s = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select * from student order by sumScore DESC,name DESC";
            s = connection.createStatement();
            resultSet = s.executeQuery(sql);
            ArrayList<String[]> students = new ArrayList<>();
            String[] single;
            while(resultSet.next()){
                single = new String[5];
                single[0] = resultSet.getString("name");
                single[1] = resultSet.getString("id");
                single[2] = resultSet.getString("firstScore");
                single[3] = resultSet.getString("secondScore");
                single[4] = resultSet.getString("sumScore");
                students.add(single);
            }
            return students;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(connection,s,resultSet);
        }
        return null;
    }

    @Override
    public int[] AutoJudgingFirstItem(String id,ArrayList<String> array, ArrayList<String> answer)  {
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            int[] score = new int[answer.size()];
            connection = JDBCUtils.getConnection();
            String sql;
            for (int i = 0; i < answer.size(); i++) {
                if (answer.get(i).equalsIgnoreCase(array.get(i))) {
                    score[i] = 3;
                    //记录到小题分中
                    String colName = "Q";
                    colName += (i + 1);
                    sql = "update detailScore set "+ colName +" = 3 where id = ? ";
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, id);
                    ps.executeUpdate();
                }
            }
            return score;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            JDBCUtils.close(connection,ps);
        }
        return null;
    }
}
