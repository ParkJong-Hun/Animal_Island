package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import petstone.project.animalisland.R;

public class PetfriendUserSelect extends AppCompatActivity {

    Button chat_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petfriend_user_select);


        chat_btn = findViewById(R.id.petfriend_start_chating_button);



        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }
}
