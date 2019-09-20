package com.zlc.friendcirclephoto.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author : zlc
 * @date : On 2017/4/17
 * @eamil : zlc921022@163.com
 * 以后都用这个Edittext
 */
public class CommonEditText extends EditText {

    public CommonEditText(Context context) {
        super(context);
    }

    public CommonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(new MyTextWatcher());
    }

    public class MyTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(onTextChaged!=null){
                onTextChaged.setText(s+"");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    public interface OnTextChaged{
        void setText(String s);
    }

    public OnTextChaged onTextChaged;

    public void setOnTextChaged(OnTextChaged onTextChaged) {
        this.onTextChaged = onTextChaged;
    }

    public void setTextSize(float size){
        setTextSize(size);
    }

    public void setTextColor(Context context,int colorId){
        setTextColor(ContextCompat.getColor(context,colorId));
    }

    public void setHintColor(Context context,int colorId){
        setHintTextColor(ContextCompat.getColor(context,colorId));
    }
}
