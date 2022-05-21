package com.zomindianew.comman.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zomindianew.R;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.HomeActivityProvider;
import com.zomindianew.providernew.fragment.HomeFragmentProvider;
import com.zomindianew.user.activity.HomeActivityUser;
import com.zomindianew.user.fragment.HomeFragment;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactAsFragment extends Fragment {

    private EditText bookingET;
    private EditText commentEditText;
    private TextView sendTV;
    public String bookingID = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_as, container, false);
        init(view);

        return view;
    }

    public void init(View view) {
        bookingET = view.findViewById(R.id.bookingET);
        commentEditText = view.findViewById(R.id.commentEditText);
        sendTV = view.findViewById(R.id.sendTV);
        bookingET.setText(bookingID);
        if(bookingID!=null)
            if(!bookingID.isEmpty())
                bookingET.setEnabled(false);
        sendTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValidation();
            }
        });
        view.findViewById(R.id.goToHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof HomeActivityUser){
                    ((HomeActivityUser) getActivity()).changeFragment(new HomeFragment(), "HomeFragment");
                }else if(getActivity() instanceof HomeActivityProvider){
                    ((HomeActivityProvider) getActivity()).changeFragment(new HomeFragmentProvider(), "HomeFragment");
                }
            }
        });
    }

    private void setValidation() {

        String bookingStr = bookingET.getText().toString().trim();
        String commentStr = commentEditText.getText().toString().trim();


        if (bookingStr.equals("")) {
            Constants.showToastAlert("Please enter booking ID", getActivity());

        } else if (commentStr.equals("")) {
            Constants.showToastAlert("Please enter comments", getActivity());
        } else {

            contactAPI(bookingStr, commentStr);

        }
    }

    public void contactAPI(String booking_id, String comments) {
        Api api = ApiFactory.getClient(getActivity()).create(Api.class);
        Call<ResponseBody> call;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("booking_id", booking_id);
        map.put("comments", comments);


        call = api.contactAPI(MySharedPreferances.getInstance(getActivity()).getString(Constants.ACCESS_TOKEN), map);
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
                            bookingET.setText("");
                            commentEditText.setText("");
                            Constants.showToastAlert("Contact send successfully!!", getActivity());
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
