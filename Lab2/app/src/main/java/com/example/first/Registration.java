package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        SharedPreferences Accounts = getSharedPreferences("Accounts", MODE_PRIVATE);

        Button logButton = findViewById(R.id.logButton);
        Button RegButton = findViewById(R.id.regButton);

        TextInputEditText Login = findViewById(R.id.logLogin);
        TextView ErrorText = findViewById(R.id.errorText);
        EditText Password = findViewById(R.id.Password);

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String InputLogin = Login.getText().toString();         //Введенный логин при входе
                String InputPassword = Password.getText().toString();   //Введенный пароль при входе

                if (InputLogin.trim().isEmpty())
                    ErrorText.setText("Необходимо ввести логин");
                else if (InputPassword.trim().isEmpty())
                    ErrorText.setText("Необходимо ввести пароль");
                else if (!Accounts.contains(InputLogin))
                    ErrorText.setText("Неверно указан логин или вы еще не зарегистрировались");
                else {
                    String RequirePassword = Accounts.getString(InputLogin, "");//Настроящий сохраненный пароль
                    if (RequirePassword.equals(InputPassword))  //Если пароль правильный
                    {
                        Intent intent = new Intent(Registration.this, MainActivity.class);
                        intent.putExtra("Login", InputLogin); //Передача данных в main_activity
                        startActivity(intent);
                    }
                    else
                        ErrorText.setText("Введен неверный пароль");
                }
            }
        });

        TextInputEditText RegLogin = findViewById(R.id.regLogin);
        TextView RegErrorText = findViewById(R.id.regErrorText);
        EditText RegPassword = findViewById(R.id.regPassword);

        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RegInputLogin = RegLogin.getText().toString();         //Введенный логин при регистрации
                String RegInputPassword = RegPassword.getText().toString();   //Введенный пароль при регистрации

                if (RegInputLogin.trim().isEmpty())
                    RegErrorText.setText("Необходимо ввести логин");
                else if (RegInputPassword.trim().isEmpty())
                    RegErrorText.setText("Необходимо ввести пароль");
                else if (Accounts.contains(RegInputLogin))
                    RegErrorText.setText("Выбранный логин уже занят");
                else
                {
                    SharedPreferences.Editor Editor = Accounts.edit();
                    Editor.putString(RegInputLogin, RegInputPassword);
                    Editor.commit();
                    RegErrorText.setText("Успешно");
                }
            }
        });
    }
}