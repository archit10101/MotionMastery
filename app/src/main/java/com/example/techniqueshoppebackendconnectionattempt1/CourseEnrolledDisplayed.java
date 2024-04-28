package com.example.techniqueshoppebackendconnectionattempt1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchFragment;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.VideoTutorial;

import java.util.ArrayList;
import java.util.List;

public class CourseEnrolledDisplayed extends AppCompatActivity {

    RecyclerView recyclerView;
    int courseId;


    TextView courseName, authorName, courseDescription;
    ImageView courseImg,authorImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_enrolled_displayed);

        courseImg = findViewById(R.id.image_course);
        authorImg = findViewById(R.id.image_author);
        courseName = findViewById(R.id.text_course_title);
        authorName = findViewById(R.id.text_author_name);
        courseDescription = findViewById(R.id.text_course_description);
        ImageView imageClose = findViewById(R.id.image_close);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("COURSE_ID")) {
            courseId = intent.getIntExtra(  "COURSE_ID",0);
        }else{
            Log.d("course","Where is the course Id");
        }

        List<VideoItem> videoItems = new ArrayList<>();

        RetrofitDBConnector rdbc = new RetrofitDBConnector();
        rdbc.getCourseByCourseID(courseId, new SearchFragment.CourseCallback() {

            @Override
            public void onSuccess(List<CourseInfo> courseData) {
                courseName.setText(courseData.get(0).getCourseName());
                courseImg.setImageResource(R.drawable.technique_logo);
                authorImg.setImageResource(R.drawable.technique_logo);
                authorName.setText(courseData.get(0).getCourseAuthorName());
                courseDescription.setText(courseData.get(0).getCourseDescription());
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        recyclerView = findViewById(R.id.recycler_view_related_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rdbc.getVideoTutorialsByCourseId(courseId, new RetrofitDBConnector.VideoCallback() {
            @Override
            public void onSuccess(List<VideoTutorial> fileContent) {
                for (int i = 0;i<fileContent.size();i++) {
                    videoItems.add(new VideoItem(fileContent.get(i).getVideoName(),fileContent.get(i).getVideoDescription(),fileContent.get(i).getVideoImgPath(),fileContent.get(i).getVideoID()));
                    recyclerView.getAdapter().notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
        VideoAdapter adapter = new VideoAdapter(this, videoItems);
        recyclerView.setAdapter(adapter);

    }
}