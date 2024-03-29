package com.example.picturemaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.storage.room_tables.Picture;
import com.example.picturemaker.storage.Storage;

import java.util.ArrayList;
import java.util.List;


class ViewHolderCollectionRV extends RecyclerView.ViewHolder {
    public TextView text;
    public ImageView favorite;
    public View layer;
    public ImageView image;

    public ViewHolderCollectionRV(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.imageview);
        text = (TextView) itemView.findViewById(R.id.picture_name);
        favorite = (ImageView) itemView.findViewById(R.id.favorite_image_item_gallery);
        layer = itemView;
    }
}


public class AdapterCollectionRV extends RecyclerView.Adapter<ViewHolderCollectionRV> {
    Context context;
    int layout_item;
    boolean first;
    int spacing_vertical;
    int spacing_horizontal;
    List<Picture> pictures;
    Storage storage;


    public AdapterCollectionRV(Context context, int layout_item, int spacing_vertical, int spacing_horizontal, boolean first) {
        this.context = context;
        this.first = first;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        this.pictures = new ArrayList<>();
        this.storage = Storage.getInstance(context);
    }


    @NonNull
    @Override
    public ViewHolderCollectionRV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderCollectionRV(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderCollectionRV holder, final int position) {
        Picture picture = this.pictures.get(position);

        storage.GetImage(context, picture.public_picture, holder.image);
        holder.text.setText(picture.name);


        holder.layer.setOnClickListener(v -> {
            Intent intent = new Intent(context, PictureActivity.class);
            intent.putExtra("picture_id", picture.id);
            context.startActivity(intent);
        });

        holder.favorite.setImageResource(picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);
        holder.favorite.setOnClickListener(v -> storage.SetFavoritePicture(picture.id, !picture.is_favorite));
    }

    public List<Picture> getData() {
        return this.pictures;
    }

    public void setData(List<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public int getItemCount() {
        return this.pictures.size();
    }
}
