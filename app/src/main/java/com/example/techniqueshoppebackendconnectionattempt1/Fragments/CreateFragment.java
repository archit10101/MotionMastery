package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CreateFragment extends Fragment {

    private Dialog createCourseDialog;
    View view;

    public interface CourseCallback {
        void onSuccess(List<CourseInfo> courses);
        void onFailure(String error);
    }

    TextView nocourse;

    GridView gridViewCourses;

    CourseGridViewAdapter gridViewAdapter;

    ArrayList<CourseInfo> courseList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_create, container, false);
        courseList = new ArrayList<>();
        gridViewCourses = view.findViewById(R.id.grid_view_courses);
        gridViewAdapter = new CourseGridViewAdapter(requireContext(), courseList);
        gridViewCourses.setAdapter(gridViewAdapter);

        RetrofitDBConnector connector = new RetrofitDBConnector();
        String authorName = UserInfoSingleton.getInstance().getDataList().get(0).getUserName(); // Replace with the actual author name
        connector.getCoursesByAuthor(authorName, new CreateDialog.CourseCallback() {
            @Override
            public void onSuccess(List<CourseInfo> courses) {
                nocourse.setVisibility(View.GONE);
                gridViewCourses.setVisibility(View.VISIBLE);

                for (CourseInfo course : courses) {
                    courseList.add(course);
                    Log.d("allCourses",course.getCourseName());
                }
                gridViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(String error) {
                // Handle failure
                gridViewCourses.setVisibility(View.GONE);

                Log.e("API Error", "Failed to get courses: " + error);
                nocourse.setVisibility(View.VISIBLE);
            }
        });


        gridViewAdapter.notifyDataSetChanged();

        // Handle item click listener if needed
        gridViewCourses.setOnItemClickListener((parent, view1, position, id) -> {
            CourseInfo selectedCourse = courseList.get(position);
            Toast.makeText(requireContext(), "Selected Course: " + selectedCourse.getCourseName(), Toast.LENGTH_SHORT).show();
        });

        FloatingActionButton fabCreateCourse = view.findViewById(R.id.floating_action_button);

        nocourse = view.findViewById(R.id.text_no_courses);

        fabCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the dialog
                showCreateCourseDialog();
            }
        });

        return view;
    }

    private void showCreateCourseDialog() {
        CreateDialog dialog = new CreateDialog(getContext());

        dialog.setContentView(R.layout.dialog_create_course);


        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                RetrofitDBConnector connector = new RetrofitDBConnector();
                String authorName = UserInfoSingleton.getInstance().getDataList().get(0).getUserName(); // Replace with the actual author name
                connector.getCoursesByAuthor(authorName, new CreateDialog.CourseCallback() {
                    @Override
                    public void onSuccess(List<CourseInfo> courses) {
                        nocourse.setVisibility(View.GONE);
                        gridViewCourses.setVisibility(View.VISIBLE);
                        courseList.clear();
                        for (CourseInfo course : courses) {
                            courseList.add(course);

                        }
                        gridViewAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(String error) {
                        // Handle failure
                        gridViewCourses.setVisibility(View.GONE);

                        Log.e("API Error", "Failed to get courses: " + error);
                        nocourse.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
    }

}