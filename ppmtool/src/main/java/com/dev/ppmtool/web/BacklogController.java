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

@Controller
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result, @PathVariable String backlogId) {
        ResponseEntity<?> errorMap  = mapValidationErrorService.MapValidationService(result);
        if (errorMap!=null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask);

        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.CREATED);

    }
}
