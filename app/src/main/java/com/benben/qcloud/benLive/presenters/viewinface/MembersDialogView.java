package com.benben.qcloud.benLive.presenters.viewinface;

import com.benben.qcloud.benLive.model.MemberInfo;

import java.util.ArrayList;


/**
 * 成员列表回调
 */
public interface MembersDialogView extends MvpView {

    void showMembersList(ArrayList<MemberInfo> data);

}
