package com.benben.qcloud.benLive.views;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.bean.Customer;
import com.benben.qcloud.benLive.bean.Result;
import com.benben.qcloud.benLive.service.LiveManager;
import com.benben.qcloud.benLive.utils.ResultUtils;
import com.benben.qcloud.benLive.utils.UIUtils;
import com.benben.qcloud.benLive.views.customviews.TemplateTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeActivity extends AppCompatActivity {
    private static final String TAG = "RechargeActivity";
    @BindView(R.id.ttHead)
    TemplateTitle ttHead;
    @BindView(R.id.rvServiceList)
    RecyclerView rvServiceList;

    List<Customer> mRechargeList;
    RechargeAdapter adapter;

    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initStatus();
        ButterKnife.bind(this);
        setReturn();
        mRechargeList = new ArrayList<>();
        getData();
        manager = new LinearLayoutManager(this);
        Log.e(TAG, "onCreate: list = " + mRechargeList.size());
        adapter = new RechargeAdapter(RechargeActivity.this, mRechargeList);
        rvServiceList.setAdapter(adapter);
        rvServiceList.setLayoutManager(manager);
        setListener();
    }

    private void initStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void setListener() {
        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sid = (String) v.getTag();
                copyDialog(sid);
            }
        });
    }

    private void copyDialog(String sid) {
        Map<String, Customer> mapList = new HashMap<>();
        for (Customer customer : mRechargeList) {
            mapList.put(customer.getSid(), customer);
        }

        Customer customer = mapList.get(sid);
        showCopyDialog(customer);
    }

    private void showCopyDialog(Customer customer) {
        Log.e(TAG, "showCopyDialog: custome = " + customer);
        ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(customer.getWxcode());
    }


    String json;

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = LiveManager.get().getRechargeList();
                Log.e(TAG, "run: json = " + json);
                if (json != null) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        int code = obj.getInt("code");
                        Log.e(TAG, "getData: code = " + code);
                        if (code == -1) {
                            Toast.makeText(RechargeActivity.this, "系统繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                        } else if (code == 0) {
                            Result result = ResultUtils.getListResultFromJson(json, Customer.class);
                            if (result != null && result.getRetData() != null) {
                                final List<Customer> list = (List<Customer>) result.getRetData();
                                adapter.addAll(list);
                                Log.e(TAG, "run: list = " + list.size());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "getData: error = " + e.getMessage());
                    }
                }
            }
        }).start();

    }


    private void setReturn() {
        ttHead.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.RechargeHolder> {
        Context context;
        List<Customer> customerList;

        public RechargeAdapter(Context context, List<Customer> customerList) {
            this.context = context;
            this.customerList = customerList;
        }

        @Override
        public RechargeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RechargeHolder(View.inflate(context, R.layout.item_recharge, null));
        }

        @Override
        public void onBindViewHolder(RechargeHolder holder, int position) {
            Customer customer = customerList.get(position);
            if (customer != null) {
                holder.bind(customer);
            }
        }

        @Override
        public int getItemCount() {
            return customerList.size();
        }


        View.OnClickListener listener;

        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        public void addAll(final List<Customer> mRechargeList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "addAll: list = " + mRechargeList.size());

                    if (mRechargeList.size() > 0) {
                        customerList.clear();
                        customerList.addAll(mRechargeList);
                        Log.e(TAG, "addAll: customerlist = " + customerList.size());
                        notifyDataSetChanged();
                    }
                }
            });

        }

        class RechargeHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.recharge_id)
            TextView rechargeId;
            @BindView(R.id.wechatId)
            TextView wechatId;
            @BindView(R.id.aliPayId)
            TextView aliPayId;

            RechargeHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bind(Customer customer) {
                rechargeId.setText(UIUtils.getLimitString(customer.getSid(), 4));
                wechatId.setText(UIUtils.getLimitString(customer.getWxcode(), 11));
                aliPayId.setText(UIUtils.getLimitString(customer.getAlicode(), 11));
                itemView.setTag(customer.getSid());
                itemView.setOnClickListener(listener);
            }
        }
    }

}
