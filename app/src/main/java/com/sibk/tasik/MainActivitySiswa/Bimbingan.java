package com.sibk.tasik.MainActivitySiswa;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sibk.tasik.Adapter.RiwayatPelanggaranAdapter;
import com.sibk.tasik.Api.BimbinganApi;
import com.sibk.tasik.Api.PelanggaranApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.Model.RiwayatPelanggaranModel;
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

public class Bimbingan extends AppCompatActivity {
    private User userModel;
    private int userid, usertypeid;
    private ScreenSize ss;
    private DrawerLayout drawerLayout;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TextView titleBarName;
    private SmartRefreshLayout refreshLayout;
    private SpinKitView spinKit;
    private LinearLayout cvAll;
    private LinearLayout llJadwalBesokError;
    private ExpandableHeightListView ivPelajaran;
    private List<RiwayatPelanggaranModel> listHari = new ArrayList<RiwayatPelanggaranModel>();
    private RiwayatPelanggaranAdapter adapterHari;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bimbingan);
        ss = new ScreenSize(Bimbingan.this);
        DBUser dbUser = new DBUser(Bimbingan.this);
        userModel = dbUser.findUser();
        userid = userModel.getuserid();
        usertypeid = userModel.getusertypeid();


        initView();
        getData();
        setClick();
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleBarName = (TextView) findViewById(R.id.titleBarName);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        spinKit = (SpinKitView) findViewById(R.id.spinKit);
        cvAll = (LinearLayout) findViewById(R.id.cvAll);
        llJadwalBesokError = (LinearLayout) findViewById(R.id.llJadwalBesokError);
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
        listHari.clear();
        cvAll.setVisibility(View.GONE);
        AndroidNetworking.post(BimbinganApi.POST_FINDS_JADWAL)
                .addBodyParameter("userid", String.valueOf(userid))
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
                    JSONObject result = response.getJSONObject("sibk");
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray dataUser = result.getJSONArray("data_user");
                        for (int i = 0; i < dataUser.length(); i++) {
                            JSONObject c = dataUser.getJSONObject(i);
                            RiwayatPelanggaranModel x = new RiwayatPelanggaranModel();
                            x.setPoin(c.getInt("pp"));
                            x.setNamaPelanggaran(c.getString("name"));
                            x.setDate(c.getString("tg"));

                            listHari.add(x);
                        }
                        adapterHari = new RiwayatPelanggaranAdapter(Bimbingan.this, listHari);
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
