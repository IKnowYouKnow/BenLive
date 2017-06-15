package com.benben.qcloud.benLive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.benben.qcloud.benLive.model.RoomInfoJson;
import com.benben.qcloud.benLive.presenters.LiveListViewHelper;
import com.benben.qcloud.benLive.presenters.UserServerHelper;
import com.benben.qcloud.benLive.utils.SxbLog;
import com.benben.qcloud.benLive.utils.UIUtils;
import com.tencent.ilivesdk.core.ILiveLoginManager;
;
import com.tencent.livesdk.ILVLiveManager;
import com.benben.qcloud.benLive.presenters.viewinface.LiveListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 跨房连麦
 */
public class LinkAdapter extends BaseAdapter implements LiveListView {
    public interface OnItemClick{
        void onClick(RoomInfoJson info);
    }

    private static String TAG = "LinkAdapter";
    private Context mContext;
    private LiveListViewHelper helper;
    private OnItemClick mItemListener = null;
    private ArrayList<RoomInfoJson> mRoomList = null;

    private class ViewHolder{
        TextView tvTitle;
        TextView tvId;
        Button btnLink;
    }

    public LinkAdapter(Context context) {
        mContext = context;
        helper = new LiveListViewHelper(this);
        helper.getPageData();
    }

    @Override
    public int getCount() {
        return null!=mRoomList ? mRoomList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return null!=mRoomList&&i<mRoomList.size() ? mRoomList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder)convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(com.benben.qcloud.benLive.R.layout.item_link_info, null);

            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(com.benben.qcloud.benLive.R.id.tv_title);
            holder.tvId = (TextView) convertView.findViewById(com.benben.qcloud.benLive.R.id.tv_id);
            holder.btnLink = (Button) convertView.findViewById(com.benben.qcloud.benLive.R.id.btn_link);

            convertView.setTag(holder);
        }

        if (null != mRoomList && mRoomList.size() > position) {
            final RoomInfoJson info = mRoomList.get(position);
            holder.tvTitle.setText(UIUtils.getLimitString(info.getInfo().getTitle(), 10));
            holder.tvId.setText(info.getHostId()+"("+info.getInfo().getRoomnum()+")");

            List<String> linkedList = ILVLiveManager.getInstance().getCurrentLinkedUserArray();
            if (linkedList.contains(info.getHostId())
                    || ILiveLoginManager.getInstance().getMyUserId().equals(info.getHostId())){
                holder.btnLink.setVisibility(View.INVISIBLE);
            }else{
                holder.btnLink.setVisibility(View.VISIBLE);
                holder.btnLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mItemListener){
                            mItemListener.onClick(info);
                        }
                    }
                });
            }
        }
        if (null != mRoomList){
            SxbLog.d(TAG, "position: "+position+"/"+mRoomList.size());
        }

        return convertView;
    }

    @Override
    public void showRoomList(UserServerHelper.RequestBackInfo result, ArrayList<RoomInfoJson> roomlist) {
        if (0 == result.getErrorCode()){
            mRoomList = roomlist;
            notifyDataSetChanged();
            SxbLog.d(TAG, "showRoomList->size::"+roomlist.size());
        }else{
            SxbLog.d(TAG, "showRoomList->result:"+result.getErrorCode()+"|"+result.getErrorInfo());
        }
    }

    public void setOnItemClickListenr(OnItemClick listener){
        mItemListener = listener;
    }
}
