package com.example.demo.ControlLayer;

import com.example.demo.EntityLayer.Entitymodel;
import com.example.demo.ServiceLayer.ServInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class AppController {
   private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @Autowired
    public ServInterface serv;

    @GetMapping("/")
    public String welcome() {
        log.debug("Hi.....");
        log.info("Application Initiated Successfully ... ");
        return "Welcome!!!";
    }

    @GetMapping("/viewbyid/{id}")
    public Optional<Entitymodel> getEnt(@PathVariable Long id) {
        log.info("Viewing Record by Id ... ");
        return serv.getEntityById(id);
    }

    @GetMapping("/viewAll")
    public List<Entitymodel> getAllEntity() {
        log.info("View All Records ... ");
        return serv.getAllEntity();
    }

    @PostMapping("/addEntities")
    public List<Entitymodel> postEntities(@RequestBody List<Entitymodel> ent) {
        log.info("Adding Records ... ");
        return serv.postEntities(ent);

    }

    @PutMapping("/update/{id}")
    public Entitymodel updateRecord(@RequestBody Entitymodel ent, @PathVariable Long id) {

        System.out.println("");
        log.info("updated Successfully ... " + id + " ... ");
        return serv.updated(ent, id);
    }

    @DeleteMapping("/delete/{id}")
    public String del(@PathVariable Long id) {

        serv.deleteById(id);
        log.info("deleted Successfully for id ... " + id);
        return "deleted Successfully for id ... " + id;
    }

}
