package com.benben.qcloud.benLive.views.customviews;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.qcloud.benLive.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wei on 2016/7/25.
 */
public class RoomUserDetailsDialog extends DialogFragment {
    private static final String TAG = "RoomUserDetailsDialog";

    Unbinder unbinder;
    @BindView(R.id.eiv_avatar)
    ImageView eivAvatar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.layout_live_no_talk)
    LinearLayout layoutLiveNoTalk;
    @BindView(R.id.layout_live_add_blacklist)
    LinearLayout layoutLiveAddBlacklist;
    @BindView(R.id.layout_live_kick)
    LinearLayout layoutLiveKick;
    @BindView(R.id.btn_set_admin)
    Button btnSetAdmin;

    private String username;
    private String chatroomId;
    private String liveId;

    public static RoomUserDetailsDialog newInstance(String username) {
        RoomUserDetailsDialog dialog = new RoomUserDetailsDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_user_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        customDialog();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        syncUserInfo();
        //mentionBtn.setText("@TA");
    }

    private void syncUserInfo() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (!BenLiveHelper.getInstance().getContactList().containsKey(username)){
//                    try {
//                        User user = LiveManager.get().(username);
//                        LiveHelper.getInstance().saveAppContact(user);
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                EaseUserUtils.setAppUserNick(username,usernameView);
//                            }
//                        });
//                    } catch (LiveException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

    }

    private void customDialog() {
        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }

    @OnClick(R.id.layout_live_add_blacklist)
    void addToBlacklist() {
    }

    @OnClick(R.id.layout_live_kick)
    void kickMember() {
    }

    @OnClick(R.id.btn_set_admin)
    void setToAdmin() {
    }

    private List<String> getUserList() {
        List<String> users = new ArrayList<>();
        users.add(username);
        return users;
    }

    private void showToast(final String toast) {
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
