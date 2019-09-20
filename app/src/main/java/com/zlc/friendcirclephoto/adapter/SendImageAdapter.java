package com.zlc.friendcirclephoto.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zlc.friendcirclephoto.R;
import com.zlc.friendcirclephoto.utils.DensityUtil;
import com.zlc.friendcirclephoto.utils.LogUtil;
import com.zlc.friendcirclephoto.utils.ScreenUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by zlc on 2017/5/25.
 */
public class SendImageAdapter extends RecyclerView.Adapter<SendImageAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Activity mContext;
    private List<Object> mDatas;
    private LayoutInflater mLayoutInflater;

    public SendImageAdapter(Activity context, List<Object> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public SendImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.send_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SendImageAdapter.ViewHolder viewHolder, final int position) {

        Object image = mDatas.get(position);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.id_rl_img.getLayoutParams();
        int w = params.width = (int) ((ScreenUtil.getScreenWidth(mContext) - DensityUtil.dp2px(mContext, 38)) / 3.0);
        params.height = w;
        params.bottomMargin = DensityUtil.dp2px(mContext, 7);
        params.rightMargin = DensityUtil.dp2px(mContext, 7);
        viewHolder.id_rl_img.setLayoutParams(params);

        if (image instanceof String) {
            viewHolder.im_delete.setVisibility(View.VISIBLE);
            viewHolder.iv_add.setVisibility(View.GONE);
            viewHolder.im_img.setVisibility(View.VISIBLE);
            //这里最好使用glide Picasso加载本地图片 bitmap容易造成内存溢出

           Glide.with(mContext).load(image).into(viewHolder.im_img);

//            Bitmap bitmap = BitmapFactory.decodeFile(image.toString());
//            viewHolder.im_img.setImageBitmap(bitmap);

            LogUtil.e("手机照片", image.toString());
        } else {
            viewHolder.im_delete.setVisibility(View.INVISIBLE);
            viewHolder.iv_add.setVisibility(View.VISIBLE);
            viewHolder.im_img.setVisibility(View.GONE);
            viewHolder.iv_add.setImageResource(R.drawable.tupiantianjia2x);
            LogUtil.e("添加照片", "" + image);
        }
        viewHolder.iv_add.setOnClickListener(new MyClickListener(viewHolder));
        viewHolder.im_img.setOnClickListener(new MyClickListener(viewHolder));
        viewHolder.im_delete.setOnClickListener(new MyClickListener(viewHolder));
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size() >= 9 ? 9 : mDatas.size();
    }

    public List<Object> getDatas() {
        return mDatas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView im_img;
        private final ImageView iv_add;
        private final ImageView im_delete;
        private final FrameLayout id_rl_img;

        public ViewHolder(View itemView) {
            super(itemView);
            im_img = (ImageView) itemView.findViewById(R.id.iv_img);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            im_delete = (ImageView) itemView.findViewById(R.id.im_delete);
            id_rl_img = (FrameLayout) itemView.findViewById(R.id.id_rl_img);
        }
    }

    //拖拽排序相关
    @Override
    public void onItemMove(int fromPos, int toPos) {
        if (fromPos == mDatas.size() - 1 || toPos == mDatas.size() - 1)
            return;
        if (fromPos < toPos)
            //向下拖动
            for (int i = fromPos; i < toPos; i++) {
                Collections.swap(mDatas, i, i + 1);
            }
        else {
            //向上拖动
            for (int i = fromPos; i > toPos; i--) {
                Collections.swap(mDatas, i, i - 1);
            }
        }
        notifyItemMoved(fromPos, toPos);
    }

    //滑动删除相关
    @Override
    public void onItemDel(int pos) {
        if (pos == mDatas.size() - 1)
            return;
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }


    public interface OnClickListener {
        void onClick(View v, int position);
    }

    private static OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class MyClickListener implements View.OnClickListener {

        private ViewHolder mHolder;

        public MyClickListener(ViewHolder holder) {
            this.mHolder = holder;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_add:
                case R.id.im_delete:
                case R.id.iv_img:
                    if (onClickListener != null) {
                        int pos = mHolder.getAdapterPosition();
                        onClickListener.onClick(view, pos);
                    }
                    break;
            }
        }
    }
}