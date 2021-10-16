package petstone.project.animalisland.activity;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.SellSubmitImageAdapter;

public class RehomeSellSubmitActivity extends AppCompatActivity {

    private static final String TAG = "MultiImageActivity";
    RecyclerView recycler_img;
    SellSubmitImageAdapter adapter;
    ArrayList<Uri> uriList = new ArrayList<>();
    Button img_button;

    ImageView back;
    Button cancel, submit;
    Spinner city, borough, town, type, breed, inoculation;
    EditText birth;

    ArrayAdapter<CharSequence> type_adapter, breed_adapter, inoculation_adapter;

    String[] city_name = {"시/도"};
    String[] borough_name = {"시/구/군"};
    String[] town_name = {"동/읍/면"};

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
        type = findViewById(R.id.type);
        breed = findViewById(R.id.breed);
        inoculation = findViewById(R.id.inoculation);

        birth = findViewById(R.id.birth);

        img_button = findViewById(R.id.img_button);
        recycler_img = findViewById(R.id.sell_recycler_img);

        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city_name);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        city.setAdapter(city_adapter);

        ArrayAdapter<String> borough_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, borough_name);
        borough_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        borough.setAdapter(borough_adapter);

        ArrayAdapter<String> town_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, town_name);
        town_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        town.setAdapter(town_adapter);

        type_adapter = ArrayAdapter.createFromResource(this, R.array.spinner_type, android.R.layout.simple_spinner_dropdown_item);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(type_adapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (type_adapter.getItem(i).equals("강아지")){
                    breed_adapter = ArrayAdapter.createFromResource(RehomeSellSubmitActivity.this, R.array.spinner_dog, android.R.layout.simple_spinner_dropdown_item);
                    breed_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    breed.setAdapter(breed_adapter);
                }
                else if (type_adapter.getItem(i).equals("고양이")){
                    breed_adapter = ArrayAdapter.createFromResource(RehomeSellSubmitActivity.this, R.array.spinner_cat, android.R.layout.simple_spinner_dropdown_item);
                    breed_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    breed.setAdapter(breed_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        inoculation_adapter = ArrayAdapter.createFromResource(this, R.array.spinner_inoculation, android.R.layout.simple_spinner_dropdown_item);
        inoculation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(RehomeSellSubmitActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth ,new DatePickerDialog.OnDateSetListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(data == null){
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            }
            else{
                if(data.getClipData() == null){
                    Log.e("single choice: ", String.valueOf(data.getData()));
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);

                    recycler_img.setVisibility(View.VISIBLE);

                    adapter = new SellSubmitImageAdapter(uriList, getApplicationContext());
                    recycler_img.setAdapter(adapter);
                    recycler_img.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                }
                else{
                    ClipData clipData = data.getClipData();
                    Log.e("clipData", String.valueOf(clipData.getItemCount()));

                    if(clipData.getItemCount() > 5){
                        Toast.makeText(getApplicationContext(), "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Log.e(TAG, "multiple choice");

                        for (int i = 0; i < clipData.getItemCount(); i++){
                            Uri imageUri = clipData.getItemAt(i).getUri();
                            try {
                                uriList.add(imageUri);

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }

                        recycler_img.setVisibility(View.VISIBLE);

                        adapter = new SellSubmitImageAdapter(uriList, getApplicationContext());
                        recycler_img.setAdapter(adapter);
                        recycler_img.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    }
                }
            }
        }
    }
}

