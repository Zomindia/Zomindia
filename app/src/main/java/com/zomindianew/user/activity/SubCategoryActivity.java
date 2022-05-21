package com.zomindianew.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zomindianew.R;
import com.zomindianew.bean.ServiceSubCategoryBean;
import com.zomindianew.bean.SubCategoryBean;
import com.zomindianew.comman.activity.BaseActivity;
import com.zomindianew.comman.fragment.ContactAsFragment;
import com.zomindianew.comman.fragment.FaqFragment;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.BookingDetailActivity;
import com.zomindianew.user.adapter.ServiceCategoryAdapter;
import com.zomindianew.user.adapter.SubCategoryAdapter;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SubCategoryActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView subcategoryRecycle;
    private SubCategoryAdapter subCategoryAdapter;
    private TextView textView_header;
    private String categoryId;
    private String categoryName;
    private ImageView img_back_arrow;
    private TextView no_record_txt;
    private ImageView imageNew;
    int pos;
    private ArrayList<SubCategoryBean> subCategoryBeanArrayList = new ArrayList<>();
    private ArrayList<ServiceSubCategoryBean> serviceSubCategoryBeanArrayList = new ArrayList<>();
    int count = 0;
    String description;

    SharedPreferences sharedpreferences;
    String get_email,get_mobile,get_uname,get_id,get_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        categoryId = getIntent().getStringExtra("id");
        categoryName = getIntent().getStringExtra("categoryName");

        imageNew = findViewById(R.id.imageNew);
        img_back_arrow = findViewById(R.id.img_back_arrow);
        if(categoryName.equalsIgnoreCase("Salon at home")){
            imageNew.setImageResource(R.mipmap.back_logo_2);
        }else {
            imageNew.setImageResource(R.mipmap.back_logo_1);
        }
        img_back_arrow.setOnClickListener(this);
        textView_header = findViewById(R.id.textView_header);
        textView_header.setText(categoryName);

        no_record_txt = findViewById(R.id.no_record_txt);
        subcategoryRecycle = findViewById(R.id.subcategoryRecycle);
        subcategoryRecycle.setItemAnimator(new DefaultItemAnimator());
        subcategoryRecycle.setLayoutManager(new GridLayoutManager(SubCategoryActivity.this,LinearLayoutManager.VERTICAL));
        sharedpreferences = getApplicationContext().getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        get_email= sharedpreferences.getString("email", "");
        get_mobile= sharedpreferences.getString("mobile", "");
        get_uname= sharedpreferences.getString("uname", "");
        get_id= sharedpreferences.getString("id", "");
        get_token= sharedpreferences.getString("accesstoken", "");
//        getSubCategory();

        if (Constants.isInternetOn(SubCategoryActivity.this)) {
            getSubCategory();
        } else {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) SubCategoryActivity.this.findViewById(android.R.id.content)).getChildAt(0);
            Constants.showSnackbar(SubCategoryActivity.this, viewGroup, getResources().getString(R.string.no_internet), "Retry");
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    private void setAdapter() {
        subCategoryAdapter = new SubCategoryAdapter(SubCategoryActivity.this, subCategoryBeanArrayList);
        subcategoryRecycle.setAdapter(subCategoryAdapter);

        if (subCategoryBeanArrayList.size() > 0) {
            no_record_txt.setVisibility(View.GONE);
            subcategoryRecycle.setVisibility(View.VISIBLE);
        } else {
            no_record_txt.setVisibility(View.VISIBLE);
            subcategoryRecycle.setVisibility(View.GONE);
        }

        subCategoryAdapter.onItemClickMethod(new SubCategoryAdapter.ItemInterFace() {
            @Override
            public void onItemClick(View view, int position) {



                switch (view.getId()) {
                    case R.id.relative_1:

                        showInfoDialog(position);


                          break;

                    default:
                        Intent intent= new Intent(SubCategoryActivity.this,SelectSubActivity.class);
                        intent.putExtra("id",subCategoryBeanArrayList.get(position).getId());
                        intent.putExtra("name",subCategoryBeanArrayList.get(position).getCategory_name());
                        intent.putExtra("cid",categoryId);
                        intent.putExtra("categoryName",categoryName);
                        startActivity(intent);


                        break;



                }
            }
        });


    }

    public void getSubCategory() {
        Api api = ApiFactory.getClient(SubCategoryActivity.this).create(Api.class);
        Call<ResponseBody> call;

        call = api.getSubCategory(MySharedPreferances.getInstance(SubCategoryActivity.this).getString(Constants.ACCESS_TOKEN), categoryId);
        Log.e(Constants.LOG_CAT, "API REQUEST SUB CATEGORY LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().toString());
        Constants.showProgressDialog(SubCategoryActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        Log.e("","sub category "+response);
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        Log.e("...","get opt"+output);
                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());

                            JSONArray jsonArray = object.optJSONArray("data");
                            //  categoryBeanArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SubCategoryBean subCategoryBean = new SubCategoryBean();

                                subCategoryBean.setId(jsonObject.optString("id"));
                                subCategoryBean.setCategory_icon(jsonObject.optString("sub_category_icon"));
                                subCategoryBean.setCategory_name(jsonObject.optString("name"));
                                subCategoryBean.setCreated_at(jsonObject.optString("created_at"));
                                subCategoryBean.setInfo_desc(jsonObject.optString("description"));
                                subCategoryBean.setRating_point(jsonObject.optString("rating"));
                                subCategoryBeanArrayList.add(subCategoryBean);
                            }
                            setAdapter();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, SubCategoryActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(SubCategoryActivity.this);
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), SubCategoryActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), SubCategoryActivity.this);
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


    public void getServiceSubCategory(final String subId, final String subName) {
        Api api = ApiFactory.getClient(SubCategoryActivity.this).create(Api.class);
        Call<ResponseBody> call;

        call = api.getServiceSubCategory(MySharedPreferances.getInstance(SubCategoryActivity.this).getString(Constants.ACCESS_TOKEN), subId);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(SubCategoryActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());
                            serviceSubCategoryBeanArrayList.clear();
                            JSONArray jsonArray = object.optJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ServiceSubCategoryBean subCategoryBean = new ServiceSubCategoryBean();
                                subCategoryBean.setId(jsonObject.optString("id"));
                                subCategoryBean.setName(jsonObject.optString("name"));
                                subCategoryBean.setAmount(jsonObject.optString("amount"));
                                description = jsonObject.optString("description");
                                subCategoryBean.setCreated_at(jsonObject.optString("created_at"));
                                subCategoryBean.setSub_category_id(jsonObject.optString("sub_category_id"));
                                subCategoryBean.setQuntity("1");
                                serviceSubCategoryBeanArrayList.add(subCategoryBean);
                            }

                            if (!serviceSubCategoryBeanArrayList.isEmpty()) {
                                showUserDialog(subName, subId);
                            } else {
                                Constants.showToastAlert("Service not available, Please try again later!!", SubCategoryActivity.this);
                            }


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, SubCategoryActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(SubCategoryActivity.this);
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), SubCategoryActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), SubCategoryActivity.this);
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

    public String method(String str) {
        if (str.charAt(str.length() - 1) == 'x') {
            str = str.replace(str.substring(str.length() - 1), "");
            return str;
        } else {
            return str;
        }
    }

    private void showUserDialog(final String name, final String subId) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(SubCategoryActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_booking, null);
            TextView headerDialogTV = view.findViewById(R.id.headerDialogTV);

            TextView doneTextView = view.findViewById(R.id.doneTextView);
            doneTextView.setVisibility(View.GONE);
            headerDialogTV.setText("Choose a service option (step 2/3)");
            RecyclerView recyclerStateList = view.findViewById(R.id.recyclerStateList);
            // description
            ImageView informationTV = view.findViewById(R.id.informationTV);
            informationTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showinformatoin(description);
                    //showDialog(view, description);/**/
                }
            });
            TextView bookingConfirmText = view.findViewById(R.id.bookingConfirmText);
            final ServiceCategoryAdapter serviceCategoryAdapter = new ServiceCategoryAdapter(SubCategoryActivity.this, serviceSubCategoryBeanArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SubCategoryActivity.this);
            recyclerStateList.setLayoutManager(linearLayoutManager);
            recyclerStateList.setAdapter(serviceCategoryAdapter);

            builder.setView(view);
            //Creating dialog box
            final AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();

            bookingConfirmText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (serviceSubCategoryBeanArrayList.get(pos).isSlected()) {
                        Intent intent = new Intent(SubCategoryActivity.this, ConfirmBookingScreen.class);
                        intent.putExtra("categoryID", categoryId);
                        intent.putExtra("subCategoryID", subId);
                        intent.putExtra("subCategoryName", name);
                        intent.putExtra("categoryName", categoryName);
                        intent.putExtra("amount", serviceSubCategoryBeanArrayList.get(pos).getAmount());
                        intent.putExtra("serviceID", serviceSubCategoryBeanArrayList.get(pos).getId());
                        intent.putExtra("count", serviceSubCategoryBeanArrayList.get(pos).getQuntity());
                        startActivity(intent);
                    } else {
                        Constants.showToastAlert("Please select!!", SubCategoryActivity.this);
                    }


                }
            });

            serviceCategoryAdapter.onItemClickMethod(new ServiceCategoryAdapter.ItemInterFace() {
                @Override
                public void onItemClick(View view, int position) {

                    switch (view.getId()) {
                        case R.id.addItem:
                            try {
                                ServiceSubCategoryBean serviceSubCategoryBean = serviceSubCategoryBeanArrayList.get(position);
                                if (count < 10) {

                                    count++;
                                    serviceSubCategoryBean.setQuntity(String.valueOf(count));
                                   /* int amount = Integer.parseInt(method(serviceSubCategoryBean.getAmount().trim()));
                                    int newAmount = amount * count;
                                    serviceSubCategoryBean.setAmount("" + newAmount);*/
                                }

                                serviceCategoryAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.removeItem:
                            try {
                                if (count >= 1) {
                                    ServiceSubCategoryBean subCategoryBean = serviceSubCategoryBeanArrayList.get(position);
                                    subCategoryBean.setQuntity(String.valueOf(count));
                                   /* int amount = Integer.parseInt(method(subCategoryBean.getAmount().trim()));
                                    int newAmount = amount / count;
                                    subCategoryBean.setAmount("" + newAmount);*/
                                    count--;
                                }

                                serviceCategoryAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.subcategoryRelative:
                            pos = position;
                            for (int i = 0; i < serviceSubCategoryBeanArrayList.size(); i++) {
                                ServiceSubCategoryBean subCategoryBean1 = serviceSubCategoryBeanArrayList.get(i);
                                if (serviceSubCategoryBeanArrayList.get(i).isSlected()) {
                                    subCategoryBean1.setSlected(false);
                                } else {
                                    subCategoryBean1.setSlected(false);
                                }

                                if (i == position) {
                                    subCategoryBean1.setSlected(true);
                                }
                                serviceSubCategoryBeanArrayList.set(i, subCategoryBean1);
                                serviceCategoryAdapter.notifyDataSetChanged();

                            }
                            break;
                    }


                }
            });


        } catch (
                Exception ex) {
            ex.printStackTrace();
        }

    }

    private void showinformatoin(String info) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(SubCategoryActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_info, null);

            builder.setView(view);
            //Creating dialog box
            final AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();
            TextView cancelTv = view.findViewById(R.id.bookingConfirmText);
            TextView doneTextView = view.findViewById(R.id.informationRL);

            doneTextView.setText(info);


            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });


        } catch (
                Exception ex) {
            ex.printStackTrace();
        }

    }

    private void showDialog(View view, String message) {
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(SubCategoryActivity.this)
                .anchorView(view)
                .arrowColor(getResources().getColor(R.color.colorPrimary))
                .margin(getResources().getDimension(R.dimen.default_padding))
                .padding(getResources().getDimension(R.dimen.default_padding))
                .gravity(Gravity.TOP)
                .animated(true)
                .text(message)
                .textColor(getResources().getColor(R.color.white))
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(false)
                .maxWidth(R.dimen.simpletooltip_max_width)
                .focusable(true)
                .build();
        tooltip.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_arrow:
                finish();
                break;
        }
    }



    private void showInfoDialog(final int position) {

        try {


            final Dialog dialog = new Dialog(SubCategoryActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_infol);
            TextView category_naame = dialog.findViewById(R.id.category_naame);
            TextView tv_info_detail = dialog.findViewById(R.id.tv_info_detail);
            TextView select_service = dialog.findViewById(R.id.select_service);
            TextView cancel = dialog.findViewById(R.id.cancel);

            tv_info_detail.setText(subCategoryBeanArrayList.get(position).getInfo_desc());
            category_naame.setText(subCategoryBeanArrayList.get(position).getCategory_name() + " info");

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();

                }
            });
            select_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SubCategoryActivity.this, SelectSubActivity.class);
                    intent.putExtra("id", subCategoryBeanArrayList.get(position).getId());
                    intent.putExtra("name", subCategoryBeanArrayList.get(position).getCategory_name());
                    intent.putExtra("cid", categoryId);
                    intent.putExtra("categoryName", categoryName);
                    startActivity(intent);
                    dialog.cancel();

                }
            });

            dialog.show();
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }

    }
}
