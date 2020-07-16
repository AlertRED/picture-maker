package com.example.picturemaker.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.picturemaker.FirebaseDB;
import com.example.picturemaker.adapters.AdapterGalleryRV;
import com.example.picturemaker.R;
import com.example.picturemaker.support.Item;

import java.util.List;

public class RecentlyFragment extends Fragment {
    private AdapterGalleryRV rvMain_adapter;
    private RecyclerView rvMain;

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

    @Override
    public void onResume() {
        super.onResume();
        rvMain_adapter.notifyDataSetChanged();
    }

    private void RefreshAdapter(List<Item> items) {
        rvMain_adapter = new AdapterGalleryRV(this.getContext() ,R.layout.item_pictute_gallery, items,30,30, false);
        rvMain.setAdapter(rvMain_adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        rvMain = (RecyclerView) this.getActivity().findViewById(R.id.rv_recently);
        rvMain.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rvMain_adapter = new AdapterGalleryRV();
        FirebaseDB.loadItem(this::RefreshAdapter);

    }

}