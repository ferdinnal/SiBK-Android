package com.sibk.tasik.MainActivitySiswa;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
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
import com.google.zxing.integration.android.IntentResult;
import com.orhanobut.dialogplus.DialogPlus;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sibk.tasik.Adapter.AbsensiSiswaAdapter;
import com.sibk.tasik.Api.AbsensiApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.Model.SiswaAbsensiModel;
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

public class AbsensiHarianSiswa extends AppCompatActivity {
    private User userModel;
    private int userid, usertypeid;
    public static int idKelas;
    private int idJadwalPelajaran;
    private String namaGuru;
    private String kodeMataPelajaran;
    private String namaMataPelajaran;
    private ScreenSize ss;
    private DrawerLayout drawerLayout;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TextView titleBarName;
    private ImageView ivSearch;
    private SmartRefreshLayout refreshLayout;
    public static SpinKitView spinKit;
    private LinearLayout cvAll;
    private CardView cvAtas;
    private LinearLayout llTop;
    private TextView tvPelajaran;
    private TextView tvKode;
    public static TextView tvTotalSiswa;
    public static TextView tvHadir;
    public static TextView tvSakit;
    public static TextView tvIjin;
    public static TextView tvAlfa;
    private LinearLayout llJadwalBesokError;
    public static ExpandableHeightListView ivPelajaran;
    private String qrCodeAbsensi = "";
    private DialogPlus dialogPlus;
    private ImageView ivBarcode;
    private List<SiswaAbsensiModel> listHari = new ArrayList<SiswaAbsensiModel>();
    public static AbsensiSiswaAdapter adapterHari;
    public static int historyQrCodeId;
    public static TextView tvBelumAbsen;
    private Location mLastLocation;
    private static double latitude = 0, longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_siswa);

        DBUser dbUser = new DBUser(AbsensiHarianSiswa.this);
        userModel = dbUser.findUser();
        userid = userModel.getuserid();
        usertypeid = userModel.getusertypeid();
        ss = new ScreenSize(AbsensiHarianSiswa.this);

        Intent i = this.getIntent();
        idJadwalPelajaran = i.getIntExtra("idJadwalPelajaran", 0);
        kodeMataPelajaran = i.getStringExtra("kodeMataPelajaran");
        namaMataPelajaran = i.getStringExtra("namaMataPelajaran");
        idKelas = i.getIntExtra("idKelas", 0);
        historyQrCodeId = i.getIntExtra("historyQrCodeId", 0);
        Log.d("testKElasNa", historyQrCodeId + "");
        Log.d("testKElasNa", userid + "");

        mLastLocation = getLastKnownLocation();
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        } else {
            //arahkan ke pusat kota tasikmalaya
            latitude = -7.319563;
            longitude = 108.202972;
        }

        initView();
        getDataSiswa();
        setClick();
    }
    private Location getLastKnownLocation() {
        Location bestLocation = null;
        try {
            LocationManager mLocationManager;
            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            for (String provider : providers) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null;
                }
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bestLocation;
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
        cvAtas = (CardView) findViewById(R.id.cvAtas);
        llTop = (LinearLayout) findViewById(R.id.llTop);
        tvPelajaran = (TextView) findViewById(R.id.tvPelajaran);
        tvKode = (TextView) findViewById(R.id.tvKode);
        tvTotalSiswa = (TextView) findViewById(R.id.tvTotalSiswa);
        tvHadir = (TextView) findViewById(R.id.tvHadir);
        tvSakit = (TextView) findViewById(R.id.tvSakit);
        tvIjin = (TextView) findViewById(R.id.tvIjin);
        tvAlfa = (TextView) findViewById(R.id.tvAlfa);
        llJadwalBesokError = (LinearLayout) findViewById(R.id.llJadwalBesokError);
        ivPelajaran = (ExpandableHeightListView) findViewById(R.id.ivPelajaran);
        ivPelajaran.setExpanded(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

        tvPelajaran.setText(namaMataPelajaran);
        tvKode.setText(kodeMataPelajaran);

        tvBelumAbsen = (TextView) findViewById(R.id.tvBelumAbsen);


    }

    private void setClick() {

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentIntegrator integrator = new IntentIntegrator(AbsensiHarianSiswa.this);
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
                getDataSiswa();
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

    private void getDataSiswa() {
        spinKit.setVisibility(View.VISIBLE);
        listHari.clear();
        cvAll.setVisibility(View.GONE);
        AndroidNetworking.post(AbsensiApi.POST_FIND_ABSENSI)
                .addBodyParameter("history_qr_code_id", String.valueOf(historyQrCodeId))
                .addBodyParameter("id_kelas", String.valueOf(idKelas))
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
                Log.d("test", "onResponse object : " + response.toString());
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
                            SiswaAbsensiModel x = new SiswaAbsensiModel();
                            x.setIdSiswa(c.getInt("id_siswa"));
                            x.setAbsensiId(c.getInt("absensi_id"));
                            x.setNamaSiswa(c.getString("nama_pengguna"));
                            x.setNisn(c.getString("nisn"));
                            x.setStatusAbsenId(c.getInt("status_absen_id"));
                            x.setStatusAbsen(c.getString("status_absen"));

                            listHari.add(x);
                        }
                        adapterHari = new AbsensiSiswaAdapter(AbsensiHarianSiswa.this, listHari);
                        ivPelajaran.setAdapter(adapterHari);

                        JSONObject total_siswa = result.getJSONObject("total_siswa");
                        JSONObject total_hadir = result.getJSONObject("total_hadir");
                        JSONObject total_ijin = result.getJSONObject("total_ijin");
                        JSONObject total_alfa = result.getJSONObject("total_alfa");
                        JSONObject total_belum = result.getJSONObject("total_belum");
                        JSONObject total_sakit = result.getJSONObject("total_sakit");
                        tvTotalSiswa.setText(total_siswa.getString("total_siswa"));
                        tvHadir.setText(total_hadir.getString("total_siswa"));
                        tvIjin.setText(total_ijin.getString("total_siswa"));
                        tvAlfa.setText(total_alfa.getString("total_siswa"));
                        tvBelumAbsen.setText(total_belum.getString("total_siswa"));
                        tvSakit.setText(total_sakit.getString("total_siswa"));
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("test", "disini");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Log.d("QR Pay", "Absensi Dibatalkan");
            } else {
                try {
                    result.getContents();

                    prosesAbsensi(result.getContents());

                } catch (Exception ex) {

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void prosesAbsensi(final String qrCode) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
        pDialog.setTitleText("Sedang Mengupdate Data....");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(AbsensiApi.POST_PROSES_ABSENSI)
                .addBodyParameter("qr_code_absensi", String.valueOf(qrCode))
                .addBodyParameter("history_qr_code_id", String.valueOf(historyQrCodeId))
                .addBodyParameter("id_kelas", String.valueOf(idKelas))
                .addBodyParameter("id_pengguna", String.valueOf(userid))
                .addBodyParameter("latitude", String.valueOf(latitude))
                .addBodyParameter("longitude", String.valueOf(longitude))
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
                Log.d("test", "onResponse object : " + response.toString());
                try {
                    pDialog.dismissWithAnimation();
                    JSONObject result = response.getJSONObject("prilude");
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        getDataSiswa();
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
                alert(anError.toString(), "Error");
            }
        });
    }



}
