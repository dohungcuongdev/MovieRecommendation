package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

import model.Movie;

public class CSVReader {

    public static void main(String[] args) {
        try {

            CsvReader movies = new CsvReader("src/dataset/movies.csv");

            movies.readHeaders();

            while (movies.readRecord()) {
                String movieId = movies.get("movieId");
                String title = movies.get("title");
                String genres = movies.get("genres");
                Movie movie = new Movie(movieId, title, genres);

                // perform program logic here
                System.out.println(movie);
            }

            movies.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Movie> getAllMovieFromCSV() {
        List<Movie> listMovies = new ArrayList<>();
        try {

            CsvReader movies = new CsvReader("src/dataset/movies.csv");

            movies.readHeaders();

            while (movies.readRecord()) {
                String movieId = movies.get("movieId");
                String title = movies.get("title");
                String genres = movies.get("genres");
                String image = movies.get("image");
                Movie movie = new Movie(movieId, title, genres, image);

                // perform program logic here
                listMovies.add(movie);
            }

            movies.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listMovies;
    }

}
