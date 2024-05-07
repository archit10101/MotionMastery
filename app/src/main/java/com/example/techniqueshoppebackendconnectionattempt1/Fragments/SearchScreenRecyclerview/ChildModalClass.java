package com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview;

import android.media.Image;
import android.util.Log;

import com.example.techniqueshoppebackendconnectionattempt1.R;

public class ChildModalClass {
    String image;

    String courseName;

    String authorName;

    String authorImage;

    int id;
    public ChildModalClass(String image, String courseName, String authorName, String authorImage, int id) {
        this.image = image;
        this.courseName = courseName;
        this.authorName = authorName;
        this.authorImage = authorImage;
        Log.d("inModalClass",this.authorImage+" : "+this.authorName);
        this.id = id;
    }
}
