package com.imooc.bigdata.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.plaf.PanelUI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HBaseAppCopy
{

    Connection connection = null ;
    Table table = null;
    Admin admin = null;

    String tableName = "hbase_java_api" ;

    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        configuration.set("hbase.rootdir", "hdfs://hadoop000:8020/hbase");
        configuration.set("hbase.zookeeper.quorum", "hadoop000:2181");
//        System.setProperty("hadoop.home.dir", "D:\\hadoop\\");

        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();

            Assert.assertNotNull(connection);
            Assert.assertNotNull(admin);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getConnecton(){

    }

    @Test
    public void createTable(){
        TableName table = TableName.valueOf(tableName);
        try {
            if(admin.tableExists(table)){
                System.out.println(tableName+ "已经存在");

            }
            else{
                /***
                 * HTableDescriptor ===> 表的描述符
                 * HColumnDescriptor ===> 列族的描述符
                 */

                HTableDescriptor descriptor = new HTableDescriptor(table);
                descriptor.addFamily(new HColumnDescriptor("info"));
                descriptor.addFamily(new HColumnDescriptor("ADDRESS"));
                admin.createTable(descriptor);
                System.out.println(tableName + "创建成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void queryTableInfos(){
        try {
            HTableDescriptor[] tables = admin.listTables();
            if(tables.length >0){
                for(HTableDescriptor table: tables){
                    System.out.println(table.getNameAsString());
                    HColumnDescriptor[] columnDescriptors = table.getColumnFamilies();
                    for(HColumnDescriptor c_f: columnDescriptors){
                        System.out.println("\t"+c_f.getNameAsString());
                    }
                    System.out.println("------------------------------------");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPut() throws IOException {
        table = connection.getTable(TableName.valueOf(tableName));
//        Put put = new Put(Bytes.toBytes("chris"));
//        // 通过Put设置要添加数据的cf、qualifier(column)、value
//        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes("26"));
//        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("birthday"), Bytes.toBytes("1993-06-16"));
//        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("company"), Bytes.toBytes("28"));
//        put.addColumn(Bytes.toBytes("ADDRESS"), Bytes.toBytes("country"), Bytes.toBytes("CN"));
//        put.addColumn(Bytes.toBytes("ADDRESS"), Bytes.toBytes("province"), Bytes.toBytes("jilin"));
//        put.addColumn(Bytes.toBytes("ADDRESS"), Bytes.toBytes("city"), Bytes.toBytes("changchun"));

        //将数据put到hbase中去
//        table.put(put);
        List<Put> puts = new ArrayList<>();

        Put put1 = new Put(Bytes.toBytes("sunshine"));
        // 通过Put设置要添加数据的cf、qualifier(column)、value
        put1.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes("26"));
        put1.addColumn(Bytes.toBytes("info"), Bytes.toBytes("birthday"), Bytes.toBytes("1993-03-16"));
        put1.addColumn(Bytes.toBytes("info"), Bytes.toBytes("company"), Bytes.toBytes("apple"));
        put1.addColumn(Bytes.toBytes("ADDRESS"), Bytes.toBytes("country"), Bytes.toBytes("CN"));
        put1.addColumn(Bytes.toBytes("ADDRESS"), Bytes.toBytes("province"), Bytes.toBytes("jilin"));
        put1.addColumn(Bytes.toBytes("ADDRESS"), Bytes.toBytes("city"), Bytes.toBytes("changchun"));

        Put put2 = new Put(Bytes.toBytes("dark"));
        // 通过Put设置要添加数据的cf、qualifier(column)、value
        put2.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes("27"));
        put2.addColumn(Bytes.toBytes("info"), Bytes.toBytes("birthday"), Bytes.toBytes("1992-03-16"));
        put2.addColumn(Bytes.toBytes("info"), Bytes.toBytes("company"), Bytes.toBytes("sums"));
        put2.addColumn(Bytes.toBytes("ADDRESS"), Bytes.toBytes("country"), Bytes.toBytes("CN"));
        put2.addColumn(Bytes.toBytes("ADDRESS"), Bytes.toBytes("province"), Bytes.toBytes("dongbei"));
        put2.addColumn(Bytes.toBytes("ADDRESS"), Bytes.toBytes("city"), Bytes.toBytes("heilong"));

        puts.add(put1);
        puts.add(put2);

        table.put(puts);
    }

    @Test
    public void testGet01() throws IOException {
        table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get("chris".getBytes());
        get.addColumn(Bytes.toBytes("info"), (Bytes.toBytes("age")));
        Result result = table.get(get);
        printRequest(result);
    }

    @Test
    public void testScan01() throws IOException {
        table = connection.getTable(TableName.valueOf(tableName));
        Scan scan  =new Scan();
//        scan.addFamily(Bytes.toBytes("info"));
        scan.addColumn(Bytes.toBytes("info"),Bytes.toBytes("company"));
//        Scan scan = new Scan(Bytes.toBytes("chris"));
//        Scan scan = new Scan(new Get(Bytes.toBytes("chris")));
//        Scan scan = new Scan(Bytes.toBytes("chris"),Bytes.toBytes("sunshine"));
        ResultScanner rs = table.getScanner(scan);
        for(Result result : rs){
            printRequest(result);
            System.out.println("~~~~~~~~~~~~~~~~~");
        }
    }

    private void printRequest(Result result){
        for(Cell cell :result.rawCells()){
            System.out.println(Bytes.toString(result.getRow())+ "\t"
            +Bytes.toString(CellUtil.cloneFamily(cell))+ "\t"
            +Bytes.toString(CellUtil.cloneQualifier(cell))+ "\t"
            +Bytes.toString(CellUtil.cloneValue(cell))+ "\t"
            +cell.getTimestamp());

        }

    }

    @Test
    public void testFilter() throws IOException {
        table = connection.getTable(TableName.valueOf(tableName));

        Scan scan = new Scan();
//        String reg = "^c";
//        // RowFilter 过滤行， RegexStringComparator 通过正则匹配的方式
//        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(reg));
//        scan.setFilter(filter);

//        Filter filter = new PrefixFilter(Bytes.toBytes("d"));
//        scan.setFilter(filter);

        ResultScanner rs = table.getScanner(scan);
        for(Result result : rs){
            printRequest(result);
            System.out.println("~~~~~~~~~~~~~~~~~");
        }

    }
    @After
    public void tearDown(){
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
