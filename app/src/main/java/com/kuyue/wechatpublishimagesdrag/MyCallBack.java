package com.kuyue.wechatpublishimagesdrag;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * 自定义拖拽
 * Created by kuyue on 2017/6/19 下午5:42.
 * 邮箱:595327086@qq.com
 */

public class MyCallBack extends ItemTouchHelper.Callback {

    private int dragFlags;
    private int swipeFlags;
    private PostArticleImgAdapter adapter;
    private List<String> images;//图片经过压缩处理
    private List<String> originImages;//图片没有经过处理，这里传这个进来是为了使原图片的顺序与拖拽顺序保持一致
    private boolean up;//手指抬起标记位

    public MyCallBack(PostArticleImgAdapter adapter, List<String> images, List<String> originImages) {
        this.adapter = adapter;
        this.images = images;
        this.originImages = originImages;
    }

    /**
     * 设置item是否处理拖拽事件和滑动事件，以及拖拽和滑动操作的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //判断 recyclerView的布局管理器数据
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {//设置能拖拽的方向
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;//0则不响应事件
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 当用户从item原来的位置拖动可以拖动的item到新位置的过程中调用
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到item原来的position
        int toPosition = target.getAdapterPosition();//得到目标position
        if (toPosition == images.size() - 1 || images.size() - 1 == fromPosition) {
            return true;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(images, i, i + 1);
                Collections.swap(originImages, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(images, i, i - 1);
                Collections.swap(originImages, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * 设置是否支持长按拖拽
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        adapter.notifyDataSetChanged();
        initData();
    }

    /**
     * 重置
     */
    private void initData() {
        if (dragListener != null) {
            dragListener.deleteState(false);
            dragListener.dragState(false);
        }
        up = false;
    }

    /**
     * 自定义拖动与滑动交互
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (null == dragListener) {
            return;
        }

        if (dY >= (recyclerView.getHeight()
                - viewHolder.itemView.getBottom()//item底部距离recyclerView顶部高度
                - CommonUtils.getPixelById(R.dimen.article_post_delete))) {//拖到删除处
            dragListener.deleteState(true);
            if (up) {//在删除处放手，则删除item
                viewHolder.itemView.setVisibility(View.INVISIBLE);//先设置不可见，如果不设置的话，会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHolder删除
                originImages.remove(viewHolder.getAdapterPosition());
                images.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                initData();
                return;
            }
        } else {//没有到删除处
            if (View.INVISIBLE == viewHolder.itemView.getVisibility()) {//如果viewHolder不可见，则表示用户放手，重置删除区域状态
                dragListener.dragState(false);
            }
            dragListener.deleteState(false);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     *
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && dragListener != null) {
            dragListener.dragState(true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 设置手指离开后ViewHolder的动画时间，在用户手指离开后调用
     *
     * @param recyclerView
     * @param animationType
     * @param animateDx
     * @param animateDy
     * @return
     */
    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        //手指放开
        up = true;
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    interface DragListener {
        /**
         * 用户是否将 item拖动到删除处，根据状态改变颜色
         *
         * @param delete
         */
        void deleteState(boolean delete);

        /**
         * 是否于拖拽状态
         *
         * @param start
         */
        void dragState(boolean start);
    }

    private DragListener dragListener;

    void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

}
