package com.nexus.model;

import com.nexus.exception.NexusValidationException;
import java.time.LocalDate;

/** Classe de Tarefa
 * 
 * Esta classe possui métodos e atributos relacionados a tarefas a 
 * serem manipuladas no Nexus. Uma tarefa deve possuir um status e 
 * sua lógica de transição de estado está implementada. 
 */

public class Task {
    public static int totalTasksCreated = 0;
    public static int totalValidationErrors = 0;
    public static int activeWorkload = 0;
    public int estimatedEffort;

    private static int nextId = 1;
    private final int id;
    private final LocalDate deadline;
    private String title;
    private TaskStatus status;
    private User owner;
    private Project projeto;

    /** Construtor de Tarefa
     * 
     * Inicializa uma nova Taarefa
     * 
     * @param title String - título da Tarefa
     * @param deadline data limite para realização da Tarefa
     * @param horas inteiro - horas dedicadas para realização da Tarefa
     * @param projeto projeto no qual a Tarefa está atribuída
     */
    public Task(String title, LocalDate deadline, int horas, Project projeto) {
        this.id = nextId++;
        this.deadline = deadline;
        this.title = title;
        this.status = TaskStatus.TO_DO;
        this.estimatedEffort = horas;
        this.projeto = projeto;
        this.owner = null;
        
        totalTasksCreated++; 
    }

    /** Método para mudar o estado de uma tarefa para IN_PROGRESS
     * 
     * Ao mudar o status, o contador activeWorkload é incrementado.
     * A Tarefa deve possuir um dono {@link User} e não deve estar bloqueada.
     * Caso contrário, o status da tarefa não mudará e retornará
     * NexusValidationException {@link NexusValidationException}
     * 
     * @param user usuário para quem a tarefa será atribuída
     */
    public void moveToInProgress(User user) {
        if (user == null)
        {
            totalValidationErrors++;
            throw new NexusValidationException("Tarefa precisa ter um dono.");
        }
        if(getStatus() == TaskStatus.BLOCKED)
        {
            totalValidationErrors++;
            throw new NexusValidationException("Tarefa bloqueada: Não é possível alterar seu status.");
        } else
        {
            this.owner = user;
            this.status = TaskStatus.IN_PROGRESS;
            activeWorkload++;
        }
    }

    /** Método para mudar o estado de uma tarefa para DONE
     * 
     * O contador activeWorkload é decrementado.
     * Para mudar o estado de uma tarefa, ela não pode estar bloqueada.
     * Caso contrário, o status da tarefa não mudará e retornará
     * NexusValidationException {@link NexusValidationException}
     */
    public void markAsDone() {
        if(getStatus() == TaskStatus.BLOCKED)
        {
            totalValidationErrors++;
            throw new NexusValidationException("Tarefa bloqueada: Não é possível alterar seu status.");
        }
        this.status = TaskStatus.DONE;
        activeWorkload--;
    }

    /** Método para mudar o estado de uma tarefa para BLOCKED
     * 
     * Para ser bloqueada, uma tarefa não deve possuir o estado DONE.
     * Caso contrário, o status da tarefa não mudará e retornará
     * NexusValidationException {@link NexusValidationException}
     * 
     * @param blocked Boolean - parâmetro que indica se uma tarefa deve 
     * ser bloqueada ou não
     */
    public void setBlocked(boolean blocked) {
        if((this.status != TaskStatus.DONE)){
            if (blocked) 
            {
                this.status = TaskStatus.BLOCKED;
            } else {
                this.status = TaskStatus.TO_DO;
            }
        } else {
            totalValidationErrors++;
            throw new NexusValidationException("Tarefa em status \"DONE\" não pode ser bloqueada.");
        }
    }

    /** Método para definir o dono {@link User} da tarefa
     * 
     * @param u User - usuário a ser colocado como dono da tarefa
     */
    public void setOwner(User u){
        this.owner = u;
    }

    // Getters
    public int getId() { return id; }
    public TaskStatus getStatus() { return status; }
    public String getTitle() { return title; }
    public LocalDate getDeadline() { return deadline; }
    public User getOwner() { return owner; }
    public Project getProject() {return projeto;}
}