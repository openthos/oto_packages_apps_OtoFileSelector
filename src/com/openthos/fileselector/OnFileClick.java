package com.openthos.fileselector;

import android.view.View;

public interface OnFileClick {
    void onClick(View view, int position);

    void onDoubleClick(View view, int position);
}
