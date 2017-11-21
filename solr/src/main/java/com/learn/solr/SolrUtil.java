package com.learn.solr;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Collation;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Correction;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:solr.properties")
public class SolrUtil {

	@Autowired 
	private Environment env;

    /**
     * 获取solr服务
     * 
     * @return
     */
    public SolrClient getSolrClient(String core) {
    	
    	/*//单solr连接
    	HttpSolrClient client = new HttpSolrClient("/" + env.getProperty("com.solr.url")+ core);
        client.setConnectionTimeout(30000);
        client.setDefaultMaxConnectionsPerHost(100);
        client.setMaxTotalConnections(100);
        client.setSoTimeout(30000);
        return client;*/
        
        //solrCloud连接
        String zkUrl = "10.17.1.234:2181,10.17.1.235:2181,10.17.1.236:2181";  
        CloudSolrClient cloudSolrClient = new  CloudSolrClient(zkUrl);  
        cloudSolrClient.setDefaultCollection(core);
        cloudSolrClient.connect();  
        return cloudSolrClient;
        
    }

    /**
     * 添加文档
     * 
     * @param map
     * @param core
     * @throws Exception
     */
    public void addDocument(Map<String, String> map, String core)
            throws Exception {
        SolrInputDocument sid = new SolrInputDocument();
        for (Entry<String, String> entry : map.entrySet()) {
            sid.addField(entry.getKey(), entry.getValue());
        }
        SolrClient solrClient = getSolrClient(core);
        solrClient.add(sid);
        commitAndCloseSolr(solrClient);
    }

    /**
     * 添加文档，通过bean方式
     * 
     * @param persons
     * @param core
     * @throws Exception
     */
    public void addDocumentByBean(List<Business> business, String core)
            throws Exception {
        SolrClient solrClient = getSolrClient(core);
        solrClient.addBeans(business);
        commitAndCloseSolr(solrClient);
    }

    /**
     * 根据id集合删除索引
     * 
     * @param ids
     * @param core
     * @throws Exception
     */
    public void deleteDocumentByIds(List<String> ids, String core)
            throws Exception {
        SolrClient solrClient = getSolrClient(core);
        solrClient.deleteById(ids);
        commitAndCloseSolr(solrClient);
    }

    public void getDocument(String core) throws Exception {
        SolrClient solrClient = getSolrClient(core);
        SolrQuery sq = new SolrQuery();

        // q查询
        sq.set("q", "id:00003");

        // filter查询
        sq.addFilterQuery("id:[0 TO 00003]");

        // 排序
        sq.setSort("id", SolrQuery.ORDER.asc);

        // 分页 从第0条开始取，取一条
        sq.setStart(0);
        sq.setRows(1);

        // 设置高亮
        sq.setHighlight(true);

        // 设置高亮的字段
        sq.addHighlightField("name");

        // 设置高亮的样式
        sq.setHighlightSimplePre("<font color='red'>");
        sq.setHighlightSimplePost("</font>");

        QueryResponse result = solrClient.query(sq);

        // 这里可以从result获得查询数据(两种方式如下)

        // 1.获取document数据
        System.out.println("1.获取document数据-------------------------");
        SolrDocumentList results = result.getResults();
        // 获取查询的条数
        System.out.println("一共查询到" + results.getNumFound() + "条记录");
        for (SolrDocument solrDocument : results) {
            System.out.println("id:" + solrDocument.get("id"));
            System.out.println("name:" + solrDocument.get("name"));
            System.out.println("age:" + solrDocument.get("age"));
            System.out.println("addr:" + solrDocument.get("addr"));
        }

        // 2.获取对象信息,需要传入对应对象的类class
        System.out.println("2.获取对象信息,需要传入对应对象的类class-----------");
        List<Business> persons = result.getBeans(Business.class);
        System.out.println("一共查询到" + persons.size() + "条记录");
        for (Business person : persons) {
            System.out.println(person);
        }
        commitAndCloseSolr(solrClient);
    }

    /**
     * 查询使用spell接口，输入错误，solr可以给出建议词
     * 
     * @param core
     * @throws Exception
     */
    public void getSpell(String core) throws Exception {
        SolrClient solrClient = getSolrClient(core);
        SolrQuery sq = new SolrQuery();
        sq.set("qt", "/spell");

        // 原本是lisi，这里拼写错误，测试solr返回建议词语
        sq.set("q", "liss");
        QueryResponse query = solrClient.query(sq);
        SolrDocumentList results = query.getResults();

        // 获取查询条数
        long count = results.getNumFound();

        // 判断是否查询到
        if (count == 0) {
            SpellCheckResponse spellCheckResponse = query
                    .getSpellCheckResponse();
            List<Collation> collatedResults = spellCheckResponse
                    .getCollatedResults();
            for (Collation collation : collatedResults) {
                long numberOfHits = collation.getNumberOfHits();
                System.out.println("建议条数为:" + numberOfHits);

                List<Correction> misspellingsAndCorrections = collation
                        .getMisspellingsAndCorrections();
                for (Correction correction : misspellingsAndCorrections) {
                    String source = correction.getOriginal();
                    String current = correction.getCorrection();
                    System.out.println("推荐词语为：" + current + "原始的输入为：" + source);
                }
            }
        } else {
            for (SolrDocument solrDocument : results) {
                // 获取key集合
                Collection<String> fieldNames = solrDocument.getFieldNames();

                // 根据key集合输出value
                for (String field : fieldNames) {
                    System.out.println("key: " + field + ",value: "
                            + solrDocument.get(field));
                }
            }
        }

        // 关闭连接
        commitAndCloseSolr(solrClient);
    }
    
    public void facet(String core) throws Exception, Exception{  
    	SolrClient solrClient = getSolrClient(core);
        SolrQuery query = new SolrQuery("*:*");
        //query.setIncludeScore(false);
        query.setFacet(true);
        /* query.addFacetField("Province"); */
        query.addFacetField("cateName","level");//两个域有各自独立的结果
     
        /*  query.addFacetField(new String[] {"salary","publishDate","educateBackground","jobExperience","companytype","jobsType" });//设置需要facet的字段*/
        //query.addFacetQuery("Spare3:[4 TO 7]");
 
        /*
         * FacetComponet有两种排序选择，分别是count和index，
         * count是按每个词出现的次数，index是按词的字典顺序。如果查询参数不指定facet.sort，solr默认是按count排序。
         */
        /* query.setFacetSort("index"); */
        query.setFacetSort("count");
        /*query.setFacetLimit(101);  */ // 设置返回结果条数 ,-1表示返回所有,默认值为100
        /* query.setParam(FacetParams.FACET_OFFSET, "100");*/   //开始条数,偏移量,它与facet.limit配合使用可以达到分页的效果
        //query.setFacetSort(FacetParams.FACET_SORT_COUNT);
        query.setFacetMinCount(1);//设置 限制 count的最小返回值，默认为0 
        query.setFacetMissing(false);//不统计null的值
        /* query.setFacetPrefix("湖");//设置前缀 */
 
         
         
           
        System.out.println(query.toString());
 
        QueryResponse res = solrClient.query(query);
 
         
        System.out.println("-------单个facet结果--------");
        List<Count> spare3List = res.getFacetField("cateName").getValues();
        for (Count count : spare3List) {
            System.out.println(count.getName() + "#" + count.getCount());
        }
         
        System.out.println("-------所有facet结果--------");
        // 得到所有Facet结果
         List<FacetField> facets = res.getFacetFields();//返回的facet列表
 
          for (FacetField facet :facets) {
 
             System.out.println(facet.getName());
 
             System.out.println("----------------");
 
             List<Count> counts = facet.getValues();
                     int i=1;//计数器
             for (Count count : counts){
 
                System.out.println(i+count.getName()+":"+ count.getCount());
                i++;
             }
 
             System.out.println();
 
          }
                 
          System.out.println("-------FacetQuery结果--------");
        // 得到FacetQuery结果
        Map<String, Integer> facetQueryResult = res.getFacetQuery();
 
        for (Map.Entry<String, Integer> fqr : facetQueryResult.entrySet()) {
            System.out.println(fqr.getKey() + ":" + fqr.getValue());
        }    
         
         
         
         
        res.getFacetDate(null);
        res.getFacetDates();
        res.getFacetRanges();
    }

    /**
     * 提交以及关闭服务
     * 
     * @param solrClient
     * @throws Exception
     */
    public void commitAndCloseSolr(SolrClient solrClient)
            throws Exception {
        solrClient.commit();
        solrClient.close();
    }

}