package com.sibk.tasik.MainActivitySiswa;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.orhanobut.dialogplus.DialogPlus;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sibk.tasik.Adapter.AbsensiHariAdapter;
import com.sibk.tasik.Api.AbsensiApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.Model.HariAbsensiModel;
import com.sibk.tasik.Model.User;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Response;

public class Absensi extends AppCompatActivity {

        private User userModel;
    private int userid, usertypeid;
    private int idKelas;
    private int idMataPelajaranGuru;
    private String namaGuru;
    private String kodeMataPelajaran;
    private String namaMataPelajaran;
    private DrawerLayout drawerLayout;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TextView titleBarName;
    private ImageView ivSearch;
    private SmartRefreshLayout refreshLayout;
    private SpinKitView spinKit;
    private LinearLayout cvAll;
    private LinearLayout llJadwalBesokError;
    private LinearLayout llJadwalAll;
    public static String qrCodeAbsensi = "";
    private DialogPlus dialogPlus;
    private ImageView ivBarcode;
    private ScreenSize ss;
    private List<HariAbsensiModel> listHari = new ArrayList<HariAbsensiModel>();
    private AbsensiHariAdapter adapterHari;
    private ExpandableHeightListView ivPelajaran;
    public static int historyQrCodeId;
    private int idJadwalPelajaran;
    private int hari_now;
    private int hari;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi);
        ss = new ScreenSize(Absensi.this);
        DBUser dbUser = new DBUser(Absensi.this);
        userModel = dbUser.findUser();
        userid = userModel.getuserid();
        usertypeid = userModel.getusertypeid();
        ss = new ScreenSize(Absensi.this);

        Intent i = this.getIntent();
        idMataPelajaranGuru = i.getIntExtra("idMataPelajaranGuru", 0);
        namaGuru = i.getStringExtra("namaGuru");
        kodeMataPelajaran = i.getStringExtra("kodeMataPelajaran");
        namaMataPelajaran = i.getStringExtra("namaMataPelajaran");
        idKelas = i.getIntExtra("idKelas", 0);
        idJadwalPelajaran = i.getIntExtra("idJadwalPelajaran", 0);

        Log.d("testKElasNa", idKelas + "");
        Log.d("testKElasNa", userid + "");

        initView();
        getDataHari();
        setClick();

    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleBarName = (TextView) findViewById(R.id.titleBarName);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        spinKit = (SpinKitView) findViewById(R.id.spinKit);
        cvAll = (LinearLayout) findViewById(R.id.cvAll);
        llJadwalBesokError = (LinearLayout) findViewById(R.id.llJadwalBesokError);
        ivPelajaran = (ExpandableHeightListView) findViewById(R.id.ivPelajaran);
        ivPelajaran.setExpanded(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        ivSearch.setVisibility(View.GONE);

    }

    private void setClick() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentIntegrator integrator = new IntentIntegrator(Absensi.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                        integrator.setCaptureActivity(ScanQr.class);
                        integrator.setOrientationLocked(false);
                        integrator.setBeepEnabled(false);
                        integrator.initiateScan();
                    }
                });

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
                getDataHari();
                refreshlayout.finishRefresh(2000);

            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void getDataHari() {
        spinKit.setVisibility(View.VISIBLE);
        listHari.clear();
        cvAll.setVisibility(View.GONE);
        Log.d("testidKelas", idKelas + "");
        Log.d("testidKelas", idJadwalPelajaran + "");
        AndroidNetworking.post(AbsensiApi.POST_FIND_HARI)
                .addBodyParameter("id_pengguna", String.valueOf(userid))
                .addBodyParameter("id_kelas", String.valueOf(idKelas))
                .addBodyParameter("id_jadwal_pelajaran", String.valueOf(idJadwalPelajaran))
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
                    spinKit.setVisibility(View.GONE);
                    cvAll.setVisibility(View.VISIBLE);
                    JSONObject result = response.getJSONObject("prilude");
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray dataSiswa = result.getJSONArray("data_siswa");
                        for (int i = 0; i < dataSiswa.length(); i++) {
                            JSONObject c = dataSiswa.getJSONObject(i);
                            HariAbsensiModel x = new HariAbsensiModel();
                            x.setHari(c.getInt("hari"));
                            x.setJamBeres(c.getString("jam_beres"));
                            x.setJamMulai(c.getString("jam_mulai"));
                            x.setNamaKelas(c.getString("nama_kelas"));
                            x.setNamaMataPelajaran(c.getString("nama_mata_pelajaran"));
                            x.setKodeMataPelajaran(c.getString("kode_mata_pelajaran"));
                            x.setDateCreated(c.getString("date_created"));
                            x.setIdJadwalPelajaran(c.getInt("id_jadwal_pelajaran"));
                            x.setIdKelas(c.getInt("id_kelas"));
                            x.setHistoryQrCodeId(c.getInt("history_qr_code_id"));

                            listHari.add(x);
                        }
                        adapterHari = new AbsensiHariAdapter(Absensi.this, listHari);
                        ivPelajaran.setAdapter(adapterHari);
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
