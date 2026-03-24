package com.nexus.model;

import java.util.List;
import java.util.Objects;

/** Classe de Usuário
 * 
 * Esta classe pssui atributos que definem um usuário no Nexus.
 * Com opção para verificar quantas tarefas {@link Task} estão
 * atribuídas a este usuário, além de métodos para verificar
 * seus atributos.
 */
public class User {
    private final String username;
    private final String email;

    /** Construtor de Usuário
     * Inicializa um novo usuário
     * 
     * @param username String - nome do usuário
     * @param email String - email do usuário
     */
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

    /** Override do método equals para Usuário
     *
     * Considera-se o email como atributo que diferencia objetos. 
     * 
     * @return boolean - True se dois usuários tem a mesma posição
     * de memória ou tem os mesmos atributos e False caso contário.
     */
    @Override
    public boolean equals(Object u){
        if(this == u) return true;
        if (u == null || getClass () != u.getClass ()) return false ; 
        User user = (User) u;
        return this.email == user.email;
    }

    /** Override do método hashCode para Usuário
     * 
     * @return hash relacionado ao email do usuário
     */
    @Override
    public int hashCode(){
        return Objects.hash(email); /* Gera hashCode a partir do email */
    }

    /** Override do método de exibição de Usuário
     * 
     * @return nome do usuário 
     */
    @Override
    public String toString() {
        return this.consultUsername();
    }

    /** Método de verificação de email válido
     * 
     * @param email String - email a ser verificado
     * @return boolean - True caso o email é válido e False caso contrário
     */
    public static boolean isValidEmailAddress(String email) {
           String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
           java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
           java.util.regex.Matcher m = p.matcher(email);
           return m.matches();
    }

    /** Método que retorna email de Usuário
     * 
     * @return String - email do usuário
     */
    public String consultEmail() {
        return email;
    }

    /** Método que retorna nome de Usuário
     * 
     * @return String - nome do usuário
     */
    public String consultUsername() {
        return username;
    }

    /** Método que calcula quantas tarefas com estado IN_PROGRESS
     * atribuídas ao Usuário
     * 
     * @param listaDeTarefas lista das tarefas {@link Task} criadas
     * @return quantidade de tarefas atribuídas ao usuário
     */
    public long calculateWorkload(List<Task> listaDeTarefas) {
        long qntdTarefas = 0;

        for(Task task : listaDeTarefas){
            if ((task.getStatus() == TaskStatus.IN_PROGRESS) && (this.equals(task.getOwner())))
            {
                qntdTarefas++;
            }
        }

        return qntdTarefas; 
    }
}