package com.example.picturemaker.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.adapters.AdapterCollectionRV;
import com.example.picturemaker.R;
import com.example.picturemaker.support.PictureDiffUtilCallback;

import java.util.List;

public class RecentlyFragment extends Fragment {
    private AdapterCollectionRV rvMain_adapter;
    private RecyclerView rvMain;
    private Storage storage;

//    private MainViewModel mViewModel;

    public static RecentlyFragment newInstance() {
        return new RecentlyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recently, container, false);
    }

    private void RefreshAdapter(List<Picture> pictures) {
        PictureDiffUtilCallback pictureDiffUtilCallback = new PictureDiffUtilCallback(rvMain_adapter.getData(), pictures);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(pictureDiffUtilCallback);
        rvMain_adapter.setData(pictures);
        productDiffResult.dispatchUpdatesTo(rvMain_adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        this.storage = Storage.getInstance(this.getContext());

        rvMain = (RecyclerView) this.getActivity().findViewById(R.id.rv_recently);
        rvMain.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        ((SimpleItemAnimator) rvMain.getItemAnimator()).setSupportsChangeAnimations(false);
        rvMain_adapter = new AdapterCollectionRV(this.getContext() ,R.layout.item_pictute_gallery, 30,30, false);
        rvMain.setAdapter(rvMain_adapter);
        LiveData<List<Picture>> liveData = this.storage.GetLiveDataFromView("Collection");
        liveData.observe(getViewLifecycleOwner(), this::RefreshAdapter);
        this.storage.LoadPicturesByCollection();

    }

}