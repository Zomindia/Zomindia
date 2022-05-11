package com.zomindianew.providernew.fragment;

import android.content.Intent;
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
import com.zomindianew.providernew.activity.BookingDetailActivity;


import com.zomindianew.providernew.adapter.PastAdapter;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PastFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<AppointmentBean> appointmentBeanArrayList = new ArrayList<>();
    private PastAdapter adapter;
    private TextView no_record_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past, container, false);
        init(view);
        if (Constants.isInternetOn(getActivity())) {
            getAppointment();
        } else {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            Constants.showSnackbar(getActivity(), viewGroup, getResources().getString(R.string.no_internet), "Retry");
        }

        return view;
    }

    public void init(View view) {
        no_record_txt = view.findViewById(R.id.no_record_txt);
        recyclerView = view.findViewById(R.id.onGoingRecyclerView);
        adapter = new PastAdapter(getActivity(), appointmentBeanArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.onItemClickMethod(new PastAdapter.ItemInterFace() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.onGoingRelative:
                        Intent intent = new Intent(getActivity(), BookingDetailActivity.class);
                        intent.putExtra("come_from", "past");
                        intent.putExtra("bookingID", appointmentBeanArrayList.get(position).getId());
                        startActivity(intent);
                        break;
                }
            }
        });
    }


    public void getAppointment() {
        Api api = ApiFactory.getClient(getActivity()).create(Api.class);
        Call<ResponseBody> call;

        call = api.getAppointment(MySharedPreferances.getInstance(getActivity()).getString(Constants.ACCESS_TOKEN), "past");
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
                                appointmentBean.setService_id(jsonObject.optString("service_id"));
                                appointmentBean.setName(jsonObject.optString("user_full_name"));
                                appointmentBean.setEmail(jsonObject.optString("email"));
                                appointmentBean.setTime(jsonObject.optString("time"));
                                appointmentBean.setDate(jsonObject.optString("date"));
                                appointmentBean.setAmount(jsonObject.optString("amount"));
                                appointmentBean.setQuantity(jsonObject.optString("quantity"));
                                appointmentBean.setStatus(jsonObject.optString("status"));
                                appointmentBean.setUser_profile(jsonObject.optString("user_profile"));
                                appointmentBean.setConfirm_code(jsonObject.optString("confirm_code"));
                                appointmentBean.setCreated_at(jsonObject.optString("created_at"));
                                appointmentBean.setAddress(jsonObject.optString("address"));
                                appointmentBeanArrayList.add(appointmentBean);
                            }

                            if (appointmentBeanArrayList.size() > 0) {
                                no_record_txt.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                            } else {
                                no_record_txt.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }

                            adapter.notifyDataSetChanged();
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