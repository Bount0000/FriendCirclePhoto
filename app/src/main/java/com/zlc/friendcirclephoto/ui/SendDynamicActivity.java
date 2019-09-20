package com.zlc.friendcirclephoto.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.zlc.friendcirclephoto.adapter.MyItemTouchHelperCallback;
import com.zlc.friendcirclephoto.adapter.SendImageAdapter;
import com.zlc.friendcirclephoto.view.CommonEditText;
import com.zlc.friendcirclephoto.R;
import com.zlc.friendcirclephoto.utils.LogUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by zlc
 * 发布朋友圈
 */
public class SendDynamicActivity extends BaseActivity implements View.OnClickListener,SendImageAdapter.OnClickListener{

    private ImageView im_fanhui;
    private TextView tv_title;
    private CommonEditText id_et_content;
    private TextView id_tv_num;
    private TextView id_tv_max_num;
    private SendImageAdapter mPhotoAdapter;
    public static final int maxPhoto = 9; //最大选择几张照片
    private List<Object> images = new ArrayList<>();
    private ArrayList<String> photos = new ArrayList<>();
    private RecyclerView mRecycleView;
    private TextView id_tv_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_note);
        initView();
        initData();
        initListener();
    }

    private void initView() {

        im_fanhui = findView(R.id.id_iv_back);
        tv_title = findView(R.id.id_tv_title);
        id_tv_send = findView(R.id.id_tv_send);
        tv_title.setText("发布");
        id_tv_send.setText("发送");
        id_et_content = findView(R.id.id_common_et);
        mRecycleView = findView(R.id.id_recycleview);
        id_tv_num = findView(R.id.tv_num);
        id_tv_max_num = findView(R.id.id_tv_max_num);
        id_et_content.setHint("这一刻的想法......");
        id_tv_num.setText("0");
        id_tv_max_num.setText("/188");
        id_et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(188)});
    }


    private void initData() {
        images.add(0);
        mRecycleView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        mPhotoAdapter = new SendImageAdapter(SendDynamicActivity.this, images);
        mRecycleView.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnClickListener(this);
        MyItemTouchHelperCallback callBack = new MyItemTouchHelperCallback(mPhotoAdapter);
        //实现拖拽排序
        final ItemTouchHelper touchHelper = new ItemTouchHelper(callBack);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(mRecycleView);
    }

    private void initListener() {
        im_fanhui.setOnClickListener(this);
        id_tv_send.setOnClickListener(this);
        id_et_content.setOnTextChaged(new CommonEditText.OnTextChaged() {
            @Override
            public void setText(String s) {
                id_tv_num.setText(s.length()+"");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_iv_back:
                finish();
                break;
            case R.id.id_tv_send:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            if (photos != null) {
                clearPhotoS(photos.size());
                images.addAll(images.size()-1,photos);
                mPhotoAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onClick(View v, int position) {
        switch (v.getId()){
            case R.id.iv_add:
                selectPhoto();
                break;
            case R.id.im_delete:
                delImages(position);
                break;
            case R.id.iv_img:
                lookPhoto(position);
                break;
        }
    }

    public void delImages(int position){
        images.remove(position);
        if(mPhotoAdapter!=null)
            mPhotoAdapter.notifyItemRemoved(position);
    }

    public void  clearPhotoS(int size){
        LogUtil.e("返回有几张照片",size+"");
        images.clear();
        images.add(1);
    }

    //选择图片
    private void selectPhoto() {

        List<Object> images = mPhotoAdapter.getDatas();
        photos.clear();
        for (int i = 0;i< images.size()-1;i++ ){
            photos.add(images.get(i).toString());
        }
        //权限
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        LogUtil.e("msg", "权限外全部通过");
                        PhotoPicker.builder()
                                .setPhotoCount(maxPhoto)
                                .setShowCamera(true)
                                .setSelected(photos)
                                .start(SendDynamicActivity.this);
                    }
                    @Override
                    public void onDenied(List<String> permissions) {
                        LogUtil.e(permissions.toString() ,"权限拒绝");
                    }
                });
    }

    //查看图片
    private void lookPhoto(int position){
        photos.clear();
        List<Object> images = mPhotoAdapter.getDatas();
        for (int i = 0;i< images.size()-1;i++ ){
            photos.add(images.get(i).toString());
        }
        PhotoPreview.builder()
                .setPhotos(photos)
                .setCurrentItem(position)
                .setShowDeleteButton(true)  //是否显示删除按钮
                .start(this);
    }
}
