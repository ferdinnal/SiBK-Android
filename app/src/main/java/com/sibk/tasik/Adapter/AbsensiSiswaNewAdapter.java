package com.sibk.tasik.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sibk.tasik.Model.SiswaAbsensiModel;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Response;

public class AbsensiSiswaNewAdapter extends BaseAdapter {

    private Activity act;
    private LayoutInflater inflater;
    private List<SiswaAbsensiModel> listBank;
    private ScreenSize ss;
    private SimpleDraweeView ivProfile;
    private String fotoProfile;
    private TextView tvNamaLengkap2, tvNoHandphone, tvJenisKelamin, tvAlamatEmail, tvNisn;
    private boolean is_development_mode;
    private DialogPlus dialogPlus;
    private TextView tvStatusAbsen;
    private String absensiName;
    private int abensiIdStatus;

    public AbsensiSiswaNewAdapter(final Activity act, List<SiswaAbsensiModel> listBank) {
        this.act = act;
        this.listBank = listBank;

    }

    @Override
    public int getCount() {
        return listBank.size();
    }

    @Override
    public Object getItem(int position) {
        return listBank.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_siswa_absensi, null);

        ss = new ScreenSize(act);

        final SiswaAbsensiModel tp = listBank.get(position);
        TextView tvNamaSiswa = (TextView) convertView.findViewById(R.id.tvNamaSiswa);
        TextView tvNisn = (TextView) convertView.findViewById(R.id.tvNisn);
        tvStatusAbsen = (TextView) convertView.findViewById(R.id.tvStatusAbsen);
        ImageView ivChangeStatus = (ImageView) convertView.findViewById(R.id.ivChangeStatus);
        CardView cvAll = (CardView) convertView.findViewById(R.id.cvAll);
        ivChangeStatus.setVisibility(View.INVISIBLE);

        tvNamaSiswa.setText(tp.getNamaSiswa());
        tvNisn.setText(tp.getNisn());
        tvStatusAbsen.setText(tp.getStatusAbsen());

//        ivChangeStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setDialog(tp.getAbsensiId());
//            }
//        });

        return convertView;
    }

//    void setDialog(final int absensiId) {
//        final String isina[] = {"Hadir", "Sakit", "Ijin", "Tanpa Keterangan"};//, "Merk"};
//        final int idNa[] = {1, 2, 3, 4};//, "Merk"};
//        final List<SimpleList> listItems = new ArrayList<>();
//        for (int i = 0; i < isina.length; i++) {
//            SimpleList item = new SimpleList();
//            item.setName(isina[i]);
//            item.setId(idNa[i]);
//            listItems.add(item);
//        }
//
//        ListDialogShortAdapter adapter3 = new ListDialogShortAdapter(act, listItems);
//        DialogPlus dialog = DialogPlus.newDialog(act)
//                .setAdapter(adapter3)
//                .setHeader(R.layout.dialog_pilih_short)
//                .setInAnimation(R.anim.abc_fade_in)
//                .setOutAnimation(R.anim.abc_fade_out)
//                .setOnItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//                        SimpleList list = listItems.get(position);
//                        absensiName = list.getName();
//                        abensiIdStatus = list.getId();
//                        tvStatusAbsen.setText(absensiName);
//                        if (list.getId() == 2 || list.getId() == 3) {
//                            Handler handler = new Handler();
//                            final Runnable r = new Runnable() {
//                                public void run() {
//                                    setDialogKet(absensiId, abensiIdStatus);
//                                    handler.postDelayed(this, 1000);
//                                }
//                            };
//
//                            handler.postDelayed(r, 1000);
//                        } else {
//                            updateAbsensi(absensiId, abensiIdStatus, "");
//                        }
//                        dialog.dismiss();
//                    }
//                })
//                .setExpanded(true)
//                .create();
//        dialog.show();
//    }

//    private void setDialogKet(final int absensiId, final int abensiIdStatus) {
//        dialogPlus = DialogPlus.newDialog(act)
//                .setContentHolder(new ViewHolder(R.layout.dialog_keterangan))
//                .setExpanded(false)
//                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)  // or any custom width ie: 300
//                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                .setInAnimation(R.anim.slide_in_bottom)
//                .setOutAnimation(R.anim.slide_out_bottom)
//                .create();
//        dialogPlus.show();
//        EditText etKet = (EditText) dialogPlus.findViewById(R.id.etKet);
//        Button btnOk = (Button) dialogPlus.findViewById(R.id.btnOk);
//
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (etKet.getText().length() > 0) {
//                    dialogPlus.dismiss();
//                    updateAbsensi(absensiId, abensiIdStatus, etKet.getText().toString());
//                } else {
//                    alert("error", "Jangan Kosongkan Keterangan!");
//                }
//            }
//        });
//
//
//    }

//    private void updateAbsensi(final int absensiId, final int abensiIdStatus, final String keterangan) {
//        final SweetAlertDialog pDialog = new SweetAlertDialog(act, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3D3489"));
//        pDialog.setTitleText("Sedang Mengupdate Data....");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        AndroidNetworking.post(AbsensiApi.POST_UPDATE_GURU)
//                .addBodyParameter("absensi_id", absensiId + "")
//                .addBodyParameter("status_absen_id", abensiIdStatus + "")
//                .addBodyParameter("keterangan", keterangan + "")
//                .setTag(this)
//                .setPriority(Priority.HIGH)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//
//                    }
//                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
//            @Override
//            public void onResponse(Response okHttpResponse, JSONObject response) {
//                Log.d("test", "onResponse object : " + response.toString());
//                pDialog.dismissWithAnimation();
//                try {
//                    JSONObject result = response.getJSONObject("prilude");
//                    String status = result.getString("status");
//                    String message = result.getString("message");
//
//                    if (status.equalsIgnoreCase("success")) {
//                        AbsensiHarianSiswa.getDataSiswa();
//                    } else {
//                        alert(status, message);
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    alert("Gagal menampilkan data!", "Error");
//                }
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//                alert("Tidak Ada Koneksi!", "Error");
//
//            }
//        });
//    }

    public void alert(final String status, final String message) {
        if (status.equalsIgnoreCase("succes")) {
            SweetAlertDialog pDialog = new SweetAlertDialog(act, SweetAlertDialog.SUCCESS_TYPE);
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
            SweetAlertDialog pDialog = new SweetAlertDialog(act, SweetAlertDialog.ERROR_TYPE);
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

