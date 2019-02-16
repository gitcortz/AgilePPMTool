package com.dev.ppmtool.services;

import com.dev.ppmtool.domain.Backlog;
import com.dev.ppmtool.domain.Project;
import com.dev.ppmtool.domain.ProjectTask;
import com.dev.ppmtool.exceptions.ProjectNotFoundException;
import com.dev.ppmtool.repositories.BacklogRepository;
import com.dev.ppmtool.repositories.ProjectRepository;
import com.dev.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){


        //exception : not found
        //PTs to be added to a specific project, project not null, BL exists
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        /*if (backlog == null){
            throw new ProjectNotFoundException("Project does not exists");
        }*/

            //set backlog to pt
            projectTask.setBacklog(backlog);
            //we want our project sequence to be like : IDPRO-1 IDPRO-2
            Integer backLogSequence = backlog.getPTSequence();
            //update the bl sequence
            backLogSequence++;
            backlog.setPTSequence(backLogSequence);
            //add sequence to project task
            projectTask.setProjectSequence(projectIdentifier + "-" + backLogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //initial priority when priority is null
            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }
            //initial status when status is null
            if (projectTask.getStatus() == null || projectTask.getStatus().isEmpty()) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        }
        catch (Exception e){
            throw new ProjectNotFoundException("Project not found.");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);
        if (project == null)
            throw new ProjectNotFoundException("Project with ID '" + id + "' does not exist");
        return projectTaskRepository.findByProjectIdentifier(id);
    }
}
