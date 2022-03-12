package com.S.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils {
    private static String url;
    private static String username;
    private static String password;
    private static String driver;

    /**
     * 静态代码块读取配置文件，加载JDBC
     */
    static{
        try {
            Properties prop = new Properties();
            prop.load(new FileReader("./src/com/jdbc.properties"));
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            driver = prop.getProperty("driver");
            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() {
        try {
            Connection c = DriverManager.getConnection(url, username, password);
            return c;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 释放资源
     * @param c
     * @param ps
     * @param rs
     */
    public static void close(Connection c, PreparedStatement ps, ResultSet rs){
        if(c!=null){
            try{
                c.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(rs!=null){
            try{
                rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(ps!=null){
            try{
                ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection c, PreparedStatement ps){
        if(c!=null){
            try{
                c.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(ps!=null){
            try{
                ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection c, Statement s, ResultSet rs){
        if(c!=null){
            try{
                c.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(s!=null){
            try{
                s.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(rs!=null){
            try{
                rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
