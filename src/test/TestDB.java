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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Movie;
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
            

    private static double getSpadePrecision(int cid, List<Integer> listRecommendation) {
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
        System.out.println("listTest = " + listTest);
        return evaluate(listRecommendation, listTest);
    }

    public static void main(String args[]) {
        double avgPercent = 0;
        List<Integer> listRecommendation = getListRecommendation();
        System.out.println("\n\n\n\n\n\n\n-------------------------------------------------\n\n\n\n\n");
        System.out.println("listRecommendation = " + listRecommendation);
        for (int i = 100; i <= 200; i++) {
            System.out.println("User " + i + ": ");
//            testSpade(i, listRecommendation);
            double testSpadeResult = getSpadePrecision(i, listRecommendation);
            System.out.println("Precision=" + testSpadeResult + "%");
            avgPercent+=testSpadeResult;
        }
        System.out.println("AVG=" + avgPercent/100);
    }

    public static int getAllCID() {

        MySQLConnector m = new MySQLConnector();
        try {
            String sql = "SELECT count(DISTINCT cid) FROM rating";
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
            String sql = "SELECT cid, mid, rating FROM rating where cid = " + i;
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
