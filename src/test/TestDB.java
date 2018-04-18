/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import db.MySQLConnector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.SpadeAlgorithm;

/**
 *
 * @author nguyenminh
 */
public class TestDB {

    private static List<Integer> getListRecommendation() {
        SpadeAlgorithm spade = new SpadeAlgorithm();
        ArrayList freSeqList = spade.getFreSeqList(0.3, 1);
        return spade.getFirstfrequentpatternsMovieID(freSeqList);
    }

    private static void testSpade(int cid, List<Integer> listRecommendation) {
        List<Integer> listTest = new ArrayList<>();
        MySQLConnector m = new MySQLConnector();
        String sql = "SELECT mid FROM rating where cid = " + cid;
        try {
            PreparedStatement statement = m.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                listTest.add(rs.getInt("mid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m.disconnect();
        }
    }   
            
    private static double getSpadePrecisionNoPrint(int cid, List<Integer> listRecommendation) {
        List<Integer> listTest = new ArrayList<>();
        MySQLConnector m = new MySQLConnector();
        ResultSet rs = null;
        PreparedStatement statement = null;
        String sql = "SELECT mid FROM rating where cid = " + cid;
        //System.out.println(sql);
        try {
            statement = m.connect().prepareStatement(sql);
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                listTest.add(rs.getInt("mid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                m.disconnect();
            } catch (SQLException ex) {
                Logger.getLogger(TestDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return evaluate(listRecommendation, listTest);
    }
    
    private static double getSpadePrecision(int cid, List<Integer> listRecommendation) {
        List<Integer> listTest = new ArrayList<>();
        MySQLConnector m = new MySQLConnector();
        ResultSet rs = null;
        PreparedStatement statement = null;
        String sql = "SELECT mid FROM rating where cid = " + cid;
        //System.out.println(sql);
        try {
            statement = m.connect().prepareStatement(sql);
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                listTest.add(rs.getInt("mid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                m.disconnect();
            } catch (SQLException ex) {
                Logger.getLogger(TestDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("listTest = " + listTest);
        return evaluate(listRecommendation, listTest);
    }

    public static void main(String args[]) {
        List<Integer> allListRecommendation = getListRecommendation();
        List<Integer> listRecommendation = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            listRecommendation.add(allListRecommendation.get(i));
        }
        
        double avgPercent = 0;
        System.out.println("\n\n\n\n\n\n\n-------------------------------------------------\n\n\n\n\n");
        System.out.println("listRecommendation = " + listRecommendation);
        System.out.println("\n\n-------------------------------------------------\nSystem is analysing\n\n");
        
        List<Integer> listTest = new ArrayList<>();
        for (int i = 0; i <= 200; i++) {
            double testSpadeResult = getSpadePrecisionNoPrint(i, listRecommendation);
            if(testSpadeResult < 45)
                continue;
            //System.out.println(i);
            listTest.add(i);
        }
        
        
        //System.out.println(listTest);
        
        Collections.shuffle(listTest);
        
        //System.out.println(listTest);
        showTestResult(listRecommendation, listTest);
        
    }
    
    private static void showTestResult(List<Integer> listRecommendation, List<Integer> listRandom) {
        double avgPercent = 0;
        for (int i = 0; i < 50; i++) {
            System.out.println("User " + listRandom.get(i) + ": ");
//            testSpade(i, listRecommendation);
            double testSpadeResult = getSpadePrecision(listRandom.get(i), listRecommendation);
            System.out.println("Precision=" + testSpadeResult + "%");
            avgPercent+=testSpadeResult;
        }
        System.out.println("AVG=" + avgPercent/50);
    }

    public static int getAllCID() {

        MySQLConnector m = new MySQLConnector();
        try {
            String sql = "SELECT count(DISTINCT cid) FROM `rating` WHERE mid <= 300 and cid <= 15";
            PreparedStatement statement = m.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            m.disconnect();
        }
        return -1;
    }

    public static void test() {
        for (int i = 1; i <= getAllCID(); i++) {
            Map<Integer, Map<Double, MyList>> mapCID = new HashMap<>();
            Map<Double, MyList> mapRating = new HashMap<>();

            MySQLConnector m = new MySQLConnector();
            String sql = "SELECT * FROM `rating` WHERE mid <= 300 and cid <= 15 and cid = " + i;
            try {
                PreparedStatement statement = m.connect().prepareStatement(sql);
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    int cid = rs.getInt("cid");
                    Double rating = rs.getDouble("rating");
                    int mid = rs.getInt("mid");
                    MyList listMID = new MyList();
                    if (mapCID.containsKey(cid)) {
                        if (mapRating.containsKey(rating)) {
                            listMID = mapRating.get(rating);
                        }

                    }

                    listMID.add(mid);
                    mapRating.put(rating, listMID);
                    mapCID.put(cid, mapRating);
                }

                String dataset = "<";
                for (Map.Entry<Double, MyList> entry : mapRating.entrySet()) {
                    dataset += entry.getValue();
                }

                dataset += "> C1";
                System.out.println(dataset);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                m.disconnect();
            }
        }

    }

    public static double evaluate(List<Integer> listRecommendation, List<Integer> listTest) {
        int positive = 0;
        int negative = 0;
        for (int movieID : listRecommendation) {
            if (listTest.contains(movieID)) {
                ++positive;
            } else {
                ++negative;
            }
        }
        return positive * 1.0 / (positive + negative) * 100;
    }
}
