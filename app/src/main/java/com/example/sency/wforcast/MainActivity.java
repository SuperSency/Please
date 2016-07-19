package com.example.sency.wforcast;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    private TextView    city;
    private TextView    temp;
    private TextView    weather;
    private TextView    degree;
    private TextView    week1;
    private TextView    date;
    private TextView    degree1;
    private TextView    weather1;
    private TextView    week2;

    private TextView    degree2;
    private TextView    weather2;
    private TextView    week3;

    private TextView    degree3;
    private TextView    weather3;
    private TextView    week4;

    private TextView    degree4;
    private TextView    weather4;
    private TextView    week5;

    private TextView    degree5;
    private TextView    weather5;
    private TextView    week6;

    private TextView    degree6;
    private TextView    weather6;
    private EditText    et_city;
    private Button      summit;

    private Thread t;
    private Myhandler handler;
    private FuHandler handler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        init();

        summit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread gt = new thread();
                t = new Thread(gt, "Refresh");
                t.start();
                handler = new Myhandler();
                thread2 t2 = new thread2();
                t2.start();
                handler2 = new FuHandler();
            }
        });


    }

    private void init() {
        city = (TextView)findViewById(R.id.city);
        temp = (TextView)findViewById(R.id.temp);
        weather = (TextView)findViewById(R.id.weather);
        degree = (TextView)findViewById(R.id.degree);
        week1 = (TextView)findViewById(R.id.week1);
        week2 = (TextView)findViewById(R.id.week2);
        week3 = (TextView)findViewById(R.id.week3);
        week4 = (TextView)findViewById(R.id.week4);
        week5 = (TextView)findViewById(R.id.week5);
        week6 = (TextView)findViewById(R.id.week6);
        degree1 = (TextView)findViewById(R.id.degree1);
        degree2 = (TextView)findViewById(R.id.degree2);
        degree3 = (TextView)findViewById(R.id.degree3);
        degree4 = (TextView)findViewById(R.id.degree4);
        degree5 = (TextView)findViewById(R.id.degree5);
        degree6 = (TextView)findViewById(R.id.degree6);
        weather1 = (TextView)findViewById(R.id.weather1);
        weather2 = (TextView)findViewById(R.id.weather2);
        weather3 = (TextView)findViewById(R.id.weather3);
        weather4 = (TextView)findViewById(R.id.weather4);
        weather5 = (TextView)findViewById(R.id.weather5);
        weather6 = (TextView)findViewById(R.id.weather6);
        date = (TextView)findViewById(R.id.data);
        summit = (Button)findViewById(R.id.summit);
        et_city = (EditText)findViewById(R.id.et_city);

    }


    private class thread implements Runnable{
        @Override
        public void run() {
            String todayStart   = "http://api.k780.com:88/?app=weather.today&weaid=";
            String todayCity    = et_city.getText().toString();
            String todayEnd     = "&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
            String todayWeather = getURLConnection(todayStart + todayCity + todayEnd);
            System.out.println("11111"+todayWeather);
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("todayWeather", todayWeather);
            msg.setData(bundle);
            //getBitmap(URL);
            handler.sendMessage(msg);

        }
    }

    private String getURLConnection(String path){
        String xml = "";
        try {
            HttpClient client = new DefaultHttpClient();//创建一个http对象
            HttpGet    get    = new HttpGet(path);
            HttpResponse response = client.execute(get);
            int code = response.getStatusLine().getStatusCode();//得到http响应结果的状态代码
            Log.d("http", "code");
            if(code==200){
                //getEntity()是获取实体,getContent()是获取数据流
                InputStream reader = response.getEntity().getContent();
                BufferedReader bf = new BufferedReader(new InputStreamReader(reader));
                String list = bf.readLine();//读一行
                while(list!=null){
                    xml += list;
                    list = bf.readLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("22222" + xml);
        return xml;
    }

    public class Myhandler extends android.os.Handler {
        public void handleMessage(Message msg)  {

            String todayWeather = msg.getData().getString("todayWeather");
            if(!todayWeather.equals("")){
                try {
                    todayWeather = String.valueOf(new JSONObject(todayWeather).getJSONObject("result"));
                    JSONObject json = new JSONObject(todayWeather);
                    city.setText(et_city.getText().toString());
                    temp.setText(json.getString("temp_curr")+"°");
                    weather.setText(json.getString("weather"));
                    degree.setText(json.getString("temperature"));
                    date.setText(json.getString("time"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class thread2 extends Thread {
        @Override
        public void run() {
            String todayStart   = "http://api.k780.com:88/?app=weather.future&weaid=";
            String todayCity    = et_city.getText().toString();
            String todayEnd     = "&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
            String futureWeather = getURLConnection(todayStart + todayCity + todayEnd);
            System.out.println("future"+futureWeather);
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("futureWeather", futureWeather);
            msg.setData(bundle);
            //getBitmap(URL);
            handler2.sendMessage(msg);
        }
    }

    public class FuHandler extends android.os.Handler {
        public void handleMessage(Message msg)  {

            String futureWeather = msg.getData().getString("futureWeather");
            System.out.println("come in"+futureWeather);
            if(!futureWeather.equals("")){
                try {

                   //futureWeather = String.valueOf(new JSONObject(futureWeather).getJSONObject("result"));
                    //System.out.println("array");
                    JSONArray json = new JSONObject(futureWeather).getJSONArray("result");
                   // JSONArray array = new JSONArray(json);
                    //System.out.println("array"+array);
                    for(int i=0;i<json.length();i++){
                        JSONObject arr = json.getJSONObject(i);
                        System.out.println(i);
                        if(i==1) {
                            week1.setText(arr.getString("week"));
                            //date1.setText(arr.getString("days"));
                            weather1.setText(arr.getString("weather"));
                            degree1.setText(arr.getString("temperature"));
                            System.out.println("week1");
                        }
                        if(i==2) {
                            week2.setText(arr.getString("week"));
                            //date2.setText(arr.getString("days"));
                            weather2.setText(arr.getString("weather"));
                            degree2.setText(arr.getString("temperature"));
                            System.out.println("week2");
                        }
                        if(i==3) {
                            week3.setText(arr.getString("week"));
                            //date3.setText(arr.getString("days"));
                            weather3.setText(arr.getString("weather"));
                            degree3.setText(arr.getString("temperature"));
                            System.out.println("week3");
                        }
                        if(i==4) {
                            week4.setText(arr.getString("week"));
                            //date4.setText(arr.getString("days"));
                            weather4.setText(arr.getString("weather"));
                            degree4.setText(arr.getString("temperature"));
                            System.out.println("week4");
                        }
                        if(i==5) {
                            week5.setText(arr.getString("week"));
                            //date5.setText(arr.getString("days"));
                            weather5.setText(arr.getString("weather"));
                            degree5.setText(arr.getString("temperature"));
                            System.out.println("week5");
                        }
                        if(i==6) {
                            week6.setText(arr.getString("week"));
                            //date6.setText(arr.getString("days"));
                            weather6.setText(arr.getString("weather"));
                            degree6.setText(arr.getString("temperature"));
                            System.out.println("week6");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
