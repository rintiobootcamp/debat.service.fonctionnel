package com.bootcamp.servcice;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.crud.DebatCRUD;
import com.bootcamp.entities.Debat;
import com.bootcamp.entities.Secteur;
import com.bootcamp.services.DebatService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by Ibrahim on 12/9/17.
 */

@RunWith(PowerMockRunner.class)
@WebMvcTest(value = DebatService.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(DebatCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class DebatServiceTest {


    @InjectMocks
    private DebatService debatService;

    @Test
    public void getAllDebat() throws Exception {
        List<Debat> debats = loadDataDebatFromJsonFile();
        PowerMockito.mockStatic(DebatCRUD.class);
        Mockito.
                when(DebatCRUD.read()).thenReturn(debats);
        List<Debat> resultDebats = debatService.getAll();
        Assert.assertEquals(debats.size(), resultDebats.size());

    }


    @Test
    public void create() throws Exception{
        List<Debat> debats = loadDataDebatFromJsonFile();
        Debat debat = debats.get(1);

        PowerMockito.mockStatic(DebatCRUD.class);
        Mockito.
                when(DebatCRUD.create(debat)).thenReturn(true);
    }

    @Test
    public void delete() throws Exception{
        List<Debat> debats = loadDataDebatFromJsonFile();
        Debat debat = debats.get(1);

        PowerMockito.mockStatic(DebatCRUD.class);
        Mockito.
                when(DebatCRUD.delete(debat)).thenReturn(true);
    }

    @Test
    public void update() throws Exception{
        List<Debat> debats = loadDataDebatFromJsonFile();
        Debat debat = debats.get(1);

        PowerMockito.mockStatic(DebatCRUD.class);
        Mockito.
                when(DebatCRUD.update(debat)).thenReturn(true);
    }


    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
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

    public List<Secteur> loadDataSecteurFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "secteurs.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Secteur>>() {
        }.getType();
        List<Secteur> secteurs = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return secteurs;
    }

    private Debat getDebatById(int id) throws Exception {
        List<Debat> debats = loadDataDebatFromJsonFile();
        Debat debat = debats.stream().filter(item -> item.getId() == id).findFirst().get();

        return debat;
    }

}