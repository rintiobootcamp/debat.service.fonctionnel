package com.bootcamp.controllers;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.entities.*;
import com.bootcamp.services.DebatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
import org.junit.runner.RunWith;
*/
/*
 *
 * Created by darextossa on 12/5/17.
 */

//@TestExecutionListeners(MockitoTestExecutionListener.class)
//@ActiveProfiles({"controller-unit-test"})
//@SpringBootApplication(scanBasePackages={"com.bootcamp"})
//@ContextConfiguration(classes = ControllerUnitTestConfig.class)
//@WebMvcTest(value = PilierController.class, secure = false, excludeAutoConfiguration = {HealthIndicatorAutoConfiguration.class, HibernateJpaAutoConfiguration.class, FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class})

@RunWith(SpringRunner.class)
@WebMvcTest(value = DebatController.class, secure = false)
@ContextConfiguration(classes={Application.class})
public class DebatControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DebatService debatService;


    @Test
    public void getAllDebat() throws Exception{
        List<Debat> debats =  loadDataDebatFromJsonFile();
        //Mockito.mock(DebatCRUD.class);
        when(debatService.getAll()).thenReturn(debats);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/debats")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());
        System.out.println("*********************************Test for get all pilar  in secteur controller done *******************");


    }

    @Test
    public void getDebatById() throws Exception{
        int id = 1;
        Debat debat = getDebatById(id);
        when(debatService.read(id)).thenReturn(debat);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/debats/{id}",id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        System.out.println(response.getContentAsString());
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for get pilar by id in pilar controller done *******************");

    }


    @Test
    public void createDebat() throws Exception{
        List<Debat> debats =  loadDataDebatFromJsonFile();
        Debat debat = debats.get(0);

        when(debatService.exist(debat)).thenReturn(false);
                when(debatService.create(debat)).thenReturn(debat);

        RequestBuilder requestBuilder =
                post("/debats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(debat));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        System.out.println(response.getContentAsString());

        System.out.println("*********************************Test for create debat in debat controller done *******************");
    }


    @Test
    public void updateDebat() throws Exception{
        List<Debat> debats =  loadDataDebatFromJsonFile();
        Debat debat = debats.get(0);
        debat.setSujet("debat update");
        when(debatService.exist(debat.getId())).thenReturn(true);
        when(debatService.update(debat)).thenReturn(true);

        RequestBuilder requestBuilder =
                put("/debats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(debat));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for update debat in debat controller done *******************");


    }

    @Test
    public void deleteDebat() throws Exception{
        int id = 5;
        when(debatService.exist(id)).thenReturn(true);
              when(debatService.delete(id)).thenReturn(true);

        RequestBuilder requestBuilder =
                delete("/debats/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for delete debat in debat controller done *******************");


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

    private Debat getDebatById(int id) throws Exception {
        List<Debat> debats = loadDataDebatFromJsonFile();
        Debat debat = debats.stream().filter(item -> item.getId() == id).findFirst().get();

        return debat;
    }



    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if(!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    private static String objectToJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  List<Projet> getProjectsFromJson() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "projets.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Projet>>() {
        }.getType();
        List<Projet> projets = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return projets;
    }

    public List<Secteur> loadDataSecteurFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "secteurs.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Secteur>>() {
        }.getType();
        List<Secteur> secteurs = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return secteurs;
    }

    private Secteur getSecteurById(int id) throws Exception {
        List<Secteur> secteurs = loadDataSecteurFromJsonFile();
        Secteur secteur = secteurs.stream().filter(item->item.getId()==id).findFirst().get();

        return secteur;
    }

    public Axe getAxeById(int id) throws Exception {
        List<Axe> axes = loadDataAxeFromJsonFile();
        Axe axe = axes.stream().filter(item->item.getId()==id).findFirst().get();

        return axe;
    }

    public Pilier getPilierDataById(int id) throws Exception {
        List<Pilier> piliers = loadDataPilierFromJsonFile();
        Pilier pilier = piliers.stream().filter(item->item.getId()==id).findFirst().get();

        return pilier;
    }


    public List<Axe> loadDataAxeFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "axes.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Axe>>() {
        }.getType();
        List<Axe> axes = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        for (int i = 0; i < axes.size(); i++) {
            Axe axe = axes.get(i);
            List<Secteur> secteurs = new LinkedList();
            switch (i) {
                case 0:
                    secteurs.add(getSecteurById(8));
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    secteurs.add(getSecteurById(1));
                    secteurs.add(getSecteurById(2));
                    secteurs.add(getSecteurById(5));
                    secteurs.add(getSecteurById(9));
                    break;
                case 4:
                    secteurs.add(getSecteurById(3));
                    break;
                case 5:
                    secteurs.add(getSecteurById(8));
                    break;
                case 6:
                    secteurs.add(getSecteurById(6));
                    break;
            }
            axe.setSecteurs(secteurs);
        }

        return axes;
    }

    public List<Pilier> loadDataPilierFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "piliers.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Pilier>>() {
        }.getType();
        List<Pilier> piliers = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);
        //List<Axe> axes = axeRepository.findAll();
        for (int i = 0; i < piliers.size(); i++) {
            List<Axe> axes = new LinkedList();
            Pilier pilier = piliers.get(i);
            switch (i) {
                case 0:
                    axes.add(getAxeById(1));
                    axes.add(getAxeById(2));
                    break;
                case 1:
                    axes.add(getAxeById(3));
                    axes.add(getAxeById(4));
                    axes.add(getAxeById(5));
                    break;
                case 2:
                    axes.add(getAxeById(6));
                    axes.add(getAxeById(7));
                    break;
            }
            pilier.setAxes(axes);
        }

        return piliers;
    }
}
