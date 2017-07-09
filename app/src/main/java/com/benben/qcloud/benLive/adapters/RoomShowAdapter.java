package com.benben.qcloud.benLive.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.model.RoomInfoJson;
import com.benben.qcloud.benLive.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 直播列表的Adapter
 */
public class RoomShowAdapter extends RecyclerView.Adapter<RoomShowAdapter.RoomShowHolder> {
    private static String TAG = "LiveShowAdapter";
    private int resourceId;
    List<RoomInfoJson> list;
    Context context;
    Activity activity;

    public RoomShowAdapter(Context context, List<RoomInfoJson> list, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RoomShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RoomShowHolder(View.inflate(context, R.layout.item_liveshow, null));
    }

    @Override
    public void onBindViewHolder(RoomShowHolder holder, int position) {
        RoomInfoJson data = list.get(position);
        holder.bind(data, position);

    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 0;
    }

    View.OnClickListener mListener;

    public void setListener(View.OnClickListener listener) {
        mListener = listener;
    }
    class RoomShowHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cover)
        ImageView cover;
        @BindView(R.id.live_title)
        TextView liveTitle;
        @BindView(R.id.host_name)
        TextView hostName;
        @BindView(R.id.live_members)
        TextView liveMembers;

        RoomShowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(RoomInfoJson data, int position) {

            if (!TextUtils.isEmpty(data.getInfo().getCover())) {
                RequestManager req = Glide.with(activity);
                req.load(data.getInfo().getCover()).into(cover); //获取网络图片
            } else {
                cover.setImageResource(R.drawable.fangjian);
            }
            liveTitle.setText(UIUtils.getLimitString(data.getInfo().getTitle(), 10));
            hostName.setText(UIUtils.getLimitString(data.getHostId(), 5));
            liveMembers.setText(""+data.getInfo().getMemsize());
            itemView.setTag(position);
            itemView.setOnClickListener(mListener);
        }
    }
}
