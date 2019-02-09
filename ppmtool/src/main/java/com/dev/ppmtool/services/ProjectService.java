package com.dev.ppmtool.services;

import com.dev.ppmtool.domain.Project;
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
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() +"'already exists");
        }

    }


}
