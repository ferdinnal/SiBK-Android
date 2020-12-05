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
import com.sibk.tasik.Model.HariAbsensiModel;
import com.sibk.tasik.Model.RiwayatPelanggaranModel;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RiwayatPelanggaranAdapter extends BaseAdapter {

    private Activity act;
    private LayoutInflater inflater;
    private List<RiwayatPelanggaranModel> listBank;
    private ScreenSize ss;
    private SimpleDraweeView ivProfile;
    private String fotoProfile;
    private TextView tvNamaLengkap2, tvNoHandphone, tvJenisKelamin, tvAlamatEmail, tvNisn;
    private boolean is_development_mode;
    private DialogPlus dialogPlus;

    public RiwayatPelanggaranAdapter(final Activity act, List<RiwayatPelanggaranModel> listBank) {
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
            convertView = inflater.inflate(R.layout.list_pelanggaran, null);

        ss = new ScreenSize(act);

        final RiwayatPelanggaranModel tp = listBank.get(position);
        TextView tvPelanggaran = (TextView) convertView.findViewById(R.id.tvPelanggaran);
        TextView tvPoin = (TextView) convertView.findViewById(R.id.tvPoin);
        TextView tvTanggal = (TextView) convertView.findViewById(R.id.tvTanggal);

        tvPelanggaran.setText(tp.getNamaPelanggaran());
        tvPoin.setText(tp.getPoin() + "");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy");
        String date2 = "";
        Date date = null;

        try {
            date = sdf.parse(tp.getDate());
            date2 = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvTanggal.setText(date2);

        return convertView;
    }


}