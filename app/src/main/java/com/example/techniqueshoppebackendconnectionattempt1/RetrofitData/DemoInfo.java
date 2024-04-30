package com.example.techniqueshoppebackendconnectionattempt1.RetrofitData;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class DemoInfo implements Serializable {
    @SerializedName("demoID")
    private String demoId;

    @SerializedName("videoID")
    private String videoId;

    @SerializedName("videoPath")
    private String demoPath;

    @SerializedName("image1")
    private String image1;

    @SerializedName("image2")
    private String image2;

    @SerializedName("image3")
    private String image3;

    @SerializedName("image4")
    private String image4;

    @SerializedName("image5")
    private String image5;

    @SerializedName("image6")
    private String image6;

    @SerializedName("image7")
    private String image7;

    @SerializedName("image8")
    private String image8;

    @SerializedName("image9")
    private String image9;

    @SerializedName("image10")
    private String image10;

    @SerializedName("image11")
    private String image11;

    @SerializedName("image12")
    private String image12;

    private String[] images;

    public DemoInfo(String demoId, String videoId, String demoPath, String[] imagestring) {
        this.demoId = demoId;
        this.videoId = videoId;
        this.demoPath = demoPath;
        this.image1 = imagestring[0];
        this.image2 = imagestring[1];
        this.image3 = imagestring[2];
        this.image4 = imagestring[3];
        this.image5 = imagestring[4];
        this.image6 = imagestring[5];
        this.image7 = imagestring[6];
        this.image8 = imagestring[7];
        this.image9 = imagestring[8];
        this.image10 = imagestring[9];
        this.image11 = imagestring[10];
        this.image12 = imagestring[11];
    }

    public String[] getImages() {
        images = new String[]{image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11, image12};
        return images;
    }

    public String getDemoId() {
        return demoId;
    }

    public void setDemoId(String demoId) {
        this.demoId = demoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public String getImage7() {
        return image7;
    }

    public void setImage7(String image7) {
        this.image7 = image7;
    }

    public String getDemoPath() {
        return demoPath;
    }

    public void setDemoPath(String demoPath) {
        this.demoPath = demoPath;
    }

    public String getImage8() {
        return image8;
    }

    public void setImage8(String image8) {
        this.image8 = image8;
    }

    public String getImage9() {
        return image9;
    }

    public void setImage9(String image9) {
        this.image9 = image9;
    }

    public String getImage10() {
        return image10;
    }

    public void setImage10(String image10) {
        this.image10 = image10;
    }

    public String getImage11() {
        return image11;
    }

    public void setImage11(String image11) {
        this.image11 = image11;
    }

    public String getImage12() {
        return image12;
    }

    public void setImage12(String image12) {
        this.image12 = image12;
    }
}
