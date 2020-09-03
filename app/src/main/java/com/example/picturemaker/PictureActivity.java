package com.example.picturemaker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;

import java.util.Objects;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class PictureActivity extends AppCompatActivity {

    public ImageView image;
    private Picture picture;
    private TextView name;
    private TextView my_score;
    private TextView total_score;
    private ImageView favorite;
    private Button button_start;

    private Storage storage;
    private ImageView imageBackground;
    private BlurView blurLayout;
    private Toolbar toolbar;
    private ViewGroup root;

    public void ShowRating() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.activity_rating_picture, null);
        popDialog.setView(view);

        final RatingBar progress = view.findViewById(R.id.ratingBar);
        progress.setRating((float) picture.score);

        popDialog.setPositiveButton("Готово",
                (dialog, which) -> {
                    picture.score = (int) progress.getRating();
                    RefreshData();
                    dialog.dismiss();
                }).setNegativeButton("Отмена",
                (dialog, id) -> {
                    RefreshData();
                    dialog.cancel();
                });

        popDialog.create();
        popDialog.show();
    }


    private void RefreshData() {

        this.storage.GetImage(this, this.picture.public_picture, imageBackground);
        this.storage.GetImage(this, this.picture.public_picture, image);

        this.name.setText(this.picture.name);
        this.total_score.setText("Рейтинг: ".concat(String.valueOf(this.picture.total_score)));
        this.favorite.setImageResource(this.picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);


        this.my_score.setText("Ваша оценка: ".concat(String.valueOf(this.picture.score)));
        final Activity activity = this;

        this.button_start.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PaintActivity.class);
            intent.putExtra("picture_id", picture.id);
            startActivity(intent);
        });

        this.favorite.setOnClickListener(v -> storage.SetFavoritePicture(picture.id, !picture.is_favorite));
    }

    private void LoadItem(Picture picture) {
        this.picture = picture;
        RefreshData();
    }

    private void backArrow(){
        ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(this.toolbar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        assert upArrow != null;
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        long picture_id = getIntent().getIntExtra("pictureId", 0);

        this.image = findViewById(R.id.picture);
        this.name = findViewById(R.id.activity_picture_name);
        this.my_score = findViewById(R.id.picture_my_score);
        this.total_score = findViewById(R.id.picture_total_score);
        this.favorite = findViewById(R.id.activity_picture_favorite);

        this.button_start = findViewById(R.id.button_start);


        this.storage = Storage.getInstance(this);
        LiveData<Picture> live = this.storage.GetLivePicture(picture_id);
        live.observe(this, this::LoadItem);

        toolbar = (Toolbar) findViewById(R.id.filter_gallery_toolbar2);
        this.backArrow();
        imageBackground = findViewById(R.id.backgroundPicture);
        blurLayout = (BlurView) findViewById(R.id.blurLayout);

        this.root = findViewById(R.id.content_profile_picture);
        blurBackground();
    }


    private void blurBackground() {
        float radius = 8f;

        View decorView = getWindow().getDecorView();
        Drawable windowBackground = decorView.getBackground();

        blurLayout.setupWith(this.root)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);
    }

}