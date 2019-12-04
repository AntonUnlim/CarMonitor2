package com.unlim.carmonitor;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NumberTextWatcher implements TextWatcher {

    private EditText editText;

    public NumberTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        try
        {
            editText.removeTextChangedListener(this);
            String value = editText.getText().toString().replaceAll(" ", "");
            if (!value.equals(""))
            {
                if(value.startsWith(".")) {
                    editText.setText("");
                } else if (value.length() == 2) {
                    if(value.startsWith("0") && !value.startsWith("0.")) {
                        String ch = value.substring(1, 2);
                        editText.setText(ch);
                    }
                } else {
                    editText.setText(Const.getDecimalSpacedString(value));
                }
                editText.setSelection(editText.getText().toString().length());
            }
            editText.addTextChangedListener(this);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            editText.addTextChangedListener(this);
        }
    }
}
