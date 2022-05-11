package com.zomindianew.user.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zomindianew.R;
import com.zomindianew.comman.activity.BaseActivity;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileUserActivity extends BaseActivity implements View.OnClickListener {

    private ImageView editImageView;
    private EditText firstNameTextView;
    private EditText lastNameText;
    private EditText emailTextView;
    private EditText phoneNumBer;
    private EditText addressText;
    private TextView saveButton;
    private ImageView userImageView;


    private String photoFileName = "";
    private File mFile;
    private Uri capturedImageUri;
    private Uri resultUri;
    private String photoPath = "";

    private String firstNameStr;
    private String lastNameStr;
    private String emailStr;
    private String phoneStr;
    private String addressStr;
    private ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);
        intitView();
        setClick();
    }

    private void intitView() {
        backIV = findViewById(R.id.backIV);
        editImageView = findViewById(R.id.editImageView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameText = findViewById(R.id.lastNameText);
        emailTextView = findViewById(R.id.emailTextView);
        phoneNumBer = findViewById(R.id.phoneNumBer);
        addressText = findViewById(R.id.addressText);
        saveButton = findViewById(R.id.saveButton);

        userImageView = findViewById(R.id.userImageView);
        phoneNumBer.setText(getIntent().getStringExtra("phoneStr"));
        Log.e(Constants.LOG_CAT, "intitView: " + getIntent().getStringExtra("firstNameStr"));
        if (getIntent().getStringExtra("firstNameStr") != null && !getIntent().getStringExtra("firstNameStr").equalsIgnoreCase("") && !getIntent().getStringExtra("firstNameStr").equalsIgnoreCase("null")) {
            if (getIntent().getStringExtra("lastNameStr") != null && !getIntent().getStringExtra("lastNameStr").equalsIgnoreCase("") && !getIntent().getStringExtra("lastNameStr").equalsIgnoreCase("null")) {
                lastNameText.setText(getIntent().getStringExtra("lastNameStr"));
            }
            firstNameTextView.setText(getIntent().getStringExtra("firstNameStr"));


            emailTextView.setText(getIntent().getStringExtra("emailStr"));

            addressText.setText(getIntent().getStringExtra("addressStr"));

        }

        Glide.with(EditProfileUserActivity.this)
                .load(getIntent().getStringExtra("imageViewProfileStr"))
                .apply(RequestOptions.circleCropTransform())
                .into(userImageView);
    }

    private void setClick() {
        saveButton.setOnClickListener(this);
        backIV.setOnClickListener(this);
        editImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editImageView:
                checkRequiredPermission(Constants.MEDIA_PERMISSION);
                break;
            case R.id.saveButton:
                setValidation();
                break;
            case R.id.backIV:
                finish();
                break;
        }

    }

    public void setfile(File file) {
        this.mFile = file;
    }

    public File getFile() {
        return mFile;
    }

    public void pickImageFromGallery() {

        /*Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, Constants.PICK_IMAGE_REQUEST);
*/
        mGetContent.launch("image/*");

    }

    private void setValidation() {
        firstNameStr = firstNameTextView.getText().toString().trim();
        lastNameStr = lastNameText.getText().toString().trim();
        emailStr = emailTextView.getText().toString().trim();
/*
        phoneStr = phoneNumBer.getText().toString().trim();
*/
        addressStr = addressText.getText().toString().trim();
       /* if (photoPath.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_select_image), EditProfileUserActivity.this);
        } else */
        if (firstNameStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.enter_first_name), EditProfileUserActivity.this);
        } else if (lastNameStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.enter_last_name), EditProfileUserActivity.this);
        } else if (emailStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_enter_email_address), EditProfileUserActivity.this);
        } else if (!Constants.isValidEmail(emailStr)) {
            Constants.showToastAlert(getResources().getString(R.string.enter_valid_email_id), EditProfileUserActivity.this);
        } else if (addressStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_enter_address), EditProfileUserActivity.this);
        } else {

            if (Constants.isInternetOn(this)) {
                updateProfileAPI();
            } else {
                final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) EditProfileUserActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
            }


        }

    }

    public void updateProfileAPI() {

        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
        RequestBody first_name = RequestBody.create(MediaType.parse("multipart/form-data"), firstNameStr);
        RequestBody last_name = RequestBody.create(MediaType.parse("multipart/form-data"), lastNameStr);
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emailStr);
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), addressStr);
        RequestBody userRole = RequestBody.create(MediaType.parse("multipart/form-data"), MySharedPreferances.getInstance(this).getString(Constants.USER_ROLE));
        map.put("first_name", first_name);
        map.put("last_name", last_name);
        map.put("email", email);
        map.put("address", address);
        map.put("role", userRole);
        if (getFile() != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), getFile());
            map.put("profile\"; filename=\"" + getFile().getName() + "\"", requestBody);
            Log.e(Constants.LOG_CAT, "API SIGN UP: image============>>>>>>>>>>>>" + map.toString());
        }
        Api api = ApiFactory.getClient(EditProfileUserActivity.this).create(Api.class);
        Call<ResponseBody> call;
        call = api.updateProfileAPI(MySharedPreferances.getInstance(EditProfileUserActivity.this).getString(Constants.ACCESS_TOKEN), map);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(EditProfileUserActivity.this, Constants.LOADING);
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

                            finish();
                            Constants.showToastAlert("Success", EditProfileUserActivity.this);


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, EditProfileUserActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(EditProfileUserActivity.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), EditProfileUserActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), EditProfileUserActivity.this);
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

    public void pickImageFromCamera() {

        try {


            photoFileName = "";
            photoFileName = photoFileName + System.currentTimeMillis() + ".jpg";

            takeImageResult.launch(getPhotoFileUri(photoFileName));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.LOG_CAT);
            File outputFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
            Uri outputFileUri = FileProvider.getUriForFile(
                    EditProfileUserActivity.this,
                    EditProfileUserActivity.this.getApplicationContext()
                            .getPackageName() + ".provider", outputFile);
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.e(Constants.LOG_CAT, "failed to create directory");
            }
            return outputFileUri;
        }
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public void pickImageDialog() {
        final Dialog slidDialog = new Dialog(EditProfileUserActivity.this);
        slidDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        slidDialog.setCancelable(false);
        slidDialog.setCanceledOnTouchOutside(false);
        slidDialog.setContentView(R.layout.dialog_edit_pic);
        slidDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        slidDialog.getWindow().getAttributes().windowAnimations = R.style.SmileWindow;
        RelativeLayout takePhotoRelative = slidDialog.findViewById(R.id.takePhotoRelative);
        RelativeLayout photoRelativeLayout = slidDialog.findViewById(R.id.photoRelativeLayout);
        TextView btnCancel = slidDialog.findViewById(R.id.btn_cancel);
        takePhotoRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromCamera();
                slidDialog.dismiss();
            }
        });
        photoRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
                slidDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidDialog.dismiss();
            }
        });
        slidDialog.show();
    }

    @Override
    public void invokedWhenNoOrAllreadyPermissionGranted() {
        super.invokedWhenNoOrAllreadyPermissionGranted();
        pickImageDialog();
    }

    @Override
    public void invokedWhenPermissionGranted() {
        super.invokedWhenPermissionGranted();
        pickImageDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri takenPhotoUri = getPhotoFileUri(photoFileName);
            CropImage.activity(takenPhotoUri).setFixAspectRatio(false).setAspectRatio(5, 5).start(this);

        } else if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            CropImage.activity(selectedImage).setFixAspectRatio(false).setAspectRatio(5, 5).start(this);
        }
        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();
                    mFile = new File(resultUri.getPath());
                    setfile(mFile);
                    photoPath = resultUri.getPath();

                    Glide.with(EditProfileUserActivity.this)
                            .load(photoPath)
                            .apply(RequestOptions.circleCropTransform())
                            .into(userImageView);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
            default:
                break;
        }
    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    CropImage.activity(uri).setFixAspectRatio(false).setAspectRatio(5, 5).start(EditProfileUserActivity.this);

                }
            });


    ActivityResultLauncher<Uri> takeImageResult = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result)
                {
                    Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                    CropImage.activity(takenPhotoUri).setFixAspectRatio(false).setAspectRatio(5, 5).start(EditProfileUserActivity.this);

                }
            });

}
