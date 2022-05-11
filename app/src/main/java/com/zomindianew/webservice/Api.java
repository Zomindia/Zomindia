package com.zomindianew.webservice;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;


public interface Api {

    @POST("loginApp")
    Call<ResponseBody> loginnew(@Body Map<String, String> fields);

    @POST("loginUserWithEmail")
    Call<ResponseBody> loginUserWithEmail(@Body Map<String, String> fields);

    @POST("loginUser")
    Call<ResponseBody> loginUser(@Body Map<String, String> fields);


    @POST("signupUser")
    Call<ResponseBody> signupUser(@Body Map<String, String> fields);
    @POST("signupApp")
    Call<ResponseBody> signUpnew(@Body Map<String, String> fields);

    @POST("forgetPassword")
    Call<ResponseBody> forget(@Body Map<String, String> fields);

    @POST("resetPassword")
    Call<ResponseBody> resetPassword(@Body Map<String, String> fields);

    @POST("signup")
    Call<ResponseBody> signUp(@Body Map<String, String> fields);

    @POST("signupProvider")
    Call<ResponseBody> signupProvider(@Body Map<String, String> fields);

    @POST("loginProvider")
    Call<ResponseBody> loginProvider(@Body Map<String, String> fields);

    @POST("verify")
    Call<ResponseBody> verify(@Body Map<String, String> fields);

    @POST("resend-code")
    Call<ResponseBody> resendOTP(@Body Map<String, String> fields);


    @GET("category")
    Call<ResponseBody> getCategory(@Header("access-token") String access_token);


    @GET("sub-category")
    Call<ResponseBody> getSubCategory(@Header("access-token") String access_token, @Query("category_id") String arrray);


    @GET("banners")
    Call<ResponseBody> getBanner(@Header("access-token") String access_token);


//    http://service-provider.zomindia.com/api/banners
    @POST("logout")
    Call<ResponseBody> logOutAPI(@Header("access-token") String access_token);

    @Multipart
    @POST("update-profile")
    Call<ResponseBody> updateProfileAPI(@Header("access-token") String accessToken, @PartMap Map<String, RequestBody> map);


    @GET("user-detail")
    Call<ResponseBody> userDetail(@Header("access-token") String access_token);

    @POST("provider-category")
    Call<ResponseBody> providerCategoryAPI(@Header("access-token") String access_token, @Body Map<String, Object> fields);

    @GET("sub-category-type")
    Call<ResponseBody> getServiceSubCategory(@Header("access-token") String access_token, @Query("sub_category_id") String arrray);

    @GET("serverDateTime")
    Call<ResponseBody> serverDateTime();


    @POST("booking")
    Call<ResponseBody> bookingApi(@Header("access-token") String access_token, @Body Map<String, Object> fields);

    @GET("booking-list")
    Call<ResponseBody> getAppointment(@Header("access-token") String access_token, @Query("type") String type);

    @POST("booking-accept-decline")
    Call<ResponseBody> acceptAndDecline(@Header("access-token") String access_token, @Body Map<String, Object> fields);

    @GET("booking-detail")
    Call<ResponseBody> getBookingDetail(@Header("access-token") String access_token, @Query("booking_id") String type);

    @POST("confirm-booking")
    Call<ResponseBody> confrimAPI(@Header("access-token") String access_token, @Body Map<String, String> fields);

    @POST("complete-booking")
    Call<ResponseBody> completeAPI(@Header("access-token") String access_token, @Body Map<String, String> fields);

    @POST("rating")
    Call<ResponseBody> rating(@Header("access-token") String access_token, @Body Map<String, String> fields);

    @POST("booking-cancel")
    Call<ResponseBody> cancel(@Header("access-token") String access_token, @Body Map<String, String> fields);


    @GET("notification")
    Call<ResponseBody> notificationList(@Header("access-token") String access_token);

    @POST("contact-us")
    Call<ResponseBody> contactAPI(@Header("access-token") String access_token, @Body Map<String, Object> fields);

    @GET("get-check-sum")
    Call<ResponseBody> checkSumAPI(@Header("access-token") String access_token, @Query("booking_id") String booking_id);


    @POST("transaction-save")
    Call<ResponseBody> transactionAPI(@Header("access-token") String access_token, @Body Map<String, Object> fields);

}
