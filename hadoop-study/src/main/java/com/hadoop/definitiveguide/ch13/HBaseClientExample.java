package com.hadoop.definitiveguide.ch13;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 기본 테이블 관리와 접속
 * 
 * @author cuckoo03
 *
 */
public class HBaseClientExample {
	public static void main(String[] args) throws IOException {
		Configuration conf = HBaseConfiguration.create();

		// create table
		HBaseAdmin admin = new HBaseAdmin(conf);
		HTableDescriptor htd = new HTableDescriptor("test");
		HColumnDescriptor hcd = new HColumnDescriptor("data");
		htd.addFamily(hcd);
		admin.createTable(htd);
		byte[] tableName = htd.getName();
		HTableDescriptor[] tables = admin.listTables();
		if (tables.length != 1 && Bytes.equals(tableName, tables[0].getName())) {
			throw new IOException("Failed create of table");
		}

		// Run some operations
		HTablePool pool = new HTablePool(conf, Integer.MAX_VALUE);
		HTableInterface table = pool.getTable(tableName);
		byte[] row1 = Bytes.toBytes("row1");
		Put p1 = new Put(row1);
		byte[] databytes = Bytes.toBytes("data");
		p1.add(databytes, Bytes.toBytes("1"), Bytes.toBytes("value1"));
		table.put(p1);

		Get g = new Get(row1);
		g.addColumn(databytes, Bytes.toBytes("1"));
		Result result = table.get(g);
		System.out.println("Get:" + result);

		Scan scan = new Scan();
		ResultScanner scanner = table.getScanner(scan);

		try {
			for (Result scannerResult : scanner) {
				System.out.println("scan:" + scannerResult);
			}
		} finally {
			scanner.close();
			pool.closeTablePool(tableName);
			pool.close();
		}

		// drop the table
		Delete d = new Delete(row1);
		table.delete(d);
		
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
	}
}