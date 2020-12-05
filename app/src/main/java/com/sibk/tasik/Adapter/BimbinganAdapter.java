package com.sibk.tasik.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.dialogplus.DialogPlus;
import com.sibk.tasik.MainActivitySiswa.AbsensiHarianSiswa;
import com.sibk.tasik.Model.BimbinganModel;
import com.sibk.tasik.Model.HariAbsensiModel;
import com.sibk.tasik.Model.RiwayatPelanggaranModel;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BimbinganAdapter extends BaseAdapter {

    private Activity act;
    private LayoutInflater inflater;
    private List<BimbinganModel> listBank;
    private ScreenSize ss;
    private SimpleDraweeView ivProfile;
    private String fotoProfile;
    private TextView tvNamaLengkap2, tvNoHandphone, tvJenisKelamin, tvAlamatEmail, tvNisn;
    private boolean is_development_mode;
    private DialogPlus dialogPlus;

    public BimbinganAdapter(final Activity act, List<BimbinganModel> listBank) {
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
            convertView = inflater.inflate(R.layout.list_bimbingan, null);

        ss = new ScreenSize(act);

        final BimbinganModel tp = listBank.get(position);
        TextView tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
        TextView tvWaktu = (TextView) convertView.findViewById(R.id.tvWaktu);
        TextView tvTanggal = (TextView) convertView.findViewById(R.id.tvTanggal);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);

        tvSubject.setText(tp.getSubject());
        tvStatus.setText(tp.getSt() + "");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy");
        SimpleDateFormat timana = new SimpleDateFormat("hh:mm:ss");
        DateFormat Hasilwaktu = new SimpleDateFormat("HH:mm");
        String date2 = "";
        Date date = null;
        Date mulaiNa = null;
        String timeMulai = "";

        try {
            date = sdf.parse(tp.getTg());
            date2 = dateFormat.format(date);
            mulaiNa = timana.parse(tp.getWk());
            timeMulai = Hasilwaktu.format(mulaiNa);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvTanggal.setText(date2);
        tvWaktu.setText(timeMulai);

        return convertView;
    }


}