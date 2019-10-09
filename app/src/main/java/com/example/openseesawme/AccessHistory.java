package com.example.openseesawme;

import android.animation.TimeAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class AccessHistory extends AppCompatActivity {
    String d_user_index=Dglobal.getDoorID();
    Toolbar myToolbar;
    private UserAdapter_Horizontal userAdapter;    //상단 사용자 목록
    private HistotyAdapter ioAdapter;            //출입내역 목록

    String[] iorow;
    TextView count;
    ImageView ivRestart;
    //int position=-1;

    //사람마다 다르게 보이도록 하는 거 해야 함------------------------------
    //userAdapter에 있는 항목?을 가져가서 그거에 해당하는 이름이면 안넣도록 if문..?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_history);

        //상단 사용자 목록
        uinit();
        getUserData();

        hinit();
        getData();

        /*UserAdapter_Horizontal.Data data = new UserAdapter_Horizontal.Data();
        if(!data.getSelectUser().equals("")){
            Intent intent = getIntent(); *//*데이터 수신*//*
            position = intent.getExtras().getInt("position");
            //position=Integer.parseInt(data.getSelectUser());
            Toast.makeText(getApplicationContext(),position,Toast.LENGTH_LONG).show(); //안됨
        }*/


        //출입내역 개수
        count=findViewById(R.id.count);
        count.setText("총 "+Integer.toString(iorow.length)+"개");

        //새로고침
        ivRestart=findViewById(R.id.ivRestart);
        ivRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        // 추가된 소스, Toolbar를 생성한다.
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //추가된 소스코드, Toolbar의 왼쪽에 버튼을 추가하고 버튼의 아이콘을 바꾼다.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        //기본 타이틀 보여줄지 말지 설정. 안보여준다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //여기까지 툴바

    }

    //여기부터 상단 사용자 정보 띄우기
    private void uinit() {
        RecyclerView rvUserList = findViewById(R.id.rvUserList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvUserList.setLayoutManager(linearLayoutManager);
        userAdapter = new UserAdapter_Horizontal(getApplicationContext());
        rvUserList.setAdapter(userAdapter);
    }

    private void getUserData() {
        String result;
        String[] row;
        String[] detailrow;
        Integer[] index = new Integer[10000];
        String[] manager = new String[10000];
        String[] name = new String[10000];
        String[] img = new String[10000];
        try {
            result = new UserListActivity().execute(d_user_index).get();
            row = result.split("spl");
            for(int i=0;i<row.length;i++){
                detailrow=row[i].split(",");
                index[i]=Integer.parseInt(detailrow[0]);
                manager[i]=detailrow[1];
                name[i]=detailrow[2];
                img[i]=detailrow[3];
            }
            List<Integer> listUserindex = Arrays.asList(index);
            List<String> listUsername = Arrays.asList(name);
            List<String> listUserimg = Arrays.asList(img);

            for (int i = 0; i < row.length; i++) {
                UserAdapter_Horizontal.Data data = new UserAdapter_Horizontal.Data();
                // 각 List의 값들을 data 객체에 set 해줍니다.
                data.setUserindex(listUserindex.get(i));
                data.setUsername(listUsername.get(i));
                data.setUserimg(listUserimg.get(i));
                data.setContext(getApplicationContext());
                // 각 값이 들어간 data를 adapter에 추가합니다.
                userAdapter.addItem(data);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
    //여기까지 상단 사용자 정보 띄우기

    //여기부터 출입내역 띄우기
    private void hinit() {
        RecyclerView recycler = findViewById(R.id.rvIoList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(linearLayoutManager);
        ioAdapter = new HistotyAdapter();
        recycler.setAdapter(ioAdapter);
    }

    private void getData() {
        String[] detailrow;
        String[] img=new String[1000];
        String[] name=new String[1000];
        String[] ox=new String[1000];
        String[] time=new String[1000];
        try {
            String result = new HistoryActivity().execute(d_user_index).get();
            iorow=result.split("spl");
            for(int i=0;i<iorow.length;i++){
                detailrow=iorow[i].split(",");
                img[i]=detailrow[0];
                name[i]=detailrow[1];
                ox[i]=detailrow[2];
                time[i]=detailrow[3];
            }
            String dname = new HistoryDoorNameActivity().execute(d_user_index).get();
            List<String> listImg = Arrays.asList(img);
            List<String> listName = Arrays.asList(name);
            List<String> listDName = Arrays.asList(dname);
            List<String> listOx = Arrays.asList(ox);
            List<String> listTime = Arrays.asList(time);

            for (int i = 0; i < iorow.length; i++) {
                HistotyAdapter.Data data = new HistotyAdapter.Data();
                // 각 List의 값들을 data 객체에 set 해줍니다.
                data.setImg(listImg.get(i));
                data.setName(listName.get(i));
                data.setDname(listDName.get(0));
                data.setOx(listOx.get(i));
                data.setTime(listTime.get(i));
                data.setContext(getApplicationContext());

                // 각 값이 들어간 data를 adapter에 추가합니다.
                ioAdapter.addItem(data);
                //Toast.makeText(getApplicationContext(),data.getSelectUser(),Toast.LENGTH_LONG).show();
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
    //여기까지 출입내역 띄우기

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
