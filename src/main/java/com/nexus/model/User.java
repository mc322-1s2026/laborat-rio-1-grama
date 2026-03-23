package com.nexus.model;

//import com.nexus.model.Task;
//import java.util.ArrayList;
import java.util.List;
//import java.util.Collections;
import java.util.Objects;

public class User {
    private final String username;
    private final String email;

    public User(String username, String email) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username não pode ser vazio.");
        }
        if (email == null || email.isBlank() || !isValidEmailAddress(email)){
            // Talvez achar um jeito de considerar coisa além do ".com"
            throw new IllegalArgumentException("Insira um email válido.");
        }
        this.username = username;
        this.email = email;
    }

    @Override
    public boolean equals(Object u){
        if(this == u) return true;  /* Se tiver mesma posição de memória, é igual */
        if (u == null || getClass () != u.getClass ()) return false ; /* Endereço null ou classes diferentes */
        /* Considerando email como atributo que diferencia objetos */
        User user = (User) u;
        return this.email == user.email;
    }

    @Override
    public int hashCode(){
        return Objects.hash(email); /* Gera hashCode a partir do email */
    }

    public boolean isValidEmailAddress(String email) {
           String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
           java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
           java.util.regex.Matcher m = p.matcher(email);
           return m.matches();
    }

    public String consultEmail() {
        return email;
    }

    public String consultUsername() {
        return username;
    }

    public long calculateWorkload(List<Task> listaDeTarefas) {
        long qntdTarefas = 0; /* Contador de Tarefas */

        for(Task task : listaDeTarefas){ /* Iterando a lista de tarefas */
            /* Contar tasks in progress que pertencem ao usuário */
            if ((task.getStatus() == TaskStatus.IN_PROGRESS) && (this.equals(task.getOwner())))
            {
                qntdTarefas++; /* Incrementa, caso positivo */
            }
        }

        return qntdTarefas; 
    }
}