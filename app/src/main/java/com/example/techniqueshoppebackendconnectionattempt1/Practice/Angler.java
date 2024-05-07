package com.example.techniqueshoppebackendconnectionattempt1.Practice;

import android.util.Log;

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Only for pose detection for now

public class Angler {
    private List<NormalizedLandmark> results;

    private HashMap<String, int[]> angleDictionary;

    private ArrayList<String> allKeys = new ArrayList<>();

    public Angler(List<NormalizedLandmark> resu){
        results = resu;
        angleDictionary = new HashMap<>();

        initDictinary();

    }

    public double getAngle(int pt1, int pt2, int pt3){
        NormalizedLandmark pt1xy = results.get(pt1);
        NormalizedLandmark pt2xy = results.get(pt2);
        NormalizedLandmark pt3xy = results.get(pt3);
        double a = Math.sqrt(Math.pow(pt1xy.x()-pt2xy.x(),2)+Math.pow(pt1xy.y()-pt2xy.y(),2));
        double b = Math.sqrt(Math.pow(pt3xy.x()-pt2xy.x(),2)+Math.pow(pt3xy.y()-pt2xy.y(),2));
        double c = Math.sqrt(Math.pow(pt1xy.x()-pt3xy.x(),2)+Math.pow(pt1xy.y()-pt3xy.y(),2));
        double cosang = (Math.pow(c,2)-Math.pow(b,2)-Math.pow(a,2))/(-2*a*b);
        double ang = Math.acos(cosang);
        return ang;
    }

    public void initDictinary(){
        angleDictionary.put("Left Knee", new int[]{23, 25, 27});
        angleDictionary.put("Right Knee", new int[]{28, 26, 24});
        angleDictionary.put("Right Hip-Leg", new int[]{26, 24, 23});
        angleDictionary.put("Left Hip-Leg", new int[]{24,23,25});
        angleDictionary.put("Right Hip", new int[]{12,24,26});
        angleDictionary.put("Left Hip", new int[]{25, 23, 11});
        angleDictionary.put("Right Shoulder", new int[]{14,12,24});
        angleDictionary.put("Left Shoulder", new int[]{13,11,23});
        angleDictionary.put("Right Elbow", new int[]{12,14,16});
        angleDictionary.put("Left Elbow", new int[]{15,13,11});

        allKeys.add("Left Knee");
        allKeys.add("Right Knee");
        allKeys.add("Right Hip-Leg");
        allKeys.add("Left Hip-Leg");
        allKeys.add("Right Hip");
        allKeys.add("Left Hip");
        allKeys.add("Right Shoulder");
        allKeys.add("Left Shoulder");
        allKeys.add("Right Elbow");
        allKeys.add("Left Elbow");

    }

    public ArrayList<String> getAllKeys() {
        return allKeys;
    }

    public HashMap<String, int[]> getAngleDictionary() {
        return angleDictionary;
    }

    public double getAngle(String node){
        int[] pts= angleDictionary.get(node);
        if (pts != null) {
            Log.d(node,(getAngle(pts[0],pts[1],pts[2])*180/Math.PI)+"");
            return getAngle(pts[0],pts[1],pts[2])*180/Math.PI;

        }else{
            Log.d("error","the points are null brother.");
            return 0.0;
        }
    }
}
