package com.example.demo.RepositLayer;

import com.example.demo.EntityLayer.Entitymodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DAOLayer extends JpaRepository<Entitymodel, Long> {
}
