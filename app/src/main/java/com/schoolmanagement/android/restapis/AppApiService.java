package com.schoolmanagement.android.restapis;

import com.schoolmanagement.android.models.User;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AppApiService {

    @POST("/auth/authenticate")
    Observable<Response<User>> login(@Body User user);

    @POST("/users")
    Observable<Response<User>> userRegistration(@Body User user);
}
