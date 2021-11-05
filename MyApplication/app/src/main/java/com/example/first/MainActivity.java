package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String [] Manufacturers = {"Samsung", "Apple", "Huawei", "Xiaomi", "Honor"};
    String [] Models = {"N73", "S8", "A9", "G6", "S9", "H80"};
    Random Rand = new Random();
    Telephone SelectedItem;   // Выделенный элемент в ListView
    ArrayAdapter<Telephone> Adapter;
    Button AddButton, DelButton, LangButton;
    boolean Lang = true; //Переменная для хранения языка {0 - En, 1 - Ру}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddButton = findViewById(R.id.addButton);
        DelButton = findViewById(R.id.delButton);
        LangButton = findViewById(R.id.langButton);

        // Создание адаптера и его привязка к ListView
        Adapter = new ArrayAdapter<Telephone> (this, android.R.layout.simple_list_item_1);
        ListView List = findViewById(R.id.list);
        List.setAdapter(Adapter);

        // Обработчик события нажатия на кнопку "Добавить"
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Telephone Temp = new Telephone(
                        Manufacturers[Rand.nextInt(5)],
                        Models[Rand.nextInt(6)],
                        10 * Rand.nextInt(100) + 400,
                        10 * Rand.nextInt(100) + 400);
                Adapter.add(Temp);
                Adapter.notifyDataSetChanged();
            }
        });

        // Обработчик события нажатия на кнопку "Удалить"
        DelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adapter.remove(SelectedItem);
                Adapter.notifyDataSetChanged();
                List.setSelector(android.R.color.transparent);
            }
        });

        // Обработчик события нажатия на кнопку "Рус / Eng"
        LangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lang = !Lang;   //Смена языка
                SetLang(Lang);
            }
        });

        //Обработчик события нажатия на элемент ListView
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                SelectedItem = (Telephone) parent.getItemAtPosition(position);
                List.setSelector(R.color.orange);
            }
        });

        // Обработчик события выделения элемента ListView
        List.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedItem = (Telephone) parent.getItemAtPosition(position);
                List.setSelector(R.color.orange);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    //Сохранение данных при повороте экрана
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList <Telephone> T = new ArrayList<Telephone>();
        for (int i = 0; i < Adapter.getCount(); i ++)
            T.add(Adapter.getItem(i));
        outState.putSerializable("Values", T);
        outState.putBoolean("Lang", Lang);
        super.onSaveInstanceState(outState);
    }

    // Получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Lang = savedInstanceState.getBoolean("Lang");
        SetLang(Lang);

        ArrayList<Telephone> T = (ArrayList<Telephone>) savedInstanceState.getSerializable("Values");
        Adapter.addAll(T);
        Adapter.notifyDataSetChanged();
    }

    //Установить язык (Именение текста на кнопках)
    private void SetLang(boolean Lang)
    {
        if (Lang)
        {
            AddButton.setText("Добавить");
            DelButton.setText("Удалить");
            LangButton.setText("Рус / Eng");
        }
        else
        {
            AddButton.setText("Add");
            DelButton.setText("Delete");
            LangButton.setText("Eng / Рус");
        }
    }
}