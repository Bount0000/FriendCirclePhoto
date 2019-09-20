package com.zlc.friendcirclephoto.adapter;

/**
 * Created by zlc on 2017/10/12.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPos, int toPos); //长按拖拽

    void onItemDel(int pos);  //滑动删除
}
