package com.example.raymaletdin.testtimenativealghoritm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native double calc(int n);


    @BindView(R.id.java)
    TextView java;

    @BindView(R.id.cpp)
    TextView c;

    @BindView(R.id.result)
    TextView result;

    @BindView(R.id.send)
    Button send;

    @BindView(R.id.editText)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //скроем текст
                editText.setText("");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int N = Integer.parseInt(editText.getText().toString());
                if (N == 0) {
                    Toast.makeText(MainActivity.this, "Введите размер массива", Toast.LENGTH_SHORT).show();
                } else {

                    double timeJava = 0, timeCPP = 0;

                    try {
                        timeJava = calc1(N);
                        timeCPP = calc(N);
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(MainActivity.this, "Для вычисления не хватает ОЗУ", Toast.LENGTH_LONG).show();
                    }

                    java.setText("java = " + timeJava + " sec");
                    c.setText("c++ = " + timeCPP + " sec");
                    double form = timeJava / timeCPP;
                    String formatDouble = String.format("%.3f", form);
                    result.setText("The difference = " + formatDouble);
                }
            }
        });
    }

    private double calc1(int n) {
        double[] arr;
        double startTime = System.currentTimeMillis();

        arr = new double[n];

        for (int i = 0; i < n; i++) {
            if (i == 0 || i == 1)
                arr[i] = 1;
            else
                arr[i] = arr[i - 1] + arr[i - 2];
        }

        for (int i = 1; i < n; i++)
            arr[i] /= arr[i - 1];

        return (System.currentTimeMillis() - startTime) / 1000;

    }
}
