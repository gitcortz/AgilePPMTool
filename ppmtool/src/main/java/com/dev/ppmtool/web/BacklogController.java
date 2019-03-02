package com.dev.ppmtool.web;

import com.dev.ppmtool.domain.ProjectTask;
import com.dev.ppmtool.services.MapValidationErrorService;
import com.dev.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.BindException;
import java.security.Principal;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result, @PathVariable String backlogId,
                                            Principal principal) {

        ResponseEntity<?> errorMap  = mapValidationErrorService.MapValidationService(result);
        if (errorMap!=null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask, principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.CREATED);

    }

    @GetMapping("/{backlogId}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlogId, Principal principal){
        return projectTaskService.findBacklogById(backlogId, principal.getName());
    }

    @GetMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlogId, @PathVariable String ptId) {
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlogId, ptId);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String backlogId, @PathVariable String ptId) {
        ResponseEntity<?> errorMap  = mapValidationErrorService.MapValidationService(result);
        if (errorMap!=null) return errorMap;

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask, backlogId, ptId);
        return new ResponseEntity<ProjectTask>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlogId}/{ptId}")
    public  ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String ptId) {
        projectTaskService.deletePTByProjectSequence(backlogId, ptId);
        return new ResponseEntity<String>("Project task '" + ptId + "' deleted successfully", HttpStatus.OK);
    }
}
