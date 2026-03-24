package com.nexus.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nexus.model.Project;
import com.nexus.model.Task;
import com.nexus.model.TaskStatus;
import com.nexus.model.User;

public class Workspace{
    private final List<Task> tasks = new ArrayList<>();
    private final List<Project> projetos = new ArrayList<>();
    private final List<User> usuarios = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addUser(User user){
        usuarios.add(user);
    }

    public void addProj(Project proj) {
        projetos.add(proj);
    }

    public List<Task> getTasks() {
        // Retorna uma visão não modificável para garantir encapsulamento
        return Collections.unmodifiableList(tasks);
    }

    public List<User> getUsers(){
        return Collections.unmodifiableList(usuarios);
    }

    // Um método que retorna os 3 usuários que possuem 
    // o maior número de tarefas no status DONE.
    public List<User> TopPerformers(){

        List<User> tp = tasks.stream()
            .filter(t -> t.getStatus().equals(TaskStatus.DONE))
            .filter(t -> t.getOwner() != null)
            .collect(Collectors.groupingBy(
                Task::getOwner,
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted((e1,e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(3)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
            
        return tp; 
    }

    // Overloaded Users: Listar todos os usuários cuja carga de
    // trabalho atual (IN_PROGRESS) ultrapassa 10 tarefas.
    public List<User> OverloadUsers(){
        
        List<User> olu = tasks.stream()
            .filter(t -> t.getStatus().equals(TaskStatus.IN_PROGRESS))
            .collect(Collectors.groupingBy(
                Task::getOwner,
                Collectors.counting()
            ))
            .entrySet().stream()
            .filter(e -> e.getValue() > 10)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        return olu; 
    }

    // Project Health: Para um dado projeto, calcular o percentual
    // de conclusão (Tarefas DONE / Total de Tarefas).
    public Map<Project, String> ProjectHealth(){

        Map<Project, String> res = projetos.stream()
        .collect(Collectors.toMap(
            p -> p,
            p -> {
                long t_total = p.getTasks().size();
                long t_done = p.getTasks().stream()
                    .filter(t -> t.getStatus().equals(TaskStatus.DONE))
                    .count();

                double percent;

                if(t_total == 0){
                    percent = 0;
                } 
                else{
                    percent = (double) t_done / t_total;
                }
                return String.format("%.2f%%", percent * 100);
            }
        ));
        return res;
    }

    // Global Bottlenecks: Identificar qual o status que possui o 
    // maior número de tarefas no sistema (exceto DONE).
    public TaskStatus GlobalBottlenecks(){
        
        TaskStatus maiorTask = tasks.stream()
            .filter(t -> !t.getStatus().equals(TaskStatus.DONE))
            .collect(Collectors.groupingBy(
                Task::getStatus,
                Collectors.counting()
            ))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
        
        return maiorTask;
    }

    public Project buscarProj(String nome){
        
        Project proj = projetos.stream()
            .filter(p -> p.getName().equalsIgnoreCase(nome))
            .findFirst()
            .orElse(null);
        
        return proj;
    }
}