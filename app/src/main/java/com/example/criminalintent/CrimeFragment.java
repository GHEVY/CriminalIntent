package com.example.criminalintent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime Crime = new Crime();
    public Button DateButton;
    private CheckBox SolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private static final String ARG_CRIME_ID = "crime_id";
    private static final int REQUEST_CONTACT = 1;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        EditText titleField = v.findViewById(R.id.crime_title);

        DateButton = v.findViewById(R.id.crime_date);
        DateButton.setText(R.string.date_button);
        mReportButton = v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(v12 -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
            i.putExtra(Intent.EXTRA_SUBJECT,
                    getString(R.string.crime_report_subject));
            i = Intent.createChooser(i, getString(R.string.send_report));
            startActivity(i);
        });
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        mSuspectButton = v.findViewById(R.id.crime_suspect);
        if(Crime.getSuspect()==null){
            mSuspectButton.setText(R.string.suspect_button);
        }
        else {
            mSuspectButton.setText(Crime.getSuspect());
        }
        mSuspectButton.setOnClickListener(v13 -> startActivityForResult(pickContact, REQUEST_CONTACT));
        String sus = Crime.getSuspect();

        if (sus == null) {
            mSuspectButton.setText(R.string.suspect_button);
        } else {
                Crime.setSuspect(sus);
                mSuspectButton.setText(Crime.getSuspect());
        }


        DateButton.setOnClickListener(v1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    LocalDate newdate = LocalDate.of(year, month + 1, dayOfMonth);
                    Date date = Date.from(newdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Crime.setDate(date);
                    updateDate();
                }
            }, year, month, dayOfMonth);

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
        if (Crime.getDate() != null) {
            DateButton.setText(Crime.getDate().toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(Crime);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (Crime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, Crime.getDate()).toString();
        String suspect = Crime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, Crime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();

            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                Crime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }

        }
    }
}