package com.nexus.model;
import java.util.List;

import com.nexus.exception.NexusValidationException;

import java.util.ArrayList;

public class Project {
    private String name;
    private List<Task> Tasks;
    private int totalBudget;

    public Project(String name, int totalBudget){
        this.name = name;
        this.Tasks = new ArrayList<Task>();
        this.totalBudget = totalBudget;
    }

    public void addTask(Task t){
        int horas = 0; 
        for(Task task : Tasks){
            horas += task.estimatedEffort;
        }

        if((horas + t.estimatedEffort) > totalBudget){
            throw new NexusValidationException("Limite de horas do projeto excedido.");
        }
        else{ Tasks.add(t); }
    }
}
