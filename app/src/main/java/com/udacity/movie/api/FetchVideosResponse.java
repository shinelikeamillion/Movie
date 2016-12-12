package com.udacity.movie.api;

/** http://api.themoviedb.org/3/movie/284052/videos?api_key=****
 {
     "id": 284052,
     "results": [
         {
             "id": "57992d84c3a3687e5c003a3b",
             "iso_639_1": "en",
             "iso_3166_1": "US",
             "key": "HSzx-zryEgM",
             "name": "Doctor Strange Official Trailer 2",
             "site": "YouTube",
             "size": 1080,
             "type": "Trailer"
         },
         ** same **
     ]
 }
 */

public class FetchVideosResponse {
    public int id;
    public Results[] results;

    public class Results {
        public static final String BASE_YOUTUBE_URL = "http://www.youtube.com/watch?v=";
        private String id;
        private String iso_639_1;
        private String iso_3166_1;
        public String key;
        private String name;
        private String site;
        private int size;
        private String type;

        public boolean isYouTube(){
            return this.site.equals("YouTube");
        }
    }
}
