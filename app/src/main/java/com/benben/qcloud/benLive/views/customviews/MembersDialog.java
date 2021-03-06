package com.benben.qcloud.benLive.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.benben.qcloud.benLive.presenters.GetMemberListHelper;
import com.benben.qcloud.benLive.adapters.MembersAdapter;
import com.benben.qcloud.benLive.model.MemberInfo;
import com.benben.qcloud.benLive.presenters.viewinface.LiveView;
import com.benben.qcloud.benLive.presenters.viewinface.MembersDialogView;

import java.util.ArrayList;

/**
 * 成员列表
 */
public class MembersDialog extends Dialog implements MembersDialogView {
    private Context mContext;
    private GetMemberListHelper mGetMemberListHelper;
    private ListView mMemberList;
    private MembersAdapter mMembersAdapter;
    private ArrayList<MemberInfo> data = new ArrayList<MemberInfo>();

    public MembersDialog(Context context, int theme, LiveView view) {
        super(context, theme);
        mContext = context;
        setContentView(com.benben.qcloud.benLive.R.layout.members_layout);
        mMemberList = (ListView) findViewById(com.benben.qcloud.benLive.R.id.member_list);
        mMembersAdapter = new MembersAdapter(mContext, com.benben.qcloud.benLive.R.layout.members_item_layout, data, view, this);
        mMemberList.setAdapter(mMembersAdapter);
        Window window = getWindow();
        window.setGravity(Gravity.TOP);
        setCanceledOnTouchOutside(true);
        applyCompat();
    }

    @Override
    protected void onStart() {
        //获取成员信息
        mGetMemberListHelper = new GetMemberListHelper(mContext, this);
        mGetMemberListHelper.getMemberList();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGetMemberListHelper.onDestory();
        super.onStop();
    }

    /**
     * 通过Helper获得数据
     *
     * @param data
     */
    @Override
    public void showMembersList(ArrayList<MemberInfo> data) {
        if (data == null) return;
        mMembersAdapter.clear();
        for (int i = 0; i < data.size(); i++) {
            mMembersAdapter.insert(data.get(i), i);
        }
        mMembersAdapter.notifyDataSetChanged();
    }

    private void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
