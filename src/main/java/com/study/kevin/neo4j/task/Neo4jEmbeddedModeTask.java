package com.study.kevin.neo4j.task;

import com.study.kevin.neo4j.lable.MyLabel;
import com.study.kevin.neo4j.relation.RelationTypes;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: kevin
 * @Description:  嵌入式开发
 * @Company: 上海博般数据技术有限公司
 * @Version: 1.0.0
 * @Date: Created in 10:59 2020/5/21
 * @ProjectName: neo4j_learning
 */
public class Neo4jEmbeddedModeTask {

    GraphDatabaseService graphDB;

    public Neo4jEmbeddedModeTask(File dbFile) {
        //创建一个graph DB 实例
        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(dbFile);
    }

    public static void main(String[] args) throws IOException {
        final File dbDir = new File("D:\\study\\Neo4jDB");
        //清空数据库
        FileUtils.deleteRecursively(dbDir);

        Neo4jEmbeddedModeTask task = new Neo4jEmbeddedModeTask(dbDir);
//        task.createDataByApi();
        task.createDataByCypher();
        task.searchData();
        task.shutDown();
        System.out.println("进程结束");
    }

    /**
     * 使用cql创建数据
     */
    private void createDataByCypher(){
        String cql = "CREATE (l:Mode {id:1,type:1,systemId:'武汉',mrid:'L0001',title:'线路1'})," +
                "(d1:Mode {id:1,type:2,systemId:'武汉',mrid:'dot001',title:'线端1'})," +
                "(d2:Mode {id:2,type:2,systemId:'武汉',mrid:'dot002',title:'线端2'})," +
                "(l)-[:Connect {systemId:'武汉'}]->(d1)," +
                "(l)-[:Connect {systemId:'武汉'}]->(d2) " +
                "return l";
        
        Result result = graphDB.execute(cql);
        Node l = (Node) result.columnAs("l").next();
        System.out.println(l);
    }

    private void createDataByApi(){
        //创建一个事务
        try (Transaction transaction = graphDB.beginTx();){
            Node line = graphDB.createNode();
            //设置标签
            line.addLabel(MyLabel.Mode);
            //设置属性
            line.setProperty("id" , 1);
            line.setProperty("type" , 1);
            line.setProperty("systemId" , "武汉");
            line.setProperty("mrid" , "L0001");
            line.setProperty("title" , "线路1");

            Node dot1 = graphDB.createNode();
            dot1.addLabel(MyLabel.Mode);
            dot1.setProperty("id" , 1);
            dot1.setProperty("type" , 2);
            dot1.setProperty("systemId" , "武汉");
            dot1.setProperty("mrid" , "dot001");
            dot1.setProperty("titel" , "线端1");

            Node dot2 = graphDB.createNode();
            dot2.addLabel(MyLabel.Mode);
            dot2.setProperty("id" , 2);
            dot2.setProperty("type" , 2);
            dot2.setProperty("systemId" , "武汉");
            dot2.setProperty("mrid" , "dot002");
            dot2.setProperty("titel" , "线端2");

            //创建 line -> dot1 的关系
            Relationship r1 = line.createRelationshipTo(dot1, RelationTypes.Connect);
            r1.setProperty("systemId" , "武汉");

            //创建 line -> dot2 的关系
            Relationship r2 = line.createRelationshipTo(dot2, RelationTypes.Connect);
            r2.setProperty("systemId" , "武汉");

            //提交事务
            transaction.success();

        }
    }

    private void searchData(){
        try(Transaction transaction = graphDB.beginTx();){
//            ResourceIterator<Node> nodes = graphDB.findNodes(MyLabel.Mode);
//            ResourceIterable<Relationship> relations = graphDB.getAllRelationships();
            Map<String , Object> properties = new HashMap<>();
            properties.put("type" , 1);
            properties.put("systemId" , "武汉");
            properties.put("mrid" , "L0001");
            ResourceIterator<Node> nodes = graphDB.findNodes(MyLabel.Mode, properties);
            if (nodes != null){
                nodes.stream().forEach(node -> {
                    System.out.println("Find Node! id:" + node.getId() + ",Labels:" + node.getLabels() + ",Properties:" + node.getAllProperties());
                    Iterable<Relationship> rels = node.getRelationships();
                    rels.forEach(rel -> {
                        Node endNode = rel.getEndNode();
                        System.out.println("根据Node<" + node.getId() + ">找到关系! id:" + rel.getId() + ",Type:" + rel.getType() + ",Properties:" + rel.getAllProperties() +
                        ",EndNodeId:" + endNode.getId() + ",Labels:" + endNode.getLabels() + ",Properties:" + endNode.getAllProperties());
                    });
                });
            }
        }
    }

    private void shutDown(){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                graphDB.shutdown();
            }
        });
    }

}
