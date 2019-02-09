package com.dev.ppmtool.services;

import com.dev.ppmtool.domain.Project;
import com.dev.ppmtool.exceptions.CustomResponseEntityExceptionHandler;
import com.dev.ppmtool.exceptions.ProjectIdException;
import com.dev.ppmtool.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() +"' already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project ID '" + projectId + "' does not exists");
        }
        return project;
    }

    public  Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdenifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null) {
            throw new ProjectIdException("Cannot delete project with ID '" +projectId+ "'. This project does not exists");
        }

        projectRepository.delete(project);
    }

}
