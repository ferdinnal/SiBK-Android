package com.sibk.tasik.MainActivitySiswa;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sibk.tasik.Adapter.BimbinganAdapter;
import com.sibk.tasik.Api.BimbinganApi;
import com.sibk.tasik.Api.KonselingApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.Model.BimbinganModel;
import com.sibk.tasik.Model.User;
import com.sibk.tasik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Response;

public class FragJadwal extends Fragment {
    private View view;
    private User userModel;
    private int idPengguna, idTipePengguna;

    private List<BimbinganModel> listHari = new ArrayList<BimbinganModel>();
    private BimbinganAdapter adapterHari;
    private DrawerLayout drawerLayout;
    private SmartRefreshLayout refreshLayout;
    private SpinKitView spinKit;
    private LinearLayout cvAll;
    private LinearLayout llJadwalBesokError;
    private ExpandableHeightListView ivPelajaran;

    public FragJadwal() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jadwal, container, false);

        DBUser dbUser = new DBUser(getActivity());
        userModel = dbUser.findUser();
        idPengguna = userModel.getuserid();
        idTipePengguna = userModel.getusertypeid();

        initView();
        getData();
        setClick();

        return view;
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


    }

    private void getData() {
        spinKit.setVisibility(View.VISIBLE);
        listHari.clear();
        cvAll.setVisibility(View.GONE);
        AndroidNetworking.post(KonselingApi.POST_FINDS_JADWAL)
                .addBodyParameter("userid", String.valueOf(idPengguna))
                .addBodyParameter("status", "Pending")
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
                        JSONArray dataBimbingan = result.getJSONArray("data_bimbingan");
                        for (int i = 0; i < dataBimbingan.length(); i++) {
                            JSONObject c = dataBimbingan.getJSONObject(i);
                            BimbinganModel x = new BimbinganModel();
                            x.setBimidl(c.getInt("konid"));
                            x.setSubject(c.getString("subject"));
                            x.setTg(c.getString("tg"));
                            x.setWk(c.getString("wk"));
                            x.setSt(c.getString("st"));

                            listHari.add(x);
                        }
                        adapterHari = new BimbinganAdapter(getActivity(), listHari);
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
            SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
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
            SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
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


    private void initView() {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        spinKit = (SpinKitView) view.findViewById(R.id.spinKit);
        cvAll = (LinearLayout) view.findViewById(R.id.cvAll);
        llJadwalBesokError = (LinearLayout) view.findViewById(R.id.llJadwalBesokError);
        ivPelajaran = (ExpandableHeightListView) view.findViewById(R.id.ivPelajaran);
        ivPelajaran.setExpanded(true);
    }
}
