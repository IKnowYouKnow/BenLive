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

import com.benben.qcloud.benLive.R;

import java.util.ArrayList;

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

    int money = 100;
    private static String URL = "http://218.244.151.190/demo/charge";

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

        rvGift.setLayoutManager(gm);
        rvGift.setHasFixedSize(true);
        customDialog();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        giftList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Gift gift = new Gift();
            gift.setGname("鲜花"+i);
            gift.setGprice(17+i);
            gift.setId(i);
            giftList.add(gift);
        }

        if (giftList.size() > 0) {
            if (adapter == null) {
                adapter = new GiftAdapter(getContext(), giftList);
                rvGift.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

            for (Gift gift1 : giftList) {
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
        int[] giftPicId = {
                R.drawable.pl_blue, R.drawable.pl_red, R.drawable.pl_yellow, R.drawable.test_avatar1,
                R.drawable.test_avatar2, R.drawable.test_avatar3, R.drawable.test_avatar4, R.drawable.test_avatar5,
                R.drawable.test_avatar6, R.drawable.test_avatar7, R.drawable.test_avatar8, R.drawable.test_avatar9};

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
            vh.bind(gift, position);
        }

        @Override
        public int getItemCount() {
            return giftList != null ? giftList.size() - 1 : 0;
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

            public void bind(Gift gift, int position) {
                tvGiftName.setText(gift.getGname());
                tvGiftPrice.setText(gift.getGprice() + "");
                ivGiftThumb.setImageResource(giftPicId[position]);
                Log.e(TAG, "bind: gift.getID = " + gift.getId());
                itemView.setTag(gift.getId());
                itemView.setOnClickListener(mListener);
            }
        }
    }
}
