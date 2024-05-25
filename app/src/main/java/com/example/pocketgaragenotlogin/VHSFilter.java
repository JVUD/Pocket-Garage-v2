package com.example.pocketgaragenotlogin;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class VHSFilter {

    public static Bitmap applyVHSFilter(Bitmap inputBitmap) {
        int width = inputBitmap.getWidth();
        int height = inputBitmap.getHeight();

        // Create a new Bitmap to hold the result
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, inputBitmap.getConfig());

        // Create Canvas and Paint objects
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();

        // Draw the original image
        canvas.drawBitmap(inputBitmap, 0, 0, paint);

        // Apply VHS effect by manipulating the bitmap
        // Here you can implement your own VHS effect algorithm

        // Example: Add scan lines
        int scanLineHeight = 6; // Height of each scan line
        for (int y = 0; y < height; y += scanLineHeight * 2) {
            Rect rect = new Rect(0, y, width, y + scanLineHeight);
            paint.setColor(Color.argb(10, 255, 255, 255)); // Adjust opacity and color
            canvas.drawRect(rect, paint);
        }

        return outputBitmap;
    }
}
