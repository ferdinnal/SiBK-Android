package com.sibk.tasik.MainActivitySiswa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sibk.tasik.Api.KonselingApi;
import com.sibk.tasik.Api.UserApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.Model.User;
import com.sibk.tasik.R;
import com.sibk.tasik.StartActivity.LoginActivity;
import com.sibk.tasik.StartActivity.WelcomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Response;

import static maes.tech.intentanim.CustomIntent.customType;

public class TambahPengajuan extends AppCompatActivity {

    private LinearLayout kelompok;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TextView titleBarName;
    private SmartRefreshLayout refreshLayout;
    private SpinKitView spinKit;
    private LinearLayout cvPelajaran;
    private CardView cvAll;
    private EditText etSubject;
    private RelativeLayout rlButton;
    private LinearLayout llError;
    private LinearLayout llMateri;
    private TimePickerDialog timePickerDialog;
    private EditText etWaktu;
    private EditText etTanggal;
    private String datePengajuan;
    private User userModel;
    private int idPengguna, idTipePengguna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengajuan);

        DBUser dbUser = new DBUser(TambahPengajuan.this);
        userModel = dbUser.findUser();
        idPengguna = userModel.getuserid();

        initView();
        setClick();
    }

    private void initView() {
        kelompok = (LinearLayout) findViewById(R.id.kelompok);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleBarName = (TextView) findViewById(R.id.titleBarName);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        spinKit = (SpinKitView) findViewById(R.id.spinKit);
        cvPelajaran = (LinearLayout) findViewById(R.id.cvPelajaran);
        cvAll = (CardView) findViewById(R.id.cvAll);
        etSubject = (EditText) findViewById(R.id.etSubject);
        rlButton = (RelativeLayout) findViewById(R.id.rlButton);
        llError = (LinearLayout) findViewById(R.id.llError);
        llMateri = (LinearLayout) findViewById(R.id.llMateri);
        etWaktu = (EditText) findViewById(R.id.etWaktu);
        etTanggal = (EditText) findViewById(R.id.etTanggal);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

    }

    public void setClick() {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSubject.length() > 0 && etTanggal.length() > 0 && etWaktu.length() > 0) {
                    simpan();
                } else {
                    alert("Jangan kosongkan inputan!", "Error");
                }
            }
        });
        etWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(etWaktu);
            }
        });
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar(etTanggal);
            }
        });

    }

    private void simpan() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
        pDialog.setTitleText("Sedang Mengambil Data....");
        pDialog.setCancelable(false);
        pDialog.show();

        AndroidNetworking.post(KonselingApi.POST_PENGAJUAN)
                .addBodyParameter("subject", etSubject.getText().toString().trim() + "")
                .addBodyParameter("waktu", etWaktu.getText().toString().trim() + "")
                .addBodyParameter("tanggal", datePengajuan + "")
                .addBodyParameter("userid", idPengguna + "")
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
                        setResult(RESULT_OK);
                        finish();
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


    private void showTimeDialog(final TextView isi) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String jamNa = "", menitNa = "";
                if (hourOfDay < 10) {
                    jamNa = "0" + hourOfDay;
                } else {
                    jamNa = hourOfDay + "";
                }

                if (minute < 10) {
                    menitNa = "0" + minute;
                } else {
                    menitNa = minute + "";
                }

                isi.setText(jamNa + ":" + menitNa);
            }
        },


                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    void showCalendar(final TextView tvMode) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(TambahPengajuan.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                java.text.DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy");
                tvMode.setText(dateFormat.format(newDate.getTime()));
                datePengajuan = dateFormatter.format(newDate.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
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
