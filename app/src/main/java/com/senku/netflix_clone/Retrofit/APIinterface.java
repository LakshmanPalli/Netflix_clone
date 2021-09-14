package com.senku.netflix_clone.Retrofit;

import com.senku.netflix_clone.Model.AllCategory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIinterface {
    //as we created a base url in RetrofitClient.java we initiate few useful transport methods of api
    @GET("{categoryId}/all_movies.json")
    Observable<List<AllCategory>> getAllCategoryMovies(@Path("categoryId") int categoryId); //passing all the from category id to AllCategory class

}
