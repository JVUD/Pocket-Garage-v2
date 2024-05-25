package com.example.pocketgaragenotlogin;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private final Context context;
    private final ArrayList<String> imagePathArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<String> imagePathArrayList) {
        this.context = context;
        this.imagePathArrayList = imagePathArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String imageUrl = imagePathArrayList.get(position);


        String fileName = Uri.parse(imageUrl).getLastPathSegment();
        Log.e("Gallery", "imgname: " + fileName);

        if (fileName != null) {
            String[] carDetails = extractCarDetails(fileName);
            if (carDetails != null) {
                holder.nameTextView.setText(carDetails[0]);
                holder.ageTextView.setText(carDetails[1]);
                holder.ratingTextView.setText(carDetails[2]);
            } else {
                holder.nameTextView.setText("Unknown");
                holder.ageTextView.setText("Unknown");
                holder.ratingTextView.setText("Unknown");
            }
        } else {
            Log.e("RecyclerViewAdapter", "Invalid file name: " + imageUrl);
        }

        // Load the image using Picasso
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.garage) // Placeholder image while loading
                .error(R.drawable.plus) // Error image if loading fails
                .into(holder.imageIV);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageDetailActivity.class);
            intent.putExtra("imgPath", imageUrl);
            context.startActivity(intent);
        });
    }
    private static String[] extractCarDetails(String input) {
        // Regex pattern to extract car details
        String regex = "\\[(\\w+)_(\\w+)_(\\d+\\.\\d+)\\.jpg\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            // Extracted details from the input string
            String make = matcher.group(1);
            String model = matcher.group(2);
            String version = matcher.group(3);
            return new String[] { make, model, version };
        }
        return null;
    }
    @Override
    public int getItemCount() {
        return imagePathArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageIV;
        private final TextView nameTextView;
        private final TextView ageTextView;
        private final TextView ratingTextView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.idIVImage);
            nameTextView = itemView.findViewById(R.id.Name);
            ageTextView = itemView.findViewById(R.id.GenName);
            ratingTextView = itemView.findViewById(R.id.Rate);
        }
    }
}


