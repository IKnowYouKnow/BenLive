package com.benben.qcloud.benLive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.model.MemberInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/29.
 */

public class MembersListAdapter extends RecyclerView.Adapter<MemberHolder> {
    private static final String TAG = "MembersListAdapter";

    private Context context;
    private List<MemberInfo> list;

    public MembersListAdapter(Context context, List<MemberInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MemberHolder(LayoutInflater.from(context)
                .inflate(R.layout.members_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(MemberHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder:传递过来的数据list= " + list.get(position).getUserId());
        final String id = list.get(position).getUserId();
        holder.avatar.setImageResource(R.drawable.default_avatar);
        holder.userId.setText(id);
        /*Glide.with(context).load(info.getAvatar() != null ? info.getAvatar() : R.drawable.default_avatar).transform(new GlideCircleTransform(context))
                .into(holder.avatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "好友id+" + info.getUserName(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 0;
    }

}

class MemberHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.member_avatar)
    ImageView avatar;
    @BindView(R.id.userId)
    TextView userId;

    MemberHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
