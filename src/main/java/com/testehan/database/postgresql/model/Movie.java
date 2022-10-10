package com.testehan.database.postgresql.model;

public class Movie {
    private int movieId;
    private String title;
    private int year;
    private float rating;
    private String director;
    private String description;

    public Movie(String title){
        this.title = title;
    }

    public Movie(String title, int year, float rating, String director, String description) {
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.director = director;
        this.description = description;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", rating=" + rating +
                ", director='" + director + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
