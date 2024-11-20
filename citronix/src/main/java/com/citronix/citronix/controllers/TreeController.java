package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.TreeDTO;
import com.citronix.citronix.services.impl.TreeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/trees")
@Validated
public class TreeController {
    private final TreeServiceImpl treeServiceImpl;

    @Autowired
    public TreeController(TreeServiceImpl treeServiceImpl){
        this.treeServiceImpl=treeServiceImpl;
    }
    @PostMapping
    public ResponseEntity<TreeDTO> saveTree(@RequestBody @Valid TreeDTO treeDTO){
       TreeDTO savedTree= treeServiceImpl.saveTree(treeDTO);
       return ResponseEntity.ok(savedTree);
    }
}
