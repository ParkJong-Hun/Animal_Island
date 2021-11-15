package petstone.project.animalisland.activity;

import android.annotation.SuppressLint;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.SellSubmitImageAdapter;

public class RehomeSellSubmitActivity extends AppCompatActivity {

    private static final String TAG = "MultiImageActivity";
    RecyclerView recycler_img;
    SellSubmitImageAdapter adapter;
    ArrayList<Uri> uriList = new ArrayList<>();
    Button img_button;

    EditText title, birth, content, sell;
    String s_title, s_birth, s_content, s_type, s_breed, s_inoculation, s_sex, s_neutering, s_district, s_sell;
    RadioGroup radio_sex, radio_neutering;
    ImageView back;
    Button cancel, submit;
    Spinner city, borough, town, type, breed, inoculation;

    ArrayAdapter<CharSequence> type_adapter, breed_adapter, inoculation_adapter;

    String[] city_name = {"시/도"};
    String[] borough_name = {"시/구/군"};
    String[] town_name = {"동/읍/면"};

    FirebaseAuth auth;
    FirebaseFirestore db;
    String uid;
    String document_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rehome_sell_submit);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
        sell = findViewById(R.id.getPrice);
        title = findViewById(R.id.getTitle);
        content = findViewById(R.id.getContent);
        radio_sex = findViewById(R.id.sell_submit_sex);
        radio_neutering = findViewById(R.id.sell_submit_neutering);

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

        //취소 버튼
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //등록하기 버튼
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();   //저장
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
                birth.setText(year%100 + ". " + (month+1) + ". " + dayOfMonth );
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

    //데이터베이스 추가
    @SuppressLint("ResourceType")
    private void upload(){
        //저장되는 날짜 가져오기
        SimpleDateFormat sddate = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
        Date date = new Date();
        String s_date = sddate.format(date);

        //현재 사용자 아이디 가져오기
        uid = auth.getCurrentUser().getUid();

        document_id = uid + "_" + s_date;

        s_title = title.getText().toString();
        s_content = content.getText().toString();
        s_birth = birth.getText().toString();
        s_type = type.getSelectedItem().toString();
        s_breed = breed.getSelectedItem().toString();
        s_inoculation = inoculation.getSelectedItem().toString();
        s_sell = sell.getText().toString();

        if(radio_sex.getCheckedRadioButtonId() == 1) {
            s_sex = "암컷";
        } else if (radio_sex.getCheckedRadioButtonId() == 2){
            s_sex = "수컷";
        }else{
            s_sex = "null";
        }

        RadioButton rd_sex = findViewById(radio_sex.getCheckedRadioButtonId());
        s_sex = rd_sex.getText().toString();

        RadioButton rd_neutering = findViewById(radio_neutering.getCheckedRadioButtonId());
        s_neutering = rd_neutering.getText().toString();

        Map<String, Object> sale_posts = new HashMap<>();
        sale_posts.put("uid", uid);
        sale_posts.put("document_id", document_id);
        sale_posts.put("date", s_date);
        sale_posts.put("title", s_title);
        sale_posts.put("district", null);
        sale_posts.put("animal_type", s_type);
        sale_posts.put("animal_breed", s_breed);
        sale_posts.put("sex", s_sex);
        sale_posts.put("is_inoculated", s_inoculation);
        sale_posts.put("is_neutralized", s_neutering);
        sale_posts.put("article", s_content);
        sale_posts.put("is_sell", s_sell);
        sale_posts.put("images", null);
        sale_posts.put("birth", s_birth);

        //데이터베이스 추가
        db.collection("sale_posts").document(document_id)
                .set(sale_posts)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //데이터 베이스 저장 성공
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //데이터 베이스 저장 실패
                    }
                });

    }
}

