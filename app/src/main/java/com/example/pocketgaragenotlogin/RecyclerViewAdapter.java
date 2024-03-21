package com.example.pocketgaragenotlogin;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        String filePath = imagePathArrayList.get(position);

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

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            imageIV = itemView.findViewById(R.id.idIVImage);
        }
    }
}
