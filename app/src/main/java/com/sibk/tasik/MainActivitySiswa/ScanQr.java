package com.sibk.tasik.MainActivitySiswa;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.sibk.tasik.R;

public class ScanQr extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {


    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageView switchFlashlightButton;
    private androidx.appcompat.widget.Toolbar toolbar;
    private boolean isHurung = false;
    private ViewfinderView zxing_viewfinder_view;
    private BarcodeView zxing_barcode_surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        zxing_viewfinder_view = findViewById(R.id.zxing_viewfinder_view);
        zxing_barcode_surface = findViewById(R.id.zxing_barcode_surface);
        barcodeScannerView.setTorchListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Animation animation = AnimationUtils.loadAnimation(ScanQr.this, R.anim.anim_scan);
        final View bar = findViewById(R.id.bar);
        bar.setVisibility(View.VISIBLE);
        bar.startAnimation(animation);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                bar.setVisibility(View.VISIBLE);
//                bar.startAnimation(animation);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                bar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//
//        zxing_barcode_surface.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                return false;
//            }
//        });

//        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ef_ic_arrow_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        switchFlashlightButton = findViewById(R.id.switch_flashlight);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        switchFlashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHurung) {
                    barcodeScannerView.setTorchOff();
                } else {
                    barcodeScannerView.setTorchOn();
                }
                isHurung = !isHurung;
            }
        });

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();


    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }


    @Override
    public void onTorchOn() {
        switchFlashlightButton.setColorFilter(getResources().getColor(R.color.putih));
        switchFlashlightButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_gradient_2));
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setColorFilter(getResources().getColor(R.color.colorPrimaryDarkNew));
        switchFlashlightButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_rounded_line_3));
    }

}