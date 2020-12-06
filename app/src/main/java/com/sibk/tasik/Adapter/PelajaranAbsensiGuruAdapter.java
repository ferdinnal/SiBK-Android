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
import com.sibk.tasik.MainActivityGuru.AbsensiGuru;
import com.sibk.tasik.Model.PelajaranModel;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;

import java.util.List;

public class PelajaranAbsensiGuruAdapter extends BaseAdapter {
    private Activity act;
    private LayoutInflater inflater;
    private List<PelajaranModel> listBank;
    private ScreenSize ss;
    private SimpleDraweeView ivProfile;
    private String fotoProfile;
    private TextView tvNamaLengkap2,tvNoHandphone,tvJenisKelamin,tvAlamatEmail,tvNisn;
    private boolean is_development_mode;
    private DialogPlus dialogPlus;

    public PelajaranAbsensiGuruAdapter(final Activity act, List<PelajaranModel> listBank)
    {
        this.act=act;
        this.listBank=listBank;

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
            inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_pelajaran, null);

        ss = new ScreenSize(act);

        final PelajaranModel tp = listBank.get(position);
        TextView tvPelajaran=(TextView)convertView.findViewById(R.id.tvPelajaran);
        TextView tvKode=(TextView)convertView.findViewById(R.id.tvKode);
        TextView tvGuru=(TextView)convertView.findViewById(R.id.tvGuru);
        CardView cvAll=(CardView)convertView.findViewById(R.id.cvAll);

        tvPelajaran.setText(tp.getNamaMataPelajaran());
        tvKode.setText(tp.getKodeMataPelajaran());
        tvGuru.setText(tp.getNamaGuru());

        cvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, AbsensiGuru.class);
                i.putExtra("idMataPelajaranGuru", tp.getIdMataPelajaranGuru());
                i.putExtra("namaGuru", tp.getNamaGuru());
                i.putExtra("kodeMataPelajaran", tp.getKodeMataPelajaran());
                i.putExtra("namaMataPelajaran", tp.getNamaMataPelajaran());
                i.putExtra("idKelas", tp.getIdKelas());
                i.putExtra("idJadwalPelajaran", tp.getIdJadwalPelajaran());
                act.startActivity(i);
            }
        });
        return convertView;
    }
}

