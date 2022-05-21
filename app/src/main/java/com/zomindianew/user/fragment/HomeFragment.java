package com.zomindianew.user.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zomindianew.R;
import com.zomindianew.bean.CategoryBean;
import com.zomindianew.bean.SliderModel;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.user.activity.SubCategoryActivity;
import com.zomindianew.user.adapter.CategoryAdapter;
import com.zomindianew.user.adapter.SlidingImage_Adapter;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private RecyclerView categoryRecycle;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryBean> categoryBeanArrayList;

    SlidingImage_Adapter slidingImage_adapter;
    private ViewPager viewPager;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private ArrayList<SliderModel> ImagesArray = new ArrayList<SliderModel>();
//    CirclePageIndicator circlePageIndicator;
    String getmobile,getmobile1;

    SharedPreferences sharedpreferences;
    String get_email,get_mobile,get_uname,get_id,get_token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
//        Circ indicator = (CirclePageIndicator)view.findViewById(R.id.indicator);
        ((TextView)getActivity().findViewById(R.id.textView_header)).setText("Home");

//        getnew();

        if (Constants.isInternetOn(getActivity())) {

//            getnew();
            getCategory();
        } else {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            Constants.showSnackbar(getActivity(), viewGroup, getResources().getString(R.string.no_internet), "Retry");
        }

        getBanner();
        categoryRecycle = view.findViewById(R.id.categoryRecycle);
        categoryRecycle.setItemAnimator(new DefaultItemAnimator());
        categoryRecycle.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        sharedpreferences = getActivity().getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        get_email= sharedpreferences.getString("email", "");
        get_mobile= sharedpreferences.getString("mobile", "");
        get_uname= sharedpreferences.getString("uname", "");
        get_id= sharedpreferences.getString("id", "");
        get_token= sharedpreferences.getString("accesstoken", "");

        return view;
    }

//    public void setAdapter() {
//        categoryAdapter = new CategoryAdapter(getActivity(), categoryBeanArrayList);
//        categoryRecycle.setAdapter(categoryAdapter);
//        categoryAdapter.onItemClickMethod(new CategoryAdapter.ItemInterFace() {
//            @Override
//            public void onItemClick(View view, int position) {
//                switch (view.getId()) {
//                    case R.id.cardView:
//                        Intent intent_login = new Intent(getActivity(), SubCategoryActivity.class);
//                        intent_login.putExtra("categoryName", categoryBeanArrayList.get(position).getCategory_name());
//                        intent_login.putExtra("id", categoryBeanArrayList.get(position).getId());
//                        startActivity(intent_login);
//                        break;
//                }
//            }
//        });
//
//    }


    public void getCategory() {
        Api api = ApiFactory.getClient(getActivity()).create(Api.class);
        Call<ResponseBody> call;

        call = api.getCategory(MySharedPreferances.getInstance(getActivity()).getString(Constants.ACCESS_TOKEN));
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(getActivity(), Constants.LOADING);

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
                            setAdapter();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, getActivity());
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(getActivity());
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), getActivity());
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), getActivity());
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

    private void init() {
//        for(int i=0;i<IMAGES.length;i++)
//            ImagesArray.add(IMAGES[i]);
//        mPager.setAdapter(new SlidingImage_Adapter(getContext(),ImagesArray));
////        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
//        indicator.setRadius(5 * density);

//        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 8000, 8000);

        // Pager listener over indicator
//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                currentPage = position;
//
//            }
//
//            @Override
//            public void onPageScrolled(int pos, float arg1, int arg2) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int pos) {
//
//            }
//        });

    }


//public  void getnew() {
//
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        String url = "http://service-provider.zomindia.com/api/category";
//        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
//                new com.android.volley.Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("category responce","response"+response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            if (jsonObject.getString("success").equals("true")) {
//
//
//                                JSONArray jsonArray = jsonObject.optJSONArray("data");
//
//                            categoryBeanArrayList = new ArrayList<>();
//                                Log.e("jsonArray offerObject","response"+jsonArray);
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    CategoryBean categoryBean = new CategoryBean();
//                                    categoryBean.setId(jsonObject1.optString("id"));
//                                    categoryBean.setCategory_icon(jsonObject1.optString("category_icon"));
//                                    categoryBean.setCategory_name(jsonObject1.optString("category_name"));
//                                    categoryBean.setStatus(jsonObject1.optString("status"));
//                                    categoryBean.setCreated_at(jsonObject1.optString("created_at"));
//                                    categoryBeanArrayList.add(categoryBean);
//                                }
//
//                                setAdapter();
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new com.android.volley.Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//                        Log.d("ERROR","error => "+error.toString());
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("access-token",get_token );
//
//                return params;
//            }
//        };
//        queue.add(getRequest);
//
//}

    public void setAdapter() {
        //categoryBeanArrayList.addAll(categoryBeanArrayList);
        categoryAdapter = new CategoryAdapter(getActivity(), categoryBeanArrayList);
        categoryRecycle.setAdapter(categoryAdapter);
        categoryAdapter.onItemClickMethod(

                new CategoryAdapter.ItemInterFace() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.cardView:
                        Intent intent_login = new Intent(getActivity(), SubCategoryActivity.class);
                        intent_login.putExtra("categoryName", categoryBeanArrayList.get(position).getCategory_name());
                        intent_login.putExtra("id", categoryBeanArrayList.get(position).getId());
                        startActivity(intent_login);
                        break;
                }
            }
        });

    }





    public void getBanner() {
        Api api = ApiFactory.getClient(getActivity()).create(Api.class);
        Call<ResponseBody> call;

        call = api.getBanner(MySharedPreferances.getInstance(getActivity()).getString(Constants.ACCESS_TOKEN));
        Log.e(Constants.LOG_CAT, "API REQUEST LOG banner ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS banner: " + call.request().headers());
        Constants.showProgressDialog(getActivity(), Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response===banner=============>>>>>" + object.toString());

                            JSONArray jsonArray = object.optJSONArray("data");
                            ImagesArray = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SliderModel sliderModel = new SliderModel();
                                sliderModel.setSlider_id(jsonObject.optString("id"));
                                sliderModel.setSlider_img(jsonObject.optString("banner_image"));
                                sliderModel.setCategory_name(jsonObject.optString("category_name"));
//                                categoryBean.setStatus(jsonObject.optString("status"));
//                                categoryBean.setCreated_at(jsonObject.optString("created_at"));
                                ImagesArray.add(sliderModel);
                            }

                            NUM_PAGES = jsonArray.length();

                            viewPager.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));
                            init();


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, getActivity());
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(getActivity());
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), getActivity());
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), getActivity());
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

