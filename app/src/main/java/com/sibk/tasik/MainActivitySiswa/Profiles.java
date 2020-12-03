package com.sibk.tasik.MainActivitySiswa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sibk.tasik.Api.UserApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.Model.User;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;
import com.sibk.tasik.Utility.Website;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Response;

public class Profiles extends AppCompatActivity {


    private AppBarLayout include;
    private Toolbar toolbar;
    private TextView tvToolbar;
    private SmartRefreshLayout refreshLayout;
    private SpinKitView spinKit;
    private LinearLayout llTop;
    private SimpleDraweeView ivProfile;
    private TextView tvFullname;
    private TextView tvEmail;
    private ImageView ivEditEmail;
    private TextView tvPhone;
    private ImageView ivEditPhone;
    private TextView tvNisn;
    private TextView tvJenisKelamin;
    private TextView tvAddress;
    private TextView tvTanggalLahir;
    private ImageView ivEditPassword;
    private ViewGroup kelompok;
    private int idPengguna, idTipePengguna;
    private User userModel;
    private Website website;
    private ScreenSize ss;
    private String namaPengguna;
    private String nisn;
    private String gambarProfil;
    private String jenisKelamin;
    private String noHandphone;
    private SlidingUpPanelLayout slidingUp;
    private TextView tvTitle;
    private LinearLayout inputData;
    private EditText etFullname;
    private TextView tvUsername;
    private LinearLayout inputCode;
    private EditText etOldPass;
    private EditText etNewPass;
    private EditText etRenewPass;
    private Button alertButtonNo;
    private Button alertButtonYes;
    private boolean isInputError = true;
    private LinearLayout llPassword;
    private LinearLayout inputData2;
    private LinearLayout llNoHp;
    private EditText etNoHp;
    private TextView tvNohp;
    private LinearLayout inputData3;
    private LinearLayout llPassword2;
    private EditText etPasswordLama;
    private EditText etPasswordBaru;
    private EditText etpasswordBaruUlangi;
    private TextView tvErrorPassword;
    private String emailAddress;
    private LinearLayout llButton;
    private String noOrtu;
    private TextView tvPhoneOrtu;
    private ImageView ivEditPhoneOrtu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_siswa);

        DBUser dbUser = new DBUser(Profiles.this);
        userModel = dbUser.findUser();
        idPengguna = userModel.getuserid();
        idTipePengguna = userModel.getusertypeid();

        website = new Website();
        ss = new ScreenSize(Profiles.this);

        setLayout();
        getDataPengguna();
        setClick();
    }


    private void setLayout() {
        include = (AppBarLayout) findViewById(R.id.include);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        spinKit = (SpinKitView) findViewById(R.id.spinKit);
        llTop = (LinearLayout) findViewById(R.id.llTop);
        ivProfile = (SimpleDraweeView) findViewById(R.id.iv_profile);
        tvFullname = (TextView) findViewById(R.id.tv_fullname);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        ivEditEmail = (ImageView) findViewById(R.id.ivEditEmail);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        ivEditPhone = (ImageView) findViewById(R.id.ivEditPhone);
        tvNisn = (TextView) findViewById(R.id.tvNisn);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvPhoneOrtu = (TextView) findViewById(R.id.tvPhoneOrtu);
        ivEditPassword = (ImageView) findViewById(R.id.ivEditPassword);
        kelompok = findViewById(R.id.kelompok);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        slidingUp = (SlidingUpPanelLayout) findViewById(R.id.slidingUp);
//        slidingUp.setPanelHeight(ss.getHeight() / 3);
        slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout

        tvTitle = (TextView) findViewById(R.id.tv_title);
        inputData = (LinearLayout) findViewById(R.id.input_data);
        etFullname = (EditText) findViewById(R.id.et_fullname);
        tvUsername = (TextView) findViewById(R.id.tv_username);
//        inputCode = (LinearLayout) findViewById(R.id.input_code);
//        etOldPass = (EditText) findViewById(R.id.et_old_pass);
//        etNewPass = (EditText) findViewById(R.id.et_new_pass);
//        etRenewPass = (EditText) findViewById(R.id.et_renew_pass);
        alertButtonNo = (Button) findViewById(R.id.alert_button_no);
        alertButtonYes = (Button) findViewById(R.id.alert_button_yes);
        llPassword = (LinearLayout) findViewById(R.id.llPassword);
        inputData2 = (LinearLayout) findViewById(R.id.input_data2);
        llNoHp = (LinearLayout) findViewById(R.id.llNoHp);
        etNoHp = (EditText) findViewById(R.id.et_no_hp);
        tvNohp = (TextView) findViewById(R.id.tv_nohp);
        inputData3 = (LinearLayout) findViewById(R.id.input_data3);
        llPassword2 = (LinearLayout) findViewById(R.id.llPassword2);
        llButton = (LinearLayout) findViewById(R.id.llButton);
        ivEditPhoneOrtu = (ImageView) findViewById(R.id.ivEditPhoneOrtu);

        etPasswordLama = (EditText) findViewById(R.id.etPasswordLama);
        etPasswordBaru = (EditText) findViewById(R.id.etPasswordBaru);
        etpasswordBaruUlangi = (EditText) findViewById(R.id.etpasswordBaruUlangi);
        tvErrorPassword = (TextView) findViewById(R.id.tv_error_password);
    }

    public void setClick() {

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        refreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new BezierCircleHeader(context);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDataPengguna();
                refreshlayout.finishRefresh(2000);

            }
        });
        ivEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingPanelUp(1);
            }
        });

        ivEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingPanelUp(2);
            }
        });
        ivEditPhoneOrtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingPanelUp(4);
            }
        });

        ivEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingPanelUp(3);
            }
        });

    }

    void slidingPanelUp(final int type) {
//        slidingUp.setPanelHeight(ss.getHeight() / 3);
        if (slidingUp.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            if (type == 1) {
                inputData.setVisibility(View.VISIBLE);
                llButton.setVisibility(View.VISIBLE);
                inputData3.setVisibility(View.GONE);
                inputData2.setVisibility(View.GONE);
                tvTitle.setText("Masukan Alamat Email Baru Anda");
                tvUsername.setVisibility(View.INVISIBLE);
                etFullname.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                etFullname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!isValidEmail(etFullname.getText().toString())) {
                            tvUsername.setVisibility(View.VISIBLE);
                            tvUsername.setTextColor(getResources().getColor(R.color.merah));
                            tvUsername.setText("Alamat email tidak tersedia");
                            isInputError = true;
                        } else {
                            tvUsername.setVisibility(View.INVISIBLE);
                            isInputError = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                alertButtonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isInputError) {
                            updateUserEmail(etFullname.getText().toString().trim());
                            slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
                        } else {

                        }
                    }
                });
                alertButtonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
                    }
                });
            } else if (type == 2) {
                inputData.setVisibility(View.GONE);
                inputData2.setVisibility(View.VISIBLE);
                llButton.setVisibility(View.VISIBLE);
                inputData3.setVisibility(View.GONE);
                tvTitle.setText("Masukan No. Hp Baru Anda");
                tvNohp.setVisibility(View.INVISIBLE);
                etNoHp.setInputType(InputType.TYPE_CLASS_NUMBER);
                etNoHp.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (etNoHp.length() > 12) {
                            tvNohp.setVisibility(View.VISIBLE);
                            tvNohp.setTextColor(getResources().getColor(R.color.merah));
                            tvNohp.setText("Inputan Maksimal 12");
                            isInputError = true;
                        } else {
                            tvNohp.setVisibility(View.INVISIBLE);
                            isInputError = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                alertButtonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isInputError) {
                            String no_telephone = etNoHp.getText().toString();
                            updateUserNoHp(no_telephone.replace("0", "+62") + "");
                            slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
                        } else {

                        }
                    }
                });
                alertButtonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
                    }
                });
            } else if (type == 3) {
                inputData3.setVisibility(View.VISIBLE);
                llButton.setVisibility(View.VISIBLE);
                inputData2.setVisibility(View.GONE);
                inputData.setVisibility(View.GONE);
                tvTitle.setText("Edit Password");
                tvErrorPassword.setVisibility(View.INVISIBLE);
                etPasswordBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etPasswordLama.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etpasswordBaruUlangi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etPasswordBaru.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (etNoHp.length() < 0) {
                            tvErrorPassword.setVisibility(View.VISIBLE);
                            tvErrorPassword.setTextColor(getResources().getColor(R.color.merah));
                            tvErrorPassword.setText("Inputan Tidak Boleh Kosong");
                            isInputError = true;
                        } else {
                            tvErrorPassword.setVisibility(View.INVISIBLE);
                            isInputError = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                alertButtonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etPasswordLama.getText().length() < 0 || etPasswordBaru.getText().length() < 0 || etpasswordBaruUlangi.getText().length() < 0) {
                            tvErrorPassword.setVisibility(View.VISIBLE);
                            tvErrorPassword.setTextColor(getResources().getColor(R.color.merah));
                            tvErrorPassword.setText("Inputan Tidak Boleh Kosong");
                        } else {
                            if (etPasswordBaru.getText().toString().equalsIgnoreCase(etpasswordBaruUlangi.getText().toString())) {
//                                updateUserPassword(etPasswordBaru.getText().toString().trim());
                                slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
                            } else {
                                tvErrorPassword.setVisibility(View.VISIBLE);
                                tvErrorPassword.setTextColor(getResources().getColor(R.color.merah));
                                tvErrorPassword.setText("Konfirmasi Password Tidak Cocok.");
                            }
                        }
                    }
                });
                alertButtonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
                    }
                });
            } else if (type == 4) {
                inputData.setVisibility(View.GONE);
                inputData2.setVisibility(View.VISIBLE);
                llButton.setVisibility(View.VISIBLE);
                inputData3.setVisibility(View.GONE);
                tvTitle.setText("Masukan No. Hp Baru Ortu Anda");
                tvNohp.setVisibility(View.INVISIBLE);
                etNoHp.setInputType(InputType.TYPE_CLASS_NUMBER);
                etNoHp.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (etNoHp.length() > 12) {
                            tvNohp.setVisibility(View.VISIBLE);
                            tvNohp.setTextColor(getResources().getColor(R.color.merah));
                            tvNohp.setText("Inputan Maksimal 12");
                            isInputError = true;
                        } else {
                            tvNohp.setVisibility(View.INVISIBLE);
                            isInputError = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                alertButtonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isInputError) {
                            String no_telephone = etNoHp.getText().toString();
                            updateUserNoHpOrtu(no_telephone.replace("0", "+62") + "");
                            slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
                        } else {

                        }
                    }
                });
                alertButtonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
                    }
                });
            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void getDataPengguna() {
        spinKit.setVisibility(View.VISIBLE);
        llTop.setVisibility(View.GONE);
        AndroidNetworking.post(UserApi.POST_FIND_USER)
                .addBodyParameter("userid", String.valueOf(idPengguna))
                .addBodyParameter("usertypeid", String.valueOf(idTipePengguna))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {

                    }
                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                try {
//                    TransitionManager.beginDelayedTransition(kelompok);
                    spinKit.setVisibility(View.GONE);
                    llTop.setVisibility(View.VISIBLE);
                    JSONObject result = response.getJSONObject("sibk");
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONObject dataUser = result.getJSONObject("data_user");
                        JSONObject dataSiswa = result.getJSONObject("data_siswa");
                        namaPengguna = dataUser.getString("fullname");
                        nisn = dataSiswa.getString("nisn");
                        gambarProfil = dataUser.getString("gambar_profil");
                        noHandphone = dataUser.getString("no_user");
                        noOrtu = dataUser.getString("no_ortu");
                        emailAddress = dataUser.getString("email");
                        tvAddress.setText(dataSiswa.getString("alamat_rumah"));

                        tvFullname.setText(namaPengguna);
                        tvNisn.setText(nisn);
                        tvPhone.setText("No Siswa " + noHandphone);
                        tvPhoneOrtu.setText("No Ortu " + noOrtu);
                        tvEmail.setText(emailAddress);

                        Uri lin1 = Uri.parse(website.getMainDomain() + "/image/" + gambarProfil);
                        ivProfile.setImageURI(lin1);
                    } else {
                        alert(status, message);
                    }

                } catch (JSONException e) {
                    spinKit.setVisibility(View.GONE);
                    e.printStackTrace();
                    alert("Gagal menampilkan data!", "Error");
                }

            }

            @Override
            public void onError(ANError anError) {
                spinKit.setVisibility(View.GONE);
                alert(anError.toString(), "Error");
            }
        });
    }

    public void alert(final String status, final String message) {
        if (status.equalsIgnoreCase("succes")) {
            SweetAlertDialog pDialog = new SweetAlertDialog(Profiles.this, SweetAlertDialog.SUCCESS_TYPE);
            pDialog.setTitleText("Sukses");
            pDialog.setContentText(message);
            pDialog.setCancelable(false);
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            pDialog.show();
        } else if (status.equalsIgnoreCase("logout")) {
            SweetAlertDialog pDialog = new SweetAlertDialog(Profiles.this, SweetAlertDialog.WARNING_TYPE);
            pDialog.setTitleText("Logout");
            pDialog.setContentText("Apakah Anda yakin akan keluar?");
            pDialog.setCancelable(false);
            pDialog.setConfirmText("Ya");
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
//                    doLogout();
                }
            });
            pDialog.setCancelText("Tidak");
            pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            pDialog.show();
        } else {
            SweetAlertDialog pDialog = new SweetAlertDialog(Profiles.this, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Error");
            pDialog.setContentText(message);
            pDialog.setCancelable(false);
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            pDialog.show();
        }

    }

//    private void doLogout() {
//        final SweetAlertDialog pDialog = new SweetAlertDialog(Profiles.this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
//        pDialog.setTitleText("Sedang Mengupdate Data....");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        DBUser dbUser = new DBUser(Profiles.this);
//        User userModel = dbUser.findUser();
//        int idPengguna = userModel.getusertypeid();
//
//        AndroidNetworking.post(UserApi.POST_LOGOUT)
//                .addBodyParameter("id_pengguna", idPengguna + "")
//                .setTag(this)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//
//                    }
//                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
//            @Override
//            public void onResponse(Response okHttpResponse, JSONObject response) {
//                try {
//                    pDialog.dismissWithAnimation();
//                    JSONObject result = response.getJSONObject("sibk");
//                    String status = result.getString("status");
//                    String message = result.getString("message");
//
//                    if (status.equalsIgnoreCase("success")) {
//                        DBUser db_user = new DBUser(DetailSiswa.this);
//                        db_user.delete();
//                        Intent i = new Intent(DetailSiswa.this, LoginActivity.class);
//                        startActivity(i);
//                        finish();
//                    } else {
//                        alert(status, message);
//                    }
//
//                } catch (JSONException e) {
//                    pDialog.dismissWithAnimation();
//                    e.printStackTrace();
//                    alert("Gagal menampilkan data!", "Error");
//                }
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//                pDialog.dismissWithAnimation();
//                alert("Tidak Ada Koneksi!", "Error");
//            }
//        });
//    }


    private void updateUserEmail(final String alamatEmail) {
        slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
        pDialog.setTitleText("Sedang Menyimpan Data....");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(UserApi.POST_UPDATE_EMAIL)
                .addBodyParameter("userid", String.valueOf(idPengguna))
                .addBodyParameter("email", String.valueOf(alamatEmail))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {

                    }
                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                try {
//                    TransitionManager.beginDelayedTransition(kelompok);
                    pDialog.dismissWithAnimation();
                    JSONObject result = response.getJSONObject("sibk");
                    String status = result.getString("status");
                    String message = result.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        alert("succes", "Berhasil Menyimpan Data");
                        getDataPengguna();
                    } else {
                        alert(status, message);
                    }

                } catch (JSONException e) {
                    pDialog.dismissWithAnimation();
                    e.printStackTrace();
                    alert("Gagal menambahkan data!", "Error");
                }

            }

            @Override
            public void onError(ANError anError) {

                alert(anError.toString(), "Error");
            }
        });
    }

    private void updateUserNoHpOrtu(final String noHp) {
        slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
        pDialog.setTitleText("Sedang Menyimpan Data....");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(UserApi.POST_UPDATE_NO_HP_ORTU)
                .addBodyParameter("userid", String.valueOf(idPengguna))
                .addBodyParameter("no_ortu", String.valueOf(noHp))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {

                    }
                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                try {
//                    TransitionManager.beginDelayedTransition(kelompok);
                    pDialog.dismissWithAnimation();
                    JSONObject result = response.getJSONObject("sibk");
                    String status = result.getString("status");
                    String message = result.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        alert("succes", "Berhasil Menyimpan Data");
                        getDataPengguna();
                    } else {
//                        alert(status, message);
                    }

                } catch (JSONException e) {
                    pDialog.dismissWithAnimation();
                    e.printStackTrace();
//                    alert("Gagal menambahkan data!", "Error");
                }

            }

            @Override
            public void onError(ANError anError) {

//                alert(anError.toString(), "Error");
            }
        });
    }

    private void updateUserNoHp(final String noHp) {
        slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
        pDialog.setTitleText("Sedang Menyimpan Data....");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(UserApi.POST_UPDATE_NO_HP)
                .addBodyParameter("userid", String.valueOf(idPengguna))
                .addBodyParameter("no_user", String.valueOf(noHp))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {

                    }
                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                try {
//                    TransitionManager.beginDelayedTransition(kelompok);
                    pDialog.dismissWithAnimation();
                    JSONObject result = response.getJSONObject("sibk");
                    String status = result.getString("status");
                    String message = result.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        alert("succes", "Berhasil Menyimpan Data");
                        getDataPengguna();
                    } else {
                        alert(status, message);
                    }

                } catch (JSONException e) {
                    pDialog.dismissWithAnimation();
                    e.printStackTrace();
                    alert("Gagal menambahkan data!", "Error");
                }

            }

            @Override
            public void onError(ANError anError) {

                alert(anError.toString(), "Error");
            }
        });
    }

//    private void updateUserPassword(final String password) {
//        slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//        slidingUp.setPanelHeight(-200);//agar slide tidak muncul ketika checkout
//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
//        pDialog.setTitleText("Sedang Menyimpan Data....");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        AndroidNetworking.post(UserApi.POST_UPDATE_PASSWORD)
//                .addBodyParameter("id_pengguna", String.valueOf(idPengguna))
//                .addBodyParameter("password", String.valueOf(password))
//                .setTag(this)
//                .setPriority(Priority.LOW)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//
//                    }
//                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onResponse(Response okHttpResponse, JSONObject response) {
//                try {
////                    TransitionManager.beginDelayedTransition(kelompok);
//                    pDialog.dismissWithAnimation();
//                    JSONObject result = response.getJSONObject("sibk");
//                    String status = result.getString("status");
//                    String message = result.getString("message");
//                    if (status.equalsIgnoreCase("success")) {
//                        alert("succes", "Berhasil Menyimpan Data, anda akan otomatis logout aplikasi");
//                        doLogout();
//                    } else {
//                        alert(status, message);
//                    }
//
//                } catch (JSONException e) {
//                    pDialog.dismissWithAnimation();
//                    e.printStackTrace();
//                    alert("Gagal menambahkan data!", "Error");
//                }
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//
//                alert(anError.toString(), "Error");
//            }
//        });
//    }
}
