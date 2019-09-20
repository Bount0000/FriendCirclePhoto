package me.iwf.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.R;

/**
 * Created by donglua on 15/6/21.
 */
public class PhotoPagerAdapter extends PagerAdapter {

  private List<String> paths = new ArrayList<>();
  private List<String> times = new ArrayList<>();
  private List<String> coms = new ArrayList<>();
  private RequestManager mGlide;


  public PhotoPagerAdapter(RequestManager glide, List<String> paths,List<String> times,List<String> coms) {
    this.paths = paths;
    if(times!=null){
      this.times = times;
    }
   if(coms!=null){
     this.coms = coms;
   }

    this.mGlide = glide;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    final Context context = container.getContext();
    View itemView = LayoutInflater.from(context)
        .inflate(R.layout.__picker_picker_item_pager, container, false);

    final ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pager);

    TextView tv_time =  (TextView) itemView.findViewById(R.id.tv_time);
    TextView tv_com =  (TextView) itemView.findViewById(R.id.tv_com);
    LinearLayout ll_com = (LinearLayout)itemView.findViewById(R.id.ll_com);

    if(coms==null||coms.size()==0){
      ll_com.setVisibility(View.GONE);
    }else{  //如有带文字描述的
      ll_com.setVisibility(View.VISIBLE);
      final String time = times.get(position);
      final String com = coms.get(position);
      tv_time.setText(time);
      tv_com.setText(com);
    }

    final String path = paths.get(position);
    final Uri uri;
    if (path.startsWith("http")) {
      uri = Uri.parse(path);
    } else {
      uri = Uri.fromFile(new File(path));
    }

    Log.e("加载图片",uri.toString());
    mGlide.load(uri)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .thumbnail(0.1f)
        .dontAnimate()
        .dontTransform()
        .override(800, 800)
        .placeholder(R.drawable.__picker_ic_photo_black_48dp)
        .error(R.drawable.__picker_ic_broken_image_black_48dp)
        .into(imageView);

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (context instanceof Activity) {
          if (!((Activity) context).isFinishing()) {
            ((Activity) context).onBackPressed();
          }
        }
      }
    });

    container.addView(itemView);

    return itemView;
  }


  @Override public int getCount() {
    return paths == null ? 0 : paths.size();
  }


  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }


  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
    Glide.clear((View) object);
  }

  @Override
  public int getItemPosition (Object object) { return POSITION_NONE; }

}
