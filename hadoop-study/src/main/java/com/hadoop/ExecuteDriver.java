package com.hadoop;

import com.hadoop.cloudcomputing.ch05.HDFSClient;
import com.hadoop.cloudcomputing.ch05.HDFSClientExample;
import com.hadoop.doithadoop.ch04.WordCount;
import com.hadoop.doithadoop.ch06.countcitation.CountCitation;
import com.hadoop.doithadoop.ch06.counttrigram.CountTrigram;
import com.hadoop.doithadoop.ch06.join.JoinIDTitle;
import com.hadoop.doithadoop.ch06.topn.TopN;
import com.hadoop.doithadoop.ch06.wordcount.WordCount2;
import com.hadoop.doithadoop.ch07.index.CreateESIndex;
import com.hadoop.doithadoop.ch07.index.InvertedIndex;
import com.hadoop.doithadoop.ch07.index.InvertedIndex2;
import com.hadoop.doithadoop.ch07.index.InvertedIndex3;
import com.hadoop.doithadoop.ch07.join2.JoinIDTitle2;
import com.hadoop.doithadoop.ch07.stringsort.StringSort;
import com.hadoop.mapreducepatterens.ch02.minmaxcount.MinMaxCountDriver;

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
			pgd.addClass("CreateESIndex", CreateESIndex.class, "");
			pgd.addClass("HDFSClient", HDFSClient.class, "");
			pgd.addClass("HDFSClientGroovy", HDFSClientExample.class, "");
			pgd.addClass("MinMaxCount", MinMaxCountDriver.class, "");
			pgd.driver(argv);
			// Success
			exitCode = 0;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.exit(exitCode);
	}
}
