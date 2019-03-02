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

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

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

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

        projectService.findProjectByIdentifier(id, username);
        return projectTaskRepository.findByProjectIdentifier(id);
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String ptId, String username) {

        projectService.findProjectByIdentifier(backlogId, username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptId);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Project Task with ID '" + ptId + "' does not exist");
        }

        //make sure we are searching on the right backlog
        if (!projectTask.getProjectIdentifier().equals(backlogId)) {
            throw new ProjectNotFoundException("Project Task '" + ptId + "' does not exist in project '" + backlogId + "'");
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String ptId, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlogId, ptId, username);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public  void deletePTByProjectSequence(String backlogID, String ptId, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlogID, ptId, username);

        projectTaskRepository.delete(projectTask);
    }
}
