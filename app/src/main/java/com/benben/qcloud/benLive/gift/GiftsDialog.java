package com.benben.qcloud.benLive.gift;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.qcloud.benLive.I;
import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.data.LiveDao;
import com.benben.qcloud.benLive.gift.bean.Gift;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by wei on 2016/7/25.
 */
public class GiftsDialog extends DialogFragment {


    Unbinder unbinder;

    @BindView(R.id.rv_gift)
    RecyclerView rvGift;

    ArrayList<Gift> giftList;

    GridLayoutManager gm;

    GiftAdapter adapter;
    @BindView(R.id.tv_my_bill)
    TextView tvMyBill;
    @BindView(R.id.tv_my_money)
    TextView tvMyMoney;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    private static final String TAG = "GiftHolder";

    LiveDao dao;
    public static GiftsDialog newInstance() {
        GiftsDialog dialog = new GiftsDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        gm = new GridLayoutManager(getContext(), 4);

        dao = new LiveDao();
        giftList = new ArrayList<>();
        rvGift.setLayoutManager(gm);
        rvGift.setHasFixedSize(true);
        customDialog();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Map<String, Gift> giftMap = dao.getGiftList();
        Iterator<Map.Entry<String, Gift>> iterator = giftMap.entrySet().iterator();
        while (iterator.hasNext()) {
            giftList.add(iterator.next().getValue());
        }
        Collections.sort(giftList, new Comparator<Gift>() {
            @Override
            public int compare(Gift o1, Gift o2) {
                return Integer.parseInt(o1.getGprice())-Integer.parseInt(o2.getGprice());
            }
        });

        if (this.giftList.size() > 0) {
            if (adapter == null) {
                adapter = new GiftAdapter(getContext(), this.giftList);
                rvGift.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

            for (Gift gift1 : this.giftList) {
                Log.e(TAG, "onActivityCreated: gift1" + gift1);
            }
        }

        tvMyBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "查看我的账单", Toast.LENGTH_SHORT).show();
            }
        });
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

    View.OnClickListener mListener;

    public void setListener(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class GiftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;
        ArrayList<Gift> mList;

        public GiftAdapter(Context mContext, ArrayList<Gift> giftList) {
            this.mContext = mContext;
            this.mList = giftList;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(mContext, R.layout.item_gift, null);
            GiftHolder vh = new GiftHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
            Gift gift = giftList.get(position);
            GiftHolder vh = (GiftHolder) parentHolder;
            vh.bind(gift);
        }

        @Override
        public int getItemCount() {
            return giftList != null ? giftList.size() : 0;
        }

        class GiftHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.ivGiftThumb)
            ImageView ivGiftThumb;
            @BindView(R.id.tvGiftName)
            TextView tvGiftName;
            @BindView(R.id.tvGiftPrice)
            TextView tvGiftPrice;
            @BindView(R.id.layout_gift)
            LinearLayout layoutGift;

            GiftHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bind(Gift gift) {
                tvGiftName.setText(gift.getGname());
                tvGiftPrice.setText(gift.getGprice() + "");
                Glide.with(mContext).load(I.GIFT_THUMB_URL+gift.getGurl()).into(ivGiftThumb);
                Log.e(TAG, "bind: gift.getID = " + gift.getId());
                itemView.setTag(gift.getId());
                itemView.setOnClickListener(mListener);
            }
        }
    }
}
