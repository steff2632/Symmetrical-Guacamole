package com.example.symmetrical_guacamole.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.symmetrical_guacamole.ImageLoader;
import com.example.symmetrical_guacamole.InternetService;
import com.example.symmetrical_guacamole.MyItem;
import com.example.symmetrical_guacamole.R;
import com.example.symmetrical_guacamole.fragments.PlaceholderFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by stefanmay on 03/02/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private PlaceholderFragment placeholderFragment;
    // Can you think of a better way to represent these items???
    private MyItem[] mItems;
    private Context context;
    private BitmapBroadcastReceiver bitmapBroadcastReceiver;

    public MyAdapter(Context context, MyItem[] items) {
        this.context = context;
        mItems = items;
        bitmapBroadcastReceiver = new BitmapBroadcastReceiver();
//        LocalBroadcastManager.getInstance(context).registerReceiver(bitmapBroadcastReceiver, new IntentFilter(InternetService.Actions.GET_BYTES));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyItem myItem = mItems[position];
        holder.mItem = myItem;

        holder.mTitle.setText(myItem.title);
        holder.mDescription.setText(myItem.desc);

        if(myItem.image != null)
            Picasso.with(holder.mImageView.getContext()).load(myItem.image).into(holder.mImageView);
//            holder.mImageView.setImageBitmap(PicmyItem.image);
    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

    public void setItems(MyItem[] items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public MyItem getItem(String title) {
        for(MyItem myItem : mItems) {
            if(myItem.title.matches(title))
                return myItem;
        }
        return null;
    }

    public void updateItem(MyItem myItem) {
        for(int i = 0; i < mItems.length; i++) {
            MyItem myItem1 = mItems[i];
            if(myItem1.title.matches(myItem.title))
                mItems[i] = myItem;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTitle;
        public final TextView mDescription;
        public final ImageView mImageView;
        public Object mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTitle = (TextView) mView.findViewById(R.id.title);
            mDescription = (TextView) mView.findViewById(R.id.desc);
            mImageView = (ImageView) mView.findViewById(R.id.image);
        }
    }

    public class BitmapBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra(InternetService.Extras.BYTES_EXTRA)) {
                byte[] bytes = intent.getByteArrayExtra(InternetService.Extras.BYTES_EXTRA);

                Bitmap bitmap = ImageLoader.convertToBitmap(bytes);

                MyItem myItem = (MyItem) intent.getSerializableExtra(InternetService.Extras.ITEM_EXTRA);
 //               myItem.image = bitmap;

                updateItem(myItem);
            }
        }
    }
}
