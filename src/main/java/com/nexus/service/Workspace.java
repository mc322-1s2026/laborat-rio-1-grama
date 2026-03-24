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

/**
* Classe que atua como o contêiner principal.
* 
* Responsável por armazenar as listas globais de tarefas {@link Task}, usuários {@link User} 
* e projetos {@link  Project}. Oferece métodos de busca e filtragem, usados nos relatórios.  
*/
public class Workspace{
    private final List<Task> tasks = new ArrayList<>();
    private final List<Project> projetos = new ArrayList<>();
    private final List<User> usuarios = new ArrayList<>();

    /**
     * Adiciona nova Task {@link Task} na lista de tarefas
     *
     * @param task tarefa a ser adicionada
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Adiciona novo usuário {@link User} na lista de usuários
     *
     * @param user usuário a ser adicionado
     */
    public void addUser(User user){
        usuarios.add(user);
    }


    /**
     * Adiciona uum novo projeto {@link Project} na lista de projetos
     * 
     * @param proj Projeto a ser adicionado
     */
    public void addProj(Project proj) {
        projetos.add(proj);
    }


    /**
     * Retorna a lista de tarefas cadastradas global como objeto imodificável
     * 
     * @return lista inalterável com todas as Tasks 
     */
    public List<Task> getTasks() {
        // Retorna uma visão não modificável para garantir encapsulamento
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Retorna a lista de usuários cadastrados global como objeto imodificável
     *
     * @return lista inalterável com todos os usuários
     */
    public List<User> getUsers(){
        return Collections.unmodifiableList(usuarios);
    }

    /**
     * Retorna os 3 usuários que possuem mais tarefas concluídas,
     * ou seja, com status DONE {@link TaskStatus} com o uso de Stream API.
     *
     * @return lista contendo três usuários
     */
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

    /**
     * Lista todos os usuários com mais de 10 tarefas com status
     * IN_PROGRESS {@link TaskStatus} com uso de Stream API.
     *
     * @return lista com nome de usuários
     */
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

    /**
     * Calcula o percentual de conclusão dos projetos {@link Project}
     * no sistema, dividindo o total de tarefas concluídas
     * pelo total de tarefas. Com uso de Stream API.
     * 
     * se as tarefas totais forem 0, o percentual é definido como 0. 
     *
     * @return lista de pares com nome do projeto e seu respectivo percentual
     */
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

    /**
     * Identifica qual status de tarefa {@link TaskStatus}, exceto DONE,
     * possui o maior número de Tasks cadastradas com uso de Stream API.
     *
     * @return TaskStatus com maior recorrência
     */
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

    /**
     * Busca um objeto do tipo Project {@link Project} pelo seu nome,
     * retorna valor null se não existir.
     *
     * @param nome nome do projeto sendo procurado
     * @return projeto que corresponde ao nome
     */
    public Project buscarProj(String nome){
        
        Project proj = projetos.stream()
            .filter(p -> p.getName().equalsIgnoreCase(nome))
            .findFirst()
            .orElse(null);
        
        return proj;
    }
}