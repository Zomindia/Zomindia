package com.zomindianew.user.activity;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.zomindianew.R;
import com.zomindianew.bean.SearchViewBean;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.user.adapter.SearchViewAdapter;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    private ArrayList<SearchViewBean> searchBeanArrayList = new ArrayList<SearchViewBean>();
    SearchViewAdapter searchViewAdapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (SearchView) findViewById(R.id.searchView);
        recyclerView = (RecyclerView) findViewById(R.id.categoryRecycle_serach);

        if (Constants.isInternetOn(getApplicationContext())) {

        } else {
          final ViewGroup viewGroup = (ViewGroup) ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
            Constants.showSnackbar(getApplicationContext(), viewGroup, getResources().getString(R.string.no_internet), "Retry");
        }            getseasrchCategory();


        searchCity();


    }


    public void getseasrchCategory() {
        Api api = ApiFactory.getClient(getApplicationContext()).create(Api.class);
        Call<ResponseBody> call;

        call = api.getCategory(MySharedPreferances.getInstance(getApplicationContext()).getString(Constants.ACCESS_TOKEN));
        Log.e(Constants.LOG_CAT, "search data ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "search header   : " + call.request().headers());
//        Constants.showProgressDialog(getApplicationContext(), Constants.LOADING);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT search================>>>>>" + object.toString());

                            JSONArray jsonArray = object.optJSONArray("data");
//                            searchBeanArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SearchViewBean searchViewBean = new SearchViewBean();
                                searchViewBean.setId(jsonObject.optString("id"));
                                searchViewBean.setSvcategory_icon(jsonObject.optString("category_icon"));
                                searchViewBean.setSvcategory_name(jsonObject.optString("category_name"));
                                searchViewBean.setSvstatus(jsonObject.optString("status"));
                                searchViewBean.setSvcreated_at(jsonObject.optString("created_at"));
                                searchBeanArrayList.add(searchViewBean);
                            }
//                            setAdapter();

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            searchViewAdapter = new SearchViewAdapter(getApplicationContext(),searchBeanArrayList);
                            recyclerView.setAdapter(searchViewAdapter);

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, getApplicationContext());
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(getApplicationContext());
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), getApplicationContext());
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), getApplicationContext());
                    }
                } catch (JSONException e) {
//                    Constants.hideProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Constants.hideProgressDialog();
            }
        });


    }

//    public void setAdapter() {
//        searchViewAdapter = new SearchViewAdapter(getApplicationContext(), searchBeanArrayList);
//        recyclerView.setAdapter(searchViewAdapter);
//        searchViewAdapter.onItemClickMethod(
//
//                new SearchViewAdapter.ItemInterFace() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        switch (view.getId()) {
//                            case R.id.cardView:
//                                Intent intent_login = new Intent(getApplicationContext(), SubCategoryActivity.class);
//                                intent_login.putExtra("categoryName", searchBeanArrayList.get(position).getSvcategory_name());
//                                intent_login.putExtra("id", searchBeanArrayList.get(position).getId());
//                                startActivity(intent_login);
//                                break;
//                        }
//                    }
//                });
//
//    }
private void searchCity() {


    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {


            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onQueryTextChange(String newText) {

            ArrayList<SearchViewBean> result = new ArrayList<>();

            for (SearchViewBean city : searchBeanArrayList) {

                if (city.getSvcategory_name().toLowerCase().contains(newText) || city.getSvcategory_name().contains(newText)) {

                    result.add(city);
                }

                ((SearchViewAdapter) Objects.requireNonNull(recyclerView.getAdapter())).updates(result);

            }


            return false;
        }
    });


}

}




