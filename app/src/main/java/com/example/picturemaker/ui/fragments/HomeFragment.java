package com.example.picturemaker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.picturemaker.R;
import com.example.picturemaker.adapters.AdapterHomeNews;
import com.example.picturemaker.adapters.AdapterHomePopular;
import com.example.picturemaker.storage.room_tables.Picture;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.support.PictureDiffUtilCallback;

import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {

    Storage storage;
    private AdapterHomeNews rvHomeAdapterNews;
    private AdapterHomePopular rvHomeAdapterPopular;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void RefreshAdapterNews(List<Picture> pictures) {
        PictureDiffUtilCallback pictureDiffUtilCallback = new PictureDiffUtilCallback(rvHomeAdapterNews.getData(), pictures);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(pictureDiffUtilCallback);
        rvHomeAdapterNews.setData(pictures);
        productDiffResult.dispatchUpdatesTo(rvHomeAdapterNews);
    }

    public void RefreshAdapterPopular(List<Picture> pictures) {
        PictureDiffUtilCallback pictureDiffUtilCallback = new PictureDiffUtilCallback(rvHomeAdapterPopular.getData(), pictures);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(pictureDiffUtilCallback);
        rvHomeAdapterPopular.setData(pictures);
        productDiffResult.dispatchUpdatesTo(rvHomeAdapterPopular);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.storage = Storage.getInstance(this.getContext());

        RecyclerView rvHomeNews = (RecyclerView) Objects.requireNonNull(this.getActivity()).findViewById(R.id.rv_news);
        rvHomeNews.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        ((SimpleItemAnimator) Objects.requireNonNull(rvHomeNews.getItemAnimator())).setSupportsChangeAnimations(false);
        rvHomeAdapterNews = new AdapterHomeNews(this.getContext(),R.layout.item_pictute_news,0, 30);
        rvHomeNews.setAdapter(rvHomeAdapterNews);

        LiveData<List<Picture>> liveDataNews = this.storage.GetLiveDataFromView("News");
        liveDataNews.observe(getViewLifecycleOwner(), this::RefreshAdapterNews);
        this.storage.LoadPicturesByNews();


        RecyclerView rvHomePopular = (RecyclerView) this.getActivity().findViewById(R.id.rv_popular);
        rvHomePopular.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        ((SimpleItemAnimator) Objects.requireNonNull(rvHomePopular.getItemAnimator())).setSupportsChangeAnimations(false);
        rvHomeAdapterPopular = new AdapterHomePopular(this.getContext(),R.layout.item_pictute_popular,0, 0);
        rvHomePopular.setAdapter(rvHomeAdapterPopular);

        LiveData<List<Picture>> liveDataPopular = this.storage.GetLiveDataFromView("Popular");
        liveDataPopular.observe(getViewLifecycleOwner(), this::RefreshAdapterPopular);
        this.storage.LoadPicturesByPopular();
    }
}