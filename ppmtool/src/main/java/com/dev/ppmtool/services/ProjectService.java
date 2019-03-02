package com.dev.ppmtool.services;

import com.dev.ppmtool.domain.Backlog;
import com.dev.ppmtool.domain.Project;
import com.dev.ppmtool.domain.ProjectTask;
import com.dev.ppmtool.domain.User;
import com.dev.ppmtool.exceptions.CustomResponseEntityExceptionHandler;
import com.dev.ppmtool.exceptions.ProjectIdException;
import com.dev.ppmtool.exceptions.ProjectNotFoundException;
import com.dev.ppmtool.repositories.BacklogRepository;
import com.dev.ppmtool.repositories.ProjectRepository;
import com.dev.ppmtool.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;


    public Project saveOrUpdateProject(Project project, String username){

        if (project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if (existingProject == null) {
                throw new ProjectNotFoundException("Project not ID '" + project.getProjectIdentifier() + "' cannot be updated because it does not exists");
            }
            else if(existingProject!=null && (!project.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account");
            }
        }

        try{

            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            String projectIdentifier = project.getProjectIdentifier().toUpperCase();
            project.setProjectIdentifier(projectIdentifier);
            if (project.getId() ==null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            } else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            }

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() +"' already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId, String username) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project ID '" + projectId + "' does not exists");
        }
        if (!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public  Iterable<Project> findAllProjects(String username) {

        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdenifier(String projectId, String username) {
        Project project = findProjectByIdentifier(projectId, username);

        projectRepository.delete(project);
    }

}
