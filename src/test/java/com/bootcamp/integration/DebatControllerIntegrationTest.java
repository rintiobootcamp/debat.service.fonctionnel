package com.bootcamp.integration;

import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.entities.*;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

/**
 * <h2> The integration test for Debat controller</h2>
 * <p>
 * In this test class,
 * the methods :
 * <ul>
 * <li>create a debat </li>
 * <li>get one debat by it's id</li>
 * <li>get all debat</li>
 * <li>And update a debat have been implemented</li>
 * </ul>
 * before  getting started , make sure , the categorie fonctionnel service is deploy and running as well.
 * you can also test it will the online ruuning service
 * As this test interact directly with the local database, make sure that the specific database has been created
 * and all it's tables.
 * If you have data in the table,make sure that before creating a data with it's id, do not use
 * an existing id.
 * </p>
 */

public class DebatControllerIntegrationTest {
    private static Logger logger = LogManager.getLogger(DebatControllerIntegrationTest.class);

    /**
     *The Base URI of categorie fonctionnal service,
     * it can be change with the online URIof this service.
     */
    private String BASE_URI = "http://localhost:8088/debat";

    /**
     * The path of the Debat controller, according to this controller implementation
     */
    private String DEBAT_PATH ="/debats";

    /**
     * This ID is initialize for create , getById, and update method,
     * you have to change it if you have a save data on this ID otherwise
     * a error or conflit will be note by your test.
     */
    private int debatId = 0;

    /**
     * The startDate initialize for statistic method, you have
     * make sure that this is correct in one of the value in database
     */
    private long startDate = 1511907379;

    /**
     * The endDate initialize for statistic method, you have
     * make sure that this is correct in one of the value in database
     */
    private long endDate = 1511907390;

    /**
     * A entity of type:
     * <ul>
     *     <li>
     *         PROJET
     *     </li>
     *     <li>
     *         SECTEUR
     *     </li>
     *     <li>
     *         PILIER
     *     </li>
     *     <li>
     *         AXE
     *     </li>
     *     <li>
     *         PROJET
     *     </li>
     * </ul>
     *
     */
    private String entityType = "PROJET";

    /**
     *The given entity type ID
     * you have to specify this ID according to record in your data
     */
    private int entityId = 2;


    /**
     * This method create a new debat with the given id
     * @see Debat#id
     * <b>you have to chenge the name of
     * the debat if this name already exists in the database
     * @see Debat#getSujet()
     * else, the debat  will be created but not wiht the given ID.
     * and this will accure an error in the getById and update method</b>
     * Note that this method will be the first to execute
     * If every done , it will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 0, groups = {"DebatTest"})
    public void createDebatTest() throws Exception{
        String createURI = BASE_URI+DEBAT_PATH;
        Debat debat = getDebatById( 1 );
        debat.setId( debatId );
        debat.setSujet( "debat change in" );
        Gson gson = new Gson();
        String debatData = gson.toJson( debat );
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(debatData)
                .expect()
                .when()
                .post(createURI);

        debatId = gson.fromJson( response.getBody().print(),Debat.class ).getId();

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200) ;



    }

    /**
     * This method get a debat with the given id
     * @see Debat#id
     * <b>
     *     If the given ID doesn't exist it will log an error
     * </b>
     * Note that this method will be the second to execute
     * If every done , it will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 1, groups = {"DebatTest"})
    public void getDebatByIdTest() throws Exception{
        String getDebatById = BASE_URI+DEBAT_PATH+"/"+debatId;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getDebatById);
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200) ;
    }

    /**
     * Update a debat with the given ID
     * <b>
     *     the debat must exist in the database
     *     </b>
     * Note that this method will be the third to execute
     * If every done , it will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 2, groups = {"DebatTest"})
    public void updateDebatTest() throws Exception{
        String updateURI = BASE_URI+DEBAT_PATH;
        Debat debat = getDebatById( 1 );
        debat.setId( debatId );
        debat.setSujet( "update debat during test" );
        Gson gson = new Gson();
        String debatData = gson.toJson( debat );
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(debatData)
                .expect()
                .when()
                .put(updateURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200) ;

    }

    /**
     * Get the statistics of the given entity type
     * <b>
     * the comments must exist in the database
     * </b>
     * Note that this method will be the third to execute If every done , it
     * will return a 200 httpStatus code
     *
     * @throws Exception
     */
    @Test(priority = 3, groups = {"CommentaireTest"})
    public void statsCommentaire() throws Exception {
        String statsURI = BASE_URI + DEBAT_PATH +"/stats/"+entityType;
        Response response = given()
                .queryParam( "startDate",startDate)
                .queryParam( "endDate",endDate )
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(statsURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }


    /**
     * Get All the debats in the database
     * If every done , it will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 4, groups = {"DebatTest"})
    public void getAllDebatsTest()throws Exception{
        String getAllDebatURI = BASE_URI+DEBAT_PATH;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getAllDebatURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200) ;

    }

    /**
     * Get All the debats in the database
     * If every done , it will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 5, groups = {"DebatTest"})
    public void getDebatsByEntityTest()throws Exception{
        String getDebatByEntityURI = BASE_URI+DEBAT_PATH+"/"+entityType+"/"+entityId;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getDebatByEntityURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200) ;

    }


    /**
     * Delete a secteur for the given ID
     * will return a 200 httpStatus code if OK
     * @throws Exception
     */
    @Test(priority = 5, groups = {"SecteurTest"})
    public void deleteSecteurTest() throws Exception{

        String deleteSecteurUI = BASE_URI+DEBAT_PATH+"/"+debatId;

        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .delete(deleteSecteurUI);

        Assert.assertEquals(response.statusCode(), 200) ;


    }

    /**
     * Convert a relative path file into a File Object type
     * @param relativePath
     * @return  File
     * @throws Exception
     */
    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    /**
     * Convert a secteurs json data to a secteur objet list
     * this json file is in resources
     * @return a list of secteur in this json file
     * @throws Exception
     */
    public List<Secteur> loadDataSecteurFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile( "data-json" + File.separator + "secteurs.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Secteur>>() {
        }.getType();
        List<Secteur> secteurs = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return secteurs;
    }

    /**
     * Get on debat by a given ID from the List of axes
     * @param id
     * @return
     * @throws Exception
     */
    public Debat getDebatById(int id) throws Exception {
        List<Debat> debats = loadDataDebatFromJsonFile();
        Debat debat = debats.stream().filter(item -> item.getId() == id).findFirst().get();

        return debat;
    }



    public List<Debat> loadDataDebatFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "debats.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Debat>>() {
        }.getType();
        List<Debat> debats = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return debats;
    }

}
