package com.cellumed.healthcare.microrehab.knee.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cellumed.healthcare.microrehab.knee.DataBase.SqlImp;
import com.cellumed.healthcare.microrehab.knee.R;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by test on 2016-11-19.
 */
public class DialogUserInfoEdit extends Dialog implements  SqlImp {

    @Bind(R.id.et_userName)
    EditText etUserName;
    @Bind(R.id.et_birth)
    EditText etUserBirth;
    @Bind(R.id.cb_male)
    CheckBox cbMale;
    @Bind(R.id.cb_female)
    CheckBox cbFemale;

    @Bind(R.id.cb_left)
    CheckBox cbLeft;
    @Bind(R.id.cb_right)
    CheckBox cbRight;

    @Bind(R.id.saveDone)
    Button saveDone;
    @Bind(R.id.cancel)
    Button cancel;

    private Context mContext;

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (java.text.ParseException pe) {
            return false;
        }
        return true;
    }

    public DialogUserInfoEdit(Context mContext) {
        super(mContext);
        this.mContext = mContext;



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setTitle(mContext.getString(R.string.SettingUser));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_user_info_edit);
        ButterKnife.bind(this);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = ((WindowManager) mContext.getApplicationContext().getSystemService(mContext.getApplicationContext().WINDOW_SERVICE));
        lp.width = (int) (wm.getDefaultDisplay().getWidth() * 0.95);
        lp.height = (int) (wm.getDefaultDisplay().getHeight() * 0.8);
        getWindow().setAttributes(lp);

        // 설정 가져올 값
        // UserInfo
        // - name:string 10
        // - gender: 0: male, 1: female
        // - lr : 0: left, 1: right
        String sfName="EMS_USER_INFO";   // 임시 shared 저장용
        SharedPreferences sf = mContext.getSharedPreferences(sfName, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sf.edit();

        String username=sf.getString(UserName,"");
        etUserName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    downKeyboard(mContext, etUserName);
                    return true;
                }
                return false;
            }
        });
        if( username== null || username=="")
        {
            // Dialog open
        }
        else
        {
            etUserName.setText(username);
        }

        String userbirth=sf.getString(UserBirthday,"");
        if( userbirth== null || userbirth=="")
        {
            // Dialog open
        }
        else
        {
            etUserBirth.setText(userbirth);
        }



        int gender_idx=sf.getInt(UserGender,0);
        if(gender_idx==0)
        {
            cbMale.setChecked(true);
            cbFemale.setChecked(false);
        }
        else
        {
            cbMale.setChecked(false);
            cbFemale.setChecked(true);
        }

        int legtype_idx=sf.getInt(UserLegType,0);
        if(legtype_idx==0)
        {
            cbLeft.setChecked(true);
            cbRight.setChecked(false);
        }
        else
        {
            cbLeft.setChecked(false);
            cbRight.setChecked(true);
        }

        cbLeft.setOnClickListener(v-> {
            cbLeft.setChecked(true);
            cbRight.setChecked(false);
        });

        cbRight.setOnClickListener(v-> {
            cbLeft.setChecked(false);
            cbRight.setChecked(true);
        });

        cbMale.setOnClickListener(v-> {
            cbMale.setChecked(true);
            cbFemale.setChecked(false);
        });


        cbFemale.setOnClickListener(v-> {
            cbMale.setChecked(false);
            cbFemale.setChecked(true);
        });

        saveDone.setOnClickListener(v -> {


            if(etUserName.getText().toString().equals(""))
            {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.pleaseName), Toast.LENGTH_SHORT).show();
            }
            else if(etUserBirth.getText().toString().equals(""))
            {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.pleaseBirthDay), Toast.LENGTH_SHORT).show();
            }
            else if(!isValidDate(etUserBirth.getText().toString()))
            {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.pleaseRightBirthDay), Toast.LENGTH_SHORT).show();

            }
            else {
                editor.putString(UserName, etUserName.getText().toString());
                editor.putString(UserBirthday, etUserBirth.getText().toString());
                editor.putInt(UserGender, cbFemale.isChecked() ? 1 : 0);
                editor.putInt(UserLegType, cbRight.isChecked() ? 1 : 0);

                editor.commit();
                dismiss();
            }
        });
        cancel.setOnClickListener(v -> dismiss());

        show();
    }




    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
