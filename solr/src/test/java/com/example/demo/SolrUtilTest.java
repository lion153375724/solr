package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.learn.solr.Business;
import com.learn.solr.SolrApplication;
import com.learn.solr.SolrUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SolrApplication.class)
@PropertySource("classpath:solr.properties")
public class SolrUtilTest {

	@Autowired
	private SolrUtil solrUtil;
	
	@Autowired
	private Environment env;
	
	@Test
	public void addDocument() throws Exception{
		List<Business> list = new ArrayList<Business>();
        Business business = new Business();
        business.setBusinessId(1000002);
        business.setAddress("address0001");
        business.setAreaId("1");
        business.setMarketId("1");
        business.setMarketName("武汉白沙2");
        business.setName("狮子大超市2");
        list.add(business);
	    System.out.println("solrProperties.getSolrUrl():"+env.getProperty("com.solr.url"));
	    System.out.println("solrProperties.getSolrCore():"+env.getProperty("com.solr.core"));
        solrUtil.addDocumentByBean(list,env.getProperty("com.solr.core"));
		
	}
	
	@Test
	public void deleteDocument() throws Exception{
		List<String> list = new ArrayList<String>();
		list.add("1000002");
		solrUtil.deleteDocumentByIds(list, env.getProperty("com.solr.core"));
	}
	
	@Test
	public void facet() throws Exception{
		//solrUtil.facet(env.getProperty("com.solr.core"));
		solrUtil.facet("product");
	}
}
