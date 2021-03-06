package com.sibk.tasik.MainActivityGuru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.sibk.tasik.Adapter.DashboardAdapterGuru;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.Model.DashboardModel;
import com.sibk.tasik.Model.User;
import com.sibk.tasik.R;
import com.sibk.tasik.Service.NotificationService;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivityGuru extends AppCompatActivity {
    RecyclerView recyclerView;
    private boolean doublePressed = false;
    private String fullname;
    private int usertypeid;
    private TextView tvName;
    private User userModel;
    private int idPengguna, idTipePengguna;

    ArrayList<DashboardModel> datamenu;
    GridLayoutManager gridLayoutManager;
    DashboardAdapterGuru adapter;
    private Timer mTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentService = new Intent(this, NotificationService.class);
        startService(intentService);


        DBUser dbUser = new DBUser(MainActivityGuru.this);
        userModel = dbUser.findUser();
        idPengguna = userModel.getuserid();
        idTipePengguna = userModel.getusertypeid();
        fullname = userModel.getfullname();

        recyclerView = findViewById(R.id.rv_menu);

        initview();
        adddata();
        gridLayoutManager = new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new DashboardAdapterGuru(datamenu);

    }

    private void initview(){
        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(fullname);
    }

    public void adddata(){
        datamenu = new ArrayList<>();
        datamenu.add(new DashboardModel("Absensi","logo1",1));
        datamenu.add(new DashboardModel("Jadwal Pelajaran","logo2",2));
        datamenu.add(new DashboardModel("Profile","logo5",3));
        datamenu.add(new DashboardModel("Logout","logo6",4));

        adapter = new DashboardAdapterGuru(MainActivityGuru.this, datamenu);
        recyclerView.setAdapter(adapter);

    }
    public void onBackPressed() {
        if (doublePressed) {
            super.onBackPressed();
            return;
        }

        this.doublePressed = true;
        Toast.makeText(this, "Tekan lagi untuk keluar !", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doublePressed = false;
            }
        }, 2000);
    }
}