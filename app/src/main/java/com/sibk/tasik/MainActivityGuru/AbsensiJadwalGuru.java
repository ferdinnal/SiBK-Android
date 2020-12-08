package com.sibk.tasik.MainActivityGuru;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sibk.tasik.Adapter.PelajaranAbsensiAdapter;
import com.sibk.tasik.Adapter.PelajaranAbsensiGuruAdapter;
import com.sibk.tasik.Api.AbsensiApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.Model.PelajaranModel;
import com.sibk.tasik.Model.User;
import com.sibk.tasik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Response;

public class AbsensiJadwalGuru extends AppCompatActivity {
    private User userModel;
    private int idPengguna, idTipePengguna;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private TextView tvWebTitle;
    private SmartRefreshLayout refreshLayout;
    private SpinKitView spinKit;
    private LinearLayout cvPelajaran;
    private ExpandableHeightListView ivPelajaran;
    private ViewGroup kelompok;
    private List<PelajaranModel> listPelajaran = new ArrayList<PelajaranModel>();
    private PelajaranAbsensiGuruAdapter adapterPelajaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi_dan_tugas);

        DBUser dbUser = new DBUser(AbsensiJadwalGuru.this);
        userModel = dbUser.findUser();
        idPengguna = userModel.getuserid();

        setLayout();
        getData();
        setClick();
    }

    private void setLayout() {
        kelompok = findViewById(R.id.kelompok);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvWebTitle = (TextView) findViewById(R.id.tv_web_title);
        tvWebTitle.setText("Absensi");
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        spinKit = (SpinKitView) findViewById(R.id.spinKit);
        cvPelajaran = (LinearLayout) findViewById(R.id.cvPelajaran);
        ivPelajaran = (ExpandableHeightListView) findViewById(R.id.ivPelajaran);
        ivPelajaran.setExpanded(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

    }

    private void setClick() {
        refreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new BezierCircleHeader(context);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();
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

    private void getData() {
        spinKit.setVisibility(View.VISIBLE);
        cvPelajaran.setVisibility(View.GONE);
        listPelajaran.clear();
        AndroidNetworking.post(AbsensiApi.POST_FIND_PELAJARAN_GURU)
                .addBodyParameter("id_pengguna", String.valueOf(idPengguna))
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
                    cvPelajaran.setVisibility(View.VISIBLE);
                    JSONObject result = response.getJSONObject("prilude");
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray dataSiswa = result.getJSONArray("data_siswa");
                        for (int i = 0; i < dataSiswa.length(); i++) {
                            JSONObject c = dataSiswa.getJSONObject(i);
                            PelajaranModel x = new PelajaranModel();
                            x.setNamaGuru(c.getString("nama_guru"));
                            x.setKodeMataPelajaran(c.getString("kodemat"));
                            x.setNamaMataPelajaran(c.getString("matpel"));
                            x.setIdMataPelajaranGuru(c.getInt("idmat"));
                            x.setIdKelas(c.getInt("id_kelas"));
                            x.setIdJadwalPelajaran(c.getInt("jadid"));

                            listPelajaran.add(x);
                        }
                        adapterPelajaran = new PelajaranAbsensiGuruAdapter(AbsensiJadwalGuru.this, listPelajaran);
                        ivPelajaran.setAdapter(adapterPelajaran);
                    } else {
//                        alert(status, message);
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
            SweetAlertDialog pDialog = new SweetAlertDialog(AbsensiJadwalGuru.this, SweetAlertDialog.SUCCESS_TYPE);
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
        } else {
            SweetAlertDialog pDialog = new SweetAlertDialog(AbsensiJadwalGuru.this, SweetAlertDialog.ERROR_TYPE);
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

}
