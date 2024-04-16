package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ChildModalClass;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ParentAdapter;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ParentModalClass;
import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;

    ArrayList<ParentModalClass> parentModalClassArrayList;

    RetrofitDBConnector rdbc;
    ParentAdapter parentAdapter;

    public interface EnrolledCoursesCallback {
        void onSuccess(List<CourseInfo> enrolledCourses);
        void onFailure(String errorMessage);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.parentRV);


        parentModalClassArrayList = new ArrayList<>();


        rdbc = new RetrofitDBConnector();
        rdbc.getEnrolledCoursesByUserID(UserInfoSingleton.getInstance().getDataList().get(0).getUserID(), new EnrolledCoursesCallback() {
            @Override
            public void onSuccess(List<CourseInfo> enrolledCourses) {
                Log.d("success","enrolled should work");
                ArrayList<ChildModalClass> courseList = new ArrayList<>();
                for (int i = 0;i<enrolledCourses.size();i++){
                    courseList.add(new ChildModalClass(enrolledCourses.get(i).getCourseImgPath(),enrolledCourses.get(i).getCourseName(),enrolledCourses.get(i).getCourseAuthorName(),enrolledCourses.get(i).getCourseAuthorImgPath(),enrolledCourses.get(i).getCourseId()));
                    Log.d("Enrolled",enrolledCourses.get(i).toString());
                }
                parentModalClassArrayList.add(new ParentModalClass("Enrolled Courses",courseList));
                parentAdapter = new ParentAdapter(parentModalClassArrayList, getContext());

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                recyclerView.setAdapter(parentAdapter);
                parentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure, such as showing an error message
                Log.d("Enrolled Courses Error", errorMessage);
            }
        });
        parentAdapter = new ParentAdapter(parentModalClassArrayList, getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();

        return rootView;
    }
}