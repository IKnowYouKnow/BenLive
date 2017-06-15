package com.benben.qcloud.benLive.presenters.viewinface;

import com.benben.qcloud.benLive.model.RoomInfoJson;
import com.benben.qcloud.benLive.presenters.UserServerHelper;

import java.util.ArrayList;


/**
 *  列表页面回调
 */
public interface LiveListView extends MvpView{


    void showRoomList(UserServerHelper.RequestBackInfo result, ArrayList<RoomInfoJson> roomlist);
}
