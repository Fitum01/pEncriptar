package com.example.pencriptar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private EditText etTexto, etPassword, etFinal;
    private Button btnEncriptar, brnDesEncriptar;
    private String textoSalida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTexto = findViewById(R.id.idEtTexto);
        etPassword = findViewById(R.id.idEtPassword);
        btnEncriptar = findViewById(R.id.idEncriptar);
        brnDesEncriptar = findViewById(R.id.idDesencriptar);
        etFinal = findViewById(R.id.idEtTextoCod);


        btnEncriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    textoSalida = encriptar(etTexto.getText().toString(), etPassword.getText().toString());
                    etFinal.setText(textoSalida);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        brnDesEncriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    textoSalida = desencriptar(textoSalida, etPassword.getText().toString());
                    etFinal.setText(textoSalida);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private String desencriptar(String datos, String password) throws Exception{
        SecretKeySpec secretKey = generateKey (password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte [] datosDecodificados = Base64.decode(datos, Base64.DEFAULT);
        byte [] datosDesencriptadosBytes = cipher.doFinal(datosDecodificados);
        String datosDesencriptadosString =new String(datosDesencriptadosBytes);
        return datosDesencriptadosString;

    }

    private String encriptar (String datos, String password) throws Exception{
        SecretKeySpec secretKey = generateKey (password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte [] datosEncriptadosBytes = cipher.doFinal(datos.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
        return  datosEncriptadosString;
    }

    private SecretKeySpec generateKey (String password) throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte [] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKey = new SecretKeySpec (key, "AES");
        return secretKey;
    }
}