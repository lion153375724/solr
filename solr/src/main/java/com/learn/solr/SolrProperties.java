package com.learn.solr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class SolrProperties {
	
	private String solrUrl;
	private String solrCore;

	public String getSolrUrl() {
		return solrUrl;
	}

	public void setSolrUrl(String solrUrl) {
		this.solrUrl = solrUrl;
	}

	public String getSolrCore() {
		return solrCore;
	}

	public void setSolrCore(String solrCore) {
		this.solrCore = solrCore;
	}

}
