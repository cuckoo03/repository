package com.avro;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.avro.tool.RpcSendTool;

public class HelloClient2 {

	public static void main(String[] args) throws Exception {
		String protocolFile = "./com/avro/hello.avpr";
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		RpcSendTool tool = new RpcSendTool();
		tool.run(null, new PrintStream(bout), System.err, Arrays.asList(
				"http://127.0.0.1:9090", protocolFile, "hello", "-data",
				"{\"userName\": \"kim\"}"));
		
		System.out.println(new String(bout.toByteArray()));
	}

}
