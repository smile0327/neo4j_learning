package com.study.kevin.neo4j.task;

import org.neo4j.driver.v1.AccessMode;
import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

/**
 * @Auther: kevin
 * @Description:  使用驱动开发  执行Cypher语句
 * @Company: 上海博般数据技术有限公司
 * @Version: 1.0.0
 * @Date: Created in 14:39 2020/5/20
 * @ProjectName: neo4j_learning
 */
public class Neo4jDriverModeTask {


    Driver driver = null;

    public Neo4jDriverModeTask(String uri , String userName , String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(userName, password));
    }

    public static void main(String[] args) {
        //使用bolt方式连接数据库
        Neo4jDriverModeTask task = new Neo4jDriverModeTask("bolt://10.172.246.236:7687", "neo4j", "bobandata123");


    }

    private void addData(){
        try(Session session = driver.session(AccessMode.WRITE)) {
            session.writeTransaction(tx -> createDataByCypher(tx));
        }
    }

    private StatementResult createDataByCypher(Transaction tx){
        String cql = "CREATE (l:Mode {id:1,type:1,systemId:'武汉',mrid:'L0001',title:'线路1'})," +
                "(d1:Mode {id:1,type:2,systemId:'武汉',mrid:'dot001',title:'线端1'})," +
                "(d2:Mode {id:2,type:2,systemId:'武汉',mrid:'dot002',title:'线端2'})," +
                "(l)-[:Connect {systemId:'武汉'}]->(d1)," +
                "(l)-[:Connect {systemId:'武汉'}]->(d2) " +
                "return l";
        return null;
    }

}
