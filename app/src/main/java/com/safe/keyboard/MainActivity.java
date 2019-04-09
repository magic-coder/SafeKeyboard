package com.safe.keyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safe.keyboard.jni.IJniInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SafeKeyboard safeKeyboard;

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

        safeEdit.setTag("number");
        editList.add(safeEdit);
        editList.add(safeEdit2);
        editList.add(safeEdit3);

        final Button clck = findViewById(R.id.feed_back);
        clck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clck.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
        });
        LinearLayout keyboardContainer = findViewById(R.id.keyboardViewPlace);
        safeKeyboard = new SafeKeyboard(MainActivity.this, keyboardContainer, editList,
                true, false, false);
       /* safeKeyboard.setDelDrawable(this.getResources().getDrawable(R.drawable.icon_del));
        safeKeyboard.setLowDrawable(this.getResources().getDrawable(R.drawable.icon_capital_default));
        safeKeyboard.setUpDrawable(this.getResources().getDrawable(R.drawable.icon_capital_selected));
        safeKeyboard.setLogoDrawable(this.getResources().getDrawable(R.mipmap.logo_zy));
        safeKeyboard.setHideDrawable(this.getResources().getDrawable(R.drawable.keyboard_done_));*/


    }


    // 当点击返回键时, 如果软键盘正在显示, 则隐藏软键盘并是此次返回无效
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (safeKeyboard.isShow()) {
                safeKeyboard.hideKeyboard();
                StringBuilder builder = new StringBuilder();
                builder.append(IJniInterface.getDecryptKey(String.valueOf(safeEdit.getId()), "1212")+"\n");
                builder.append(IJniInterface.getEncryptKey(String.valueOf(safeEdit.getId()), "1212")+"\n");
                builder.append(IJniInterface.getDecryptKey(String.valueOf(safeEdit2.getId()), "1212")+"\n");
                builder.append(IJniInterface.getEncryptKey(String.valueOf(safeEdit2.getId()), "1212")+"\n");
                builder.append(IJniInterface.getDecryptKey(String.valueOf(safeEdit3.getId()), "1212")+"\n");
                builder.append(IJniInterface.getEncryptKey(String.valueOf(safeEdit3.getId()), "1212"));
                ((TextView) findViewById(R.id.tv)).setText(builder.toString());
                return false;
            }

            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


}
