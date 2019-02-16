package com.dev.ppmtool.repositories;

import com.dev.ppmtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
    List<ProjectTask> findByProjectIdentifier(String id);

    ProjectTask findByProjectSequence(String sequence);
}
