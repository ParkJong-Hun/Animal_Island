package petstone.project.animalisland.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Calendar;
import petstone.project.animalisland.R;

public class RehomeFreeSubmitActivity extends AppCompatActivity {

    EditText birth;
    ImageView back;
    Button cancel, submit;
    Spinner city, borough, town, type, breed, inoculation;

    String[] city_name = {"시/도"};
    String[] borough_name = {"시/구/군"};
    String[] town_name = {"동/읍/면"};
    String[] type_name = {"동물종류"};
    String[] breed_name = {"품종"};
    String[] inoculation_name = {"차수 선택(최대 7차)", "1차", "2차", "3차", "4차", "5차", "6차", "7차", "접종 안함"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rehome_free_submit);

        cancel = findViewById(R.id.cancel);
        submit = findViewById(R.id.submit);

        back = findViewById(R.id.back);

        city = findViewById(R.id.local_city);
        borough = findViewById(R.id.local_borough);
        town = findViewById(R.id.local_town);
        type = findViewById(R.id.type);
        breed = findViewById(R.id.breed);
        inoculation = findViewById(R.id.inoculation);

        birth = findViewById(R.id.birth);

        ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city_name);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        city.setAdapter(city_adapter);

        ArrayAdapter<String> borough_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, borough_name);
        borough_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        borough.setAdapter(borough_adapter);

        ArrayAdapter<String> town_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, town_name);
        town_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        town.setAdapter(town_adapter);

        ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type_name);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        type.setAdapter(type_adapter);

        ArrayAdapter<String> breed_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, breed_name);
        breed_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        breed.setAdapter(breed_adapter);

        ArrayAdapter<String> inoculation_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, inoculation_name);
        inoculation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        inoculation.setAdapter(inoculation_adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RehomeFreeSubmitActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birth.setText(year + "년 " + (month+1) + "월 " + dayOfMonth + "일");
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

    }
}
