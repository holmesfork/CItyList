package com.example.holmesk.citylist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] letter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    List<String> cityLetter = new ArrayList<>();
    List<String> cityName = new ArrayList<>();
    List<String> letterToCity = new ArrayList<String>();
    ListView lv;
    ListView lv1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Gson gson = new Gson();
            List<CityBean.DataBean> data = gson.fromJson((String) msg.obj, CityBean.class).getData();
            String str = "";
            for (int i = 0; i < data.size(); i++) {
                cityLetter.add(data.get(i).getName());
                for (int i1 = 0; i1 < data.get(i).getCities().size(); i1++) {

                    cityName.add(data.get(i).getCities().get(i1));
                }
                letterToCity.add(data.get(i).getName());
                letterToCity.addAll(cityName);
                cityName.clear();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                NetUtils netUtils = new NetUtils();
                String citys = netUtils.getCitys("http://ic.snssdk.com/2/article/city/" +
                        "?iid=9387146041&device_id=35792444779&ac=wifi&" +
                        "channel=2345_1357504&aid=13&app_name=news_article&version_code" +
                        "=480&version_name=4.8.0&device_platform=android&ssmix=a&device" +
                        "_type=iToolsAVM&os_api=19&os_version=4.4.4&uuid=35228404438469" +
                        "5&openudid=84c1c7b192991cc6&manifest_version_code=480");
                Message msg = new Message();
                msg.obj = citys;
                handler.sendMessage(msg);
            }
        }).start();


        lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(new MyAdapter());
        lv.setOnItemClickListener(this);

        lv1 = (ListView) findViewById(R.id.listView2);
        lv1.setAdapter(new MyAdapter1());
        lv1.setOnItemClickListener(this);

    }

    class MyAdapter1 extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return letter.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return letter[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.letter_list, null);
            TextView tv = (TextView) view.findViewById(R.id.letterListTextView);
            tv.setText(letter[position]);
            return view;
        }

    }

    class MyAdapter extends BaseAdapter {

        final static int TYPE_1 = 1;
        final static int TYPE_2 = 2;

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return letterToCity.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return letterToCity.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            for (int i = 0; i < letter.length; i++) {
                if (letterToCity.get(position).equals(letter[i])) {
                    return TYPE_1;
                }
            }
            return TYPE_2;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        class ViewHolder1 {
            TextView tv;
        }

        class ViewHolder2 {
            TextView tv;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * 即使convertView缓存了一些布局，但是在重构时，根本不知道怎么样去让convertView返回你所需要的布局，这时你需
             * 要让adapter知道我当前有哪些布局，我重构Item时的布局选取规则，好让convertView能返回你需要的布局。
             * 需要重写以下两个函数
             * @Override
             * public int getItemViewType(int position) {}这个函数获取在getView中创建的视图的类型
             * @Override
             * public int getViewTypeCount() {}返回在getView中创建视图类型的数量
             * 至于这两个方法的详细用处，自己看api即可
             */
            ViewHolder1 vh1 = null;
            ViewHolder2 vh2 = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                switch (type) {
                    case TYPE_1:
                        convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.letter, null);
                        vh1 = new ViewHolder1();
                        vh1.tv = (TextView) convertView.findViewById(R.id.letterTextView);
                        convertView.setTag(vh1);
                        break;
                    case TYPE_2:
                        convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.city, null);
                        vh2 = new ViewHolder2();
                        vh2.tv = (TextView) convertView.findViewById(R.id.cityTextView);
                        convertView.setTag(vh2);
                        break;
                    default:
                        break;
                }
            } else {
                switch (type) {
                    case TYPE_1:
                        vh1 = (ViewHolder1) convertView.getTag();
                        break;
                    case TYPE_2:
                        vh2 = (ViewHolder2) convertView.getTag();
                        break;
                    default:
                        break;
                }
            }
            switch (type) {
                case TYPE_1:
                    vh1.tv.setText(letterToCity.get(position));
                    break;
                case TYPE_2:
                    vh2.tv.setText(letterToCity.get(position));
                    break;
                default:
                    break;
            }
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch (parent.getId()) {
            case R.id.listView1:
                boolean isLetter = false;
                for (int i = 0; i < letter.length; i++) {
                    if (letter[i].equals(letterToCity.get(position))) {
                        isLetter = true;
                        break;
                    }
                }
                if (!isLetter) {
                    Toast.makeText(this, letterToCity.get(position), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.listView2:
                for (int i = 0; i < letterToCity.size(); i++) {
                    if (letter[position].equals(letterToCity.get(i))) {
                        lv.setSelection(i);
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }
}



