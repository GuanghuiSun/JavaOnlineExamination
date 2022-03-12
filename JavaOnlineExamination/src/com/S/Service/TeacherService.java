package com.S.Service;

import com.Public.pojo.TestPaper;

import java.io.File;
import java.util.ArrayList;

public interface TeacherService {


    /**
     * 上传学生信息
     */
    void importInformation(File file);

    /**
     * 创建小题分数据库
     * @param sum
     */
    void createDetailDatabase(int sum);

    /**
     * 发布试题
     */
    TestPaper setTestPaper(int item1, int item2);

    /**
     * 搜寻未批改试卷
     * @param srcFolders
     * @return
     */
    File searchFile(File srcFolders);

    /**
     * 读取学生答案
     * @param file
     * @return
     */
    String[] readFile(File file);

    /**
     * 录入小题分中主观题分数
     * @param id 唯一标识
     * @param index 主观题初始索引
     * @param secondItemScores 分数数组
     */
    void inputSecondItemScores(String id, int index,double[] secondItemScores);

    /**
     * 将批改后的学生答案复制到新的文件夹中
     * @param file
     */
    void replaceFile(File file);

    /**
     * 查成绩
     * @return
     */
    double[] checkScore(String id);

    /**
     * 登录
     * @param name
     * @param id
     * @return
     */
    boolean login(String name, String id);

    /**
     * 录取客观题分数
     * @param firstItemScore
     */
    void saveFirstItemScore(int[] firstItemScore,String id);

    /**
     * 录取主观题分数
     * @param secondItemScore
     */
    void saveSecondItemScore(double[] secondItemScore,String id);

    /**
     * 计算总分  录入两个数据库
     * @param id
     */
    void sumScore(String id);

    /**
     * 获取答案文件
     * @param srcFolders
     * @return
     */
    File searchFileByName(File srcFolders,String fileName);

    /**
     * 查询小题分
     * @param id
     * @return
     */
    double[] getDetailScoresById(String id,int sum);

    /**
     * 获取所有学生的小题分数据库的全部信息
     * @param sum 题目总数
     * @return
     */
    ArrayList<String[]> getAllDetailScores(int sum);

    /**
     * 获取所有学生的成绩
     * @return
     */
    ArrayList<String[]> getAllScores();

    /**
     * 自动批判客观题，并记录到小题分数据库中
     * @param id 学生id
     * @param array 学生答案
     * @param answer 标准答案
     * @return
     */
    int[] AutoJudgingFirstItem(String id,ArrayList<String> array, ArrayList<String> answer);
}
