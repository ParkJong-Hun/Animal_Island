package petstone.project.animalisland.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import petstone.project.animalisland.R;

public class PetfriendUserApplyActivity extends AppCompatActivity {

    Button ok_btn,cancle_btn,back_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petfriend_user_apply);

        ok_btn = findViewById(R.id.ok_btn);
        cancle_btn = findViewById(R.id.cancle_btn);
        back_btn = findViewById(R.id.petfriend_submit_back);


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





    }


}
