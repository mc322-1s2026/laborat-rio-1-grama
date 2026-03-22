package com.nexus.model;

import com.nexus.exception.NexusValidationException;
import java.time.LocalDate;


public class Task {
    // Métricas Globais (Alunos implementam a lógica de incremento/decremento)
    public static int totalTasksCreated = 0;
    public static int totalValidationErrors = 0;
    public static int activeWorkload = 0;

    private static int nextId = 1;

    /* "Final" nos atributos que devem ser imutáveis após o nascimento do objeto */
    private final int id;
    private final LocalDate deadline; // Imutável após o nascimento
    private String title;
    private TaskStatus status;
    private User owner;

    public Task(String title, LocalDate deadline) {
        this.id = nextId++;
        this.deadline = deadline;
        this.title = title;
        this.status = TaskStatus.TO_DO;
        
        // Ação do Aluno:
        totalTasksCreated++; 
    }

    /**
     * Move a tarefa para IN_PROGRESS.
     * Regra: Só é possível se houver um owner atribuído e não estiver BLOCKED.
     */
    public void moveToInProgress(User user) {
        if (user == null)
        { /* Regra 01: Deve haver um owner atribuído: usuário informado deve existir */
            totalValidationErrors++;
            throw new NexusValidationException("Tarefa precisa ter um dono.");
        }
        if(getStatus() == TaskStatus.BLOCKED)
        {/* Regra 02: Tarefa não deve estar BLOCKED */
            totalValidationErrors++;
            throw new NexusValidationException("Tarefa bloqueada: Não é possível alterar seu status.");
        } else
        {/* Caso contrário, é possível colocar a tarefa como IN_PROGRESS */
            this.owner = user;
            this.status = TaskStatus.IN_PROGRESS;
            activeWorkload++;
        }

        // [OK] TODO: Implementar lógica de proteção e atualizar activeWorkload
        // Se falhar, incrementar totalValidationErrors e lançar NexusValidationException
    }

    /**
     * Finaliza a tarefa.
     * Regra: Só pode ser movida para DONE se não estiver BLOCKED.
     */
    public void markAsDone() {
        if(getStatus() == TaskStatus.BLOCKED)
        {
            totalValidationErrors++; /* Deveria incrementar isso aqui também? */
            throw new NexusValidationException("Tarefa bloqueada: Não é possível alterar seu status.");
        }
        this.status = TaskStatus.DONE;
        activeWorkload--;
        // [OK] TODO: Implementar lógica de proteção e atualizar activeWorkload (decrementar)
    }

    public void setBlocked(boolean blocked) {
        /* Task precisa não estar DONE! */
        if((this.status != TaskStatus.DONE)){
            if (blocked) 
            {
                this.status = TaskStatus.BLOCKED;
            } else {
                this.status = TaskStatus.TO_DO; // Simplificação para o Lab
            }
        } else { /* Tentaram bloquear uma tarefa com status "DONE" */
            totalValidationErrors++;
            throw new NexusValidationException("Tarefa em status \"DONE\" não pode ser bloqueada.");
        }
    }

    // Getters
    public int getId() { return id; }
    public TaskStatus getStatus() { return status; }
    public String getTitle() { return title; }
    public LocalDate getDeadline() { return deadline; }
    public User getOwner() { return owner; }
}