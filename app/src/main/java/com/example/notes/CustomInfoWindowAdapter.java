package com.example.notes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    Button edit ,delete;
    View view;

    //ImageView image;
    ImageView image2;

    public CustomInfoWindowAdapter(final Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker , View view){

        // Title
        String title = marker.getTitle();
        String [] arrStr = title.split("#",2);
        TextView markerTitle = (TextView) view.findViewById(R.id.markerTitle);
        markerTitle.setText(arrStr[0]);


        // Morada
        String morada = marker.getSnippet();
        TextView markerMorada = (TextView) view.findViewById(R.id.markerDescription);
        markerMorada.setText(morada);

        // Funciona
        image2 = (ImageView) mWindow.findViewById(R.id.markerImage);
        Glide.with(mContext).load(arrStr[1]).into(image2);


    }


    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }
}
