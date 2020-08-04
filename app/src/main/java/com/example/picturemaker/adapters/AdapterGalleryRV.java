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
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.support.TestData;

import java.util.List;


class ViewHolderGalleryRV extends RecyclerView.ViewHolder{
    private ImageView image;
    public TextView text;
    public ImageView favorite;
    public View layer;

    public ViewHolderGalleryRV(View itemView) {
        super(itemView);
        image = (ImageView)itemView.findViewById(R.id.imageview);
        text = (TextView)itemView.findViewById(R.id.picture_name);
        favorite = (ImageView) itemView.findViewById(R.id.favorite_image_item_gallery);
        layer = itemView;
        this.layer.setAlpha(0);
    }

    public void loadImage(Context context, String name) {
        Storage.getInstance(context).GetImage(context, name, this::setImage);
    }

    private void setImage(Bitmap bitmap){
        this.image.setImageBitmap(bitmap);
        this.layer.animate().alpha(1f).setDuration(250);
    }
}


public class AdapterGalleryRV extends RecyclerView.Adapter<ViewHolderGalleryRV> {
    Context context;
    int layout_item;
    boolean first;
    int spacing_vertical = 0;
    int spacing_horizontal = 0;
    List<String> picturesIds;
    Storage storage;

    public AdapterGalleryRV() {}

    public AdapterGalleryRV(Context context ,int layout_item, List<String> picturesIds, int spacing_vertical, int spacing_horizontal, boolean first) {
        this.context = context;
        this.first = first;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        this.picturesIds = picturesIds;
        this.storage = Storage.getInstance(context);
    }


    @Override
    public ViewHolderGalleryRV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderGalleryRV(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderGalleryRV holder, final int position) {
        String pictureId = this.picturesIds.get(position);
        storage.GetPicture(picture -> {
            holder.itemView.setOnClickListener(v -> Toast.makeText(v.getContext(), TestData.get(position).name, Toast.LENGTH_SHORT).show());

            holder.loadImage(context, picture.public_picture);
            holder.text.setText(picture.name);


            holder.layer.setOnClickListener(v -> {
                Intent intent = new Intent(context, PictureActivity.class);
                intent.putExtra("picture_id", picture.public_id);
                context.startActivity(intent);
            });

            holder.favorite.setImageResource(picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);
            holder.favorite.setOnClickListener(v -> {
                storage.SetFavoritePicture(picture.public_id, !picture.is_favorite, () -> {notifyItemChanged(position);});
            });
        }, pictureId);



        if ((this.spacing_horizontal > 0 || this.spacing_vertical > 0) && (!this.first || position > 0)){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.itemView.getLayoutParams().width, holder.itemView.getLayoutParams().height);
            params.setMargins(this.spacing_horizontal, this.spacing_vertical, 0, 0);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return this.picturesIds.size();
    }
}
