package com.zomindianew.providernew.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zomindianew.R;

import com.zomindianew.bean.AddSkillBean;
import com.zomindianew.bean.CategoryBean;
import com.zomindianew.bean.ServiceSubCategoryBean;
import com.zomindianew.bean.SubCategoryBean;
import com.zomindianew.comman.activity.BaseActivity;
import com.zomindianew.comman.activity.UnderReviewActivty;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.adapter.CategoryMultipleAdapter;
import com.zomindianew.providernew.adapter.SelectSkillsAdapter;
import com.zomindianew.providernew.adapter.SubCategoryMultipleAdapter;
import com.zomindianew.providernew.adapter.SubCategoryTypeMultipleAdapter;
import com.zomindianew.user.activity.HomeActivityUser;
import com.zomindianew.user.activity.SelectSubActivity;
import com.zomindianew.user.adapter.SelectSubAdapter;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AddSkillActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img_back_arrow;
    private ArrayList<CategoryBean> categoryBeanArrayList;
    private ArrayList<SubCategoryBean> subCategoryBeanArrayList;
    private ArrayList<ServiceSubCategoryBean> subCategoryTypeBeanArrayList;
    private TextView saveBT;
    private ArrayList<AddSkillBean> skillActivityArrayList = new ArrayList<>();
    private TextView categoryTextView;
    private TextView subcategoryText;
    //private TextView subcategoryTypeText;
    SelectSkillsAdapter subCategoryMultipleAdapter;
    private RecyclerView recycleView;

    private String categoryID;
    private String subCategoryID;
    private TextView addSkills;
    private String categoryStr;
    private String subCategoryStr;
    private String subCategoryTypeID;
    private String subCategoryTypeStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skill);
        initView();
        setClicks();
        if (Constants.isInternetOn(AddSkillActivity.this)) {
            profileAPI("");
            getCategory();
        } else {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) AddSkillActivity.this.findViewById(android.R.id.content)).getChildAt(0);
            Constants.showSnackbar(AddSkillActivity.this, viewGroup, getResources().getString(R.string.no_internet), "Retry");
        }
        subCategoryMultipleAdapter = new SelectSkillsAdapter(AddSkillActivity.this, skillActivityArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddSkillActivity.this);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.setAdapter(subCategoryMultipleAdapter);


    }

    private void setClicks() {
        img_back_arrow.setOnClickListener(this);
        categoryTextView.setOnClickListener(this);
        subcategoryText.setOnClickListener(this);
        //subcategoryTypeText.setOnClickListener(this);
        addSkills.setOnClickListener(this);
        saveBT.setOnClickListener(this);
    }

    private void initView() {
        addSkills = findViewById(R.id.addSkills);
        saveBT = findViewById(R.id.saveBT);
        recycleView = findViewById(R.id.recycleView);
        img_back_arrow = findViewById(R.id.img_back_arrow);
        categoryTextView = findViewById(R.id.categoryTextView);
        subcategoryText = findViewById(R.id.subcategoryText);
        //subcategoryTypeText = findViewById(R.id.subcategoryTypeText);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back_arrow:
                finish();
                break;
            case R.id.categoryTextView:
                showCategoryList();
                break;
            case R.id.subcategoryText:
                showSubCategoryList();
                break;
            case R.id.subcategoryTypeText:
                showSubCategoryTypeList();
                break;
            case R.id.addSkills:
                boolean checkSubCaregoryID = true;
                String category = categoryTextView.getText().toString();
                String subCategory = subcategoryText.getText().toString();
                //String subCategoryType = subcategoryTypeText.getText().toString();
                if (category.equals("")) {
                    Constants.showToastAlert(getResources().getString(R.string.please_enter_category), AddSkillActivity.this);
                } else if (subCategory.equals("")) {
                    Constants.showToastAlert(getResources().getString(R.string.please_enter_subCategory), AddSkillActivity.this);

                } /*else if (subCategoryType.equals("")) {
                    Constants.showToastAlert(getResources().getString(R.string.please_enter_subCategory_type), AddSkillActivity.this);

                } */else {
                    for (int i = 0; i < skillActivityArrayList.size(); i++) {
                        String categryID = skillActivityArrayList.get(i).getSubCAtegoryID();
                        if (subCategoryID.equals(categryID)) {
                            checkSubCaregoryID = false;
                            break;
                        }
                    }
                    if (checkSubCaregoryID) {
                        AddSkillBean addSkillBean = new AddSkillBean(subCategoryID, categoryID, categoryStr, subCategoryStr);
                        skillActivityArrayList.add(addSkillBean);
                        subCategoryMultipleAdapter.notifyDataSetChanged();

                        categoryTextView.setText("");
                        subcategoryText.setText("");
                        //subcategoryTypeText.setText("");
                    } else {
                        Constants.showToastAlert(getResources().getString(R.string.please_enter_subCategory), AddSkillActivity.this);
                    }


                }
                break;
            case R.id.saveBT:

                if (skillActivityArrayList.size() > 0) {
                    serviceProvider();
                } else {
                    Constants.showToastAlert("Please add category", AddSkillActivity.this);
                }

                break;

        }

    }


    public void getCategory() {
        Api api = ApiFactory.getClient(AddSkillActivity.this).create(Api.class);
        Call<ResponseBody> call;

        call = api.getCategory(MySharedPreferances.getInstance(AddSkillActivity.this).getString(Constants.ACCESS_TOKEN));
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(AddSkillActivity.this, Constants.LOADING);
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

                            JSONArray jsonArray = object.optJSONArray("data");
                            categoryBeanArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CategoryBean categoryBean = new CategoryBean();
                                categoryBean.setId(jsonObject.optString("id"));
                                categoryBean.setCategory_icon(jsonObject.optString("category_icon"));
                                categoryBean.setCategory_name(jsonObject.optString("category_name"));
                                categoryBean.setStatus(jsonObject.optString("status"));
                                categoryBean.setCreated_at(jsonObject.optString("created_at"));
                                categoryBeanArrayList.add(categoryBean);
                            }


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, AddSkillActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(AddSkillActivity.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), AddSkillActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), AddSkillActivity.this);
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

    public void getSubCategory(String id) {
        Api api = ApiFactory.getClient(AddSkillActivity.this).create(Api.class);
        Call<ResponseBody> call;
//        String str = subCategoryIds;

//        if (str.endsWith(",")) {
//            str = str.substring(0, str.length() - 1);
//        }
        call = api.getSubCategory(MySharedPreferances.getInstance(AddSkillActivity.this).getString(Constants.ACCESS_TOKEN), id);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(AddSkillActivity.this, Constants.LOADING);
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

                            JSONArray jsonArray = object.optJSONArray("data");

                            subCategoryBeanArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SubCategoryBean subCategoryBean = new SubCategoryBean();
                                subCategoryBean.setId(jsonObject.optString("id"));
                                subCategoryBean.setCategory_icon(jsonObject.optString("sub_category_icon"));
                                subCategoryBean.setCategory_name(jsonObject.optString("name"));
                                subCategoryBean.setCreated_at(jsonObject.optString("created_at"));
                                subCategoryBeanArrayList.add(subCategoryBean);
                            }


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, AddSkillActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(AddSkillActivity.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), AddSkillActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), AddSkillActivity.this);
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

    public void getServiceSubCategory(final String subId) {
        Api api = ApiFactory.getClient(AddSkillActivity.this).create(Api.class);
        Call<ResponseBody> call;

        call = api.getServiceSubCategory(MySharedPreferances.getInstance(AddSkillActivity.this).getString(Constants.ACCESS_TOKEN), subId);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT...secect sub.. ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS ...secect sub...: " + call.request().headers());
        Constants.showProgressDialog(AddSkillActivity.this, Constants.LOADING);
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
                                serviceSubCategoryBean.setCid(categoryID);
                                serviceSubCategoryBean.setCategoryname(categoryStr);
                                serviceSubCategoryBean.setSid(subCategoryID);
                                serviceSubCategoryBean.setsName(subCategoryStr);
                                subCategoryTypeBeanArrayList.add(serviceSubCategoryBean);
                            }
                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, AddSkillActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(AddSkillActivity.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), AddSkillActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), AddSkillActivity.this);
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

    //     show category dialog
    private void showCategoryList() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddSkillActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_category_list, null);
            TextView headerDialogTV = view.findViewById(R.id.headerDialogTV);

            TextView doneTextView = view.findViewById(R.id.doneTextView);
            doneTextView.setVisibility(View.GONE);
            headerDialogTV.setText("Category");
            RecyclerView recyclerStateList = view.findViewById(R.id.recyclerStateList);

            final CategoryMultipleAdapter categoryMultipleAdapter = new CategoryMultipleAdapter(AddSkillActivity.this, categoryBeanArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddSkillActivity.this);
            recyclerStateList.setLayoutManager(linearLayoutManager);
            recyclerStateList.setAdapter(categoryMultipleAdapter);

            builder.setView(view);
            //Creating dialog box
            final AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();

            categoryMultipleAdapter.setOntemClickListener(new CategoryMultipleAdapter.CustomItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    categoryID = categoryBeanArrayList.get(position).getId();
                    categoryStr = categoryBeanArrayList.get(position).getCategory_name();
                    categoryTextView.setText(categoryStr);
                    subcategoryText.setText("");
                    //subcategoryTypeText.setText("");
                    if (Constants.isInternetOn(AddSkillActivity.this)) {
                        getSubCategory(categoryBeanArrayList.get(position).getId());
                    } else {
                        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) AddSkillActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                        Constants.showSnackbar(AddSkillActivity.this, viewGroup, getResources().getString(R.string.no_internet), "Retry");
                    }
                    alert.dismiss();
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    //     show category dialog
    private void showSubCategoryList() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddSkillActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_category_list, null);
            TextView headerDialogTV = view.findViewById(R.id.headerDialogTV);

            TextView doneTextView = view.findViewById(R.id.doneTextView);
            doneTextView.setVisibility(View.GONE);
            headerDialogTV.setText("SubCategory");
            RecyclerView recyclerStateList = view.findViewById(R.id.recyclerStateList);

            final SubCategoryMultipleAdapter subCategoryMultipleAdapter = new SubCategoryMultipleAdapter(AddSkillActivity.this, subCategoryBeanArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddSkillActivity.this);
            recyclerStateList.setLayoutManager(linearLayoutManager);
            recyclerStateList.setAdapter(subCategoryMultipleAdapter);

            builder.setView(view);
            //Creating dialog box
            final AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();

            subCategoryMultipleAdapter.setOntemClickListener(new SubCategoryMultipleAdapter.CustomItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    subCategoryID = subCategoryBeanArrayList.get(position).getId();
                    subCategoryStr = subCategoryBeanArrayList.get(position).getCategory_name();
                    subcategoryText.setText(subCategoryStr);
                    //subcategoryTypeText.setText("");
                    //getServiceSubCategory(subCategoryID);
                    subCategoryMultipleAdapter.notifyDataSetChanged();
                    alert.dismiss();
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void showSubCategoryTypeList() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddSkillActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_category_list, null);
            TextView headerDialogTV = view.findViewById(R.id.headerDialogTV);

            TextView doneTextView = view.findViewById(R.id.doneTextView);
            doneTextView.setVisibility(View.GONE);
            headerDialogTV.setText("Sub Category Type");
            RecyclerView recyclerStateList = view.findViewById(R.id.recyclerStateList);

            final SubCategoryTypeMultipleAdapter subCategoryTypeMultipleAdapter = new SubCategoryTypeMultipleAdapter(AddSkillActivity.this, subCategoryTypeBeanArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddSkillActivity.this);
            recyclerStateList.setLayoutManager(linearLayoutManager);
            recyclerStateList.setAdapter(subCategoryTypeMultipleAdapter);

            builder.setView(view);
            //Creating dialog box
            final AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();

            subCategoryTypeMultipleAdapter.setOntemClickListener(new SubCategoryTypeMultipleAdapter.CustomItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    subCategoryTypeID = subCategoryBeanArrayList.get(position).getId();
                    subCategoryTypeStr = subCategoryBeanArrayList.get(position).getCategory_name();
                    //subcategoryTypeText.setText(subCategoryStr);
                    subCategoryTypeMultipleAdapter.notifyDataSetChanged();
                    alert.dismiss();
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void serviceProvider() {
        Api api = ApiFactory.getClient(AddSkillActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, Object> requestParam = new HashMap<>();
        ArrayList<HashMap<String, String>> paramArray = new ArrayList<>();
        for (int k = 0; k < skillActivityArrayList.size(); k++) {

            AddSkillBean addSkillBean = skillActivityArrayList.get(k);

            HashMap<String, String> paraInside = new HashMap<>();
            paraInside.put("category_id", addSkillBean.getCategoryID());
            paraInside.put("sub_category_id", addSkillBean.getSubCAtegoryID());

            paramArray.add(paraInside);
        }


        requestParam.put("category", paramArray);
        call = api.providerCategoryAPI(MySharedPreferances.getInstance(this).getString(Constants.ACCESS_TOKEN), requestParam);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(AddSkillActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        Log.d("tag", object.toString(1));

                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Constants.showToastAlert("Skills Added Successfully", AddSkillActivity.this);
                            finish();
                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, AddSkillActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(AddSkillActivity.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), AddSkillActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), AddSkillActivity.this);
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

    public void profileAPI(final String type) {
        Api api = ApiFactory.getClient(AddSkillActivity.this).create(Api.class);
        Call<ResponseBody> call;
        call = api.userDetail(MySharedPreferances.getInstance(AddSkillActivity.this).getString(Constants.ACCESS_TOKEN));
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(AddSkillActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        Log.d("tag", object.toString(1));

                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            JSONObject jsonObject = object.optJSONObject("data");
                            if (type.equals("")) {
                                JSONArray jsonArray = jsonObject.optJSONArray("categorys");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                    AddSkillBean addSkillBean = new AddSkillBean();
                                    addSkillBean.setCategory_name(jsonObject1.optString("category"));
                                    addSkillBean.setCategoryID(jsonObject1.optString("category_id"));
                                    addSkillBean.setSub_category_name(jsonObject1.optString("sub_category"));
                                    addSkillBean.setSubCAtegoryID(jsonObject1.optString("sub_category_id"));
                                    skillActivityArrayList.add(addSkillBean);
                                }
                                subCategoryMultipleAdapter.notifyDataSetChanged();
                                Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());

                            } else {
                                MySharedPreferances.getInstance(AddSkillActivity.this).putString(Constants.PROFILE_COMPLETE, jsonObject.optString("profile_complete"));
                                MySharedPreferances.getInstance(AddSkillActivity.this).putString(Constants.PROFILE_APPORVED, jsonObject.optString("is_apporved"));
                                if (jsonObject.optString("profile_complete").equalsIgnoreCase("false")) {
                                    Intent intent = new Intent(AddSkillActivity.this, EditProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (jsonObject.optString("is_apporved").equalsIgnoreCase("false")) {
                                    Intent intent = new Intent(AddSkillActivity.this, UnderReviewActivty.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (jsonObject.optString("role").equalsIgnoreCase("provider")) {
                                        Intent intent = new Intent(AddSkillActivity.this, HomeActivityProvider.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(AddSkillActivity.this, HomeActivityUser.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }


                            }


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, AddSkillActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(AddSkillActivity.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), AddSkillActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), AddSkillActivity.this);
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


}
