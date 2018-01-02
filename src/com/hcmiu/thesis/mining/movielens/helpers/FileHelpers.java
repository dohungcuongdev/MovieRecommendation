package com.hcmiu.thesis.mining.movielens.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.hcmiu.thesis.mining.movielens.utils.Constants;
import com.hcmiu.thesis.mining.movielens.utils.SortMovies;

public class FileHelpers {

	public static void parseDataToMovies(File input, String splitBy, String output){					
		String user = "1";
		
		if(!Constants.MOVIE_DIRECTORY.exists()) {
			Constants.MOVIE_DIRECTORY.mkdir();
		}
		
		try{
			List<ArrayList<String>> userMovie = new ArrayList<ArrayList<String>>();
			FileWriter writer = new FileWriter(output);
			FileInputStream fin = new FileInputStream(input);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
			reader.readLine();
			String line = null;
			while((line = reader.readLine()) != null)
			{
				String[] dataInRowArray = line.split(splitBy);
				if(!user.equals(dataInRowArray[0])){
					ParseUserMovies(writer, splitBy, userMovie);
				}
				ArrayList<String> tmpList = new ArrayList<String>();
				if(Double.parseDouble(dataInRowArray[2]) >= Constants.MIN_RATING){
					for(int i = 0; i <dataInRowArray.length; i++){
						tmpList.add(dataInRowArray[i]);
					}
					userMovie.add(tmpList);
				}
				user = dataInRowArray[0];
			}
			ParseUserMovies(writer, splitBy, userMovie);
			reader.close();
			writer.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	private static void ParseUserMovies(FileWriter writer, String splitBy, List<ArrayList<String>> userMovie){
		SortMovies.SortByTimestamp(userMovie);
		int time = 0;
		try {
			for(ArrayList<String> itemList : userMovie){
				if(time == 0){
					time = Integer.parseInt(itemList.get(3));							
				}

				if((Integer.parseInt(itemList.get(3)) - time) < Constants.PERIOD){
					writer.write(itemList.get(1));
					writer.write(splitBy);
					time = Integer.parseInt(itemList.get(3));
				} else {
					writer.write('\n');
					writer.write(itemList.get(1));
					writer.write(splitBy);
					time = Integer.parseInt(itemList.get(3));
				}
			}
			writer.write('\n');
			userMovie.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
