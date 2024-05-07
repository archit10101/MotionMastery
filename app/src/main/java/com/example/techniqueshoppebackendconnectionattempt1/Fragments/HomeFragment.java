package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.camera.core.ViewPort;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ChildModalClass;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ParentAdapter;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ParentModalClass;
import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.FullScreenCarouselStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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

        ImageView logo = rootView.findViewById(R.id.logoImageHomeScreen);

        Picasso.get()
                .load(R.drawable.technique_logo)
                .placeholder(R.drawable.technique_logo)
                .transform(new CropCircleTransformation())
                .into(logo);
        TextView welcome = rootView.findViewById(R.id.nameText);
        String name = UserInfoSingleton.getInstance().getDataList().get(0).getFirstName();
        welcome.setText("Welcome, "+name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase()+"!");

        ImageSlider recyclerViewCarousel = rootView.findViewById(R.id.image_slider);
        ArrayList<SlideModel> arrayList = new ArrayList<>();

        //Add multiple images to arraylist.
        arrayList.add(new SlideModel(R.drawable.c1,ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.c2,ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.c3,ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.c4,ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.c5,ScaleTypes.FIT));

        recyclerViewCarousel.setImageList(arrayList,ScaleTypes.FIT);

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