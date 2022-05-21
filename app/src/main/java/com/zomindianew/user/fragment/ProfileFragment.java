package com.zomindianew.user.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zomindianew.R;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.user.activity.EditProfileUserActivity;
import com.zomindianew.user.activity.HomeActivityUser;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    HomeActivityUser homeActivityUser;


    private ImageView imageViewProfile;
    private TextView userName;
    private TextView emailTextView;
    private TextView phoneNumBer;
    private TextView addressText;

    private String firstNameStr;
    private String lastNameStr;
    private String emailStr;
    private String phoneStr;
    private String addressStr;
    private String imageViewProfileStr;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        homeActivityUser = (HomeActivityUser) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        initView(view);

       /* if (Constants.isInternetOn(getActivity())) {
            profileAPI();
        } else {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            homeActivityUser.showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
        }*/

        homeActivityUser.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileUserActivity.class);
                intent.putExtra("firstNameStr", firstNameStr);
                intent.putExtra("lastNameStr", lastNameStr);
                intent.putExtra("emailStr", emailStr);
                intent.putExtra("phoneStr", phoneStr);
                intent.putExtra("addressStr", addressStr);
                intent.putExtra("imageViewProfileStr", imageViewProfileStr);
                startActivity(intent);
            }
        });

        return view;
    }


    private void initView(View view) {

        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        userName = view.findViewById(R.id.userName);
        emailTextView = view.findViewById(R.id.emailTextView);
        phoneNumBer = view.findViewById(R.id.phoneNumBer);
        addressText = view.findViewById(R.id.addressText);
    }


    @Override
    public void onClick(View view) {

    }

    public void profileAPI() {
        Api api = ApiFactory.getClient(getActivity()).create(Api.class);
        Call<ResponseBody> call;


        call = api.userDetail(MySharedPreferances.getInstance(getActivity()).getString(Constants.ACCESS_TOKEN));
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
                        Log.d("tag", object.toString(1));

                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            JSONObject jsonObject = object.optJSONObject("data");

                            firstNameStr = jsonObject.optString("first_name");
                            lastNameStr = jsonObject.optString("last_name");
                            phoneStr = jsonObject.optString("mobile");
                            imageViewProfileStr = jsonObject.optString("profile");
                            addressStr = jsonObject.optString("address");
                            emailStr = jsonObject.optString("email");
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.EMAIL, emailStr);
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.FIRST_NAME, firstNameStr);
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.LAST_NAME, lastNameStr);
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.MOBILE, phoneStr);
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.PROFILE_IMAGE, imageViewProfileStr);
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.USER_PROFILE_COMPLETE, jsonObject.optString("user_profile_complete"));
                            userName.setText(Constants.ifNullReplace(firstNameStr) + " " + Constants.ifNullReplace(lastNameStr));
                            emailTextView.setText(Constants.ifNullReplace(emailStr));
                            phoneNumBer.setText(Constants.ifNullReplace(phoneStr));
                            addressText.setText(Constants.ifNullReplace(addressStr));


                            Glide.with(getActivity())
                                    .load(imageViewProfileStr)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(imageViewProfile);


                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());


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





    @Override
    public void onResume() {
        super.onResume();

        if (Constants.isInternetOn(getActivity())) {
            profileAPI();
        }
        else {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            homeActivityUser.showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
        }
    }
}
