package com.zomindianew.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zomindianew.R;
import com.zomindianew.bean.ServiceSubCategoryBean;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.user.adapter.SelectSubAdapter;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSubActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    SelectSubAdapter selectSubAdapter;
    List<ServiceSubCategoryBean> item_datalist = new ArrayList<>();
    String sid,sName,cid,categoryname,title;
    String description;
    TextView textView_header;
    private ImageView img_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub);

        recyclerView = (RecyclerView) findViewById(R.id.select_subcategoryRecycle);


        sid = getIntent().getStringExtra("id");
        sName = getIntent().getStringExtra("name");
        cid = getIntent().getStringExtra("cid");
        categoryname = getIntent().getStringExtra("categoryName");

        getServiceSubCategory(sid,sName);

        textView_header = findViewById(R.id.textView_header);
        textView_header.setText(sName);

        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void getServiceSubCategory(final String subId, final String subName) {
        Api api = ApiFactory.getClient(SelectSubActivity.this).create(Api.class);
        Call<ResponseBody> call;

        call = api.getServiceSubCategory(MySharedPreferances.getInstance(SelectSubActivity.this).getString(Constants.ACCESS_TOKEN), subId);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT...secect sub.. ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS ...secect sub...: " + call.request().headers());
        Constants.showProgressDialog(SelectSubActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response== secect ==============>>>>>" + object.toString());
//                            serviceSubCategoryBeanArrayList.clear();
                            JSONArray jsonArray = object.optJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ServiceSubCategoryBean serviceSubCategoryBean = new ServiceSubCategoryBean();
                                serviceSubCategoryBean.setId(jsonObject.optString("id"));
                                serviceSubCategoryBean.setName(jsonObject.optString("name"));
                                serviceSubCategoryBean.setAmount(jsonObject.optString("amount"));
                                serviceSubCategoryBean.setInfodsc(jsonObject.optString("sub_category_description"));
                                serviceSubCategoryBean.setRetingpoint(jsonObject.optString("rating"));
                                serviceSubCategoryBean.setRate_list(jsonObject.optString("rate_list"));
                                serviceSubCategoryBean.setCreated_at(jsonObject.optString("created_at"));
                                serviceSubCategoryBean.setSub_category_id(jsonObject.optString("sub_category_id"));
                                serviceSubCategoryBean.setCategory_pic(jsonObject.optString("service_icon"));
                                serviceSubCategoryBean.setQuntity("1");
                                serviceSubCategoryBean.setCid(cid);
                                serviceSubCategoryBean.setCategoryname(categoryname);
                                serviceSubCategoryBean.setSid(sid);
                                serviceSubCategoryBean.setsName(sName);
                                item_datalist.add(serviceSubCategoryBean);
                            }


                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SelectSubActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            selectSubAdapter = new SelectSubAdapter(SelectSubActivity.this,  item_datalist);
                            recyclerView.setAdapter(selectSubAdapter);
 //                           setAdapter();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, SelectSubActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(SelectSubActivity.this);
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), SelectSubActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), SelectSubActivity.this);
                    }
                } catch (JSONException e) {
                    Constants.hideProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constants.hideProgressDialog();
            }
        });
    }

//    private void setAdapter() {
//
//        selectSubAdapter = new SelectSubAdapter(SelectSubActivity.this,  item_datalist);
//        recyclerView.setAdapter(selectSubAdapter);
//
//
//    }
}