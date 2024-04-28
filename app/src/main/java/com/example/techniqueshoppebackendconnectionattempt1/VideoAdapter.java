package com.example.techniqueshoppebackendconnectionattempt1;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context context;
    private List<VideoItem> videoList;

    public VideoAdapter(Context context, List<VideoItem> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_display_view, parent, false);
        return new VideoViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem videoItem = videoList.get(position);

        // Set data to views
        holder.videoName.setText(videoItem.getName());
        holder.videoDescription.setText(videoItem.getDescription());
        holder.setVidID(videoItem.getVidID()+"");

        // Here, you would load the actual thumbnail image using a library like Picasso or Glide
        // For example using Glide:
        // Glide.with(context).load(videoItem.getThumbnailUrl()).into(holder.videoThumbnail);
        // Placeholder image used for demonstration
        Picasso.get()
                .load(videoItem.getThumbnailUrl())
                .placeholder(R.drawable.loading)
                .into(holder.videoThumbnail);



    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;

        Context context;
        TextView videoName;
        TextView videoDescription;

        String vidID;

        public void setVidID(String vidID) {
            this.vidID = vidID;
        }

        boolean secondCLick = false;
        public VideoViewHolder(@NonNull View itemView, Context cont) {
            super(itemView);

            context = cont;
            videoThumbnail = itemView.findViewById(R.id.image_video_thumbnail);
            videoName = itemView.findViewById(R.id.text_video_name);
            videoDescription = itemView.findViewById(R.id.text_video_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (secondCLick){
                        Intent intent = new Intent(context,ViewVideoActivity.class);
                        intent.putExtra("vidID",vidID);
                        context.startActivity(intent);
                    }else{
                        secondCLick = true;
                        videoDescription.setVisibility(View.VISIBLE);
                        Handler handler = new Handler(Looper.getMainLooper());

                        // Post a delayed task to the handler
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                secondCLick = false;
                                videoDescription.setVisibility(View.GONE);
                            }
                        }, 3000); // 3000 milliseconds = 3 seconds
                    }
                }
            });
        }
    }
}
