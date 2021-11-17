package petstone.project.animalisland.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class ScheduleActivity extends AppCompatActivity {
    private TableLayout tl;

    private final int count = 12; // 13시 부터 1시로 변환
    private final int column = 8; // 월~일 7칸

    private int row = 17; //시간 설정 7시부터 24시 까지


    private TableLayout.LayoutParams tlParams;
    private TableRow.LayoutParams tvParams, timeTvParams;

    private TextView[][] table;

    private static final String TAG = "monday";
    ArrayList<String> list;
    ArrayList<String> wal_list;//wal_time_list;
    ArrayList<String> hwa_list;// hwa_time_list;
    ArrayList<String> su_list;// su_time_list;
    ArrayList<String> mok_list;//mok_time_list;
    ArrayList<String> keum_list;//keum_time_list;
    ArrayList<String> to_list;//to_time_list;
    ArrayList<String> il_list;//il_time_list;


    ArrayList<Integer> wal_time_list;
    ArrayList<Integer> hwa_time_list;
    ArrayList<Integer> su_time_list;
    ArrayList<Integer> mok_time_list;
    ArrayList<Integer> keum_time_list;
    ArrayList<Integer> to_time_list;
    ArrayList<Integer> il_time_list;

    Button mbtn_ok,mbtn_reset,mbtn_out;

    Intent i;
    ArrayList<String> time = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_table);
        getActionBar();
        getSupportActionBar();

        mbtn_ok = findViewById(R.id.schedule_ok_btn);
        mbtn_out = findViewById(R.id.schedule_out_btn);
        mbtn_reset = findViewById(R.id.schedule_reset_btn);

        i = new Intent(getApplicationContext(),MypagePetfriendApplyActivity.class);

        setTitle("가능한 시간대를 클릭하세요");

        //row params
        tlParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1f);
        //column 빈칸 TextView params
        tvParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 3f);
        //column 시간 Texview params
        timeTvParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f);

        tl = findViewById(R.id.tl);

        list = new ArrayList();
        // 날짜를 담을 리스트
        wal_list= new ArrayList<>();
        hwa_list= new ArrayList<>();
        su_list= new ArrayList<>();
        mok_list= new ArrayList<>();
        keum_list= new ArrayList<>();
        to_list= new ArrayList<>();
        il_list= new ArrayList<>();
        // 시간을 담을 리스트
        wal_time_list= new ArrayList<>();
        hwa_time_list= new ArrayList<>();
        su_time_list= new ArrayList<>();
        mok_time_list= new ArrayList<>();
        keum_time_list= new ArrayList<>();
        to_time_list= new ArrayList<>();
        il_time_list= new ArrayList<>();


        table = new TextView[row][column];

        try {
            //row 생성
            for (int j = 0; j < row; j++) {
                int num = j + 7;
                int id = 0;

                // column 선언
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(tlParams);

                // column 생성
                for (int i = 0; i < column; i++) {
                    //행 안에 데이터 넣기
                    TextView tv = new TextView(this);
                    tv.setBackground(this.getResources().getDrawable(R.drawable.cell_shape));
                    tv.setLayoutParams(tvParams);
                    //시간 넣기
                    if (i == 0) {
                        //시간 행안에 시간넣기
                        tv.setText(String.valueOf(num));
                        tv.setBackgroundColor(getResources().getColor(R.color.colorDaepyo_Light));
                        tv.setLayoutParams(timeTvParams);

                    }
                    else {
                        tv.setText(String.valueOf(""));
                    }
                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                    tableRow.addView(tv);
                    if (i == 0)
                        switch (j) {
                            case 1: id = 10;break;
                            case 2: id = 20;break;
                            case 3: id = 30;break;
                            case 4: id = 40;break;
                            case 5: id = 50;break;
                            case 6: id = 60;break;
                            case 7: id = 70;break;
                            case 8: id = 80;break;
                            case 9: id = 90;break;
                            case 10: id = 100;break;
                            case 11: id = 110;break;
                            case 12: id = 120;break;
                            case 13: id = 130;break;
                            case 14: id = 140;break;
                            case 15: id = 150;break;
                            case 16: id = 160;break;
                            case 17: id = 170;break;
                        }
                    tv.setId(id);
                    id++;


                    table[j][i] = tv;
                    Log.d("table", String.valueOf(j) + table[j][i].toString());

                }
                tl.addView(tableRow);
            }


        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                this.table[i][j].setOnClickListener(myListener);
            }
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.schedule_ok_btn:
                        addList();
                        break;
                    case R.id.schedule_reset_btn:
                        resetList();
                        break;
                    case  R.id.schedule_out_btn:
                        finish();
                        break;
                }
            }
        };

        mbtn_ok.setOnClickListener(listener);
        mbtn_reset.setOnClickListener(listener);
        mbtn_out.setOnClickListener(listener);


    }




    private void resetList() {

        Reset();
        for(int i = 0 ; i < row; i++) {
            for(int j = 1 ; j < column; j++) {
                if (table[i][j].getText().equals("V")) {
                    table[i][j].setText("");
                }
            }
        }

    }
    private void addList() {
        Reset();

        for(int i = 0 ; i < row; i++) {
            for(int j = 1 ; j < column; j++) {
                if (table[i][j].getText().equals("V")) {
                    list.add(String.valueOf(table[i][j].getId()));
                    Log.d("리스트", "행 : " + String.valueOf(i) + " 열 : " + String.valueOf(j) + " 텍스트뷰 아이디 : " + table[i][j].getId() + " 텍스트 내용 : " + table[i][j].getText().toString());
                }
            }
        }

        // 날짜 자르기
        for (int i= 0 ; i< list.size(); i++)
        {
            String str;
            String str0, str1;

            str = list.get(i);
            str1 = str.substring(1);
            if(str.length() == 1)
            {
                str1 = str.substring(0,1);
            }
            else if(str.length() == 3)
            {
                str1 = str.substring(2);
            }

            Log.d("문자열", "str : " + list.get(i) + " str1 : " + str1);

            switch (str1){
                case "1" : wal_list.add(str);break;
                case "2" : hwa_list.add(str);break;
                case "3" : su_list.add(str);break;
                case "4" : mok_list.add(str);break;
                case "5" : keum_list.add(str);break;
                case "6" : to_list.add(str);break;
                case "7" : il_list.add(str);break;
            }


        }

        for (int i =0; i<wal_list.size();i++)
        {
            TimeCheck(wal_list.get(i), i);
        }
        for (int i =0; i<hwa_list.size();i++)
        {
            TimeCheck(hwa_list.get(i), i);
        }
        for (int i =0; i<su_list.size();i++)
        {
            TimeCheck(su_list.get(i), i);
        }
        for (int i =0; i<mok_list.size();i++)
        {
            TimeCheck(mok_list.get(i), i);
        }
        for (int i =0; i<keum_list.size();i++)
        {
            TimeCheck(keum_list.get(i), i);
        }
        for (int i =0; i<to_list.size();i++)
        {
            TimeCheck(to_list.get(i), i);
        }
        for (int i =0; i<il_list.size();i++)
        {
            TimeCheck(il_list.get(i), i);
        }


        makeSchedule();

    }

    void Reset(){

        list.clear();

        // 날짜 배열
        wal_list.clear();
        hwa_list.clear();
        su_list.clear();
        mok_list.clear();
        keum_list.clear();
        to_list.clear();
        il_list.clear();
        // 시간을 담을 리스트
        wal_time_list.clear();
        hwa_time_list.clear();
        su_time_list.clear();
        mok_time_list.clear();
        keum_time_list.clear();
        to_time_list.clear();
        il_time_list.clear();

    }
    void TimeCheck(String str, int i){

        String str1;
        //String temp;
        int temp;

        // 시간 자르기
        if(str.length() == 1)
        {
            str1 = "7시";
            temp = 7;
            //Log.d("size.1 : " , str1);
        }
        else if(str.length() == 2)
        {
            str1 = str.substring(0,1);
            //Log.d("size.2 : " , str1);
        }
        else if(str.length() == 3)
        {
            str1 = str.substring(0,2);
            //Log.d("size.3 : " ,str1);
        }
        else {
            return;
        }

        switch (str1){
            case "1" : temp = 8;break;
            case "2" : temp = 9; break;
            case "3" : temp = 10; break;
            case "4" : temp = 11; break;
            case "5" : temp = 12; break;
            case "6" : temp = 13; break;
            case "7" : temp = 14; break;
            case "8" : temp = 15; break;
            case "9" : temp = 16; break;
            case "10" : temp = 17; break;
            case "11" : temp = 18; break;
            case "12" : temp = 19; break;
            case "13" : temp = 20; break;
            case "14" : temp = 21; break;
            case "15" : temp = 22; break;
            case "16" : temp = 23; break;

            default: temp = 7 ; break;

        }


        //날짜 자르기

        String str2 = str;

        if(str2.length() == 1)
        {
            str2 = str2.substring(0,1);
        }
        else if(str2.length() == 3)
        {
            str2 = str2.substring(2);
        }
        else {
            str2 = str2.substring(1);
        }

        switch (str2){
            case "1" :wal_time_list.add(temp);break;
            case "2" :hwa_time_list.add(temp);break;
            case "3" :su_time_list.add(temp);break;
            case "4" :mok_time_list.add(temp);break;
            case "5" :keum_time_list.add(temp);break;
            case "6" :to_time_list.add(temp);break;
            case "7" :il_time_list.add(temp);break;

        }



    }
    private void makeSchedule() {


        Schedule(wal_time_list,"월요일");
        Schedule(hwa_time_list,"화요일");
        Schedule(su_time_list,"수요일");
        Schedule(mok_time_list,"목요일");
        Schedule(keum_time_list,"금요일");
        Schedule(to_time_list,"토요일");
        Schedule(il_time_list,"일요일");

        i.putExtra("time",time);
        setResult(RESULT_OK,i);
        finish();



    }
    private void Schedule(ArrayList<Integer> iList, String day) {

        ArrayList<Integer> timelist = new ArrayList<>();
        ArrayList<Integer> startTimeList = new ArrayList<>();
        ArrayList<Integer> endTimeList = new ArrayList<>();
        //ArrayList<String> time = new ArrayList<>();


        if (iList.size() != 0) {
            // 요일 시간별로 나누기
            int n = iList.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append(n);
            timelist.add(n);
            for (int i = 1; i < iList.size(); i++) {

                if (n + 1 == iList.get(i)) {
                    n++;
                } else {
                    //sb.append("-" + (n + 1));
                    timelist.add(n+1);
                    n = iList.get(i);
                    //sb.append(" " + n);
                    timelist.add(n);
                }

            }
            //sb.append("-" + (n + 1));
            timelist.add(n+1);
            //Log.d(day, sb.toString());

            try {
                for (int i = 0; i < timelist.size(); i++) {
                    if((i+1) % 2 ==1)
                        startTimeList.add(timelist.get(i));
                    if ((i + 1) % 2 == 0 )
                        endTimeList.add(timelist.get(i));
                }

                for (int i = 0; i < startTimeList.size(); i++) {
                    Log.d(day, ":" + startTimeList.get(i)+"시-" + endTimeList.get(i) + "시");
                }
            }
            catch (Exception e)
            {
                Log.e("error",e.toString());
            }


            time.add(day);
            i.putExtra(day,timelist);



        }
        else
            return;
    }
    private View.OnClickListener myListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            for (int i = 0; i < row; i++) {
                for (int j = 1; j < column; j++) {
                    if (v.getId() == table[i][j].getId() && table[i][j].getText().equals("")) {
                        table[i][j].setText("V");
                        table[i][j].setGravity(Gravity.CENTER);

                        Log.d("넣기", "행 : " + String.valueOf(i) + " 열 : " + String.valueOf(j) + " 텍스트뷰 아이디 : " + table[i][j].getId() + " 텍스트 내용 : " + table[i][j].getText().toString());
                    }
                    else if (v.getId() == table[i][j].getId() && table[i][j].getText().equals("V")) {

                        table[i][j].setText("");
                        Log.d("제거", "행 : " + String.valueOf(i) + " 열 : " + String.valueOf(j) + " 텍스트뷰 아이디 : " + table[i][j].getId());

                    }

                }
            }
        }

    };
}
