package com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview;

import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview.ChildModalClass;

import java.util.List;

public class ParentModalClass {

    String title;

    List<ChildModalClass> childModalClassList;

    public ParentModalClass(String title, List<ChildModalClass> childModalClassList) {
        this.title = title;
        this.childModalClassList = childModalClassList;
    }
}
