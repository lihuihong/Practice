package com.example.administrator.practice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.administrator.practice.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by xieya on 2017/5/22.
 */

public class DetailFragment extends Fragment {

    private static final String IMAGE_URL = "imageUrl";

    PhotoView mPvShowPhoto;

    public static DetailFragment newInstance(String imageUrl) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_URL, imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mPvShowPhoto= (PhotoView) view.findViewById(R.id.detail_pv_show_photo);
        Bundle bundle = getArguments();
        String imageUrl = bundle.getString(IMAGE_URL);
        Glide.with(this).load(imageUrl).into(mPvShowPhoto);
        mPvShowPhoto.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}
