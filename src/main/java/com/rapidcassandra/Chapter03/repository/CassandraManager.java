package com.rapidcassandra.Chapter03.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class CassandraManager {
	
	private Cluster cluster;
	private Session session;
	private String keyspace;
	
	public Session getSession() {
		return session;
	}
	
	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	// connect to a Cassandra cluster and a keyspace
	public void connect(final String node, final int port, final String keyspace) {
		this.cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
		
		System.out.println("node="+node+" port="+port+" keyspace="+keyspace);
		System.out.printf("cluster returned: "+cluster+":xxx\n\n");
		Metadata metadata = cluster.getMetadata();
		
		System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
		
		for (Host host: metadata.getAllHosts()) {
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
					host.getDatacenter(), host.getAddress(), host.getRack());
		}
		
		session = cluster.connect(keyspace);
	}
	
	// disconnect from Cassandra
	public void close() {
		cluster.close();
	}

}
