package com.nexus.model;
import java.util.List;

import com.nexus.exception.NexusValidationException;

import java.util.ArrayList;

/**
* Classe de Projetos.
*
* Esta classe possui métodos relacionados aos projetos criados no sistema.
* Com opções de adicionar uma tarefa {@link Task} a um dado projeto e
* consultar seus atributos. 
*/
public class Project {

    private String Nome;
    private List<Task> Tasks;
    private int totalBudget;

    /**
     * Contrutor de Projetos
     * Inicializa um novo Projeto
     *
     * @param name nome do projeto 
     * @param totalBudget limite de horas do projeto 
     */
    public Project(String name, int totalBudget){
        this.Nome = name;
        this.Tasks = new ArrayList<Task>();
        this.totalBudget = totalBudget;
    }

    /**
     * Adiciona uma nova Task {@link Task} no Projeto 
     * Se as horas estimadas da tarefa excederem o limite de horas do projeto,
     * a tarefa não será adicionada e retornará NexusValidationException {@link NexusValidationException}. 
     *
     * @param t tarefa a ser adicionada 
     */
    public void addTask(Task t){
        int horas = 0; 
        for(Task task : Tasks){
            horas += task.estimatedEffort;
        }

        if((horas + t.estimatedEffort) > totalBudget){
            Task.totalValidationErrors++;
            throw new NexusValidationException("Limite de horas do projeto excedido.");
        }
        else{ Tasks.add(t); }
    }

    /**
     * Retorna a lista de Tasks {@link Task} associadas ao projeto
     *
     * @return lista de tarefas
     */
    public List<Task> getTasks(){
        return this.Tasks;
    }

    /**
     * Retorna o nome atribuído ao projeto
     *
     * @return nome do projeto
     */    
    public String getName(){
        return this.Nome;
    }

    /**
     * Override do método de exibição do Projeto
     *
     * @return nome do projeto
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
