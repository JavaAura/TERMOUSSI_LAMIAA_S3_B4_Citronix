package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.TreeDTO;
import com.citronix.citronix.services.impl.TreeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping
    public ResponseEntity<Page<TreeDTO>> getAllTrees(Pageable pageable){
     Page<TreeDTO> treesPage=   treeServiceImpl.getAllTrees(pageable);
     return ResponseEntity.ok(treesPage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TreeDTO> getTreeById(@PathVariable Long id){
        TreeDTO treeDTO=treeServiceImpl.getTreeById(id);
        return ResponseEntity.ok(treeDTO);
    }
}
