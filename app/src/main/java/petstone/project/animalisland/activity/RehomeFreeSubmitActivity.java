package petstone.project.animalisland.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.FreeSubmitImageAdapter;

public class RehomeFreeSubmitActivity extends AppCompatActivity {

    private static final String TAG = "MultiImageActivity";
    RecyclerView recycler_img;
    FreeSubmitImageAdapter adapter;
    private ArrayList<Uri> uriList = new ArrayList<>();
    Button img_button;

    EditText title, birth, content, local;
    String s_title, s_birth, s_content, s_type, s_breed, s_inoculation, s_sex, s_neutering, s_district, s_sell;
    String mDo, mCity, mRo, mDong;
    RadioGroup radio_sex, radio_neutering;
    ImageView back;
    Button cancel, submit;
    Spinner type, breed, inoculation;

    ArrayAdapter<CharSequence> type_adapter, breed_adapter, inoculation_adapter;

    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference ImgRef;

    String uid;
    String document_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rehome_free_submit);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        ImgRef = storage.getReference("PostImg");

        title = findViewById(R.id.getTitle);
        content = findViewById(R.id.getContent);
        radio_sex = findViewById(R.id.submit_sex);
        radio_neutering = findViewById(R.id.free_submit_neutering);

        cancel = findViewById(R.id.cancel);
        submit = findViewById(R.id.submit);

        back = findViewById(R.id.back);

        local = findViewById(R.id.edit_local);
        type = findViewById(R.id.type);
        breed = findViewById(R.id.breed);
        inoculation = findViewById(R.id.inoculation);
        birth = findViewById(R.id.birth);

        img_button = findViewById(R.id.img_button);
        recycler_img = findViewById(R.id.free_recycler_img);

        //이미지 선택 버튼(최대 5개)
        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                img_button.setVisibility(View.INVISIBLE);
            }
        });

        //주소 선택
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //동물종류, 품종 스피너에 데이터 넣기
        type_adapter = ArrayAdapter.createFromResource(this, R.array.spinner_type, android.R.layout.simple_spinner_dropdown_item);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(type_adapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (type_adapter.getItem(i).equals("강아지")) {
                    breed_adapter = ArrayAdapter.createFromResource(RehomeFreeSubmitActivity.this, R.array.spinner_dog, android.R.layout.simple_spinner_dropdown_item);
                    breed_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    breed.setAdapter(breed_adapter);
                } else if (type_adapter.getItem(i).equals("고양이")) {
                    breed_adapter = ArrayAdapter.createFromResource(RehomeFreeSubmitActivity.this, R.array.spinner_cat, android.R.layout.simple_spinner_dropdown_item);
                    breed_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    breed.setAdapter(breed_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //접종 스피너에 데이터 넣기
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

        //생년월일 선택하기 위한 DatePicker
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RehomeFreeSubmitActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birth.setText(year % 100 + ". " + (month + 1) + ". " + dayOfMonth);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        //등록 버튼
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("data");
                local.setText(result);
                AddressCheck();
            }
        }

        if (requestCode == 1) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            } else {
                if(data.getClipData() == null){
                    Log.e("single choice: ", String.valueOf(data.getData()));
                    Uri img = data.getData();
                    uriList.add(img);

                    recycler_img.setVisibility(View.VISIBLE);

                    adapter = new FreeSubmitImageAdapter(uriList, getApplicationContext());
                    recycler_img.setAdapter(adapter);
                    recycler_img.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

                } else {
                    ClipData clipData = data.getClipData();
                    Log.e("clipData", String.valueOf(clipData.getItemCount()));

                    if (clipData.getItemCount() > 5) {
                        Toast.makeText(getApplicationContext(), "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                    } else {
                        Log.e(TAG, "multiple choice");

                        for (int i = 0; i < clipData.getItemCount(); i++){
                            Uri img = clipData.getItemAt(i).getUri();
                            try {
                                uriList.add(img);

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }

                        }

                        recycler_img.setVisibility(View.VISIBLE);

                        adapter = new FreeSubmitImageAdapter(uriList, getApplicationContext());
                        recycler_img.setAdapter(adapter);
                        recycler_img.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    }
                }
            }


        }

    }

    private void upload() {
        //저장되는 날짜 가져오기
        SimpleDateFormat sddate = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
        Date date = new Date();
        String s_date = sddate.format(date);

        //현재 사용자 아이디 가져오기
        uid = auth.getCurrentUser().getUid();
        document_id = s_date + "_" + uid;

        s_title = title.getText().toString();
        s_content = content.getText().toString();
        s_birth = birth.getText().toString();
        s_type = type.getSelectedItem().toString();
        s_breed = breed.getSelectedItem().toString();
        s_inoculation = inoculation.getSelectedItem().toString();
        s_sell = "0";   //무료분양은 분양비 0원으로 저장

        RadioButton rd_sex = findViewById(radio_sex.getCheckedRadioButtonId());
        s_sex = rd_sex.getText().toString();

        RadioButton rd_neutering = findViewById(radio_neutering.getCheckedRadioButtonId());
        s_neutering = rd_neutering.getText().toString();

        Map<String, Object> sale_posts = new HashMap<>();
        sale_posts.put("uid", uid);
        sale_posts.put("document_id", document_id);
        sale_posts.put("date", s_date);
        sale_posts.put("title", s_title);
        sale_posts.put("district", s_district);
        sale_posts.put("animal_type", s_type);
        sale_posts.put("animal_breed", s_breed);
        sale_posts.put("sex", s_sex);
        sale_posts.put("is_inoculated", s_inoculation);
        sale_posts.put("is_neutralized", s_neutering);
        sale_posts.put("article", s_content);
        sale_posts.put("is_sell", s_sell);
        sale_posts.put("birth", s_birth);

        //데이터베이스 추가
        db.collection("sale_posts").document(document_id)
                .set(sale_posts)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "분양 데이터베이스 저장 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Fail", "분양 데이터베이스 저장 실패");
                    }
                });

        //이미지 저장
        for (int i=0; i< uriList.size(); i++){
            if(!uriList.get(i).equals(null))
            {
                String fileName = "img" + (i+1);
                StorageReference postImgRef = ImgRef.child(document_id);
                UploadTask uploadTask = postImgRef.child(fileName).putFile(uriList.get(i));
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("onFailure", exception.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Log.d("ImgUpload", taskSnapshot.toString());
                    }
                });
            }
        }

    }

    private void AddressCheck() {
        // 오리지날 주소
        s_district = local.getText().toString();

        // 숫자와 특수문자를 제거할 주소
        String sigungu = s_district;

        // 숫자 제거
        sigungu = s_district.replaceAll("\\d", "");
        // 특수 문자 제거
        sigungu = sigungu.replaceAll("\\p{Punct}", "");
        // 주소를 시 군 구로 짤라서 배열에 넣기


        String[] str_arr = sigungu.split(" ");
        Log.d("str_arr", String.valueOf(str_arr.length));

        // 배열안 데이터 확인

        for (int i = 0; i < str_arr.length; i++) {
            Log.d("juso", String.valueOf(i + " : ") + str_arr[i]);
        }


        // 도 , 시
        mDo = str_arr[1];
        Log.d("mDo", str_arr[1]);
        // 구
        mCity = str_arr[2];
        Log.d("mCity", str_arr[2]);
        // 로
        mRo = str_arr[3];
        Log.d("mRo", str_arr[3]);
        // 동
        mDong = str_arr[5];
        Log.d("mDong", str_arr[5]);

        // 최종주소(시 + 구)
        s_district = mDo + " " + mCity + " " + mDong;
    }

    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(RehomeFreeSubmitActivity.this).setMessage("게시글을 작성하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                upload();
                finish();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}
