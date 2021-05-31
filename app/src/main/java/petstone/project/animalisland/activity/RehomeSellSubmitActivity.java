package petstone.project.animalisland.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import petstone.project.animalisland.R;

public class RehomeSellSubmitActivity extends AppCompatActivity {

    ImageView back;
    Button cancel, submit;
    Spinner city, borough, town, breed, age, inoculation;

    String[] city_name = {"시/도"};
    String[] borough_name = {"시/구/군"};
    String[] town_name = {"동/읍/면"};
    String[] breed_name = {"동물종류"};
    String[] age_name= {"2개월" };
    String[] inoculation_name = {"차수 선택(최대 7차)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rehome_sell_submit);

        cancel = findViewById(R.id.cancel);
        submit = findViewById(R.id.submit);

        back = findViewById(R.id.back);

        city = findViewById(R.id.local_city);
        borough = findViewById(R.id.local_borough);
        town = findViewById(R.id.local_town);
        breed = findViewById(R.id.breed);
        age = findViewById(R.id.age);
        inoculation = findViewById(R.id.inoculation);

        ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city_name);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        city.setAdapter(city_adapter);

        ArrayAdapter<String> borough_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, borough_name);
        borough_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        borough.setAdapter(borough_adapter);

        ArrayAdapter<String> town_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, town_name);
        town_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        town.setAdapter(town_adapter);

        ArrayAdapter<String> breed_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, breed_name);
        breed_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        breed.setAdapter(breed_adapter);

        ArrayAdapter<String> age_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, age_name);
        age_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        age.setAdapter(age_adapter);

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
    }
}

