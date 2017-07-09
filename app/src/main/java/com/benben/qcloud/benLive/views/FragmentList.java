package com.benben.qcloud.benLive.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.qcloud.benLive.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tencent on 2016/12/22.
 */
public class FragmentList extends Fragment implements View.OnClickListener {
    //    TextView newLive,myLove,record,contentTitle;
    @BindView(R.id.content_title)
    TextView contentTitle;
    Unbinder unbinder;
    @BindView(R.id.newLive)
    LinearLayout newLive;
    @BindView(R.id.myLove)
    LinearLayout myLove;
    @BindView(R.id.record)
    LinearLayout record;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        newLive.setOnClickListener(this);
        myLove.setOnClickListener(this);
        record.setOnClickListener(this);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, new FragmentLiveList())
                .commit();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newLive:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_layout, new FragmentLiveList())
                        .commit();
                contentTitle.setText("最新直播");
                break;
            case R.id.myLove:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_layout, new FragmentLiveList())
                        .commit();
                contentTitle.setText("我关注的");
                break;
            case R.id.record:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_layout, new FragmentRecordList())
                        .commit();
                contentTitle.setText("录播");
                break;
        }
    }
}
