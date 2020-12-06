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
import com.sibk.tasik.MainActivityGuru.AbsensiHarianGuru;
import com.sibk.tasik.MainActivitySiswa.AbsensiHarianSiswa;
import com.sibk.tasik.Model.HariAbsensiModel;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AbsensiHariNewAdapter extends BaseAdapter {

    private Activity act;
    private LayoutInflater inflater;
    private List<HariAbsensiModel> listBank;
    private ScreenSize ss;
    private SimpleDraweeView ivProfile;
    private String fotoProfile;
    private TextView tvNamaLengkap2, tvNoHandphone, tvJenisKelamin, tvAlamatEmail, tvNisn;
    private boolean is_development_mode;
    private DialogPlus dialogPlus;

    public AbsensiHariNewAdapter(final Activity act, List<HariAbsensiModel> listBank) {
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
            convertView = inflater.inflate(R.layout.list_hari_absensi, null);

        ss = new ScreenSize(act);

        final HariAbsensiModel tp = listBank.get(position);
        TextView tvTanggal = (TextView) convertView.findViewById(R.id.tvTanggal);
        TextView tvKelas = (TextView) convertView.findViewById(R.id.tvKelas);
        TextView tvHari = (TextView) convertView.findViewById(R.id.tvHari);
        TextView tvJam = (TextView) convertView.findViewById(R.id.tvJam);
        CardView cvAll = (CardView) convertView.findViewById(R.id.cvAll);

        tvKelas.setText(tp.getNamaKelas());

        String hariNa;
        int intHari = tp.getHari();
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

        tvHari.setText(hariNa + "");

        Date mulaiNa = null;
        Date beresNa = null;
        String timeMulai = "";
        String timeBeres = "";
        SimpleDateFormat timana = new SimpleDateFormat("hh:mm:ss");
        DateFormat Hasilwaktu = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy");
        String date2 = "";
        Date date = null;

        try {
            date = sdf.parse(tp.getDateCreated());
            date2 = dateFormat.format(date);
            mulaiNa = timana.parse(tp.getJamMulai());
            timeMulai = Hasilwaktu.format(mulaiNa);
            beresNa = timana.parse(tp.getJamBeres());
            timeBeres = Hasilwaktu.format(beresNa);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvJam.setText(timeMulai + " - " + timeBeres);
        tvTanggal.setText(date2);

        cvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, AbsensiHarianGuru.class);
                i.putExtra("idJadwalPelajaran", tp.getIdJadwalPelajaran());
                i.putExtra("kodeMataPelajaran", tp.getKodeMataPelajaran());
                i.putExtra("namaMataPelajaran", tp.getNamaMataPelajaran());
                i.putExtra("idKelas", tp.getIdKelas());
                i.putExtra("historyQrCodeId", tp.getHistoryQrCodeId());
                act.startActivity(i);
            }
        });
        return convertView;
    }


}
