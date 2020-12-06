package com.sibk.tasik.StartActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sibk.tasik.Api.UserApi;
import com.sibk.tasik.Api.VersionManagementApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.MainActivityGuru.MainActivityGuru;
import com.sibk.tasik.Model.User;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Response;

import static maes.tech.intentanim.CustomIntent.customType;

public class LoginActivity extends AppCompatActivity implements Animation.AnimationListener {

    private TextView tvSibk;
    private TextView tvDesc;
    private ImageView imageView;
    private LinearLayout llUsername;
    private EditText etUsername;
    private EditText etPassword;

        private ScreenSize ss;
    private LinearLayout llPassword;
    private ImageView ivShowPassword;
    private TextView tvVersion;

    private String currentVersion;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private LinearLayout llLogin;
    private SpinKitView spinKit;
    private User userModel;
    private int userid, usertypeid;
    private boolean showPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ss = new ScreenSize(LoginActivity.this);

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = info.versionName;

        initView();
        setClick();

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(SplashScreen.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                checkUpdate(currentVersion);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(SplashScreen.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

    }


    private void initView() {
        tvSibk = (TextView) findViewById(R.id.tvSibk);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.getLayoutParams().width = ss.getWidth() / 3;
        imageView.getLayoutParams().height = ss.getWidth() / 3;
        llUsername = (LinearLayout) findViewById(R.id.llUsername);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        llPassword = (LinearLayout) findViewById(R.id.llPassword);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        llLogin = (LinearLayout) findViewById(R.id.llLogin);
        tvVersion.setText("Version " + currentVersion);
        spinKit = (SpinKitView) findViewById(R.id.spinKit);
    }

    public void setClick() {

        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isError = false;
                if (etUsername.getText().length() > 0) {
                    isError = false;
                } else {
                    isError = true;
                }
                if (etPassword.getText().length() > 0) {
                    isError = false;
                } else {
                    isError = true;
                }
                if (isError == true) {
                    alert("Jangan kosongkan inputan!", "Error");
                } else {
                    login();
                }


            }
        });
    }

    private void checkUpdate(final String currentVersion) {
        AndroidNetworking.post(VersionManagementApi.POST_VERSION_MANAGEMENT)
                .addBodyParameter("current_version", currentVersion + "")
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {

                    }
                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                Log.d("test", "onResponse object : " + response.toString());
                try {
                    JSONObject result = response.getJSONObject("sibk");
                    String status = result.getString("status");
                    String message = result.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        startAplikasi();
                    } else {
                        JSONObject data = result.getJSONObject("data");
                        String changelogNa = data.getString("changelog");
                        String judul_notifikasi = data.getString("judul_notifikasi");
                        String url_to_updateNa = data.getString("url_to_update");
                        final Dialog dialogInfo = new Dialog(LoginActivity.this);
                        dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogInfo.setContentView(R.layout.dialog_with_button);
                        dialogInfo.setCancelable(false);

                        TextView tv_dialog_title = dialogInfo.findViewById(R.id.tv_dialog_title);
                        tv_dialog_title.setText(judul_notifikasi);
                        tv_dialog_title.setVisibility(View.VISIBLE);

                        TextView tv_dialog_message = dialogInfo.findViewById(R.id.tv_dialog_message);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tv_dialog_message.setText(Html.fromHtml(changelogNa, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            tv_dialog_message.setText(Html.fromHtml(changelogNa));
                        }

                        Button btn_top = dialogInfo.findViewById(R.id.btn_top);
                        btn_top.setText(getResources().getString(R.string.update));

                        final String url_to_update = url_to_updateNa;
                        btn_top.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url_to_update));
                                startActivity(i);
                            }
                        });

                        Button btn_bottom = dialogInfo.findViewById(R.id.btn_bottom);
                        btn_bottom.setVisibility(View.GONE);

                        dialogInfo.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    alert("Gagal menampilkan data!", "Error");
                }

            }

            @Override
            public void onError(ANError anError) {
                alert("Tidak Ada Koneksi!", "Error");

            }
        });
    }


    public void startAplikasi() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("test", "masuk");
                DBUser dbUser = new DBUser(LoginActivity.this);
                if (dbUser.isNull()) {
                    spinKit.setVisibility(View.GONE);
                    llUsername.setVisibility(View.VISIBLE);
                    llPassword.setVisibility(View.VISIBLE);
//                    tvLupaPassword.setVisibility(View.VISIBLE);
                    llLogin.setVisibility(View.VISIBLE);
                    setAnimation();
                } else {
                    getDataPengguna();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void setAnimation() {
        Animation anim_zoom_username = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        anim_zoom_username.setAnimationListener(this);
        anim_zoom_username.setDuration(700);
        llUsername.startAnimation(anim_zoom_username);

        Animation anim_zoom_password = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        anim_zoom_password.setAnimationListener(this);
        anim_zoom_password.setDuration(700);
        llPassword.startAnimation(anim_zoom_password);

        Animation anim_zoom_login = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        anim_zoom_login.setAnimationListener(this);
        anim_zoom_login.setDuration(700);
        llLogin.startAnimation(anim_zoom_login);
    }

    private void getDataPengguna() {
        DBUser dbUser = new DBUser(LoginActivity.this);
        userModel = dbUser.findUser();
        userid = userModel.getuserid();
        usertypeid = userModel.getusertypeid();
        AndroidNetworking.post(UserApi.POST_FIND_USER)
                .addBodyParameter("userid", String.valueOf(userid))
                .addBodyParameter("usertypeid", String.valueOf(usertypeid))
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {

                    }
                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("sibk");
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        DBUser dbUser = new DBUser(LoginActivity.this);
                        final Intent i;
                        if (dbUser.findUser().getusertypeid() == 2) {
                            i = new Intent(LoginActivity.this, MainActivityGuru.class);
                            startActivity(i);
                            finish();
                        } else if (dbUser.findUser().getusertypeid() == 3) {

                            i = new Intent(LoginActivity.this, com.sibk.tasik.MainActivitySiswa.MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        customType(LoginActivity.this, "bottom-to-up");
                    } else {
                        alert(status, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    alert("Gagal menampilkan data!", "Error");
                }

            }

            @Override
            public void onError(ANError anError) {
                alert("Tidak Ada Koneksi!", "Error");
            }
        });
    }

    private void login() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
        pDialog.setTitleText("Sedang Mengambil Data....");
        pDialog.setCancelable(false);
        pDialog.show();

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        AndroidNetworking.post(UserApi.POST_LOGIN_USER)
                .addBodyParameter("username", username)
                .addBodyParameter("password", password)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {

                    }
                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                try {
                    pDialog.dismissWithAnimation();
                    JSONObject result = response.getJSONObject("sibk");
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONObject dataUser = result.getJSONObject("data_user");
                        User u = new User();
                        u.setuserid(dataUser.getInt("userid"));
                        u.setemail(dataUser.getString("email"));
                        u.setfullname(dataUser.getString("fullname"));
                        u.setusertypeid(dataUser.getInt("usertypeid"));
                        DBUser dbUser = new DBUser(LoginActivity.this);
                        dbUser.save(u);
                        Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
                        i.putExtra("fullname", dataUser.getString("fullname"));
                        i.putExtra("usertypeid", dataUser.getInt("usertypeid"));
                        startActivity(i);
                        finish();
                        customType(LoginActivity.this, "left-to-right");
                    } else {
                        alert(status, message);
                    }

                } catch (JSONException e) {
                    pDialog.dismissWithAnimation();
                    e.printStackTrace();
                    alert("Gagal menampilkan data!", "Error");
                }

            }

            @Override
            public void onError(ANError anError) {
                pDialog.dismissWithAnimation();
                alert("Tidak Ada Koneksi!", "Error");
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void alert(final String status, final String message) {
        if (status.equalsIgnoreCase("succes")) {
            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            pDialog.setTitleText("Sukses");
            pDialog.setContentText(message);
            pDialog.setCancelable(false);
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            pDialog.show();
        } else {
            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Error");
            pDialog.setContentText(message);
            pDialog.setCancelable(false);
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            pDialog.show();
        }

    }

}
