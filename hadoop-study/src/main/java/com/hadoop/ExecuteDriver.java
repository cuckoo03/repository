package com.hadoop;

import com.hadoop.cloudcomputing.ch05.AppendTest;
import com.hadoop.cloudcomputing.ch05.HAppendTest;
import com.hadoop.cloudcomputing.ch05.HDFSClient;
import com.hadoop.cloudcomputing.ch05.MergeFiles;
import com.hadoop.definitiveguide.ch13.HBaseClientExample;
import com.hadoop.doithadoop.ch04.WordCount;
import com.hadoop.doithadoop.ch05.LocalJobRunnerTest;
import com.hadoop.doithadoop.ch06.countcitation.CountCitation;
import com.hadoop.doithadoop.ch06.counttrigram.CountTrigram;
import com.hadoop.doithadoop.ch06.join.JoinIDTitle;
import com.hadoop.doithadoop.ch06.topn.TopN;
import com.hadoop.doithadoop.ch06.wordcount.WordCount2;
import com.hadoop.doithadoop.ch07.index.InvertedIndex;
import com.hadoop.doithadoop.ch07.index.InvertedIndex2;
import com.hadoop.doithadoop.ch07.index.InvertedIndex3;
import com.hadoop.doithadoop.ch07.join2.JoinIDTitle2;
import com.hadoop.doithadoop.ch07.stringsort.StringSort;
import com.hadoop.hbase.mapreduce.HTableInsertClient;
import com.hadoop.hbase.mapreduce.HTableRowCounter;
import com.hadoop.hbase.mapreduce.HTableUniqValueCounter;
import com.hadoop.log.TomcatLogCount;
import com.hadoop.mapreducepatterns.ch06.chainfolding.ChainFoldingDriver;
import com.hadoop.mapreducepatterns.ch06.jobmerge.MergedJobDriver;
import com.hadoop.mapreducepatterns.ch07.random.RandomDataGenerationDriver;
import com.hadoop.mapreducepatterns.ch07.redis.RedisInputDriver;
import com.hadoop.mapreducepatterns.ch07.redis.RedisOutputDriver;
import com.hadoop.mapreducepatterns.groovy.ch02.average.AverageCount;
import com.hadoop.mapreducepatterns.groovy.ch02.medianstd.MedianStdDev;
import com.hadoop.mapreducepatterns.groovy.ch02.minmaxcount.MinMaxCountDriver;
import com.hadoop.mapreducepatterns.groovy.ch02.reversedIndex.WikipediaIndex;
import com.hadoop.mapreducepatterns.groovy.ch03.counter.CountNumUsersByState;
import com.hadoop.mapreducepatterns.groovy.ch03.distinct.DistinctUser;
import com.hadoop.mapreducepatterns.groovy.ch03.floomfilter.BloomFiltering;
import com.hadoop.mapreducepatterns.groovy.ch03.grep.DistributedGrep;
import com.hadoop.mapreducepatterns.groovy.ch03.random.SimpleRandomSampling;
import com.hadoop.mapreducepatterns.groovy.ch03.topn.TopTen;
import com.hadoop.mapreducepatterns.groovy.ch04.hierarchy.PartitionedUsers;
import com.hadoop.mapreducepatterns.groovy.ch04.totalordersort.TotalOrderSorting;
import com.hadoop.mapreducepatterns.groovy.ch05.reduceside.ReduceSideJoinDriver;
import com.hadoop.mapreducepatterns.groovy.ch05.replicatedjoin.ReplicatedJoinDriver;
import com.hadoop.mapreducepatterns.groovy.ch06.jobchain.JobChainingDriver;
import com.hadoop.mapreducepatterns.groovy.ch06.jobcontrol.JobControlDriver;
import com.hadoop.mapreducepatterns.groovy.ch06.parallel.ParallelJobDriver;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * A description of an example program based on its class and a human-readable
 * description.
 */
public class ExecuteDriver {
	public static void main(String argv[]) {
		int exitCode = -1;
		ProgramDriver pgd = new ProgramDriver();
		try {
			pgd.addClass("WordCount", WordCount.class, "");
			pgd.addClass("WordCount2", WordCount2.class, "");
			pgd.addClass("TopN", TopN.class, "");
			pgd.addClass("CountTrigram", CountTrigram.class, "");
			pgd.addClass("CountCitation", CountCitation.class, "");
			pgd.addClass("JoinIDTitle", JoinIDTitle.class, "");
			pgd.addClass("StringSort", StringSort.class, "");
			pgd.addClass("JoinIDTitle2", JoinIDTitle2.class, "");
			pgd.addClass("InvertedIndex", InvertedIndex.class, "");
			pgd.addClass("InvertedIndex2", InvertedIndex2.class, "");
			pgd.addClass("InvertedIndex3", InvertedIndex3.class, "");
			pgd.addClass("HDFSClient", HDFSClient.class, "");
			pgd.addClass("MinMaxCount", MinMaxCountDriver.class, "");
			pgd.addClass("AverageCount", AverageCount.class, "");
			pgd.addClass("MedianStdDev", MedianStdDev.class, "");
			pgd.addClass("WikipediaIndex", WikipediaIndex.class, "");
			pgd.addClass("CountNumUsersByState", CountNumUsersByState.class, "");
			pgd.addClass("DistributedGrep", DistributedGrep.class, "");
			pgd.addClass("SimpleRandomSampling", SimpleRandomSampling.class, "");
			pgd.addClass("BloomFiltering", BloomFiltering.class, "");
			pgd.addClass(
					"BloomFilteringJ",
					com.hadoop.mapreducepatterns.ch03.bloomfilter.BloomFiltering.class,
					"");
			pgd.addClass("TopTen", TopTen.class, "");
			pgd.addClass("DistinctUser", DistinctUser.class, "");
			pgd.addClass(
					"PostCommentHierarchy",
					com.hadoop.mapreducepatterns.ch04.hierarchy.PostCommentHierarchy.class,
					"");
			pgd.addClass("PartitionedUsers", PartitionedUsers.class, "");
			pgd.addClass("TotalOrderSorting", TotalOrderSorting.class, "");
			pgd.addClass("ReduceSideJoin", ReduceSideJoinDriver.class, "");
			pgd.addClass("ReplicatedJoin", ReplicatedJoinDriver.class, "");
			pgd.addClass("JobChaining", JobChainingDriver.class, "");
			pgd.addClass("ParallelJob", ParallelJobDriver.class, "");
			pgd.addClass("JobControl", JobControlDriver.class, "");
			pgd.addClass("ChainFolding", ChainFoldingDriver.class, "");
			pgd.addClass("MergedJob", MergedJobDriver.class, "");
			pgd.addClass("RandomDataGeneration",
					RandomDataGenerationDriver.class, "");
			pgd.addClass("RedisOutput", RedisOutputDriver.class, "");
			pgd.addClass("RedisInput", RedisInputDriver.class, "");
			pgd.addClass("AppendTest", AppendTest.class, "");
			pgd.addClass("HAppendTest", HAppendTest.class, "");
			pgd.addClass("MergeFiles", MergeFiles.class, "");
			pgd.addClass("TomcatLogCount", TomcatLogCount.class, "");
			pgd.addClass("HBaseClientExample", HBaseClientExample.class, "");
			pgd.addClass("LocalJobRunner", LocalJobRunnerTest.class, "");
			pgd.addClass("HTableRowCounter", HTableRowCounter.class, "");
			pgd.addClass("HTableUniqValueCounter",
					HTableUniqValueCounter.class, "");
			pgd.addClass("HTableInsertClient", HTableInsertClient.class, "");
			pgd.driver(argv);
			// Success
			exitCode = 0;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.exit(exitCode);
	}
}