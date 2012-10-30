package com.bourke.glimmr.fragments.home;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;

import com.bourke.glimmr.activities.BaseActivity;
import com.bourke.glimmr.activities.GroupViewerActivity;
import com.bourke.glimmr.common.Constants;
import com.bourke.glimmr.event.Events.IGroupListReadyListener;
import com.bourke.glimmr.fragments.base.BaseFragment;
import com.bourke.glimmr.R;
import com.bourke.glimmr.tasks.LoadGroupsTask;

import com.googlecode.flickrjandroid.groups.Group;
import com.googlecode.flickrjandroid.groups.GroupList;

import java.util.ArrayList;
import android.widget.TextView;
import android.widget.ImageView;

public class GroupListFragment extends BaseFragment
        implements IGroupListReadyListener {

    private static final String TAG = "Glimmr/GroupListFragment";

    private GroupList mGroups = new GroupList();
    private LoadGroupsTask mTask;
    private ViewGroup mNoConnectionLayout;
    private AdapterView mListView;
    private ViewGroup mEmptyView;

    public static GroupListFragment newInstance() {
        GroupListFragment newFragment = new GroupListFragment();
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mLayout = (RelativeLayout) inflater.inflate(
                R.layout.group_list_fragment, container, false);
        mAq = new AQuery(mActivity, mLayout);
        mNoConnectionLayout =
            (ViewGroup) mLayout.findViewById(R.id.no_connection_layout);
        mListView = (AdapterView) mLayout.findViewById(R.id.list);
        mEmptyView = (ViewGroup) mLayout.findViewById(android.R.id.empty);
        return mLayout;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTask != null) {
            mTask.cancel(true);
            if (Constants.DEBUG)
                Log.d(TAG, "onPause: cancelling task");
        }
    }

    @Override
    protected void startTask() {
        super.startTask();
        mTask = new LoadGroupsTask(this, this);
        mTask.execute(mOAuth);
    }

    @Override
    protected void refresh() {
        super.refresh();
        startTask();
    }

    private void startGroupViewer(Group group) {
        if (group == null) {
            if (Constants.DEBUG)
                Log.e(getLogTag(),
                    "Cannot start GroupViewerActivity, group is null");
            return;
        }
        if (Constants.DEBUG)
            Log.d(getLogTag(), "Starting GroupViewerActivity for " +
                group.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_GROUPVIEWER_GROUP, group);
        bundle.putSerializable(Constants.KEY_GROUPVIEWER_USER,
                mActivity.getUser());
        Intent groupViewer = new Intent(mActivity, GroupViewerActivity.class);
        groupViewer.putExtras(bundle);
        mActivity.startActivity(groupViewer);
    }

    @Override
    public void onGroupListReady(GroupList groups) {
        if (Constants.DEBUG) Log.d(getLogTag(), "onGroupListReady");

        if (groups == null) {
            mNoConnectionLayout.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mNoConnectionLayout.setVisibility(View.GONE);
            mGroups = (GroupList) groups;
            GroupListAdapter adapter = new GroupListAdapter(mActivity,
                    R.layout.group_list_row, (ArrayList<Group>)groups);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    startGroupViewer(mGroups.get(position));
                }
            });
        }
        mEmptyView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    class GroupListAdapter extends ArrayAdapter<Group> {
        public GroupListAdapter(BaseActivity activity, int textViewResourceId,
                ArrayList<Group> objects) {
            super(activity, textViewResourceId, objects);
        }

        // TODO: add aquery delay loading for fling scrolling
        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mActivity.getLayoutInflater().inflate(
                        R.layout.group_list_row, null);
                holder = new ViewHolder();
                holder.textViewGroupName = (TextView)
                    convertView.findViewById(R.id.groupName);
                holder.textViewNumImages = (TextView)
                    convertView.findViewById(R.id.numImagesText);
                holder.imageViewGroupIcon = (ImageView)
                    convertView.findViewById(R.id.groupIcon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Group group = getItem(position);

            holder.textViewGroupName.setText(group.getName());
            holder.textViewNumImages.setText(""+group.getPhotoCount());
            mAq.id(holder.imageViewGroupIcon).image(group.getBuddyIconUrl(),
                    true, true, 0, 0, null, AQuery.FADE_IN_NETWORK);

            return convertView;
        }

        class ViewHolder {
            TextView textViewGroupName;
            TextView textViewNumImages;
            ImageView imageViewGroupIcon;
        }
    }
}
