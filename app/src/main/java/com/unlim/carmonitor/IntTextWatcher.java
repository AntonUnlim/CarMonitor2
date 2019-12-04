package com.unlim.carmonitor;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class IntTextWatcher implements TextWatcher {

    private EditText editText;

    public IntTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            editText.removeTextChangedListener(this);
            String value = editText.getText().toString().trim().replace(" ", "");
            String newValue = "";
            if (value.length() > 3) {
                int j = 0;
                int amn = value.length() - 1;
                for (int i = amn; i >= 0; i--) {
                    if (j == 3) {
                        newValue = " " + newValue;
                        j = 0;
                    }
                    newValue = value.charAt(i) + newValue;
                    j++;
                }
                editText.setText(newValue);
                editText.setSelection(editText.getText().toString().length());
            }
            editText.addTextChangedListener(this);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            editText.addTextChangedListener(this);
        }
    }
}
