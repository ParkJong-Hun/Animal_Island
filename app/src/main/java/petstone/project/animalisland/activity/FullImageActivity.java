package petstone.project.animalisland.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.net.URI;

import petstone.project.animalisland.R;

//11월 24일 추가
public class FullImageActivity extends AppCompatActivity {

    Uri uri;

    ImageView full_iv,finishi_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimg_activity);

        full_iv = findViewById(R.id.fullimg_iv);
        finishi_iv = findViewById(R.id.finish_btn);

        finishi_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {

            Intent i = getIntent();
            uri = Uri.parse(i.getStringExtra("fullimguri"));

            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(full_iv);
            Log.d("이미지 전체화면", uri.toString());


        }catch (Exception e)
        {
            Log.d("에러",e.toString());
        }


    }
}
