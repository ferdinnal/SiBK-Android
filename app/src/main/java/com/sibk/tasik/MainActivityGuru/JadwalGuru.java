package com.sibk.tasik.MainActivityGuru;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.sibk.tasik.Api.JadwalPelajaranApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.MainActivitySiswa.Jadwal;
import com.sibk.tasik.Model.User;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.HttpHandler;
import com.sibk.tasik.Utility.Website;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Response;

import static com.sibk.tasik.Api.JadwalPelajaranApi.POST_FINDS_GURU;

public class JadwalGuru extends AppCompatActivity {

    private AppBarLayout appBar;
    private Toolbar toolbar;
    private TextView tvWebTitle;
    private SmartRefreshLayout refreshLayout;
    private SpinKitView spinKit;
    private CardView cvAll;
    private LinearLayout llJadwalBesokError;
    private LinearLayout llJadwalBesok;
    private User userModel;
    private int idPengguna, idTipePengguna;
    private ViewGroup kelompok;
    private LinearLayout llBawah;
    private LayoutInflater inflater_detail;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_semester_ini);

        DBUser dbUser = new DBUser(JadwalGuru.this);
        userModel = dbUser.findUser();
        idPengguna = userModel.getuserid();
        idTipePengguna = userModel.getusertypeid();

        setLayout();
        getData();
        setClick();
    }

    private void setLayout() {
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        tvWebTitle = (TextView) findViewById(R.id.tv_web_title);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        spinKit = (SpinKitView) findViewById(R.id.spinKit);
        cvAll = (CardView) findViewById(R.id.cvAll);
        llJadwalBesokError = (LinearLayout) findViewById(R.id.llJadwalBesokError);
        llJadwalBesok = (LinearLayout) findViewById(R.id.llJadwalBesok);
        kelompok = findViewById(R.id.kelompok);
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
        cvAll.setVisibility(View.GONE);
        llJadwalBesok.removeAllViews();
        AndroidNetworking.post(JadwalPelajaranApi.POST_FINDS_GURU)
                .addBodyParameter("userid", String.valueOf(idPengguna))
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
                    cvAll.setVisibility(View.VISIBLE);
                    JSONObject result = response.getJSONObject("sibk");
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray dataJadwal = result.getJSONArray("data_jadwal");

                        for (int i = 0; i < dataJadwal.length(); i++) {
                            JSONObject c = dataJadwal.getJSONObject(i);

                            final LayoutInflater li = getLayoutInflater();
                            View line = li.inflate(R.layout.list_pertemuan_top, null);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 0, 0, 0);
                            line.setLayoutParams(layoutParams);
                            final TextView tvHari = (TextView) line.findViewById(R.id.tvHari);

                            String hariNa;
                            int intHari = c.getInt("hari");
                            if (intHari == 1) {
                                hariNa = "Senin";
                            } else if (intHari == 2) {
                                hariNa = "Selasa";
                            } else if (intHari == 3) {
                                hariNa = "Rabu";
                            } else if (intHari == 4) {
                                hariNa = "Kamis";
                            } else if (intHari == 5) {
                                hariNa = "Jumat";
                            } else if (intHari == 6) {
                                hariNa = "Sabtu";
                            } else {
                                hariNa = "Minggu";
                            }
                            tvHari.setText(hariNa);

//                            llJadwalBesok.addView(line);

                            inflater_detail = getLayoutInflater();

                            JadwalGuru.GetTransactionDetail gc = new JadwalGuru.GetTransactionDetail();

                            try {
                                String transaction_detail_json = gc.execute(c.getInt("hari")).get();

                                if (transaction_detail_json != null) {
                                    spinKit.setVisibility(View.GONE);
                                    try {
                                        JSONArray detail_items = new JSONArray(transaction_detail_json);
                                        LinearLayout llBawah = (LinearLayout) line.findViewById(R.id.llBawah);

                                        LayoutInflater inflater_detail = JadwalGuru.this.getLayoutInflater();
                                        llBawah.removeAllViews();

                                        for (int in = 0; in < detail_items.length(); in++) {
                                            final JSONObject detail = detail_items.getJSONObject(in);
                                            View vDetail = inflater_detail.inflate(R.layout.list_pertemuan_detail, null);
                                            LinearLayout.LayoutParams layoutParamDetail = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                            layoutParamDetail.setMargins(0, 0, 0, 0);
                                            vDetail.setLayoutParams(layoutParamDetail);
                                            TextView tvNamaMateri = (TextView) vDetail.findViewById(R.id.tvNamaMateri);
                                            TextView tvNamaGuru = (TextView) vDetail.findViewById(R.id.tvNamaGuru);
                                            TextView tvTanggal = (TextView) vDetail.findViewById(R.id.tvTanggal);
                                            TextView tvWaktu = (TextView) vDetail.findViewById(R.id.tvWaktu);
                                            TextView tvRuangan = (TextView) vDetail.findViewById(R.id.tvRuangan);
                                            TextView tvSemester = (TextView) vDetail.findViewById(R.id.tvSemester);
                                            TextView tvKelas = (TextView) vDetail.findViewById(R.id.tvKelas);


                                            String date2 = "";
                                            String timeMulai = "";
                                            String timeBeres = "";
                                            String mulai = detail.getString("mulai");
                                            String beres = detail.getString("beres");
//                                            "tanggal_now": "2020-02-24",
//                                                    "tanggal_1": "2020-02-25",
//                                                    "tanggal_2": "2020-02-26",
//                                                    "tanggal_3": "2020-02-27",
//                                                    "tanggal_4": "2020-02-28",
//                                                    "tanggal_5": "2020-02-29",
//                                                    "tanggal_6": "2020-03-01",
//                                                    "hari_now": "1",
//                                                    "hari_1": "2",
//                                                    "hari_2": "3",
//                                                    "hari_3": "4",
//                                                    "hari_4": "5",
//                                                    "hari_5": "6",
//                                                    "hari_6": "7",

                                            if (c.getInt("hari") == detail.getInt("hari_1")) {
                                                time = detail.getString("tanggal_1");
                                            } else if (c.getInt("hari") == detail.getInt("hari_2")) {
                                                time = detail.getString("tanggal_2");
                                            } else if (c.getInt("hari") == detail.getInt("hari_3")) {
                                                time = detail.getString("tanggal_3");
                                            } else if (c.getInt("hari") == detail.getInt("hari_4")) {
                                                time = detail.getString("tanggal_4");
                                            } else if (c.getInt("hari") == detail.getInt("hari_5")) {
                                                time = detail.getString("tanggal_5");
                                            } else if (c.getInt("hari") == detail.getInt("hari_6")) {
                                                time = detail.getString("tanggal_6");
                                            } else if (c.getInt("hari") == detail.getInt("hari_7")) {
                                                time = detail.getString("tanggal_7");
                                            }
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat timana = new SimpleDateFormat("hh:mm:ss");
                                            DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy");
                                            DateFormat Hasilwaktu = new SimpleDateFormat("HH:mm");
                                            Date date = null;
                                            Date mulaiNa = null;
                                            Date beresNa = null;
                                            try {
                                                date = sdf.parse(time);
                                                date2 = dateFormat.format(date);
                                                mulaiNa = timana.parse(mulai);
                                                timeMulai = Hasilwaktu.format(mulaiNa);
                                                beresNa = timana.parse(beres);
                                                timeBeres = Hasilwaktu.format(beresNa);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            String semesterNa = "";
                                            if (detail.getInt("semester") == 1) {
                                                semesterNa = "Ganjil";
                                            } else {
                                                semesterNa = "Genap";
                                            }

                                            tvSemester.setText(semesterNa);
                                            tvNamaMateri.setText(detail.getString("matpel"));
                                            tvNamaGuru.setText(detail.getString("nama_guru"));
                                            tvTanggal.setText(date2);

                                            tvWaktu.setText(timeMulai + " - " + timeBeres);
                                            tvRuangan.setText(detail.getString("nama_ruangan"));
                                            tvKelas.setText(c.getString("nama_kelas"));

                                            llBawah.addView(vDetail);
                                        }

                                    } catch (final JSONException e) {
                                        Log.e("JSON", "Json parsing error: " + e.getMessage());
                                        JadwalGuru.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(JadwalGuru.this,
                                                        "Json parsing error: " + e.getMessage(),
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        });
                                    }
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            llJadwalBesok.addView(line);

                        }

                    } else {
                        alert(status, message);
                        llJadwalBesokError.setVisibility(View.VISIBLE);
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

    private class GetTransactionDetail extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        @Override
        protected String doInBackground(Integer... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            Website web = new Website();

            String url = web.getNewDomain() + "/Jadwal_pelajaran/jadwal_pelajaran_detail_list/" + arg0[0] + "?hash=" + web.getHash();
            Log.d("test", url);
            String jsonStr = sh.makeServiceCall(url);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public void alert(final String status, final String message) {
        if (status.equalsIgnoreCase("succes")) {
            SweetAlertDialog pDialog = new SweetAlertDialog(JadwalGuru.this, SweetAlertDialog.SUCCESS_TYPE);
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
            SweetAlertDialog pDialog = new SweetAlertDialog(JadwalGuru.this, SweetAlertDialog.ERROR_TYPE);
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

