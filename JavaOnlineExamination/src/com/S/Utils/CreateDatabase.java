package com.S.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {
    private BufferedReader br = null;
    private File file ;

    public CreateDatabase(File file){
        this.file = file;
    }
    /**
     * 根据导入的学生信息文件，创建数据库
     * 创建数据库语句：
     * create table Student(name varchar(20),id varchar(30),firstScore double,secondScore double,sumScore double)
     */
    public void createStudent(){
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            br = new BufferedReader(new FileReader(file));
            connection = JDBCUtils.getConnection();
            String line;
            String sql = "insert into student(name,id,firstScore,secondScore,sumScore) values(?,?,0,0,0) ";
            ps = connection.prepareStatement(sql);
            while((line= br.readLine())!=null){
                String[] str = line.split("-");
                ps.setString(1,str[0]);
                ps.setString(2,str[1]);
                ps.executeUpdate();
            }
        }catch(IOException | SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(connection,ps);
        }
    }

    /**
     * 创建小题分数据库
     * 导入学生信息，小题分默认为0
     * @param sum 客观题与主观题总数
     */
    public void createDetailScore(int sum){
        Connection connection = null;
        Statement s = null;
        PreparedStatement ps = null;
        try{
            br = new BufferedReader(new FileReader(file));
            connection = JDBCUtils.getConnection();
            //创建小题分数据库
//            String sql0 = "drop table detailScore if exists;";
            String sql1 = "create table detailScore(name varchar(20),id varchar(30)";
            for(int i = 1; i <= sum; i++){
                sql1 += ",Q";
                sql1 += String.valueOf(i);
                sql1 += " double";
            }
            sql1 += ",sumScore double";
            sql1 += ");";
//            System.out.println(sql1);
            s = connection.createStatement();
            s.executeUpdate(sql1);
            //将学生信息导入数据库
            String line;
            String sql = "insert into detailScore values(?,? ";
            for(int i = 1; i <= sum + 1; i++){
                sql += ",0";
            }
            sql += ")";
            ps = connection.prepareStatement(sql);
            while((line= br.readLine())!=null){
                String[] str = line.split("-");
                ps.setString(1,str[0]);
                ps.setString(2,str[1]);
                ps.executeUpdate();
            }
        }catch(IOException | SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(connection,ps);
            JDBCUtils.close(null,s,null);
        }
    }
}
