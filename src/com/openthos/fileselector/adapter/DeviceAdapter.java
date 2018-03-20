package com.openthos.fileselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openthos.fileselector.OnFileClick;
import com.openthos.fileselector.R;
import com.openthos.fileselector.app.Constants;
import com.openthos.fileselector.bean.DeviceEntity;

import java.util.List;

public class DeviceAdapter extends BasicAdapter {
    private List<DeviceEntity> mData;
    private OnFileClick mFileClick;
    private View mLastLayout;
    private long mLastTime;

    public DeviceAdapter(Context context, List<DeviceEntity> data) {
        super(context);
        mData = data;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData != null ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_device, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DeviceEntity deviceEntity = mData.get(position);
        holder.name.setText(deviceEntity.getName());
        holder.img.setImageResource(deviceEntity.getDeviceIcon());
        holder.layout.setTag(deviceEntity.getDevicePath());
        return convertView;
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    public void setOnFileClick(OnFileClick fileClick) {
        mFileClick = fileClick;
    }

    public View getSelectView() {
        return mLastLayout;
    }

    private class ViewHolder implements View.OnTouchListener {
        private LinearLayout layout;
        private ImageView img;
        private TextView name;

        public ViewHolder(View view) {
            layout = (LinearLayout) view.findViewById(R.id.layout);
            img = (ImageView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            layout.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (event.getButtonState()) {
                    case MotionEvent.BUTTON_PRIMARY:
                        if (v == mLastLayout &&
                                System.currentTimeMillis() - mLastTime < Constants.DOUBLE_CLICK_TIME) {
                            //double click
                            if (mFileClick != null) {
                                mFileClick.onDoubleClick(v);
                            }
                        } else {
                            if (mFileClick != null) {
                                mFileClick.onClick(v);
                            }
                        }
//                        if (mLastLayout == null) {
//                            v.setSelected(true);
//                        } else if (mLastLayout != v) {
//                            mLastLayout.setSelected(false);
//                            v.setSelected(true);
//                        }
                        mLastLayout = v;
                        mLastTime = System.currentTimeMillis();
                        break;
                }
            }
            return false;
        }
    }
}
