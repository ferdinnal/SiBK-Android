package com.sibk.tasik.MainActivityGuru;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.sibk.tasik.Adapter.AbsensiHariAdapter;
import com.sibk.tasik.Adapter.AbsensiHariNewAdapter;
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

public class AbsensiGuru extends AppCompatActivity {
    private User userModel;
    private int idPengguna, idTipePengguna;
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
    private AbsensiHariNewAdapter adapterHari;
    private ExpandableHeightListView ivPelajaran;
    public static int historyQrCodeId;
    private int idJadwalPelajaran;
    public static int hari_now;
    public static int hari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi);

        DBUser dbUser = new DBUser(AbsensiGuru.this);
        userModel = dbUser.findUser();
        idPengguna = userModel.getuserid();
        idTipePengguna = userModel.getusertypeid();
        ss = new ScreenSize(AbsensiGuru.this);

        Intent i = this.getIntent();
        idMataPelajaranGuru = i.getIntExtra("idMataPelajaranGuru", 0);
        namaGuru = i.getStringExtra("namaGuru");
        kodeMataPelajaran = i.getStringExtra("kodeMataPelajaran");
        namaMataPelajaran = i.getStringExtra("namaMataPelajaran");
        idKelas = i.getIntExtra("idKelas", 0);
        idJadwalPelajaran = i.getIntExtra("idJadwalPelajaran", 0);

        initView();
        generateQrCode();
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

    }

    private void setClick() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qrCodeAbsensi.equalsIgnoreCase("")) {
                    if (hari != hari_now) {
                        alert("Gagal menggenerate QR-Code karena bukan jadwal yang ditentukan", "Error");
                    } else {
                        generateQrCode();
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

    private void generateQrCode() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
        pDialog.setTitleText("Sedang Mengambil Data....");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(AbsensiApi.POST_GENERATE)
                .addBodyParameter("id_kelas", idKelas + "")
                .addBodyParameter("id_pengguna", idPengguna + "")
                .addBodyParameter("id_jadwal_pelajaran", idJadwalPelajaran + "")
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
                pDialog.dismissWithAnimation();
                try {
                    JSONObject result = response.getJSONObject("prilude");
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONObject dataSiswa = result.getJSONObject("data_siswa");
                        qrCodeAbsensi = dataSiswa.getString("qr_code_absensi");
                        historyQrCodeId = dataSiswa.getInt("history_qr_code_id");
                        hari_now = dataSiswa.getInt("hari_now");
                        hari = dataSiswa.getInt("hari");
                        getDataHari();
                    } else {
                        getDataHari();
//                        alert(status, message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismissWithAnimation();
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

    private void getDataHari() {
        spinKit.setVisibility(View.VISIBLE);
        listHari.clear();
        cvAll.setVisibility(View.GONE);
        AndroidNetworking.post(AbsensiApi.POST_FIND_HARI_NEW)
                .addBodyParameter("id_pengguna", String.valueOf(idPengguna))
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
                            x.setJamBeres(c.getString("mulai"));
                            x.setJamMulai(c.getString("selesai"));
                            x.setNamaKelas(c.getString("nama_kelas"));
                            x.setNamaMataPelajaran(namaMataPelajaran);
                            x.setKodeMataPelajaran(kodeMataPelajaran);
                            x.setDateCreated(c.getString("date_created"));
                            x.setIdJadwalPelajaran(c.getInt("jadid"));
                            x.setIdKelas(c.getInt("id_kelas"));
                            qrCodeAbsensi = c.getString("qrcode");
                            historyQrCodeId = c.getInt("history_qr_code_id");
                            hari_now = c.getInt("hari_now");
                            hari = c.getInt("hari");

                            listHari.add(x);
                        }
                        adapterHari = new AbsensiHariNewAdapter(AbsensiGuru.this, listHari);
                        ivPelajaran.setAdapter(adapterHari);
                    } else {
                        alert(status, "Mohon maaf absensi belum di temukan, silahkan generate qr-code sesuai hari dalam jadwal pelajaran.");
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


    public void showDialog() {
        dialogPlus = DialogPlus.newDialog(AbsensiGuru.this)
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


}
