package com.safe.keyboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safe.keyboard.jni.IJniInterface;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SafeKeyboard safeKeyboard;
    private SafeKeyboard safeKeyboard2;

    static {
        System.loadLibrary("securityKey");
    }

    private EditText safeEdit;
    private EditText safeEdit2;
    private EditText safeEdit3;
    private List<EditText> editList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置当前activity不被录制以及截屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        safeEdit = findViewById(R.id.safeEditText);
        safeEdit2 = findViewById(R.id.safeEditText2);
        safeEdit3 = findViewById(R.id.safeEditText3);
        editList.add(safeEdit);
        editList.add(safeEdit2);
        editList.add(safeEdit3);
       /* disableCopyAndPaste(safeEdit);
        disableCopyAndPaste(safeEdit2);
        disableCopyAndPaste(safeEdit3);*/
        final Button clck = findViewById(R.id.feed_back);
        clck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clck.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
        });
        LinearLayout keyboardContainer = findViewById(R.id.keyboardViewPlace);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.layout_keyboard_containor, null);
        safeKeyboard = new SafeKeyboard(getApplicationContext(), keyboardContainer, editList,
                R.layout.layout_keyboard_containor, view.findViewById(R.id.safeKeyboardLetter).getId(), true, true, false);
        safeKeyboard.setDelDrawable(this.getResources().getDrawable(R.drawable.icon_del));
        safeKeyboard.setLowDrawable(this.getResources().getDrawable(R.drawable.icon_capital_default));
        safeKeyboard.setUpDrawable(this.getResources().getDrawable(R.drawable.icon_capital_selected));
        safeKeyboard.setLogoDrawable(this.getResources().getDrawable(R.mipmap.logo_zy));
        safeKeyboard.setHideDrawable(this.getResources().getDrawable(R.drawable.keyboard_done_));

       /* safeKeyboard2 = new SafeKeyboard(getApplicationContext(), keyboardContainer, safeEdit2,
                R.layout.layout_keyboard_containor, view.findViewById(R.id.safeKeyboardLetter).getId(), true, true, false);
        safeKeyboard2.setDelDrawable(this.getResources().getDrawable(R.drawable.icon_del));
        safeKeyboard2.setLowDrawable(this.getResources().getDrawable(R.drawable.icon_capital_default));
        safeKeyboard2.setUpDrawable(this.getResources().getDrawable(R.drawable.icon_capital_selected));
        safeKeyboard2.setLogoDrawable(this.getResources().getDrawable(R.mipmap.logo_zy));
        safeKeyboard2.setHideDrawable(this.getResources().getDrawable(R.drawable.keyboard_done_));*/

    }


    // 当点击返回键时, 如果软键盘正在显示, 则隐藏软键盘并是此次返回无效
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (safeKeyboard.isShow()) {
                safeKeyboard.hideKeyboard();
                Log.i("encrptPWD", IJniInterface.getEncryptKey(String.valueOf(safeEdit.getId()),"1212"));
                Log.i("decrptPWD", IJniInterface.getDecryptKey(String.valueOf(safeEdit.getId()),"1212"));
                Log.i("encrptPWD", IJniInterface.getEncryptKey(String.valueOf(safeEdit2.getId()),"1212"));
                Log.i("decrptPWD", IJniInterface.getDecryptKey(String.valueOf(safeEdit2.getId()),"1212"));
                Log.i("encrptPWD", IJniInterface.getEncryptKey(String.valueOf(safeEdit3.getId()),"1212"));
                Log.i("decrptPWD", IJniInterface.getDecryptKey(String.valueOf(safeEdit3.getId()),"1212"));
                return false;
            }
           /* if (safeKeyboard2.isShow()) {
                safeKeyboard2.hideKeyboard();
                Log.i("encrptPWD2", IJniInterface.gEP(String.valueOf(safeEdit2.getId()),"1212"));
                Log.i("decrptPWD2", IJniInterface.gDP(String.valueOf(safeEdit2.getId()),"1212"));
                return false;
            }*/
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 禁止输入框复制粘贴菜单
     */
    private void disableCopyAndPaste(final EditText editText) {
        try {
            if (editText == null) {
                return ;
            }


            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            editText.setLongClickable(false);
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // setInsertionDisabled when user touches the view
                        setInsertionDisabled(editText);
                    }


                    return false;
                }
            });
            editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInsertionDisabled(EditText editText) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);

            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);

            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
