package com.schoolmanagement.android.restapis;

import com.schoolmanagement.android.models.User;

import io.reactivex.Observable;
import retrofit2.Response;

public interface AppApiService {

    Observable<Response<User>> login(User user);
}
