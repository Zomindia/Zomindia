package com.zomindianew.comman.fragment;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zomindianew.R;
import com.zomindianew.bean.NotificationBean;
import com.zomindianew.comman.adapter.NotificationAdapter;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.BookingDetailActivity;
import com.zomindianew.user.activity.HomeActivityUser;
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


public class NotificationFragment extends Fragment {
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    LinearLayoutManager linearLayoutManager;
    public ArrayList<NotificationBean> notificationList;
    private TextView no_record_txt;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, currentPage = 0, pageCount = 0;
    private boolean loading = true;
    private boolean isFirstTimeLoading = false;
    private RelativeLayout loadMoreData;
    String booking_id;
    String fromImage;
     Dialog dialog;
     Dialog dialog1;
    EditText edttv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        init(view);

        return view;
    }

    public void init(View view) {
        notificationList = new ArrayList<>();
        loadMoreData = view.findViewById(R.id.loadMoreData);
        no_record_txt = view.findViewById(R.id.no_record_txt);
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        notificationRecyclerView.setLayoutManager(linearLayoutManager);
        notificationRecyclerView.setHasFixedSize(true);
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (Constants.isInternetOn(getActivity())) {
            isFirstTimeLoading = true;
            notificationAPI();
        } else {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            Constants.showSnackbar(getActivity(), viewGroup, getResources().getString(R.string.no_internet), "Retry");

        }
        setScroll();
    }


    private void setScroll() {

        notificationRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {

                    if (Constants.isInternetOn(getActivity())) {
                        visibleItemCount = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && currentPage <= pageCount) {
                                loading = false;
                                isFirstTimeLoading = false;
                                Log.e(Constants.LOG_CAT, "Pagination: currentPage " + currentPage + " \t pageCount" + pageCount);
                                notificationAPI();
                                showLoader();
                            }
                        }

                    } else {
                        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
                        Constants.showSnackbar(getActivity(), viewGroup, getResources().getString(R.string.no_internet), "Retry");

                    }
                }
            }
        });
    }


    public void setList() {
        if (notificationAdapter == null) {
            notificationAdapter = new NotificationAdapter(getContext(), notificationList);
            notificationRecyclerView.setAdapter(notificationAdapter);
        } else {
            notificationAdapter.notifyDataSetChanged();
        }
        notificationAdapter.onItemClickMethod(new NotificationAdapter.ItemInterFace() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_inner_lay:
                        if (notificationList.get(position).getType().equals("request_booked")) {
                            showCancelDialog(position);
                        }
                        break;
                }
            }
        });

    }

    public void cancelAPI(String booking_id) {
        Api api = ApiFactory.getClient(getActivity()).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("booking_id", booking_id);
        stringHashMap.put("status", edttv.getText().toString().trim());
        call = api.cancel(MySharedPreferances.getInstance(getActivity()).getString(Constants.ACCESS_TOKEN), stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT -cancel------------------>>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Log.e(Constants.LOG_CAT, "dataaa : " + stringHashMap.put("status", edttv.getText().toString().trim()));
        Constants.showProgressDialog(getActivity(), Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        Log.d("tag", object.toString(1));

                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());

                            Intent intent = new Intent(getActivity(), HomeActivityUser.class);
                            startActivity(intent);
                            getActivity().finishAffinity();
                            Toast.makeText(getActivity(), "Your request has been canceled !!", Toast.LENGTH_SHORT).show();
                            dialog1.dismiss();


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


    private void showCancelDialog(final int position) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_cancel);
        TextView viewtv = dialog.findViewById(R.id.view);
        TextView reschduletv = dialog.findViewById(R.id.reschdule);
        TextView needhelptv = dialog.findViewById(R.id.needhelp);
        TextView cancelorder = dialog.findViewById(R.id.cancelorder);
        LinearLayout layout = dialog.findViewById(R.id.fragment);
//        TextView layout = dialog.findViewById(R.id.fragment);
//        textViewYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (Constants.isInternetOn(getActivity())) {
//                    cancelAPI("" + notificationList.get(position).getBooking_id());
//
//                } else {
//                    final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
//                    Constants.showSnackbar(getActivity(), viewGroup, getResources().getString(R.string.no_internet), "Retry");
//
//                }
//
//
//            }
//        });
        viewtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BookingDetailActivity.class);
                intent.putExtra("come_from", "upcoming");
                intent.putExtra("bookingID",  notificationList.get(position).getBooking_id());
                startActivity(intent);


            }
        });
        reschduletv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        needhelptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof HomeActivityUser ){
                    ContactAsFragment contactAsFragment = new ContactAsFragment();
                    contactAsFragment.bookingID = notificationList.get(position).getBooking_id();
                    ((HomeActivityUser)getActivity()).changeFragment(contactAsFragment, "itemContactAs");
                    ((HomeActivityUser)getActivity()).setContactHeader();
                    dialog.cancel();
                }else {
                    FaqFragment faqFragment = new FaqFragment();
                    FragmentManager fragmentManager = getChildFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, faqFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }


            }
        });
        cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog(position);
            }
        });
        dialog.show();

    }




    private void openDialog(final int position) {
         dialog1 = new Dialog(getActivity()); // Context, this, etc.
        dialog1.setContentView(R.layout.resons_layout);
//        dialog.setTitle(R.string.dialog_title);

          edttv = dialog1.findViewById(R.id.edt_resonsdata);
        final TextView buttonsub = dialog1.findViewById(R.id.submitresons);
        buttonsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edttv.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getActivity(),"Write Your Reasons",Toast.LENGTH_SHORT).show();

                }
                else
                {
                                        cancelAPI("" + notificationList.get(position).getBooking_id());

                }

            }
        });
        dialog1.show();




    }

    public void showLoader() {
        loadMoreData.setVisibility(View.VISIBLE);
    }

    public void hideLoader() {
        loadMoreData.setVisibility(View.GONE);
    }


    public void notificationAPI() {
        Api api = ApiFactory.getClient(getActivity()).create(Api.class);
        Call<ResponseBody> call;
        call = api.notificationList(MySharedPreferances.getInstance(getActivity()).getString(Constants.ACCESS_TOKEN));
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());

        if (isFirstTimeLoading) {
            hideLoader();
            Constants.showProgressDialog(getActivity(), Constants.LOADING);
        } else {
            Constants.hideProgressDialog();
        }
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
                            JSONObject dataObject = object.optJSONObject("data");
                            JSONArray jsonArray = dataObject.optJSONArray("data");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                notificationRecyclerView.setVisibility(View.VISIBLE);
                                no_record_txt.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                                    String id = jsonObject.optString("id");
                                    String to_id = jsonObject.optString("to_id");
                                    String from_id = jsonObject.optString("from_id");
                                    String type = jsonObject.optString("type");
                                     booking_id = jsonObject.optString("booking_id");

                                    String created_at = jsonObject.optString("created_at");
                                    String fromName = jsonObject.optString("from_user_first_name");
                                    String is_read = jsonObject.optString("read_status");
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("notification_data");

                                    String message = jsonObject1.optString("message");
                                    String category = jsonObject1.optString("category");
                                    String sub_category = jsonObject1.optString("sub_category");
                                    if (jsonObject.optString("type").equals("booking_cancel") || jsonObject.optString("type").equals("request_booked") || jsonObject.optString("type").equals("account_approved")) {
                                        fromImage = jsonObject.optString("from_user_image");
                                    } else {
                                        fromImage = jsonObject1.optString("user_profiel");
                                    }


                                    NotificationBean notificationBean = new NotificationBean(id, to_id, from_id, booking_id, type, message, is_read, created_at, fromImage, fromName, category, sub_category);
                                    notificationList.add(notificationBean);


                                }
                            } else {
                                no_record_txt.setVisibility(View.VISIBLE);
                                notificationRecyclerView.setVisibility(View.GONE);
                            }

                            setList();

                            currentPage = dataObject.optInt("from");
                            pageCount = dataObject.getInt("last_page");
                            currentPage = currentPage + 1;
                            loading = true;
                            hideLoader();


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
