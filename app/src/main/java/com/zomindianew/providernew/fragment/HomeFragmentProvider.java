package com.zomindianew.providernew.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zomindianew.R;
import com.zomindianew.bean.AppointmentBean;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.adapter.BookingAdapter;
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


public class HomeFragmentProvider extends Fragment {
    private RecyclerView categoryRecycle;
    private BookingAdapter bookingAdapter;
    private ArrayList<AppointmentBean> appointmentBeanArrayList = new ArrayList<>();

    private TextView no_record_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        init(view);
        if (Constants.isInternetOn(getActivity())) {
            getAppointment();
        } else {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            Constants.showSnackbar(getActivity(), viewGroup, getResources().getString(R.string.no_internet), "Retry");
        }
        ((TextView)getActivity().findViewById(R.id.textView_header)).setText("Job Notification");
        return view;
    }

    public void init(View view) {
        no_record_txt = view.findViewById(R.id.no_record_txt);
        categoryRecycle = view.findViewById(R.id.categoryRecycle);
        bookingAdapter = new BookingAdapter(getActivity(), appointmentBeanArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        categoryRecycle.setLayoutManager(linearLayoutManager);
        categoryRecycle.setHasFixedSize(true);
        categoryRecycle.setItemAnimator(new DefaultItemAnimator());
        categoryRecycle.setAdapter(bookingAdapter);
        bookingAdapter.onItemClickMethod(new BookingAdapter.ItemInterFace() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.acceptButton:
                        if (Constants.isInternetOn(getActivity())) {
                            acceptAndDecline(position, "accept", appointmentBeanArrayList.get(position).getId());
                        } else {
                            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
                            Constants.showSnackbar(getActivity(), viewGroup, getResources().getString(R.string.no_internet), "Retry");
                        }

                        break;

                    case R.id.declineButton:
                        if (Constants.isInternetOn(getActivity())) {
                            acceptAndDecline(position, "decline", appointmentBeanArrayList.get(position).getId());
                        } else {
                            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
                            Constants.showSnackbar(getActivity(), viewGroup, getResources().getString(R.string.no_internet), "Retry");
                        }

                        break;

                }
            }
        });
    }

    public void acceptAndDecline(final int position, String status, String bookingID) {
        Api api = ApiFactory.getClient(getActivity()).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, Object> stringHashMap = new HashMap<>();
        stringHashMap.put("booking_id", bookingID);
        stringHashMap.put("status", status);


        call = api.acceptAndDecline(MySharedPreferances.getInstance(getActivity()).getString(Constants.ACCESS_TOKEN), stringHashMap);
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
                            appointmentBeanArrayList.remove(position);
                            bookingAdapter.notifyDataSetChanged();
                            if (appointmentBeanArrayList.size() > 0) {
                                no_record_txt.setVisibility(View.GONE);
                                categoryRecycle.setVisibility(View.VISIBLE);

                            } else {
                                no_record_txt.setVisibility(View.VISIBLE);
                                categoryRecycle.setVisibility(View.GONE);
                            }
                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, getActivity());
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(getActivity());
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), getActivity());
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), getActivity());
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

    public void getAppointment() {
        Api api = ApiFactory.getClient(getActivity()).create(Api.class);
        Call<ResponseBody> call;
        call = api.getAppointment(MySharedPreferances.getInstance(getActivity()).getString(Constants.ACCESS_TOKEN), "request");
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
                            appointmentBeanArrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                AppointmentBean appointmentBean = new AppointmentBean();
                                appointmentBean.setId(jsonObject.optString("id"));
                                appointmentBean.setReference_id(jsonObject.optString("reference_id"));
                                appointmentBean.setUser_id(jsonObject.optString("user_id"));
                                appointmentBean.setProvider_id(jsonObject.optString("provider_id"));
                                appointmentBean.setCategory_id(jsonObject.optString("category_id"));
                                appointmentBean.setSub_category_id(jsonObject.optString("sub_category_id"));
                                appointmentBean.setSubCategoryType(jsonObject.optString("sub_category_type"));
                                appointmentBean.setService_id(jsonObject.optString("service_id"));
                                appointmentBean.setName(jsonObject.optString("user_full_name"));
                                appointmentBean.setEmail(jsonObject.optString("email"));
                                appointmentBean.setTime(jsonObject.optString("time"));
                                appointmentBean.setDate(jsonObject.optString("date"));
                                appointmentBean.setCategory(jsonObject.optString("category"));
                                appointmentBean.setSubCategory(jsonObject.optString("sub_category"));

                                appointmentBean.setQuantity(jsonObject.optString("quantity"));
                                appointmentBean.setStatus(jsonObject.optString("status"));
                                appointmentBean.setConfirm_code(jsonObject.optString("confirm_code"));
                                appointmentBean.setCreated_at(jsonObject.optString("created_at"));
                                appointmentBean.setAddress(jsonObject.optString("address"));
                                appointmentBean.setUser_profile(jsonObject.optString("user_profile"));

                                appointmentBeanArrayList.add(appointmentBean);
                            }

                            if (appointmentBeanArrayList.size() > 0) {
                                no_record_txt.setVisibility(View.GONE);
                                categoryRecycle.setVisibility(View.VISIBLE);

                            } else {
                                no_record_txt.setVisibility(View.VISIBLE);
                                categoryRecycle.setVisibility(View.GONE);
                            }

                            bookingAdapter.notifyDataSetChanged();
                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, getActivity());
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(getActivity());
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), getActivity());
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), getActivity());
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