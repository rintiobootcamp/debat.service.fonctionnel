package com.bootcamp.controllers;

import com.bootcamp.commons.exceptions.DatabaseException;
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
import java.util.List;

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

    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Create a debat", notes = "Create a debat")
    public ResponseEntity<Debat> create(@RequestBody  Debat debat) throws SQLException {
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

    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read All Debats", notes = "Read aall the Debats")
    public ResponseEntity<List<Debat>> read() throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        List<Debat> debats = debatService.read(request);
        return new ResponseEntity<List<Debat>>(debats, HttpStatus.OK);
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get one Debats", notes = "Read a particular Debats")
    public ResponseEntity<Debat> getById(@PathVariable int id) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Debat debat = debatService.read(id);
        return new ResponseEntity<Debat>(debat, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Delet one Debats", notes = "delete a particular Debats")
    public ResponseEntity<Boolean> delete(@PathVariable int id) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        boolean done =debatService.delete(id);
        return new ResponseEntity<>(done, HttpStatus.OK);
    }

}
