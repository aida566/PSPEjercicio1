package com.example.aidas.pspejercicio1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Button bt1;
    Button bt2;
    Runtime rt;
    Process proceso;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        tv1 = (TextView) findViewById(R.id.tv1);

        rt = Runtime.getRuntime();


        bt1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String cadena = "ping -c 3 8.8.8.8";
                //cadena = "ifconfig";

                try {
                    proceso = rt.exec(cadena);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InputStream is = proceso.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                final BufferedReader br = new BufferedReader(isr);

                Thread th = new Thread(new Runnable(){

                    @Override
                    public void run() {

                        String linea;

                        try {

                            while ((linea = br.readLine()) != null) {

                                //Esto  no se debe hacer, no se puede acceder a la interfaz desde
                                //una hebra
                                //tv1.setText(tv1.getText() + " " + linea );

                                mostrarResultado(linea);
                            }

                        } catch (IOException ex) {

                            System.out.println(ex.toString());
                        }

                    }


                });

                th.start();


            }
        });

        bt2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(proceso != null){

                    // R1 Vemos cuando tarda.
                    System.out.println(System.nanoTime());

                    proceso.destroy();
                    //proceso.destroyForcibly();

                    // R1
                    try {

                        proceso.waitFor();

                    } catch (InterruptedException ex) {

                        System.out.println(".-.-.-.-");

                    }

                    System.out.println(System.nanoTime());
                }


            }
        });

    }

    private void mostrarResultado(final String linea) {

        //Post: se ejecutara en la interfeaz al ecodigo que le pasemos
        //Para ello utilizamos un runnable

        tv1.post(new Runnable() {
            @Override
            public void run() {

                tv1.append(linea + "\n");
            }
        });

        //Otra forma de hacerlo
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tv1.append("***" + linea + "\n");


            }
        });
    }
}

