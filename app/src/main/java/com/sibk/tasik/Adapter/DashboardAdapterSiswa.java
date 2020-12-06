package com.sibk.tasik.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.MainActivitySiswa.AbsensiJadwal;
import com.sibk.tasik.MainActivitySiswa.Bimbingan;
import com.sibk.tasik.MainActivitySiswa.Jadwal;
import com.sibk.tasik.MainActivitySiswa.Konseling;
import com.sibk.tasik.MainActivitySiswa.MainActivity;
import com.sibk.tasik.MainActivitySiswa.Profiles;
import com.sibk.tasik.Model.DashboardModel;
import com.sibk.tasik.R;
import com.sibk.tasik.StartActivity.LoginActivity;
import com.sibk.tasik.Utility.ScreenSize;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DashboardAdapterSiswa extends RecyclerView.Adapter<DashboardAdapterSiswa.DashboardHolder> {

    private ScreenSize ss;
      public Activity activity;
      private ArrayList<DashboardModel> listdata;

    public DashboardAdapterSiswa(ArrayList<DashboardModel> listdata){
        this.listdata = listdata;
    }

    public DashboardAdapterSiswa(MainActivity mainActivity, ArrayList<DashboardModel> datamenu) {
        this.activity = mainActivity;
        this.listdata = datamenu;
    }

    @Override
    public DashboardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard,parent,false);
    DashboardHolder holder = new DashboardHolder(view);
    return holder;
    }

    @Override
    public void onBindViewHolder(DashboardHolder holder, int position) {
        ss = new ScreenSize(activity);

        final DashboardModel getData = listdata.get(position);

        String titlemenu = getData.getTitle();
        String logomenu = getData.getImg();
        int idds = getData.getIdDs();

        holder.imgMenu.getLayoutParams().height = ss.getWidth() / 8;
        holder.imgMenu.getLayoutParams().width = ss.getWidth() / 8;

        holder.titleMenu.setText(titlemenu);
        if (logomenu.equals("logo1")) {
            holder.imgMenu.setImageResource(R.drawable.ic_absensi);
        } else if (logomenu.equals("logo2")) {
            holder.imgMenu.setImageResource(R.drawable.ic_jadwal);
        } else if (logomenu.equals("logo3")) {
            holder.imgMenu.setImageResource(R.drawable.ic_konseling);
        } else if (logomenu.equals("logo4")) {
            holder.imgMenu.setImageResource(R.drawable.ic_bimbingan);
        } else if (logomenu.equals("logo5")) {
            holder.imgMenu.setImageResource(R.drawable.ic_user_login);
        } else if (logomenu.equals("logo6")) {
            holder.imgMenu.setImageResource(R.drawable.ic_logout);
        }

        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idds == 1) {
                    Intent i = new Intent(activity, AbsensiJadwal.class);
                    activity.startActivity(i);
                } else if (idds == 2) {
                    Intent i = new Intent(activity, Jadwal.class);
                    activity.startActivity(i);
                } else if (idds == 3) {
                    Intent i = new Intent(activity, Konseling.class);
                    activity.startActivity(i);
                } else if (idds == 4) {
                    Intent i = new Intent(activity, Bimbingan.class);
                    activity.startActivity(i);
                } else if (idds == 5) {
                    Intent i = new Intent(activity, Profiles.class);
                    activity.startActivity(i);
                }else if (idds == 6) {
                    alert("logout", "");
                }
            }
        });

    }
    public void alert(final String status, final String message) {
        if (status.equalsIgnoreCase("succes")) {
            SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
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
        } else if (status.equalsIgnoreCase("logout")) {
            SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
            pDialog.setTitleText("Logout");
            pDialog.setContentText("Apakah Anda yakin akan keluar?");
            pDialog.setCancelable(false);
            pDialog.setConfirmText("Ya");
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                    DBUser db_user = new DBUser(activity);
                    db_user.delete();
                    Intent i = new Intent(activity, LoginActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                }
            });
            pDialog.setCancelText("Tidak");
            pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            pDialog.show();
        } else {
            SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
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

    @Override
    public int getItemCount() {
        return listdata.size();

    }



    public class DashboardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleMenu;
        ImageView imgMenu;
                public DashboardHolder(View itemView) {
            super(itemView);

            titleMenu = itemView.findViewById(R.id.tvtitle);
            imgMenu = itemView.findViewById(R.id.ivLogo);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {


        }
    }
}
