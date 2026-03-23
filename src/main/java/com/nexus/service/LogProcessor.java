package com.nexus.service;

import com.nexus.model.*;
import com.nexus.exception.NexusValidationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class LogProcessor {

    public void processLog(String fileName, Workspace workspace, List<User> users) {
        
        try {
            // Busca o arquivo dentro da pasta de recursos do projeto (target/classes)
            var resource = getClass().getClassLoader().getResourceAsStream(fileName);
            
            if (resource == null) {
                throw new IOException("Arquivo não encontrado no classpath: " + fileName);
            }

            try (java.util.Scanner s = new java.util.Scanner(resource).useDelimiter("\\A")) {
                String content = s.hasNext() ? s.next() : "";
                List<String> lines = List.of(content.split("\\R"));
                
                for (String line : lines){
                    if (line.isBlank() || line.startsWith("#")) continue;

                    String[] p = line.split(";");
                    String action = p[0];

                    try {
                        switch (action) {
                            case "CREATE_USER" -> {
                                if(!User.isValidEmailAddress(p[2]) || p[2] == null || p[2].isBlank()){
                                    throw new IllegalArgumentException("Insira um email válido."); 
                                }
                                else{
                                    User temp = new User(p[1], p[2]);
                                    workspace.addUser(temp);
                                    users.add(temp);
                                    System.out.println("[LOG] Usuário criado: " + p[1]);
                                }
                            }

                            case "CREATE_PROJECT" -> {
                                Project proj = new Project(p[1],Integer.parseInt(p[2]));
                                workspace.addProj(proj);
                                System.out.println("[LOG] Projeto criado: " + p[1]);
                            }

                            case "CREATE_TASK" -> {
                                Task t = new Task(p[1], LocalDate.parse(p[2]), Integer.parseInt(p[3]), workspace.buscarProj(p[4]));
                                workspace.addTask(t);
                                Project projeto = workspace.buscarProj(p[4]);
                                projeto.addTask(t);
                                System.out.println("[LOG] Tarefa criada: " + p[1]);
                            }

                            case "ASSIGN_USER" -> {
                                Task _task = null;
                                User user = null;
                                for (Task  t: workspace.getTasks()) {
                                    if(t.getId() == Integer.parseInt(p[1])){
                                        _task = t;
                                    }
                                }

                                for(User u: workspace.getUsers()){
                                    if(u.consultUsername().equals(p[2])){
                                        user = u;
                                    }
                                }

                                if(_task.getId() != Integer.parseInt(p[1])){
                                    Task.totalValidationErrors++;
                                    throw new NexusValidationException("[WARN] Tarefa não existente!");
                                }
                                

                                else {
                                    _task.setOwner(user);
                                    System.out.println("[LOG] Tarefa " + _task.getTitle() + " atribuída para " + p[2]);
                                }
                            }

                            case "CHANGE_STATUS" -> {
                                
                                Task _task = null;
                                for (Task  t: workspace.getTasks()) {
                                    if(t.getId() == Integer.parseInt(p[1])){
                                        _task = t;
                                    }
                                }

                                if(_task.getId() != Integer.parseInt(p[1])){
                                    Task.totalValidationErrors++;
                                    throw new NexusValidationException("[WARN] Tarefa não existente!");
                                }

                                else{
                                    switch (p[2]) {
                                        case "IN_PROGRESS":
                                       
                                            _task.moveToInProgress(_task.getOwner());
                                            break;
                                        
                                        case "DONE":
                                            _task.markAsDone();
                                            break;

                                        case "BLOKED":
                                            _task.setBlocked(_task.getStatus().equals(TaskStatus.BLOCKED));
                                            break;
                                        
                                        default:
                                            throw new IllegalArgumentException("[WARN] Status de Tarefa não existente.");
                                    }
                                    System.out.println("[LOG] Status da tarefa foi atualizado.");
                                }
                            }

                            case "REPORT_STATUS" ->{
                                workspace.TopPerformers();
                                workspace.ProjectHealth(workspace.buscarProj(p[1]));
                                workspace.OverloadUsers();
                                workspace.GlobalBottlenecks();
                            }

                            default -> System.err.println("[WARN] Ação desconhecida: " + action);
                        }
                    } catch (NexusValidationException e) {
                        System.err.println("[ERRO DE REGRAS] Falha no comando '" + line + "': " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[ERRO FATAL] " + e.getMessage());
        }
    }
}