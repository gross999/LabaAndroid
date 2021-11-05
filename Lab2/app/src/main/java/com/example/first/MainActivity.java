package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

        //Получение логина из reg_activity
        Bundle arguments = getIntent().getExtras();
        String Login_ = arguments.get("Login").toString();

        //Подключение к БД
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

        //Запрос на создание таблицы
        db.execSQL("CREATE TABLE IF NOT EXISTS Telephones (Manufacturer TEXT, Model TEXT, Length INTEGER, Width INTEGER, Login TEXT)");

        Adapter = new ArrayAdapter<Telephone> (this, android.R.layout.simple_list_item_1);

        Cursor query = db.rawQuery("SELECT * FROM Telephones WHERE Login = ?", new String[] {Login_});
        query.moveToFirst();
        for (int i = 0; i < query.getCount(); i ++){
            String Manufacturer = query.getString(0);
            String Model = query.getString(1);
            int Length = query.getInt(2);
            int Width = query.getInt(3);
            Telephone T = new Telephone(Manufacturer, Model, Length, Width);
            Adapter.add(T);
            query.moveToNext();
        }

        AddButton = findViewById(R.id.addButton);
        DelButton = findViewById(R.id.delButton);
        LangButton = findViewById(R.id.langButton);

        // Привязка адаптера к ListView
        ListView List = findViewById(R.id.list);
        List.setAdapter(Adapter);

        // Обработчик события нажатия на кнопку "Добавить"
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Telephone T = new Telephone(
                        Manufacturers[Rand.nextInt(5)],
                        Models[Rand.nextInt(6)],
                        10 * Rand.nextInt(100) + 400,
                        10 * Rand.nextInt(100) + 400);
                Adapter.add(T);
                Adapter.notifyDataSetChanged();
                db.execSQL("INSERT INTO Telephones (Manufacturer, Model, Length, Width, Login) values(?, ?, ?, ?, ?)",
                        new String[] {T.Manufacturer, T.Model, String.valueOf(T.Length), String.valueOf(T.Width), Login_});
            }
        });

        // Обработчик события нажатия на кнопку "Удалить"
        DelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adapter.remove(SelectedItem);
                List.setSelector(android.R.color.transparent);
                Adapter.notifyDataSetChanged();
                db.execSQL("DELETE FROM Telephones WHERE Manufacturer = ? AND Model = ? AND Length = ? AND Width = ? AND Login = ?",
                        new String[] {SelectedItem.Manufacturer, SelectedItem.Model, String.valueOf(SelectedItem.Length), String.valueOf(SelectedItem.Width), Login_});
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
        outState.putBoolean("Lang", Lang);
        super.onSaveInstanceState(outState);
    }

    // Получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Lang = savedInstanceState.getBoolean("Lang");
        SetLang(Lang);
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