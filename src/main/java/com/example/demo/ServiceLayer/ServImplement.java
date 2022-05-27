package com.example.demo.ServiceLayer;

import com.example.demo.BusinessLogicLayer.NER;
import com.example.demo.BusinessLogicLayer.ProcessLogic;
import com.example.demo.EntityLayer.Entitymodel;
import com.example.demo.RepositLayer.DAOLayer;
import edu.stanford.nlp.ling.tokensregex.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ServImplement implements ServInterface {

    private static final Logger log = LoggerFactory.getLogger(ServImplement.class);

    @Autowired
    private DAOLayer repositry;
    @Autowired
    private NER nerdemo;
    @Autowired
    private ProcessLogic pl;

    @Override
    public List<Entitymodel> postEntities(List<Entitymodel> entities) {
        List<Entitymodel> finalrepo = null;
        try {
            finalrepo = pl.process(entities);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (ParseException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return repositry.saveAll(finalrepo);

    }

    @Override
    public String deleteById(Long id) {
        repositry.deleteById(id);
        log.info("Record deleted for id --" + id);

        return "Deleted Successfully ...";
    }

    @Override
    public Optional<Entitymodel> getEntityById(Long id) {
        return repositry.findById(id);
    }

    @Override
    public Entitymodel updated(Entitymodel enty, Long id) {
        Entitymodel bufferModel = repositry.findById(id).get();
        if (Optional.ofNullable(bufferModel.getId()).isPresent()) {
            if (Objects.nonNull(enty.getDomainUrl()) && !enty.getDomainUrl().isEmpty()) {
                bufferModel.setDomainUrl(enty.getDomainUrl());
            }
            if (Objects.nonNull(enty.getLandingUrl()) && !enty.getLandingUrl().isEmpty()) {
                bufferModel.setLandingUrl(enty.getLandingUrl());
            }
                bufferModel.setPersonName(enty.getPersonName());
            repositry.save(bufferModel);

        }
        return bufferModel;
    }


    @Override
    public List<Entitymodel> getAllEntity() {
        return repositry.findAll();
    }


}
