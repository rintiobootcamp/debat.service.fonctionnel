package com.bootcamp.services;


import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.crud.DebatCRUD;
import com.bootcamp.crud.PilierCRUD;
import com.bootcamp.entities.Debat;
import com.bootcamp.entities.Pilier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@Component
public class DebatService implements DatabaseConstants {

    DebatCRUD debatCRUD;


    @PostConstruct
    public void init(){
        debatCRUD = new DebatCRUD();
    }

    public Debat create(Debat debat) throws SQLException {
        debat.setDateMiseAJour(System.currentTimeMillis());
        debatCRUD.create(debat);
        return debat;
    }

    public void update(Debat debat) throws SQLException {
        debatCRUD.update(debat);
    }

    public boolean delete(int id) throws SQLException {
        Debat debat = read(id);
        return  debatCRUD.delete(debat);
    }

    public Debat read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Debat> debats = debatCRUD.read(criterias);

        return debats.get(0);
    }



    public List<Debat> read(HttpServletRequest request) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Debat> debats = null;
        if(criterias == null && fields == null)
            debats =  debatCRUD.read();
        else if(criterias!= null && fields==null)
            debats = debatCRUD.read(criterias);
        else if(criterias== null && fields!=null)
            debats = debatCRUD.read(fields);
        else
            debats = debatCRUD.read(criterias, fields);

        return debats;
    }

    public List<Debat> getAll() throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        return DebatCRUD.read();
    }

}

