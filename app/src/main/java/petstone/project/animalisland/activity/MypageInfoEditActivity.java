package petstone.project.animalisland.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import petstone.project.animalisland.R;

//마이페이지 정보 수정
public class MypageInfoEditActivity extends AppCompatActivity {

    Button cancel, edit;
    EditText password, password_check;
    EditText email;
    TextView password_checked;
    boolean password_checked_switch = false;//비밀번호 일치하는지 판별
    Spinner city, ku, dong;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_info_edit);

        back = findViewById(R.id.back_mypage_edit_info);
        cancel = findViewById(R.id.mypage_info_edit_cancel);
        edit = findViewById(R.id.mypage_info_edit_submit);
        password = findViewById(R.id.mypage_info_edit_password_form);
        password_check = findViewById(R.id.mypage_info_edit_password_check_form);
        password_checked = findViewById(R.id.mypage_info_edit_password_checked);
        email = findViewById(R.id.mypage_info_edit_email_form);
        city = findViewById(R.id.mypage_info_edit_address_city_form);
        ku = findViewById(R.id.mypage_info_edit_address_ku_form);
        dong = findViewById(R.id.mypage_info_edit_address_dong_form);

        //비밀번호 확인
        password_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password_checked.setVisibility(View.INVISIBLE);
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(password.getText().toString().equals(password_check.getText().toString())&&!(password.getText().toString().equals(""))) {
                    password_checked.setText("일치함");
                    password_checked.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    password_checked.setVisibility(View.VISIBLE);
                    password_checked_switch = true;
                } else if(!(password_check.getText().toString().equals(""))){
                    password_checked.setText("일치하지 않음");
                    password_checked.setTextColor(0xFFF44336);
                    password_checked.setVisibility(View.VISIBLE);
                    password_checked_switch = false;
                } else if(password_check.getText().toString().equals("")){
                    password_checked.setVisibility(View.INVISIBLE);
                    password_checked_switch = false;
                }
            }
        });
        //시/도
        ArrayAdapter cityAdapter = ArrayAdapter.createFromResource(this, R.array.도광역시, android.R.layout.simple_spinner_dropdown_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(cityAdapter);
        //시/군/구
        ArrayAdapter kuAdapter = ArrayAdapter.createFromResource(this, R.array.서울시, android.R.layout.simple_spinner_dropdown_item);
        kuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ku.setAdapter(kuAdapter);
        //동/읍/면
        ArrayAdapter dongAdapter = ArrayAdapter.createFromResource(this, R.array.종로구, android.R.layout.simple_spinner_dropdown_item);
        dongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dong.setAdapter(dongAdapter);
        
        //취소버튼
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        //수정버튼
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((password_checked_switch ==  true)&& !(email.getText().toString().equals(""))) {
                    //이메일 형식 체크
                    String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(email.getText().toString());
                    if(m.matches()){
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "이메일이 옳바른 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "입력 정보가 정확하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
