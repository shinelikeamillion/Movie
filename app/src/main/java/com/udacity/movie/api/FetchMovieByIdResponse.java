package com.udacity.movie.api;

/** http://api.themoviedb.org/3/movie/{id}?api_key=***
 {
     "adult": false,
     "backdrop_path": "/tFI8VLMgSTTU38i8TIsklfqS9Nl.jpg",
     "belongs_to_collection": null,
     "budget": 165000000,
     "genres": [
         {
             "id": 28,
             "name": "Action"
         },
         {
             "id": 12,
             "name": "Adventure"
         },
         {
             "id": 14,
             "name": "Fantasy"
         },
         {
             "id": 878,
             "name": "Science Fiction"
         }
     ],
     "homepage": "http://marvel.com/doctorstrangepremiere",
     "id": 284052,
     "imdb_id": "tt1211837",
     "original_language": "en",
     "original_title": "Doctor Strange",
     "overview": "After his career is destroyed, a brilliant but arrogant surgeon gets a new lease on life when a sorcerer takes him under his wing and trains him to defend the world against evil.",
     "popularity": 20.40923,
     "poster_path": "/xfWac8MTYDxujaxgPVcRD9yZaul.jpg",
     "production_companies": [
         {
             "name": "Marvel Studios",
             "id": 420
         }
     ],
     "production_countries": [
         {
             "iso_3166_1": "US",
             "name": "United States of America"
         }
     ],
     "release_date": "2016-10-25",
     "revenue": 617025426,
     "runtime": 115,
     "spoken_languages": [
         {
             "iso_639_1": "en",
             "name": "English"
         }
     ],
     "status": "Released",
     "tagline": "Open your mind. Change your reality.",
     "title": "Doctor Strange",
     "video": false,
     "vote_average": 6.6,
     "vote_count": 1280
 }
 */

public class FetchMovieByIdResponse {
    public int id;
    public boolean adult;
    public String backdrop_path;
    public long budget;
    public Genres[] genres;
    public String homepage;
    public String imdb_id;
    public String original_language;
    public String original_title;
    public String overview;
    public float popularity;
    public String poster_path;
    public ProductionCompanies[] production_companies;
    public ProductionCountries[] production_countries;
    public String release_date;
    public long revenue;
    public int runtime;
    public String status;
    public String tagline;
    public String title;
    public boolean video;
    public float vote_average;
    public int vote_count;

    private class Genres {
        private int id;
        private String name;
    }

    private class ProductionCompanies {
        private int id;
        private String name;
    }

    private class ProductionCountries {
        private String iso_3166_1;
        private String name;
    }

    private class Spoken_languages {
        private String iso_639_1;
        private String name;
    }
}
