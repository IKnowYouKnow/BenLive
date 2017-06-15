package com.benben.qcloud.benLive.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.benben.qcloud.benLive.model.CurLiveInfo;
import com.benben.qcloud.benLive.model.RoomInfoJson;
import com.benben.qcloud.benLive.presenters.LiveListViewHelper;
import com.benben.qcloud.benLive.presenters.UserServerHelper;
import com.benben.qcloud.benLive.presenters.viewinface.LiveListView;
import com.benben.qcloud.benLive.utils.Constants;
import com.benben.qcloud.benLive.utils.SxbLog;
import com.benben.qcloud.benLive.views.customviews.RadioGroupDialog;
import com.benben.qcloud.benLive.adapters.LiveShowAdapter;
import com.benben.qcloud.benLive.adapters.RoomShowAdapter;
import com.benben.qcloud.benLive.model.MySelfInfo;

import java.util.ArrayList;


/**
 * 直播列表页面
 */
public class FragmentLiveList extends Fragment implements View.OnClickListener, LiveListView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "FragmentLiveList";
    private ListView mLiveList;
//    private ArrayList<LiveInfoJson> liveList = new ArrayList<LiveInfoJson>();
    private ArrayList<RoomInfoJson> roomList = new ArrayList<RoomInfoJson>();
    private LiveShowAdapter adapter;
    private RoomShowAdapter roomShowAdapter;
    private LiveListViewHelper mLiveListViewHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public FragmentLiveList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLiveListViewHelper = new LiveListViewHelper(this);
        View view = inflater.inflate(com.benben.qcloud.benLive.R.layout.liveframent_layout, container, false);
        mLiveList = (ListView) view.findViewById(com.benben.qcloud.benLive.R.id.live_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(com.benben.qcloud.benLive.R.id.swipe_refresh_layout_list);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright), getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),getResources().getColor(android.R.color.holo_red_light));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        roomShowAdapter =new RoomShowAdapter(getActivity(), com.benben.qcloud.benLive.R.layout.item_liveshow, roomList);
        mLiveList.setAdapter(roomShowAdapter);
        mLiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RoomInfoJson item = roomList.get(i);
                //如果是自己
                if (item.getHostId().equals(MySelfInfo.getInstance().getId())) {
                    Intent intent = new Intent(getActivity(), LiveActivity.class);
                    MySelfInfo.getInstance().setIdStatus(Constants.HOST);
                    MySelfInfo.getInstance().setJoinRoomWay(true);
                    CurLiveInfo.setHostID(item.getHostId());
                    CurLiveInfo.setHostName(MySelfInfo.getInstance().getId());
                    CurLiveInfo.setHostAvator("");
                    CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
                    CurLiveInfo.setMembers(item.getInfo().getMemsize()); // 添加自己
                    CurLiveInfo.setAdmires(item.getInfo().getThumbup());
//                    CurLiveInfo.setAddress(item.getLbs().getAddress());
                    startActivity(intent);
                }else{
                    MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
                    MySelfInfo.getInstance().setJoinRoomWay(false);
                    CurLiveInfo.setHostID(item.getHostId());
                    CurLiveInfo.setHostName("");
                    CurLiveInfo.setHostAvator("");
                    CurLiveInfo.setRoomNum(item.getInfo().getRoomnum());
                    CurLiveInfo.setMembers(item.getInfo().getMemsize()); // 添加自己
                    CurLiveInfo.setAdmires(item.getInfo().getThumbup());
//                    CurLiveInfo.setAddress(item.getLbs().getAddress());
                    checkJoinLive();
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        mLiveListViewHelper.getPageData();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (null != mLiveListViewHelper)
            mLiveListViewHelper.onDestory();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void showRoomList(UserServerHelper.RequestBackInfo info , ArrayList<RoomInfoJson> roomlist) {
        if(info.getErrorCode()!=0){
            Toast.makeText(getContext(), "error "+info.getErrorCode()+" info " +info.getErrorInfo(), Toast.LENGTH_SHORT).show();
            return ;
        }

        mSwipeRefreshLayout.setRefreshing(false);
        roomList.clear();
        if (null != roomlist) {
            for (RoomInfoJson item : roomlist) {
                roomList.add(item);
            }
        }
        roomShowAdapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        mLiveListViewHelper.getPageData();
    }

    private void checkJoinLive() {
        if (TextUtils.isEmpty(MySelfInfo.getInstance().getGuestRole())) {
            final String[] roles = new String[]{getString(com.benben.qcloud.benLive.R.string.str_video_sd), getString(com.benben.qcloud.benLive.R.string.str_video_ld)};
            final String[] values = new String[]{Constants.SD_GUEST, Constants.LD_GUEST};

            RadioGroupDialog roleDialog = new RadioGroupDialog(getContext(), roles);

            roleDialog.setTitle(com.benben.qcloud.benLive.R.string.str_video_qulity);
            roleDialog.setOnItemClickListener(new RadioGroupDialog.onItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    SxbLog.d(TAG, "showVideoQulity->onClick item:" + position);
                    MySelfInfo.getInstance().setGuestRole(values[position]);
                    MySelfInfo.getInstance().writeToCache(getContext());
                    Intent intent = new Intent(getActivity(), LiveActivity.class);
                    startActivity(intent);
                }
            });
            roleDialog.show();
        }else{
            Intent intent = new Intent(getActivity(), LiveActivity.class);
            startActivity(intent);
        }
    }
}
