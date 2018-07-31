package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.crud.DebatCRUD;
import com.bootcamp.entities.Censure;
import com.bootcamp.entities.Debat;
import com.rintio.elastic.client.ElasticClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Bello
 */
@Component
public class DebatService implements DatabaseConstants {
ElasticClient elasticClient;
private List<Debat> debats;
    @PostConstruct
public void DebatService(){
        this.debats = new ArrayList<>();
    elasticClient = new ElasticClient();
}

public List<Debat> lire() throws Exception{
       if(this.debats.isEmpty())
           getAllDebat();
       return  this.debats;
}

    public boolean createAllIndexDebat()throws Exception{
//        ElasticClient elasticClient = new ElasticClient();
        List<Debat> debats = DebatCRUD.read();
        for (Debat debat : debats){
            elasticClient.creerIndexObjectNative("debats","debat",debat,debat.getId());
//            LOG.info("debat "+debat.getId()+" created");
        }
        return true;
    }
    /**
     * Insert the given debate entity in the database
     *
     * @param debat
     * @return debate
     * @throws SQLException
     */
    public Debat create(Debat debat) throws Exception {
        debat.setDateCreation(System.currentTimeMillis());
        DebatCRUD.create(debat);
        createAllIndexDebat();
        return debat;
    }

    /**
     * Update the given debate entity in the database
     *
     * @param debat
     * @throws SQLException
     */
    public boolean update(Debat debat) throws Exception {
        if(DebatCRUD.update(debat))
            createAllIndexDebat();
        return true;
    }

    /**
     * Delete the given debate entity in the database
     *
     * @param id
     * @return debate
     * @throws SQLException
     */
    public boolean delete(int id) throws Exception {
        Debat debat = read(id);
        if(DebatCRUD.delete(debat))
            createAllIndexDebat();
        return  true;

    }

    /**
     * Get a debate by its id
     *
     * @param id
     * @return debate
     * @throws SQLException
     */
    public Debat read(int id) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria("id", "=", id));
//        List<Debat> debats = DebatCRUD.read(criterias);
        return lire().stream().filter(t->t.getId()==id).findFirst().get();
    }

    /**
     * Get all the debates in the database matching the given request
     *
     * @param request
     * @return debates list
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws DatabaseException
     * @throws InvocationTargetException
     */
    public List<Debat> read(HttpServletRequest request) throws SQLException, Exception, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Debat> debats = null;
        if (criterias == null && fields == null) {
            debats = lire();
        } else if (criterias != null && fields == null) {
            debats = DebatCRUD.read(criterias);
        } else if (criterias == null && fields != null) {
            debats = DebatCRUD.read(fields);
        } else {
            debats = DebatCRUD.read(criterias, fields);
        }

        return debats;
    }


    public List<Debat> getAllDebat() throws Exception{
//        ElasticClient elasticClient = new ElasticClient();
        List<Object> objects = elasticClient.getAllObject("debats");
        ModelMapper modelMapper = new ModelMapper();
        List<Debat> rest = new ArrayList<>();
        for(Object obj:objects){
            rest.add(modelMapper.map(obj,Debat.class));
        }
        this.debats = rest;
        return rest;
    }

    public List<Debat> getByEntity(EntityType entityType, int entityId) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));
//        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), null));
//        return DebatCRUD.read(criterias);
        List<Debat> rest =  lire().stream().filter(t->t.getEntityType().equalsIgnoreCase(entityType.toString()) ).collect(Collectors.toList());
        return rest.stream().filter(t->t.getEntityId()==entityId).collect(Collectors.toList());
    }

    public Debat getBySujet(String sujet) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria("sujet", "=", sujet));
//        List<Debat> debats = DebatCRUD.read(criterias);
        return lire().stream().filter(t->t.getSujet().equalsIgnoreCase(sujet)).findFirst().get();
    }

    public boolean exist(Debat debat) throws Exception {
        if (getBySujet(debat.getSujet()) != null) {
            return true;
        }
        return false;
    }

    public boolean exist(int id) throws Exception {
        if (read(id) != null) {
            return true;
        }
        return false;
    }

    /**
     * Get all the debates of the database
     *
     * @return debates list
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws DatabaseException
     * @throws InvocationTargetException
     */
    public List<Debat> getAll() throws Exception, IllegalAccessException, DatabaseException, InvocationTargetException {
        return lire();
    }

    /**
     * Insert the given debate entity in the database
     *
     * @param entityType
     * @return List debate
     * @throws SQLException
     */
    public List<Debat> getAllDebatByEntity(EntityType entityType) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
//        return DebatCRUD.read(criterias);
        return lire().stream().filter(t->t.getEntityType().equalsIgnoreCase(entityType.toString())).collect(Collectors.toList());
    }

    /**
     * get all debate by entity
     *
     * @param entityType
     * @param startDate
     * @param endDate
     * @return List debate
     * @throws SQLException
     * @throws java.text.ParseException
     */
    public List<Debat> getAllDebatByEntity(EntityType entityType, String startDate, String endDate) throws SQLException, ParseException {
        EntityManager em = Persistence.createEntityManagerFactory(DatabaseConstants.PERSISTENCE_UNIT).createEntityManager();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        long dateDebut = formatter.parse(startDate).getTime();
        long dateFin = formatter.parse(endDate).getTime();
        TypedQuery<Debat> query = em.createQuery(
                "SELECT e FROM Debat e WHERE e.entityType =?1 AND e.dateCreation BETWEEN ?2 AND ?3", Debat.class);
        List<Debat> debats = query.setParameter(1, entityType.name())
                                  .setParameter(2, dateDebut)
                                  .setParameter(3, dateFin)
                                  .getResultList();
        return debats;
    }

}
