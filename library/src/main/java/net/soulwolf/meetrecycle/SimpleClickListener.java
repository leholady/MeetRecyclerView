package net.soulwolf.meetrecycle;

import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Soulwolf on 2016/8/03.
 * <p>
 * This can be useful for applications that wish to implement various forms of click and longclick and childView click
 * manipulation of item views within the RecyclerView. SimpleClickListener may intercept
 * a touch interaction already in progress even if the SimpleClickListener is already handling that
 * gesture stream itself for the purposes of scrolling.
 *
 * @see RecyclerView.OnItemTouchListener
 */
public abstract class SimpleClickListener implements RecyclerView.OnItemTouchListener, GestureDetector.OnGestureListener {

    private GestureDetectorCompat mGestureDetector;
    private RecyclerView mRecyclerView;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if(mRecyclerView == null){
            this.mRecyclerView = rv;
            this.mGestureDetector = new GestureDetectorCompat(rv.getContext(),this);
        }
        this.mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        View view = getChildViewUnder(e);
        if (view != null) {
            view.setPressed(true);
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        View view = getChildViewUnder(e);
        if (view == null) return false;
        view.setPressed(false);
        int position = shiftAdjustInt(mRecyclerView.getChildAdapterPosition(view));
        if (position != -1) {
            this.onItemClick(mRecyclerView, view, position);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        View view = getChildViewUnder(e);
        if (view == null) return;
        int position = shiftAdjustInt(mRecyclerView.getChildAdapterPosition(view));
        if (position != -1) {
            this.onItemLongClick(mRecyclerView, view, position);
        }
        view.setPressed(false);
    }

    private int shiftAdjustInt(int position){
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if(adapter != null && adapter instanceof MeetRecycleView.WrapAdapter){
            MeetRecycleView.WrapAdapter wrapAdapter = (MeetRecycleView.WrapAdapter)adapter;
            return wrapAdapter.shiftAdjustInt(position);
        }
        return position;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Nullable
    private View getChildViewUnder(MotionEvent e) {
        return mRecyclerView.findChildViewUnder(e.getX(), e.getY());
    }

    public abstract void onItemClick(RecyclerView parent, View clickedView, int position);

    public abstract void onItemLongClick(RecyclerView parent, View clickedView, int position);
}


