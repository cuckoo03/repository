package com.hadoop.doithadoop.ch05;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * LocalJobRunner를 활용한 잡 테스트
 * @author cuckoo03
 *
 */
public class LocalJobRunnerTest {
	public static void main(String[] args) throws ClassNotFoundException,
			IOException, InterruptedException {
		LocalJobRunnerTest test = new LocalJobRunnerTest();
		test.run();
	}

	public void run() throws IOException, ClassNotFoundException,
			InterruptedException {
		Path inputPath = new Path("input");
		Path outputPath = new Path("output");

		Configuration conf = new Configuration();
		conf.set("mapred.job.tracker", "local");
		conf.set("fs.default.name", "file:////");

		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(outputPath)) {
			fs.delete(outputPath, true);
		}
		if (fs.exists(inputPath)) {
			fs.delete(inputPath, true);
		}
		fs.mkdirs(inputPath);

		String inputText = "A,B";
		DataOutputStream file = fs.create(new Path(inputPath, "part-" + 0));

		file.writeBytes(inputText);
		file.close();

		Job job = runJob(conf, inputPath, outputPath);
		Assert.assertTrue(job.isSuccessful());

		List<String> lines = IOUtils.readLines(fs.open(new Path(outputPath,
				"part-r-00000")));
		
		Assert.assertEquals(1, lines.size());
		String[] parts = StringUtils.split(lines.get(0), ",");
		Assert.assertEquals("A", parts[0]);
	}

	private Job runJob(Configuration conf, Path input, Path output)
			throws IOException, ClassNotFoundException, InterruptedException {
		Job job = new Job(conf);
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		FileInputFormat.setInputPaths(job, input);
		FileOutputFormat.setOutputPath(job, output);
		job.waitForCompletion(false);
		return job;
	}
}
