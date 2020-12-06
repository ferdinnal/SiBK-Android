package com.sibk.tasik.MainActivityGuru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sibk.tasik.Adapter.AbsensiSiswaAdapter;
import com.sibk.tasik.Adapter.AbsensiSiswaNewAdapter;
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

public class AbsensiHarianGuru extends AppCompatActivity {
    private User userModel;
    private int idPengguna, idTipePengguna;
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
    public static LinearLayout cvAll;
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
    public static List<SiswaAbsensiModel> listHari = new ArrayList<SiswaAbsensiModel>();
    public static AbsensiSiswaNewAdapter adapterHari;
    public static int historyQrCodeId;
    public static TextView tvBelumAbsen;
    private int hari;
    private int hari_now;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_guru);

        DBUser dbUser = new DBUser(AbsensiHarianGuru.this);
        userModel = dbUser.findUser();
        idPengguna = userModel.getuserid();
        idTipePengguna = userModel.getusertypeid();
        ss = new ScreenSize(AbsensiHarianGuru.this);
        activity=AbsensiHarianGuru.this;

        Intent i = this.getIntent();
        idJadwalPelajaran = i.getIntExtra("idJadwalPelajaran", 0);
        kodeMataPelajaran = i.getStringExtra("kodeMataPelajaran");
        namaMataPelajaran = i.getStringExtra("namaMataPelajaran");
        idKelas = i.getIntExtra("idKelas", 0);
        qrCodeAbsensi = AbsensiGuru.qrCodeAbsensi;
        historyQrCodeId = AbsensiGuru.historyQrCodeId;
        hari = AbsensiGuru.hari;
        hari_now = AbsensiGuru.hari_now;
        Log.d("testKElasNa", idKelas + "");
        Log.d("testKElasNa", historyQrCodeId + "");
        Log.d("testKElasNa", idPengguna + "");

        initView();
        getDataSiswa();
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
                if (qrCodeAbsensi.equalsIgnoreCase("")) {
                    if (hari != hari_now) {
                        alert("Gagal menggenerate QR-Code karena bukan jadwal yang ditentukan", "Error");
                    } else {
                        showDialog();
                    }
                } else {
                    showDialog();
                }
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
//                getDataSiswa();
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

    public static void getDataSiswa() {
        spinKit.setVisibility(View.VISIBLE);
        listHari.clear();
        cvAll.setVisibility(View.GONE);
        AndroidNetworking.post(AbsensiApi.POST_FIND_ABSENSI)
                .addBodyParameter("history_qr_code_id", String.valueOf(historyQrCodeId))
                .addBodyParameter("id_kelas", String.valueOf(idKelas))
                .setTag(activity)
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
                            x.setNamaSiswa(c.getString("fullname"));
                            x.setNisn(c.getString("nisn"));
                            x.setStatusAbsenId(c.getInt("status_absen_id"));
                            x.setStatusAbsen(c.getString("status_absen"));

                            listHari.add(x);
                        }
                        adapterHari = new AbsensiSiswaNewAdapter(activity, listHari);
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
                        alertNew(status, message);
                    }

                } catch (JSONException e) {
                    spinKit.setVisibility(View.GONE);
                    e.printStackTrace();
                    alertNew("Gagal menampilkan data!", "Error");
                }

            }

            @Override
            public void onError(ANError anError) {
                spinKit.setVisibility(View.GONE);
                alertNew(anError.toString(), "Error");
            }
        });
    }
    public static void alertNew(final String message, final String status) {
        if (status.equalsIgnoreCase("succes")) {
            SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
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
            SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
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

    public void alert(final String message, final String status) {
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


    public void showDialog() {
        dialogPlus = DialogPlus.newDialog(AbsensiHarianGuru.this)
                .setContentHolder(new ViewHolder(R.layout.dialog_qr_code))
                .setExpanded(false)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)  // or any custom width ie: 300
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .create();
        dialogPlus.show();
        ivBarcode = (ImageView) dialogPlus.findViewById(R.id.iv_barcode);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            int width = ss.getWidth() - 50;
            int height = ss.getWidth() - 50;

            BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeAbsensi + "", BarcodeFormat.QR_CODE, width, height);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivBarcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


}
