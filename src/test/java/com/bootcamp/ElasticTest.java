package com.bootcamp;

import com.bootcamp.crud.CensureCRUD;
import com.bootcamp.crud.DebatCRUD;
import com.bootcamp.entities.Censure;
import com.bootcamp.entities.Debat;
import com.rintio.elastic.client.ElasticClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

public class ElasticTest {
    private final Logger LOG = LoggerFactory.getLogger(ElasticTest.class);


    @Test
    public void createIndexDebat()throws Exception{
        ElasticClient elasticClient = new ElasticClient();
        List<Debat> debats = DebatCRUD.read();
        for (Debat debat : debats){
            elasticClient.creerIndexObjectNative("debats","debat",debat,debat.getId());
            LOG.info("debat "+debat.getId()+" created");
        }
    }
}
