package com.example.demo.ServiceLayer;

import com.example.demo.EntityLayer.Entitymodel;

import java.util.List;
import java.util.Optional;

public interface ServInterface {


    List<Entitymodel> getAllEntity();

    List<Entitymodel> postEntities(List<Entitymodel> ent);

    Entitymodel updated(Entitymodel ent, Long id);

    String deleteById(Long id);

    Optional<Entitymodel> getEntityById(Long id);
}
