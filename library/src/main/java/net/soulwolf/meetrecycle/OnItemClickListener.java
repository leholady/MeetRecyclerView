package net.soulwolf.meetrecycle;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by AllenCoder on 2016/8/03.
 *
 *
 * A convenience class to extend when you only want to OnItemClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 */
public abstract class OnItemClickListener extends SimpleClickListener {

    @Override
    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {

    }
}
