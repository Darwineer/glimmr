package com.bourke.glimmr.fragments.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;

import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.androidquery.AQuery;

import com.bourke.glimmr.activities.BaseActivity;
import com.bourke.glimmr.activities.PhotoViewerActivity;
import com.bourke.glimmr.activities.ProfileActivity;
import com.bourke.glimmr.common.Constants;

import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotoList;

/**
 *
 */
public abstract class BaseFragment extends SherlockFragment {

    private static final String TAG = "Glimmr/BaseFragment";

    /**
     * It's useful to keep a reference to the parent activity in our fragments.
     */
    protected SherlockFragmentActivity mActivity;

    /**
     * Should contain current user and valid access token for that user.
     */
    protected OAuth mOAuth;

    protected ActionBar mActionBar;
    protected AQuery mAq;
    protected ViewGroup mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getLogTag(), "onCreate");
        mActivity = getSherlockActivity();
        mActionBar = mActivity.getSupportActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getLogTag(), "onResume");
        startTask();
    }

    /**
     * Start the PhotoViewerActivity with a list of photos to view and an index
     * to start at in the list.
     */
    protected void startPhotoViewer(PhotoList photos, int pos) {
        if (photos == null) {
            Log.e(getLogTag(), "Cannot start PhotoViewer, photos is null");
            return;
        }
        Log.d(getLogTag(), "starting photo viewer with " + photos.size()
                + " ids");
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_PHOTOVIEWER_LIST, photos);
        bundle.putInt(Constants.KEY_PHOTOVIEWER_START_INDEX, pos);
        Intent photoViewer = new Intent(mActivity, PhotoViewerActivity.class);
        photoViewer.putExtras(bundle);
        mActivity.startActivity(photoViewer);
    }

    protected void startProfileViewer(User user) {
        if (user == null) {
            Log.e(getLogTag(), "Cannot start ProfileActivity, user is null");
            return;
        }
        Log.d(getLogTag(), "Starting ProfileActivity for "
                + user.getUsername());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_PROFILEVIEWER_USER, user);
        Intent profileViewer = new Intent(mActivity, ProfileActivity.class);
        profileViewer.putExtras(bundle);
        mActivity.startActivity(profileViewer);
    }

    protected void startTask() {
        Log.d(getLogTag(), "startTask()");
        if (mOAuth == null || mOAuth.getUser() == null) {
            SharedPreferences prefs = mActivity.getSharedPreferences(Constants
                    .PREFS_NAME, Context.MODE_PRIVATE);
            mOAuth = BaseActivity.loadAccessToken(prefs);
        }
    }

    protected String getLogTag() {
        return TAG;
    }
}
