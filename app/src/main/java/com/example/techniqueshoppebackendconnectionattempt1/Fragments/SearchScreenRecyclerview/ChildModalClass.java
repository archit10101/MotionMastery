package com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview;

import android.media.Image;

import com.example.techniqueshoppebackendconnectionattempt1.R;

public class ChildModalClass {
    int image;

    String courseName;

    String authorName;

    int authorImage;

    int id;
    public ChildModalClass(String image, String courseName, String authorName, String authorImage, int id) {
        this.image = R.drawable.technique_logo;
        this.courseName = courseName;
        this.authorName = authorName;
        this.authorImage = R.drawable.technique_logo;
        this.id = id;
    }
}
