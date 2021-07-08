package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Calculador extends AppCompatActivity {

    EditText radio, base, altura, baseA, baseB, alturaTra;
    TextView radioR, triR, traR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculador);

        radio = (EditText)findViewById(R.id.textRadio);
        radioR = (TextView)findViewById(R.id.textRadioR);

        base = (EditText)findViewById(R.id.textBase);
        altura = (EditText)findViewById(R.id.textAltura);
        triR = (TextView)findViewById(R.id.textRadioT);

        baseA = (EditText)findViewById(R.id.textBaseA);
        baseB = (EditText)findViewById(R.id.textBaseB);
        alturaTra = (EditText)findViewById(R.id.textBaseH);
        traR = (TextView)findViewById(R.id.textRadioTra);
    }

    public void calcularArea(View v){
        double resultado;
        DecimalFormat formato = new DecimalFormat("#.00");
    try {
        resultado = Math.pow(Double.parseDouble(radio.getText().toString()), 2.0) * Math.PI;
        radioR.setText("El área es: " + formato.format(resultado));
    }catch(Exception e) {

      androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Valor de radio \n invalido \n");
        builder.setPositiveButton("sry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    };


    }

    public void calcularAreaT(View v){
        double resultado;
        DecimalFormat formato = new DecimalFormat("#.00");

        try {
            resultado = (Double.parseDouble(base.getText().toString()) * Double.parseDouble(altura.getText().toString()))/2;
            triR.setText("El área es: " + formato.format(resultado));
        }catch(Exception e) {

            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Valor de base/altura \n invalido \n");
            builder.setPositiveButton("sry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        };


    }

    public void calcularAreaTra(View v){
        double resultado;
        DecimalFormat formato = new DecimalFormat("#.00");

        try {
            resultado = ((Double.parseDouble(baseA.getText().toString()) + Double.parseDouble(baseB.getText().toString()))/2)*Double.parseDouble(alturaTra.getText().toString());
            traR.setText("El área es: " + formato.format(resultado));
        }catch(Exception e) {

            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Valor de base/altura \n invalido \n");
            builder.setPositiveButton("sry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        };


    }

    public void menuC (View v){
        Calculador.this.finish();
    }
}