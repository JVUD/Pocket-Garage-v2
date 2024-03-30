package com.example.pocketgaragenotlogin;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    // creating a variable for our context and array list.
    private final Context context;
    private final ArrayList<String> imagePathArrayList;

    // on below line we have created a constructor.
    public RecyclerViewAdapter(Context context, ArrayList<String> imagePathArrayList) {
        this.context = context;
        this.imagePathArrayList = imagePathArrayList;
    }
    public void removeItem(int position) {
        // Check if the position is valid
        if (position >= 0 && position < imagePathArrayList.size()) {
            // Remove the item from the list
            imagePathArrayList.remove(position);
            // Notify adapter of item removal
            notifyItemRemoved(position);
        }
    }
    public int getPositionByImageName(String imageName) {
        for (int i = 0; i < imagePathArrayList.size(); i++) {
            String imagePath = imagePathArrayList.get(i);
            String fileName = new File(imagePath).getName();
            if (fileName.equals(imageName)) {
                return i;
            }
        }
        return -1;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout in this method which we have created.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // Get the file path at the current position
        // Get the file path at the current position
        String filePath = imagePathArrayList.get(position);

        // Extracting name, age, and rating from file name
        String[] parts = filePath.split("_");
        String dir = "/storage/emulated/0/" + Environment.DIRECTORY_PICTURES + "/Cars/";
        if (parts.length >= 3) {

            holder.nameTextView.setText(parts[0].replace(dir, ""));
            holder.ageTextView.setText(parts[parts.length - 2]);
            holder.ratingTextView.setText(parts[parts.length - 1].replace(".jpg", ""));
        } else {
            // Handling if the file name does not have enough parts
            holder.nameTextView.setText("Name");
            holder.ageTextView.setText("Age");
            holder.ratingTextView.setText("Rating");
        }

        // Load the image using Picasso
        Picasso.get()
                .load(new File(filePath))
                .placeholder(R.drawable.garage) // Placeholder image while loading
                .error(R.drawable.plus) // Error image if loading fails
                .into(holder.imageIV);

        // Set click listener for the item

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click
                Intent i = new Intent(context, ImageDetailActivity.class);
                i.putExtra("imgPath", imagePathArrayList.get(position));
                context.startActivity(i);
            }
        });

        // Log the file path for debugging
        Log.d("FilePathDebug", "File path at position " + position + ": " + filePath);

        // Check if the file exists at the specified path
        File imgFile = new File(filePath);
        if (imgFile.exists()) {
            // Load the image using Picasso
            Picasso.get()
                    .load(imgFile)
                    .placeholder(R.drawable.garage) // Placeholder image while loading
                    .error(R.drawable.plus) // Error image if loading fails
                    .into(holder.imageIV);

            // Set click listener for the item
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle item click
                    Intent i = new Intent(context, ImageDetailActivity.class);
                    i.putExtra("imgPath", imagePathArrayList.get(position));
                    context.startActivity(i);
                }
            });
        } else {
            // Log an error if the file does not exist
            Log.e("FilePathDebug", "File does not exist at path: " + filePath);
        }
    }



    @Override
    public int getItemCount() {
        // this method returns
        // the size of recyclerview
        return imagePathArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.

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