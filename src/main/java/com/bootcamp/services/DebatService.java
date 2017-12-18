package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.controllers.DebatController;
import com.bootcamp.crud.DebatCRUD;
import com.bootcamp.entities.Debat;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bello
 */
@Component
public class DebatService implements DatabaseConstants {

    /**
     * Insert the given debate entity in the database
     *
     * @param debat
     * @return debate
     * @throws SQLException
     */
    public Debat create(Debat debat) throws SQLException {
        debat.setDateMiseAJour(System.currentTimeMillis());
        DebatCRUD.create(debat);
        return debat;
    }

    /**
     * Update the given debate entity in the database
     *
     * @param debat
     * @throws SQLException
     */
    public void update(Debat debat) throws SQLException {
        DebatCRUD.update(debat);
    }


    /**
     * Delete the given debate entity in the database
     *
     * @param id
     * @return debate
     * @throws SQLException
     */
    public boolean delete(int id) throws SQLException {

        Debat debat = read(id);
        DebatCRUD.delete(debat);

        return true;

    }

    /**
     * Get a debate by its id
     *
     * @param id
     * @return debate
     * @throws SQLException
     */
    public Debat read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Debat> debats = DebatCRUD.read(criterias);

        return debats.get(0);
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
    public List<Debat> read(HttpServletRequest request) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Debat> debats = null;
        if (criterias == null && fields == null) {
            debats = DebatCRUD.read();
        } else if (criterias != null && fields == null) {
            debats = DebatCRUD.read(criterias);
        } else if (criterias == null && fields != null) {
            debats = DebatCRUD.read(fields);
        } else {
            debats = DebatCRUD.read(criterias, fields);
        }

        return debats;
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
    public List<Debat> getAll() throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        return DebatCRUD.read();
    }

    /**
     * Insert the given debate entity in the database
     *
     * @param entityType
     * @return List debate
     * @throws SQLException
     */
    public List<Debat> getAllDebatByEntity(EntityType entityType) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        return DebatCRUD.read(criterias);
    }


/**
     * get all debate by entity 
     *
     * @param entityType
     * @return List debate
     * @throws SQLException
     */

   public List<Debat> getAllDebatByEntity(EntityType entityType, long dateDebut, long dateFin) throws SQLException {
        Criterias criterias = new Criterias();

        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));

        criterias.addCriteria(new Criteria(new Rule("dateDebut", ">=", dateDebut),"AND"));
        criterias.addCriteria(new Criteria(new Rule("dateFin", "<=", dateFin),null));
       Logger.getLogger(DebatController.class.getName()).log(Level.SEVERE, null,"critères de sql"+criterias);

        return DebatCRUD.read(criterias);
    }



}


