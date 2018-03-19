package com.openthos.fileselector.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.openthos.fileselector.R;

public class SaveFileDialog extends Dialog implements View.OnClickListener {
    private static SaveFileDialog mSaveFileDialog;
    private OnSaveFileDialogListener mDialogListener;
    private TextView mTitle;
    private EditText mDirName;
    private Button mCancel;
    private Button mSave;

    public static SaveFileDialog getInstance(Context context) {
        if (mSaveFileDialog == null) {
            mSaveFileDialog = new SaveFileDialog(context);
        }
        return mSaveFileDialog;
    }

    private SaveFileDialog(Context context) {
        super(context, R.style.DialogStyle);
        create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dir_dialog);
        initView();
        initListener();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mDirName = (EditText) findViewById(R.id.dir_name);
        mCancel = (Button) findViewById(R.id.cancel);
        mSave = (Button) findViewById(R.id.save);
    }

    private void initListener() {
        mCancel.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    public void showDialog(String title, OnSaveFileDialogListener dialogListener) {
        mDialogListener = dialogListener;
        mTitle.setText(title);
        mDirName.setText("");
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                if (mDialogListener != null) {
                    mDialogListener.cancel(mSaveFileDialog);
                }
                break;
            case R.id.save:
                if (mDialogListener != null) {
                    mDialogListener.save(mSaveFileDialog, mDirName.getText().toString());
                }
                break;
        }
    }

    public interface OnSaveFileDialogListener {
        void cancel(Dialog dialog);

        void save(Dialog dialog, String dirName);
    }
}
