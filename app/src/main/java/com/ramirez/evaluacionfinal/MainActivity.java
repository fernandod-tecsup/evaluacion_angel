package com.ramirez.evaluacionfinal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> taskList;

    // elementos de la interface
    private TextInputEditText inputTask, searchTask;// aqui se referenciaran elementos de la UI
    private Button addTaskButton, searchButton; // aqui se referenciaran botones
    private ListView listViewTasks;// aqui se referenciara el listView de la UI
    private Button deleteTaskButton;
    private Button editTaskButton;


    //Adaptador que conecta el ListView de la interfaz con una Array
    private ArrayAdapter<String> adapter;// es un conector entre <array> y ListView

    //indice de la tarea seleccionada en el listView
    private int selectedTaskPosition = -1;// indice de la tarea seleccionada en el listView

    // v. 3.0 indica si se está editando
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        taskList = new ArrayList<>();// Array donde se guardan las tareas, es el array principal

        inputTask = findViewById(R.id.inputTask);
        searchTask = findViewById(R.id.searchTask);
        addTaskButton = findViewById(R.id.addTaskButton);
        searchButton = findViewById(R.id.searchButton);

        listViewTasks=findViewById(R.id.listViewTasks);//
        deleteTaskButton=findViewById(R.id.deleteTaskButton);//

        editTaskButton=findViewById(R.id.editTaskButton);//


        // Configurar el adaptador para el ListView con taskList
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listViewTasks.setAdapter(adapter);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  task = inputTask.getText().toString().trim();// trim() quita espacios en blanco al inicio o al final del txt

                if(!task.isEmpty()) // verificamos si el texto (task) no es vacio
                {
                    if (isEditing == true)  // modo edicion
                    {
                        // editamos la tarea de acuerdo al indice "selectedTaskPosition"
                        taskList.set(selectedTaskPosition, task);
                        isEditing = false;
                        addTaskButton.setText("Añadir Tarea");
                        Toast.makeText(MainActivity.this, "Tarea actualizada: "+taskList.get(selectedTaskPosition), Toast.LENGTH_LONG).show();

                    }

                    else // modo agregar
                    {
                        taskList.add(task); // agregamos la tarea (String task) al ArrayList "task"

                    }

                    inputTask.setText(""); // limpio el input de texto
                    adapter.notifyDataSetChanged();//aviso al adaptador que hay un cambio
                    selectedTaskPosition = -1; // resetamos la ubicacion del indicador de elemento seleccionado el listview
                }

                else  // si caja de texto esta vacia
                {            Toast.makeText(MainActivity.this, "El campo no puede estar vacio al agregar o actualizar", Toast.LENGTH_LONG).show();        }

            }
        }); // fin de listener addTaskButton

        /*********************************************************************/


/*************************************************************************************/
        //Detector de click en un item de listView (lista de tareas en la interface)
        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                selectedTaskPosition = position; // guardo el indice(posicion) del elemento al que se hizo click


                Toast.makeText(MainActivity.this, "Tarea seleccionada: "+taskList.get(position), Toast.LENGTH_LONG).show();

            }
        }); // fin de listener click en item de  listViewTasks
/***************************************************************************************/

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedTaskPosition != -1)
                {
                    //Creacion de AlertDialog
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Eliminar Tarea")
                            .setMessage("¿Esta seguro de proceder con la eliminación?")
                            .setPositiveButton("Si",(dialog, which) -> {
                                taskList.remove(selectedTaskPosition);
                                adapter.notifyDataSetChanged();//aviso al adaptador que hay un cambio
                                selectedTaskPosition =-1;// reiniciamos el indice de la seleccion
                                Snackbar.make(view, "Tarea Eliminada", Snackbar.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("No",(dialog, which) -> {
                                dialog.dismiss();//cierre de alert dialog en caso sea negativa la respuesta
                            })
                            .show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Debe seleccionar la tarea a eliminar", Toast.LENGTH_LONG).show();
                }

            }
        }); // fin de listener click deleteTaskButton

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String keyword = searchTask.getText().toString().trim();

                if (!keyword.isEmpty()) // si hemos escrito algo
                {
                    displaySearchResults(keyword);// metodo para buscar en el arrray principal lo que se haya escrito en al caja busqueda
                }

                else {
                    // Restablecer la lista completa de tareas si el campo de búsqueda está vacío
                    adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, taskList);
                    listViewTasks.setAdapter(adapter);
                }


            }
        }); // fin de listener searchButton



        // boton Editar  */

        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedTaskPosition != -1) // si se confirma que hay algo seleccionado
                {
                    isEditing = true; // activamos el modo edicion
                    addTaskButton.setText("Guardar Tarea Editada"); // cambiamos el texto del boton "añadir tarea"

                    String taskToEdit = taskList.get(selectedTaskPosition);
                    inputTask.setText(taskToEdit);


                }

                else { // si no hay nada seleccion

                    Toast.makeText(MainActivity.this, "Debe seleccionar la tarea a editar", Toast.LENGTH_LONG).show();
                }


            }
        });





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }// fin de onCreate



    private void displaySearchResults(String keyword)

    {

        ArrayList<String> searchResults = new ArrayList<>();// creamos un array temporal para los elementos que conincidan

        for (String task : taskList)  //recorremos el array y cada item lo vamos guardando en la variable(temporal) "task"
        {
            if (task.contains(keyword)) // verificamos si en task contiene a la palabra clave (la que estamos buscando)
            {
                searchResults.add(task);  // agregamos esa tarea (task) al array temporal(searchResults)
                // results = results + "-> "+task+"\n";  descontinuado en la version 1.5
            }
        } // fin del for

        // conectamos el listViewTasks con el adapter que ahora esta vinculado con el array temporal de resultados (searchResults)
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResults);
        listViewTasks.setAdapter(searchAdapter);






    }
}