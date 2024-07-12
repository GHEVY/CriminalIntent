package com.example.criminalintent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;


public class CrimeFragment extends Fragment {
    private Crime Crime;
    private EditText TitleField;
    public Button DateButton;
    private CheckBox SolvedCheckBox;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        TitleField =v.findViewById(R.id.crime_title);
        DateButton = v.findViewById(R.id.crime_date);
        DateButton.setText(Crime.getDate().toString());
        DateButton.setEnabled(false);
        SolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        SolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Crime.setSolved(isChecked);
            }
        });
        TitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                Crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }
}
