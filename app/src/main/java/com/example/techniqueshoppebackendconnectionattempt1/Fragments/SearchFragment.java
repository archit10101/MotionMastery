package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techniqueshoppebackendconnectionattempt1.AppActivity;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ChildModalClass;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ParentAdapter;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ParentModalClass;
import com.example.techniqueshoppebackendconnectionattempt1.LoginActivity;
import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;

    ArrayList<ParentModalClass> parentModalClassArrayList;

    RetrofitDBConnector rdbc;
    ParentAdapter parentAdapter;

    public interface CourseCallback {
        void onSuccess(List<CourseInfo> courseData);
        void onFailure(String errorMessage);
    }

    ArrayList<String> tags;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        tags = new ArrayList<>();
        tags.add("Hip Hop");
        tags.add("Karate");
        tags.add("Slow");
        tags.add("Fast");

        recyclerView = rootView.findViewById(R.id.parentRV);


        parentModalClassArrayList = new ArrayList<>();


        rdbc = new RetrofitDBConnector();

        for (String tag:tags){
            rdbc.getCourseByTag(tag, new CourseCallback() {

                @Override
                public void onSuccess(List<CourseInfo> courseData) {
                    ArrayList<ChildModalClass> courseList = new ArrayList<>();
                    for (int i = 0;i<courseData.size();i++){
                        courseList.add(new ChildModalClass(courseData.get(i).getCourseImgPath(),courseData.get(i).getCourseName(),courseData.get(i).getCourseAuthorName(),courseData.get(i).getCourseAuthorImgPath(),courseData.get(i).getCourseId()));
                    }
                    parentModalClassArrayList.add(new ParentModalClass(tag+" Courses",courseList));
                    parentAdapter = new ParentAdapter(parentModalClassArrayList, getContext());

                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    recyclerView.setAdapter(parentAdapter);
                    parentAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });

        }



        Log.d("display",parentModalClassArrayList.size()+"");

        for (int i = 0;i<parentModalClassArrayList.size();i++){
            Log.d("display",parentModalClassArrayList.get(i).toString());
        }
        parentAdapter = new ParentAdapter(parentModalClassArrayList, getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();
        return rootView;

    }
}