package com.hcmiu.thesis.mining.movielens.utils;

import java.io.File;

public class Constants {
	public static String DATA = "data/Sample3.csv";
	public static File MOVIE_DIRECTORY = new File("data");
	public static String MOVIES = "data/movies.csv";
	public static String FREQUENT_MOVIES = "data/frequent_movies.csv";
	public static String ODERED_FREQUENT_MOVIES = "data/frequent_movies.csv";
	public static String CANDIDATE = "data/candidate.csv";
	public static String SPLIT_BY = ",";
	public static Double MIN_SUPPORT = 0.5;
	public static Double MIN_RATING = 2.5;
	public static Double TRAINING_PERCENT = 0.8;
	public static Double TEST_PERCENT = 0.2;
	public static Double PREDICT_PERCENT = 0.2;
	public static Integer PERIOD = 500000;
	public static Integer TOP_N = 5;
}
