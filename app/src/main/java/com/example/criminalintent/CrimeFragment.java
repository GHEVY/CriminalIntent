package com.example.criminalintent;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime Crime;
    public Button DateButton;
    private CheckBox SolvedCheckBox;
    private static final String ARG_CRIME_ID = "crime_id";
    public int count =0;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        EditText titleField = v.findViewById(R.id.crime_title);
        DateButton = v.findViewById(R.id.crime_date);
        DateButton.setOnClickListener(v1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            LocalDate newdate = LocalDate.of( year, month+1,dayOfMonth);
                            Date date = Date.from(newdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            Crime.setDate(date);
                            updateDate();


                        }
                    },
                    year, month, dayOfMonth);

            datePickerDialog.show();

        });
        assert getArguments() != null;
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        Crime = CrimeLab.get(getActivity()).getCrime(crimeId);
        SolvedCheckBox = v.findViewById(R.id.crime_solved);
        SolvedCheckBox.setChecked(Crime.isSolved());
        titleField.setText(Crime.getTitle());
        SolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Crime.setSolved(isChecked);
            }
        });

        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void updateDate() {
        if(Crime.getDate() != null){
            DateButton.setText(Crime.getDate().toString());
            count++;
        }
    }


}
