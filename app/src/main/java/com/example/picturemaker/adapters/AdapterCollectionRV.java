package com.example.picturemaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.support.TestData;

import java.util.ArrayList;
import java.util.List;


class ViewHolderCollectionRV extends RecyclerView.ViewHolder {
    public TextView text;
    public ImageView favorite;
    public View layer;
    private ImageView image;

    public ViewHolderCollectionRV(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.imageview);
        text = (TextView) itemView.findViewById(R.id.picture_name);
        favorite = (ImageView) itemView.findViewById(R.id.favorite_image_item_gallery);
        layer = itemView;
//        this.layer.setAlpha(0);
    }

    public void loadImage(Context context, String name) {
        Storage.getInstance(context).GetImage(context, name, this::setImage);
    }

    private void setImage(Bitmap bitmap) {
        this.image.setImageBitmap(bitmap);
//        this.layer.animate().alpha(1f).setDuration(250);
    }
}


public class AdapterCollectionRV extends RecyclerView.Adapter<ViewHolderCollectionRV> {
    Context context;
    int layout_item;
    boolean first;
    int spacing_vertical = 0;
    int spacing_horizontal = 0;
    List<Picture> pictures;
    Storage storage;


    public AdapterCollectionRV(Context context, int layout_item,  int spacing_vertical, int spacing_horizontal, boolean first) {
        this.context = context;
        this.first = first;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        this.pictures = new ArrayList<>();
        this.storage = Storage.getInstance(context);
    }


    @Override
    public ViewHolderCollectionRV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderCollectionRV(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderCollectionRV holder, final int position) {
        Picture picture = this.pictures.get(position);
        holder.itemView.setOnClickListener(v -> Toast.makeText(v.getContext(), TestData.get(position).name, Toast.LENGTH_SHORT).show());

        holder.loadImage(context, picture.public_picture);
        holder.text.setText(picture.name);


        holder.layer.setOnClickListener(v -> {
            Intent intent = new Intent(context, PictureActivity.class);
            intent.putExtra("pictureId", picture.id);
            context.startActivity(intent);
        });

        holder.favorite.setImageResource(picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);
        holder.favorite.setOnClickListener(v -> storage.SetFavoritePicture(picture.id, !picture.is_favorite));

        if ((this.spacing_horizontal > 0 || this.spacing_vertical > 0) && (!this.first || position > 0)) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.itemView.getLayoutParams().width, holder.itemView.getLayoutParams().height);
            params.setMargins(this.spacing_horizontal, this.spacing_vertical, 0, 0);
            holder.itemView.setLayoutParams(params);
        }
    }

    public void setData(List<Picture> pictures){
        this.pictures = pictures;
    }

    public List<Picture> getData(){
        return this.pictures;
    }

    @Override
    public int getItemCount() {
        return this.pictures.size();
    }
}