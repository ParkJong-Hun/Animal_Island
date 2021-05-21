package petstone.project.animalisland.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import petstone.project.animalisland.R;

public class MypageInfoEditActivity extends AppCompatActivity {

    Button cancel, edit;
    EditText password, password_check;
    TextView password_checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_info_edit);
        //TODO: 뒤로가기 구현

        cancel = findViewById(R.id.mypage_info_edit_cancel);
        edit = findViewById(R.id.mypage_info_edit_submit);
        password = findViewById(R.id.mypage_info_edit_password_form);
        password_check = findViewById(R.id.mypage_info_edit_password_check_form);
        password_checked = findViewById(R.id.mypage_info_edit_password_checked);

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
                } else if(!(password_check.getText().toString().equals(""))){
                    password_checked.setText("일치하지 않음");
                    password_checked.setTextColor(0xFFF44336);
                    password_checked.setVisibility(View.VISIBLE);
                } else if(password_check.getText().toString().equals("")){
                    password_checked.setVisibility(View.INVISIBLE);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
