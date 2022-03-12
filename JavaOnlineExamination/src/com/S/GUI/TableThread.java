package com.S.GUI;

import com.S.Service.Impl.TeacherServiceImpl;
import com.S.Service.TeacherService;

import java.util.ArrayList;

public class TableThread implements Runnable{
    private final TeacherService teacherService = new TeacherServiceImpl();
    private final TeacherGUI teacherGUI;
    public TableThread(TeacherGUI teacherGUI){
        this.teacherGUI = teacherGUI;
    }
    @Override
    public void run(){
        while(true){
            ArrayList<String[]> allDetailScores = teacherService.getAllDetailScores(teacherGUI.getSum());
            ArrayList<String[]> allScores = teacherService.getAllScores();
            teacherGUI.createTable(allDetailScores,allScores,teacherGUI.getColName());
            try{
                Thread.sleep(5000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
