package com.sibk.tasik.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.dialogplus.DialogPlus;
import com.sibk.tasik.Model.SiswaAbsensiModel;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AbsensiSiswaAdapter extends BaseAdapter {

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

    public AbsensiSiswaAdapter(final Activity act, List<SiswaAbsensiModel> listBank) {
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

        tvNamaSiswa.setText(tp.getNamaSiswa());
        tvNisn.setText(tp.getNisn());
        tvStatusAbsen.setText(tp.getStatusAbsen());

        return convertView;
    }



    public void alert(final String message, final String status) {
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
