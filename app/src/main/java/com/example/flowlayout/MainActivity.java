package com.example.flowlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.flowlayout_lib.FlowLayout;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private static final String arrays[] = new String[]{
            "1.C",
            "2.Java",
            "3.Objective-C",
            "4.C++",
            "5.PHP",
            "6.C#",
            "7.(Visual) Basic",
            "8.Python",
            "9.Perl",
            "10.JavaScript",
            "11.Ruby",
            "12.Visual Basic .NET",
            "13.Transact-SQL",
            "14.Lisp",
            "15.Pascal",
            "16.Bash",
            "17.PL/SQL",
            "18.Delphi/Object Pascal",
            "19.Ada",
            "20.MATLAB",
};

    private String[] arrays2 = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FlowLayout fl2= findViewById(R.id.flowlayout2_activity_main);
        final FlowLayout fl3= findViewById(R.id.flowlayout3_activity_main);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(String current : arrays){
                    TextView textView = (TextView) (getLayoutInflater().inflate(R.layout.item_textview, fl2, false));//第三个参数需要是fasle,否则返回FlowLayout无法强转TextView
                    //自己new 不行 无法强转成和FlowLayout的layoutParams相同
                    textView.setText(current);
                    fl2.addView(textView);//每次调用requestLayout和invalidate
                }
                fl2.setHorizontalSpacing(10);

                for(String current : arrays2){
                    TextView textView = (TextView) (getLayoutInflater().inflate(R.layout.item_textview, fl3, false));//第三个参数需要是fasle,否则返回FlowLayout无法强转TextView
                    //自己new 不行 无法强转成和FlowLayout的layoutParams相同
                    textView.setText(current);
                    fl3.addView(textView);//每次调用requestLayout和invalidate
                }
                fl3.setHorizontalSpacing(10);
            }
        }, 2000);

    }
}
