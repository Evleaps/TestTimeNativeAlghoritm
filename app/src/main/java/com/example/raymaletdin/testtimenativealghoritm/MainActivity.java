package com.example.raymaletdin.testtimenativealghoritm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

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

    private ProgressDialog processDialog;
    private Handler handler;//handler это мост между потоками в андроид
    static double timeJava = 0, timeCPP = 0;
    static int N = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        processDialog = new ProgressDialog(this);
        processDialog.setTitle("Вычисляем");
        processDialog.setMessage("Пожалуйста подождите");
        //  processDialog.setMax(2);
        // меняем стиль на индикатор
        //  processDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // включаем анимацию ожидания
        processDialog.setIndeterminate(true);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == -1) {
                    java.setText("java = " + timeJava + " sec");
                    c.setText("c++ = " + timeCPP + " sec");
                    double form = timeJava / timeCPP;
                    String formatDouble = String.format("%.3f", form);
                    result.setText("The difference = " + formatDouble);
                    processDialog.dismiss();
                } else if (msg.what == 0)
                    processDialog.show();
                else if (msg.what == -2)
                    Toast.makeText(MainActivity.this, "Для вычисления не хватает ОЗУ", Toast.LENGTH_LONG).show();
            }
        };


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
                Thread thread = null;
                try {
                    N = Integer.parseInt(editText.getText().toString());
                    Log.d("LOG_TAG", "Обрабатывается число: " + N);

                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0);
                            Log.d("LOG_TAG", "Включена анимация");
                            try {
                                timeJava = calc1(N);
                                timeCPP = calc(N);
                            } catch (OutOfMemoryError e) {
                                handler.sendEmptyMessage(-2);
                                Log.d("LOG_TAG", "Для вычисления не хватает ОЗУ");
                            } finally {
                                handler.sendEmptyMessage(-1);
                                Log.d("LOG_TAG", "Анимация выключена");
                            }
                        }
                    });
                    thread.start();

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Введите размер массива", Toast.LENGTH_SHORT).show();
                    Log.d("LOG_TAG", "Для вычисления необходимо ввести число");
                    Log.d("LOG_TAG", e.getMessage());
                }
            }
        });
    }


    private double calc1(int n) {
        Log.d("LOG_TAG", "Java алгоритм включился");
        double startTime = System.currentTimeMillis();
        double[] arr;

        arr = new double[n];

        for (int i = 0; i < n; i++) {
            if (i == 0 || i == 1)
                arr[i] = 1;
            else
                arr[i] = arr[i - 1] + arr[i - 2];
        }

        for (int i = 1; i < n; i++)
            arr[i] /= arr[i - 1];

        Log.d("LOG_TAG", "Java алгоритм выключен");
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
