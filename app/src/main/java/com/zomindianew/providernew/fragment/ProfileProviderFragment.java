package com.zomindianew.providernew.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zomindianew.R;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.EditProfileActivity;
import com.zomindianew.providernew.activity.HomeActivityProvider;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileProviderFragment extends Fragment {
    HomeActivityProvider homeActivityProvider;


    private ImageView userImageView;
    private TextView userNameText;
    private TextView categoryTextView;
    private TextView userStatus;
    private RatingBar userRatingBar;
    private TextView emailTextView;
    private TextView phoneNumText;
    private TextView dobTextVIew;
    private TextView addressText;
    private String doc_proof_name;
    private TextView userRoleText;
    private TextView workExpText;


    private String firstNameStr;
    private String lastNameStr;
    private String emailStr;
    private String phoneStr;
    private String addressStr;
    private String imageViewProfileStr;

    private String categoryStr;
    private String average_rating;
    private String workExpStr;
    private String dobStr;
    private String userAvaStr;
    private String providerTypeStr;
    private String docImageStr;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        homeActivityProvider = (HomeActivityProvider) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_provider, container, false);
        initView(view);
        homeActivityProvider.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("COME_FROM", "profile");
                intent.putExtra("firstNameStr", firstNameStr);
                intent.putExtra("lastNameStr", lastNameStr);
                intent.putExtra("emailStr", emailStr);
                intent.putExtra("phoneStr", phoneStr);
                intent.putExtra("dobStr", dobStr);
                intent.putExtra("workExpStr", workExpStr);
                intent.putExtra("providerTypeStr", providerTypeStr);
                intent.putExtra("addressStr", addressStr);
                intent.putExtra("imageViewProfileStr", imageViewProfileStr);
                intent.putExtra("docImageStr", docImageStr);
                intent.putExtra("doc_proof_name", doc_proof_name);
                startActivity(intent);

            }
        });


        return view;
    }

    private void initView(View view) {
        userImageView = view.findViewById(R.id.userImageView);
        userNameText = view.findViewById(R.id.userNameText);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        userStatus = view.findViewById(R.id.userStatus);
        userRatingBar = view.findViewById(R.id.userRatingBar);
        emailTextView = view.findViewById(R.id.emailTextView);
        phoneNumText = view.findViewById(R.id.phoneNumText);
        dobTextVIew = view.findViewById(R.id.dobTextVIew);
        addressText = view.findViewById(R.id.addressText);

        userRoleText = view.findViewById(R.id.userRoleText);
        workExpText = view.findViewById(R.id.workExpText);

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
                            emailStr = jsonObject.optString("email");
                            imageViewProfileStr = jsonObject.optString("profile");
                            addressStr = jsonObject.optString("address");
                            userAvaStr = jsonObject.optString("status");
                            average_rating = jsonObject.optString("average_rating");
                            userRatingBar.setRating(Float.parseFloat(average_rating));
                            docImageStr = jsonObject.optString("doc_proof");
                            doc_proof_name = jsonObject.optString("doc_proof_name");

                            dobStr = jsonObject.optString("dob");
                            workExpStr = jsonObject.optString("experience");
                            providerTypeStr = jsonObject.optString("provider_type");
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.FIRST_NAME, firstNameStr);
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.LAST_NAME, lastNameStr);
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.MOBILE, phoneStr);
                            MySharedPreferances.getInstance(getActivity()).putString(Constants.PROFILE_IMAGE, imageViewProfileStr);

                            userNameText.setText(Constants.ifNullReplace(firstNameStr) + " " + Constants.ifNullReplace(lastNameStr));
                            emailTextView.setText(Constants.ifNullReplace(emailStr));
                            phoneNumText.setText(Constants.ifNullReplace(phoneStr));
                            addressText.setText(Constants.ifNullReplace(addressStr));
                            userStatus.setText(Constants.ifNullReplace(userAvaStr));
                            dobTextVIew.setText(Constants.ifNullReplace(dobStr));
                            userRoleText.setText(Constants.ifNullReplace(providerTypeStr));
                            workExpText.setText(Constants.ifNullReplace(workExpStr) + " Year");
                            addressText.setText(Constants.ifNullReplace(addressStr));
                            try {
                                Glide.with(getActivity())
                                        .load(imageViewProfileStr)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(userImageView);

                            }catch (Exception e){e.printStackTrace();}
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());


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

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isInternetOn(getActivity())) {
            profileAPI();
        } else {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            Constants.showSnackbar(getActivity(), viewGroup, getResources().getString(R.string.no_internet), "Retry");
        }
    }
}
