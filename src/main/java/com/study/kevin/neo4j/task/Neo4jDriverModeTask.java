package com.study.kevin.neo4j.task;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

/**
 * @Auther: kevin
 * @Description:  使用驱动开发  执行Cypher语句
 * @Company: 上海博般数据技术有限公司
 * @Version: 1.0.0
 * @Date: Created in 14:39 2020/5/20
 * @ProjectName: neo4j_learning
 */
public class Neo4jDriverModeTask {

    public static void main(String[] args) {
        //使用bolt方式连接数据库
        Driver driver = GraphDatabase.driver("bolt://10.172.246.236:7687",
                AuthTokens.basic("neo4j", "bobandata123"));
        Session session = driver.session();
    }

}
