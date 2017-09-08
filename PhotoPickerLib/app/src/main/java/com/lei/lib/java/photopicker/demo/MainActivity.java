package com.lei.lib.java.photopicker.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.iv);

        RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            //开始设置
                            //设置图片格式
                            UCrop.Options options = new UCrop.Options();
                            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                            options.setHideBottomControls(true);
                            options.setCompressionQuality(100);
//                            options.setMaxBitmapSize(500);
                            options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                            options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                            options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);

                            Matisse.from(MainActivity.this)
                                    .choose(MimeType.ofImage())
                                    .theme(R.style.Matisse_Default)
                                    .countable(true)
                                    .capture(true)
                                    .captureStrategy(
                                            new CaptureStrategy(true, "com.lei.lib.java.photopicker.demo.fileprovider"))
                                    .maxSelectable(9)
                                    .showSingleMediaType(true)
                                    .useProfilePhotoMode(true)
                                    .setCropOptions(options)
                                    .setCropRatio(0, 2)
                                    .setProfileFileName("heihei.jpeg")
                                    .gridExpectedSize(
                                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(REQUEST_CODE_CHOOSE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Log.e("测试", Matisse.obtainResult(data) + "================" + Matisse.obtainPathResult(data));
            Glide.with(MainActivity.this)
                    .load(Matisse.obtainResult(data).get(0))
                    .fitCenter()
                    .into(imageView);
        }
    }
}
