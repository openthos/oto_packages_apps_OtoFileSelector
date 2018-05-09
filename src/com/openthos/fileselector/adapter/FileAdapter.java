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
import com.openthos.fileselector.utils.FileIconHelper;
import com.openthos.fileselector.utils.Tools;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileAdapter extends BasicAdapter {
    private List<File> mData;
    private View mLastLayout;
    private long mLastTime;
    private File mSelectFile;
    private OnFileClick mFileClick;

    public FileAdapter(Context context, List<File> data) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File file = mData.get(position);
        if (file.isDirectory()) {
            holder.img.setImageResource(R.mipmap.suffix_folder);
        } else {
            String filePath = file.getAbsolutePath();
            if (filePath.toLowerCase().endsWith("apk")) {
                holder.img.setImageDrawable(FileIconHelper.getAppDrawable(mContext, filePath));
            } else {
                holder.img.setImageResource(FileIconHelper.getFileIcon(filePath));
            }
        }
        holder.name.setText(file.getName());
        holder.modify.setText(Tools.transformTimeStr(file.lastModified()));
        holder.layout.setTag(position);
        return convertView;
    }

    public void refresh() {
        Collections.sort(mData, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if ((o1.isDirectory() && o2.isDirectory()) || (o1.isFile() && o2.isFile())) {
                    return o1.getName().compareTo(o2.getName());
                } else if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        if (mLastLayout != null) {
            mLastLayout.setSelected(false);
            mLastLayout = null;
        }
        mSelectFile = null;
        notifyDataSetChanged();
    }

    public File getSelectFile() {
        return mSelectFile;
    }

    public void setOnFileClick(OnFileClick fileClick) {
        mFileClick = fileClick;
    }

    private class ViewHolder implements View.OnTouchListener {
        private LinearLayout layout;
        private ImageView img;
        private TextView name;
        private TextView modify;

        public ViewHolder(View view) {
            layout = (LinearLayout) view.findViewById(R.id.layout);
            img = (ImageView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            modify = (TextView) view.findViewById(R.id.modify);
            layout.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (event.getButtonState()) {
                    case MotionEvent.BUTTON_PRIMARY:
                        Integer position = (Integer) v.getTag();
                        mSelectFile = mData.get(position);
                        if (v == mLastLayout &&
                                System.currentTimeMillis() - mLastTime < Constants.DOUBLE_CLICK_TIME) {
                            //double click
                            if (mFileClick != null) {
                                mFileClick.onDoubleClick(v, position);
                            }
                        } else {
                            if (mFileClick != null) {
                                mFileClick.onClick(v, position);
                            }
                        }
                        if (mLastLayout == null) {
                            v.setSelected(true);
                        } else if (mLastLayout != v) {
                            mLastLayout.setSelected(false);
                            v.setSelected(true);
                        }
                        mLastLayout = v;
                        mLastTime = System.currentTimeMillis();
                        break;
                }
            }
            return false;
        }
    }
}
