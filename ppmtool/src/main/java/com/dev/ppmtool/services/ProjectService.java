package com.dev.ppmtool.services;

import com.dev.ppmtool.domain.Project;
import com.dev.ppmtool.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        //Logic

        return projectRepository.save(project);
    }


}
