package com.bourke.glimmr;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;

import com.androidquery.AQuery;

import com.gmail.yuyang226.flickr.photos.Photo;

public final class PhotoViewerFragment extends BaseFragment {

    protected String TAG = "Glimmr/PhotoViewerFragment";

    private Photo mPhoto = new Photo();
    private AQuery mAq;

    public static PhotoViewerFragment newInstance(Photo photo) {
        PhotoViewerFragment photoFragment = new PhotoViewerFragment();
        photoFragment.mPhoto = photo;
        return photoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(
                    Constants.KEY_PHOTOVIEWER_URL)) {
            mPhoto.setUrl(savedInstanceState.getString(
                        Constants.KEY_PHOTOVIEWER_URL));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mLayout = (FrameLayout) inflater.inflate(R.layout.photoviewer_fragment,
                container, false);
        mAq = new AQuery(mActivity, mLayout);
        if (mPhoto != null) {
            String url = mPhoto.getUrl();
            mAq.id(R.id.web).progress(R.id.progress).webImage(url);
        } else {
            Log.e(TAG, "onStart, mPhoto is null");
        }
        return mLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.KEY_PHOTOVIEWER_URL, mPhoto.getUrl());
    }

    @Override
    public void onPause() {
        super.onPause();
        log(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        log(TAG, "onResume");
    }
}