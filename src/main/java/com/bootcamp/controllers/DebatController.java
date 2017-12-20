package com.bootcamp.controllers;

import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.entities.Commentaire;
import com.bootcamp.entities.Debat;
import com.bootcamp.services.DebatService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bello
 */
@RestController("DebatContoller")
@RequestMapping("/debats")
@Api(value = "Debat API", description = "Debat API")
@CrossOrigin(origins = "*")
public class DebatController {
//

    @Autowired
    DebatService debatService;

    @Autowired
    HttpServletRequest request;

    /**
     * Create a new debate in the database
     *
     * @param debat
     * @return debate
     * @throws SQLException
     */
    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Create a debat", notes = "Create a debat")
    public ResponseEntity<Debat> create(@RequestBody Debat debat) throws SQLException {
        debat = debatService.create(debat);
        return new ResponseEntity<>(debat, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Update a debat", notes = "Update a debat")
    public ResponseEntity<Boolean> update(@RequestBody  Debat debat) throws SQLException {
        boolean done = debatService.update(debat);
        return new ResponseEntity<>(done, HttpStatus.OK);
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
    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read All Debats", notes = "Read all the Debats")
    public ResponseEntity<List<Debat>> read() throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        List<Debat> debats = debatService.read(request);
        return new ResponseEntity<List<Debat>>(debats, HttpStatus.OK);
    }



    /**
     * Get a debate knowing its id
     *
     * @param id
     * @return debate
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws DatabaseException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get one Debats", notes = "Read a particular Debats")
    public ResponseEntity<Debat> getById(@PathVariable int id) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Debat debat = debatService.read(id);
        return new ResponseEntity<Debat>(debat, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Delete one Debats", notes = "delete a particular Debats")
    public ResponseEntity<Boolean> delete(@PathVariable int id) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        boolean done =debatService.delete(id);
        return new ResponseEntity<>(done, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{entityType}/{entityId}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a debat", notes = "Read a debats relative to a given entityType")
    public ResponseEntity<List<Debat>> readByEntity(@PathVariable("entityType") String entityType, @PathVariable("entityId") int entityId)  throws Exception {
        EntityType entite = EntityType.valueOf( entityType.toUpperCase() );
        List<Debat> debats = debatService.getByEntity( entite, entityId );
        return new ResponseEntity<List<Debat>>( debats, HttpStatus.OK );
    }



        @RequestMapping(method = RequestMethod.GET, value = "/stats/{entityType}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read all debat on entity", notes = "Read all debat on entity")
    public ResponseEntity<List<Debat>> readAllDebatByEntity(@PathVariable("entityType") String entityType, @RequestParam("startDate") long startDate, @RequestParam("endDate") long endDate ) {
       
        EntityType entite = EntityType.valueOf(entityType);
        List<Debat> debats = new ArrayList<>();
        HttpStatus httpStatus = null;

        try {
            
            if(startDate==0 && endDate == 0)
            debats = debatService.getAllDebatByEntity(entite);
            else if(startDate!=0 && endDate != 0)
            debats = debatService.getAllDebatByEntity(entite,startDate,endDate);           
            
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(DebatController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(debats, httpStatus.OK);

    }

}
