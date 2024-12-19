package com.ramirez.evaluacionfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextInputLayout inputTextUser;
    private TextInputLayout inputTextPass;
    private TextInputEditText editTextUser;
    private TextInputEditText editTextPass;
    private int intentsSession = 0;

    List<User> list = new ArrayList<User>();
    //declaracion de usuarios estaticos
    User user1 = new User("user111", "pass111" );
    User user2 = new User("user222", "pass222" );
    User user3 = new User("user333", "pass333" );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        //referenciando componentes por id del xml
        btnLogin = findViewById(R.id.btnLogin);
        editTextUser = findViewById(R.id.UserEditText);
        editTextPass = findViewById(R.id.PassEditText);
        inputTextUser = findViewById(R.id.IngrUser);
        inputTextPass = findViewById(R.id.IngrPass);
        list.add(user1);
        list.add(user2);
        list.add(user3);
        //inicio del evento onclick del boton inicio sesion
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentsSession = intentsSession +1;
                //validacion de cantidad de intentos por inicio de sesion
                if(intentsSession > 3){
                    inputTextUser.setEnabled(false);
                    inputTextPass.setEnabled(false);
                    Snackbar.make(v,"app bloqueada: realizó"+intentsSession+"intentos fallidos",Snackbar.LENGTH_SHORT).show();
                }else{
                    //obtencion de data de las cajas de texto
                    String uss = String.valueOf(editTextUser.getText());
                    String pass = String.valueOf(editTextPass.getText());
                    //bucle para iniciar la validacion de usurios y password
                    for (int i = 0; i < list.size(); i++) {
                        if (uss.equals(list.get(i).getNameUser())) {
                            if (pass.equals(list.get(i).getPassword())) {
                                //estado de la validacion correcta para el password y el usuario
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //estado negativo del password
                                Intent intent = new Intent(LoginActivity.this, IncorrectPasswordActivity.class);
                                startActivity(intent);
                                finish();
                                Snackbar.make(v, "password incorrecto", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            //estado negativo del usuario
                            Snackbar.make(v, "usuario incorrecto", Snackbar.LENGTH_SHORT).show();
                            Log.v("list user", "" + list.get(i).getNameUser() + " " + uss);
                        }
                    }

                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}