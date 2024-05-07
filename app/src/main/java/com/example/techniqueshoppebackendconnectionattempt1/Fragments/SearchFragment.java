package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techniqueshoppebackendconnectionattempt1.AppActivity;
import com.example.techniqueshoppebackendconnectionattempt1.CourseCreatedDisplayed;
import com.example.techniqueshoppebackendconnectionattempt1.CourseEnrolledDisplayed;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ChildModalClass;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ParentAdapter;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ParentModalClass;
import com.example.techniqueshoppebackendconnectionattempt1.LoginActivity;
import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserEnrolledCourse;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;

    MaterialSearchBar searchBar;

    ArrayList<ParentModalClass> parentModalClassArrayList;

    RetrofitDBConnector rdbc;

    View searchBarLayout;
    ParentAdapter parentAdapter;

    public interface CourseCallback {
        void onSuccess(List<CourseInfo> courseData);
        void onFailure(String errorMessage);
    }

    ArrayList<String> tags;
    List<CourseInfo> coursesResult;

    CourseGridViewAdapter gridViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchBarLayout = rootView.findViewById(R.id.results);

        coursesResult = new ArrayList<>();
        GridView resultsGrid = searchBarLayout.findViewById(R.id.grid_view_courses);
        resultsGrid.setVisibility(View.GONE);
        gridViewAdapter = new CourseGridViewAdapter(requireContext(), coursesResult,false);
        resultsGrid.setOnItemClickListener((parent, view1, position, id) -> {
            CourseInfo selectedCourse = coursesResult.get(position);
            rdbc.getEnrolledCourse(UserInfoSingleton.getInstance().getDataList().get(0).getUserID()+"", selectedCourse.getCourseId()+"", new RetrofitDBConnector.EnrolledCourseCallback() {
                @Override
                public void onSuccess(UserEnrolledCourse enrolledCourse) {
                    // Handle successful retrieval of enrolled course
                    Log.d("Retrofit", "Enrolled course found");
                    Intent intent = new Intent(getContext(), CourseEnrolledDisplayed.class);
                    intent.putExtra("COURSE_ID", selectedCourse.getCourseId());
                    getContext().startActivity(intent);

                }

                @Override
                public void onFailure(String error) {
                    // Handle failure
                    CourseDetailsDialog dialog = new CourseDetailsDialog(getContext(), selectedCourse);
                    dialog.show();
                }
            });
        });
        resultsGrid.setAdapter(gridViewAdapter);
        rdbc = new RetrofitDBConnector();

        searchBar = rootView.findViewById(R.id.searchBar);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()){
                    coursesResult.clear();
                    gridViewAdapter.notifyDataSetChanged();
                    resultsGrid.setVisibility(View.GONE);
                }else{
                    rdbc.getCoursesBySearch(s.toString(), new CourseCreator.CourseCallback() {
                        @Override
                        public void onSuccess(List<CourseInfo> courses) {
                            int num = 0;
                            coursesResult.clear();
                            coursesResult.addAll(courses);
                            if (coursesResult.size() == 0){
                                resultsGrid.setVisibility(View.GONE);
                            }else{
                                resultsGrid.setVisibility(View.VISIBLE);
                            }
                            gridViewAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {



            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                if (!text.toString().equals("")){
                    rdbc.getCoursesBySearch(text.toString(), new CourseCreator.CourseCallback() {
                        @Override
                        public void onSuccess(List<CourseInfo> courses) {
                            int num = 0;
                            coursesResult.clear();
                            coursesResult.addAll(courses);
                            if (coursesResult.size() == 0){
                                resultsGrid.setVisibility(View.GONE);
                            }else{
                                resultsGrid.setVisibility(View.VISIBLE);
                            }
                            gridViewAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    });
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        tags = new ArrayList<>();
        tags.add("Martial Arts");
        tags.add("Dance");
        tags.add("Sports");
        tags.add("Fast-Paced");

        recyclerView = rootView.findViewById(R.id.parentRV);


        parentModalClassArrayList = new ArrayList<>();



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