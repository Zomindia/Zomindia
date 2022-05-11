package com.zomindianew.providernew.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.zomindianew.R;
import com.zomindianew.comman.activity.BaseActivity;
import com.zomindianew.comman.activity.UnderReviewActivty;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.adapter.UserSelectAdapter;

import com.zomindianew.user.activity.EditProfileUserActivity;
import com.zomindianew.user.activity.HomeActivityUser;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText firstNameTextView;
    private EditText lastNameText;
    private EditText phoneNumText;
    private EditText emailTextView;
    private EditText dobTextVIew;
    private EditText addressText;
    private EditText selectRoleEdit;
    private EditText dcoImageEdit;
    private ImageView userImageView;
    private ImageView editImageClickVIew;
    private TextView tv_register;
    private ImageView backIV;
    private String photoFileName = "";
    private File mFile;
    private File mDocFile;
    private Uri resultUri;
    private String photoPath = "";
    private String docPath = "";
    private String dateStr = "";
    private String selectImage = "";
    private EditText workExpEditText;
    private String firstNameStr;
    private String lastNameStr;
    private String emailStr;
    private String phoneStr;
    private String addressStr;
    private String workExpStr;
    private TextView addSkillsRL;
    String userRoleStr = "";
    private String profileImageStr;
    private String docImageStr;
    private String come_from;
    private Uri mImageUri;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
        setClicks();
    }

    private void initView() {
        come_from = getIntent().getStringExtra("COME_FROM");

        addSkillsRL = findViewById(R.id.addSkillsRL);
        workExpEditText = findViewById(R.id.workExpEditText);
        backIV = findViewById(R.id.backIV);

        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameText = findViewById(R.id.lastNameText);
        phoneNumText = findViewById(R.id.phoneNumText);
        emailTextView = findViewById(R.id.emailTextView);
        dobTextVIew = findViewById(R.id.dobTextVIew);
        addressText = findViewById(R.id.addressText);
        selectRoleEdit = findViewById(R.id.selectRoleEdit);
        dcoImageEdit = findViewById(R.id.dcoImageEdit);
        userImageView = findViewById(R.id.userImageView);
        editImageClickVIew = findViewById(R.id.editImageClickVIew);
        tv_register = findViewById(R.id.tv_register);
        if (getIntent().hasExtra("firstNameStr")) {
            dateStr = getIntent().getStringExtra("dobStr");
            userRoleStr = getIntent().getStringExtra("providerTypeStr");
            firstNameTextView.setText(getIntent().getStringExtra("firstNameStr"));
            lastNameText.setText(getIntent().getStringExtra("lastNameStr"));
            emailTextView.setText(getIntent().getStringExtra("emailStr"));
            phoneNumText.setText(getIntent().getStringExtra("phoneStr"));
            dobTextVIew.setText(getIntent().getStringExtra("dobStr"));
            workExpEditText.setText(getIntent().getStringExtra("workExpStr"));
            selectRoleEdit.setText(getIntent().getStringExtra("providerTypeStr"));
            addressText.setText(getIntent().getStringExtra("addressStr"));
            dcoImageEdit.setText(getIntent().getStringExtra("doc_proof_name"));
            profileImageStr = getIntent().getStringExtra("imageViewProfileStr");
            docImageStr = getIntent().getStringExtra("docImageStr");
            Glide.with(EditProfileActivity.this)
                    .load(getIntent().getStringExtra("imageViewProfileStr"))
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImageView);
        } else {
            firstNameTextView.setText(appPreference.getString(Constants.FIRST_NAME));
            lastNameText.setText(appPreference.getString(Constants.LAST_NAME));
            phoneNumText.setText(appPreference.getString(Constants.MOBILE));
        }


    }

    private void setClicks() {
        backIV.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        editImageClickVIew.setOnClickListener(this);

        dobTextVIew.setOnClickListener(this);
        selectRoleEdit.setOnClickListener(this);
        dcoImageEdit.setOnClickListener(this);
        addSkillsRL.setOnClickListener(this);
    }

    private void setValidation() {
        firstNameStr = firstNameTextView.getText().toString().trim();
        lastNameStr = lastNameText.getText().toString().trim();
        emailStr = emailTextView.getText().toString().trim();
        workExpStr = workExpEditText.getText().toString().trim();
/*
        phoneStr = phoneNumBer.getText().toString().trim();
*/
        addressStr = addressText.getText().toString().trim();
        /*if (photoPath.equals("") && profileImageStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_select_image), EditProfileActivity.this);
        } */
        if (firstNameStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.enter_first_name), EditProfileActivity.this);
        } else if (lastNameStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.enter_last_name), EditProfileActivity.this);
        } else if (emailStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_enter_email_address), EditProfileActivity.this);
        } else if (!Constants.isValidEmail(emailStr)) {
            Constants.showToastAlert(getResources().getString(R.string.enter_valid_email_id), EditProfileActivity.this);
        } else if (dateStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_select_dob), EditProfileActivity.this);
        } else if (addressStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_enter_address), EditProfileActivity.this);
        } else if (userRoleStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_enter_provider), EditProfileActivity.this);
        } else if (workExpStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_enter_work_exp), EditProfileActivity.this);
        } /*else if (docPath.equals("") && docImageStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.please_enter_doc), EditProfileActivity.this);
        } */ else {
            if (Constants.isInternetOn(this)) {
                updateProfileAPI();
            } else {
                final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) EditProfileActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
            }


        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                onBackPressed();
                break;

            case R.id.tv_register:
                setValidation();
                break;
            case R.id.editImageClickVIew:
                selectImage = "editImage";
                checkRequiredPermission(Constants.MEDIA_PERMISSION);
                break;
            case R.id.dcoImageEdit:
                selectImage = "docImage";
                checkRequiredPermission(Constants.MEDIA_PERMISSION);
                break;
            case R.id.dobTextVIew:
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditProfileActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(Calendar.getInstance());
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.selectRoleEdit:
                showUserDialog();
                break;
            case R.id.addSkillsRL:
                Intent intent = new Intent(EditProfileActivity.this, AddSkillActivity.class);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void setfile(File file) {
        this.mFile = file;
    }

    public File getFile() {
        return mFile;
    }

    public File getmDocFile() {
        return mDocFile;
    }

    public void setmDocFile(File mDocFile) {
        this.mDocFile = mDocFile;
    }

    public void pickImageFromGallery() {

        mGetContent.launch("image/*");

    }



    public Uri getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.LOG_CAT);
            File outputFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
            Uri outputFileUri = FileProvider.getUriForFile(
                    EditProfileActivity.this,
                    EditProfileActivity.this.getApplicationContext()
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
        final Dialog slidDialog = new Dialog(EditProfileActivity.this);
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


    /**
     * This  method is used to  Take photo from camera
     */

    private void pickImageFromCamera() {


        photoFileName = "";
        photoFileName = photoFileName + System.currentTimeMillis() + ".jpg";

        takeImageResult.launch(getPhotoFileUri(photoFileName));


    }

    /**
     * Get output media file uri.
     *
     * @param type the type
     * @return the output media file uri
     */
    public static String IMAGE_DIRECTORY_NAME = "MentorLocator";

    public static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", mImageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * This method is used to  create Image File
     */
    private String imageFilePath;

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST && resultCode == RESULT_OK) {
           /* Uri takenPhotoUri = getPhotoFileUri(photoFileName);
            CropImage.activity(takenPhotoUri).setFixAspectRatio(false).setAspectRatio(5, 5).start(this);
*/

            try {
                File temp = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    temp = new File(imageFilePath);
                } else {
                    temp = new File(mImageUri.getPath());
                }
                File _mFile = null;
                try {
                    _mFile = new Compressor(this).compressToFile(temp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                CropImage.activity(Uri.fromFile(_mFile)).setFixAspectRatio(false).setAspectRatio(5, 5).start(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    if (selectImage.equals("editImage")) {
                        mFile = new File(result.getUri().getPath());
                        setfile(mFile);
                        photoPath = result.getUri().getPath();
                        Glide.with(EditProfileActivity.this)
                                .load(photoPath)
                                .apply(RequestOptions.circleCropTransform())
                                .into(userImageView);
                    } else {
                        mDocFile = new File(result.getUri().getPath());
                        setmDocFile(mDocFile);
                        docPath = result.getUri().getPath();
                        dcoImageEdit.setText(mDocFile.getName());
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
            default:
                break;
        }
    }


    public void updateProfileAPI() {
        Api api = ApiFactory.getClient(EditProfileActivity.this).create(Api.class);
        Call<ResponseBody> call;
        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
        if (getFile() != null) {

            Uri selectedUri = Uri.fromFile(getFile());
            System.out.println("====selectedUri==" + selectedUri);

            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
            RequestBody reqBodyImageOfUser = RequestBody.create(MediaType.parse(mimeType), getFile());
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), getFile());
            map.put("profile\"; filename=\"" + getFile().getName() + "\"", reqBodyImageOfUser);

            Log.e(Constants.LOG_CAT, "providerEditProfile: " + map.toString());
        }
        if (getmDocFile() != null) {

            Uri selectedUri = Uri.fromFile(getFile());
            System.out.println("====selectedUri==" + selectedUri);
            Log.e("essss....","get msg here"+selectedUri);

            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
            RequestBody reqBodyImageOfUser = RequestBody.create(MediaType.parse(mimeType), getmDocFile());
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), getFile());
            map.put("doc_proof\"; filename=\"" + getmDocFile().getName() + "\"", reqBodyImageOfUser);

            Log.e(Constants.LOG_CAT, "providerEditProfile: " + map.toString());
        }
        RequestBody first_name = RequestBody.create(MediaType.parse("multipart/form-data"), firstNameStr);
        RequestBody last_name = RequestBody.create(MediaType.parse("multipart/form-data"), lastNameStr);
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emailStr);
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), addressStr);
        RequestBody userRole = RequestBody.create(MediaType.parse("multipart/form-data"), MySharedPreferances.getInstance(this).getString(Constants.USER_ROLE));
        RequestBody workExp = RequestBody.create(MediaType.parse("multipart/form-data"), workExpStr);
        RequestBody dobstr = RequestBody.create(MediaType.parse("multipart/form-data"), dateStr);
        RequestBody providerType;
        if (userRoleStr.equals("Zommer")) {
            providerType = RequestBody.create(MediaType.parse("multipart/form-data"), "zommer");
        } else {
            providerType = RequestBody.create(MediaType.parse("multipart/form-data"), "normal");
        }


        map.put("first_name", first_name);
        map.put("last_name", last_name);
        map.put("email", email);
        map.put("address", address);
        map.put("role", userRole);
        map.put("experience", workExp);
        map.put("dob", dobstr);
        map.put("provider_type", providerType);


        call = api.updateProfileAPI(MySharedPreferances.getInstance(EditProfileActivity.this).getString(Constants.ACCESS_TOKEN), map);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(EditProfileActivity.this, Constants.LOADING);
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


                            JSONObject jsonObject = object.optJSONObject("data");
                            MySharedPreferances.getInstance(EditProfileActivity.this).putString(Constants.PROFILE_COMPLETE, jsonObject.optString("profile_complete"));
                            MySharedPreferances.getInstance(EditProfileActivity.this).putString(Constants.PROFILE_APPORVED, jsonObject.optString("is_apporved"));
                            Constants.showToastAlert("Success", EditProfileActivity.this);
                            if (jsonObject.optString("profile_complete").equalsIgnoreCase("false")) {
                                Intent intent = new Intent(EditProfileActivity.this, AddSkillActivity.class);
                                startActivity(intent);
                            } else if (jsonObject.optString("is_apporved").equalsIgnoreCase("false")) {
                                Intent intent = new Intent(EditProfileActivity.this, UnderReviewActivty.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                if (come_from.equals("profile")) {
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    if (jsonObject.optString("role").equalsIgnoreCase("provider")) {
                                        Intent intent = new Intent(EditProfileActivity.this, HomeActivityProvider.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    } else {
                                        Intent intent = new Intent(EditProfileActivity.this, HomeActivityUser.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                }

                            }


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, EditProfileActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(EditProfileActivity.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), EditProfileActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), EditProfileActivity.this);
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


    //     show category dialog
    private void showUserDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_category_list, null);
            TextView headerDialogTV = view.findViewById(R.id.headerDialogTV);

            TextView doneTextView = view.findViewById(R.id.doneTextView);
            doneTextView.setVisibility(View.GONE);
            headerDialogTV.setText("partner Role");
            RecyclerView recyclerStateList = view.findViewById(R.id.recyclerStateList);
            final ArrayList<String> strings = new ArrayList<>();
            strings.add("Zommer");
            strings.add("Normal");

            final UserSelectAdapter userSelectAdapter = new UserSelectAdapter(EditProfileActivity.this, strings);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EditProfileActivity.this);
            recyclerStateList.setLayoutManager(linearLayoutManager);
            recyclerStateList.setAdapter(userSelectAdapter);

            builder.setView(view);
            //Creating dialog box
            final AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();

            userSelectAdapter.setOntemClickListener(new UserSelectAdapter.CustomItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    userRoleStr = strings.get(position);
                    selectRoleEdit.setText(userRoleStr);
                    userSelectAdapter.notifyDataSetChanged();
                    alert.dismiss();

                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dayOfMonth = Integer.parseInt("0" + dayOfMonth);
        }
        dateStr = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
        dobTextVIew.setText(Constants.getDateInFormat("yyyy-MM-dd", "MMM dd,yyyy", String.valueOf(dateStr)));

    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    CropImage.activity(uri).setFixAspectRatio(false).setAspectRatio(5, 5).start(EditProfileActivity.this);

                }
            });


    ActivityResultLauncher<Uri> takeImageResult = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result)
                {
                    Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                    CropImage.activity(takenPhotoUri).setFixAspectRatio(false).setAspectRatio(5, 5).start(EditProfileActivity.this);

                }
            });
}
