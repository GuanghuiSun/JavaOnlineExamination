package com.C.GUI;


import javax.swing.*;
import java.awt.*;

public class CheckScoreGUI {
    JFrame frame;
    JLabel firstItem,secondItem,sum,firstItemScore,secondItemScore,sumScore;
    JPanel panelFirst, panelSecond, panelSum;
    private final double[] scores;
    public CheckScoreGUI(double[] scores){
        this.scores = scores;
        frame = new JFrame("查看成绩");
        frame.setSize(300,200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(3,1));
        firstItem = new JLabel("客观题：");
        secondItem = new JLabel("主观题：");
        sum = new JLabel("总    分：");
        firstItemScore = new JLabel(String.valueOf(scores[0]));
        secondItemScore = new JLabel(String.valueOf(scores[1]));
        sumScore = new JLabel(String.valueOf(scores[2]));
        panelFirst = new JPanel();
        panelSecond = new JPanel();
        panelSum = new JPanel();
        panelFirst.add(firstItem);
        panelFirst.add(firstItemScore);
        panelSecond.add(secondItem);
        panelSecond.add(secondItemScore);
        panelSum.add(sum);
        panelSum.add(sumScore);
        frame.add(panelFirst);
        frame.add(panelSecond);
        frame.add(panelSum);
        frame.setVisible(true);
    }
}
