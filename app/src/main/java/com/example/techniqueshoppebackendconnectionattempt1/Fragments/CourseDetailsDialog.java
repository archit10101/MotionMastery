package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CourseDetailsDialog extends Dialog {

    private RetrofitDBConnector retrofitDBConnector;
    public CourseDetailsDialog(@NonNull Context context, @NonNull CourseInfo course) {
        super(context);
        setContentView(R.layout.dialog_course_details);

        retrofitDBConnector = new RetrofitDBConnector();
        ImageView xButton = findViewById(R.id.image_close);
        ImageView image = findViewById(R.id.CourseImg);
        TextView textCourseName = findViewById(R.id.text_course_name);
        TextView textCourseDescription = findViewById(R.id.text_course_description);
        Button enrollButton = findViewById(R.id.button_enroll);

        ImageView authorImg = findViewById(R.id.authorImg);

        TextView authorText = findViewById(R.id.authorName);

        authorText.setText(course.getCourseAuthorName());

        Log.d("course img",course.toString());
        setImageFromS3(image,course.getCourseImgPath());

        setImageFromS3(authorImg,course.getCourseAuthorImgPath());

        textCourseName.setText(course.getCourseName());
        textCourseDescription.setText(course.getCourseDescription());
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitDBConnector = new RetrofitDBConnector();
                retrofitDBConnector.enrollCourse(""+UserInfoSingleton.getInstance().getDataList().get(0).getUserID(), ""+course.getCourseId(), new RetrofitDBConnector.EnrollCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Retrofit", "User enrolled in course successfully");
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e("Retrofit", "Failed to enroll user in course: " + error);
                    }
                });
                dismiss();
            }
        });
    }

    public void setImageFromS3(ImageView img, String path){

        retrofitDBConnector.downloadFile(path, new RetrofitDBConnector.DownloadCallback() {
            @Override
            public void onSuccess(String fileContent) {
                Log.d("url","m"+fileContent);
                Picasso.get()
                        .load(fileContent)
                        .placeholder(R.drawable.loading)
                        .transform(new CropCircleTransformation())
                        .into(img);

            }

            @Override
            public void onFailure(String error) {
                // Handle download failure
                Log.e("Download", "Download failed: " + error);
            }
        });
    }

}
