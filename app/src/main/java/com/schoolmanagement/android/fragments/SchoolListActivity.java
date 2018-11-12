package com.schoolmanagement.android.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.schoolmanagement.android.R;
import com.schoolmanagement.android.activities.BaseActivity;
import com.schoolmanagement.android.adapter.SchoolAdapter;
import com.schoolmanagement.android.models.Photo;
import com.schoolmanagement.android.models.School;
import com.schoolmanagement.android.models.User;
import com.schoolmanagement.android.restapis.AppApiInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SchoolListActivity extends BaseActivity {

    private SuperRecyclerView superRecyclerView;
    private SchoolAdapter schoolAdapter;

    private View.OnClickListener onOverflowClickListener = v -> {
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_school_list);
        setToolBarTitle("Schools");
        superRecyclerView = this.findViewById(R.id.superrecyclerview);

        superRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        getData();
    }

    private void getData() {
        /*Observable<Response<User>> observable = AppApiInstance.getApi().getSchools();
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError);*/
        // TODO Remove this mock, once API is get.
        List<School> schools = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            School school = new School();
            school.setName("Excellence School");
            Photo photo = new Photo();
            photo.setReadUrl("http://educatorsally.com/files/2015/09/backtoschool.jpg");
            school.setPhoto(photo);
            school.setLocation("Pune, Maharashtra, India");
            schools.add(school);
        }
        showData(schools);
    }

    private void showData(List schools) {
        schoolAdapter = new SchoolAdapter(schools);
        superRecyclerView.setAdapter(schoolAdapter);
        schoolAdapter.setOnOverflowClickListener(onOverflowClickListener);
    }
}
