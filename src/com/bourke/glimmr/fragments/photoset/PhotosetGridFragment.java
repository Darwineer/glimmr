package com.bourke.glimmr.fragments.photoset;

import com.bourke.glimmr.fragments.base.PhotoGridFragment;
import com.bourke.glimmr.tasks.LoadPhotosetTask;
import com.gmail.yuyang226.flickr.photosets.Photoset;

public class PhotosetGridFragment extends PhotoGridFragment {

    private static final String TAG = "Glimmr/PhotosetGridFragment";

    private Photoset mPhotoset = new Photoset();

    public static PhotosetGridFragment newInstance(Photoset photoset) {
        PhotosetGridFragment newFragment = new PhotosetGridFragment();
        newFragment.mPhotoset = photoset;
        return newFragment;
    }

    @Override
    protected void startTask() {
        super.startTask();
        new LoadPhotosetTask(mActivity, this, mPhotoset).execute(mOAuth);
    }
}
