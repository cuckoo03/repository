package com.hadoop.mapreducepatterns.groovy.ch04.hierarchy

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Mapper.Context
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.hadoop.util.GenericOptionsParser
import org.w3c.dom.Attr
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.xml.sax.InputSource

import com.hadoop.mapreducepatterns.MRDPUtils

/**
 * 게시글과 코멘트에 대해 게시글과 연관된 코멘트를 포함하고 있는 구조화된 XML 계층을 생성한다.
 * 리듀스에서 출력 카운트가 0으로 되는 문제 수정해아함.
 * @author cuckoo03
 *
 */
class PostCommentHierarchy {
	static class PostMapper extends Mapper<LongWritable, Text, Text, Text> {
		private Text outkey = new Text()
		private Text outvalue = new Text()

		@Override
		public void map(LongWritable key, Text value, Context context) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())
			// 외래키는 게시글 ID
			outkey.set(parsed.get("Id"))
			if (parsed.get("Id") == null) {
				return
			}
			outvalue.set("P" + value.toString())

			context.write(outkey, outvalue)
		}
	}
	static class CommentMapper extends Mapper<LongWritable, Text, Text, Text> {
		private Text outkey = new Text()
		private Text outvalue = new Text()

		@Override
		public void map(LongWritable key, Text value, Context context) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())
			outkey.set(parsed.get("PostId"))
			if (parsed.get("PostId") == null) {
				return
			}
			outvalue.set("C" + value.toString())

			context.write(outkey, outvalue)
		}
	}
	static class PostCommentHierarchyReducer extends Reducer<Text, Text, Text,
	NullWritable> {
		private List<String> comments = new ArrayList<String>()
		private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
		private String post = null

		@Override
		public void reduce(Text key, Iterable<Text> values,
				org.apache.hadoop.mapreduce.Reducer.Context context) {
			post = null
			comments.clear()

			values.each { t ->
				if (t.toString().substring(0) == "P") {
					post = t.toString().substring(1, t.toString().size()).trim()
				} else {
					comments.add(t.toString().substring(1, t.toString().size())
							.trim())
				}
			}
			if (post != null) {
				String postWithCommentChildren = nestElements(post, comments)

				context.write(new Text(postWithCommentChildren),
						NullWritable.get())
			}
		}

		private String nestElements(String post, List<String> comments) {
			DocumentBuilder builder = dbf.newDocumentBuilder()
			Document doc = builder.newDocument()

			Element postEl = getXmlElementFromString(post)
			Element toAddPostEl = doc.createElement("post")
			copyAttributesToElements(postEl.getAttributes(), toAddPostEl)

			comments.each { commentXml ->
				Element commentEl = getXmlElementFromString(commentXml)
				Element toAddCommentEl = doc.createElement("comments")

				copyAttributesToElements(commentEl.getAttributes(),
						toAddCommentEl)

				toAddPostEl.appendChild(toAddCommentEl)
			}

			doc.appendChild(toAddPostEl)
			return transformDocumentToString(doc)
		}
		private Element getXmlElementFromString(String xml) {
			DocumentBuilder builder = dbf.newDocumentBuilder()
			return builder.parse(new InputSource(new StringReader(xml)))
			.getDocumentElement()
		}
		private void copyAttributesToElements(NamedNodeMap attributes,
				Element element) {
			for (int i = 0; i < attributes.getLength(); i++) {
				Attr toCopy = attributes.item(i)
				element.setAttribute(toCopy.getName(), toCopy.getValue())
			}
		}
		private String transformDocumentToString(Document doc) {
			TransformerFactory tf = TransformerFactory.newInstance()
			Transformer transformer = tf.newTransformer()
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes")
			StringWriter writer = new StringWriter()
			transformer.transform(new DOMSource(doc), new StreamResult(writer))

			return writer.getBuffer().toString().replaceAll("\n|\r", "")
		}
	}

	static main(args) {
		Configuration conf = new Configuration()
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 3) {
			println "Usage: PostCommentHierachy <posts> <comments> <outdir>"
			System.exit(2)
		}
		Job job = new Job(conf, "PostCommentHierarchy")
		job.setJarByClass(PostCommentHierarchy.class)

		MultipleInputs.addInputPath(job, new Path(otherArgs[0]),
				TextInputFormat.class, PostMapper.class)
		MultipleInputs.addInputPath(job, new Path(otherArgs[1]),
				TextInputFormat.class, CommentMapper.class)

		job.setOutputFormatClass(TextOutputFormat.class)
		job.setOutputKeyClass(Text.class)
		job.setOutputValueClass(Text.class)
		job.setReducerClass(PostCommentHierarchyReducer.class)

		Path outputDir = new Path(otherArgs[2])
		TextOutputFormat.setOutputPath(job, outputDir)

		FileSystem.get(conf).delete(outputDir, true)

		System.exit(job.waitForCompletion(true) ? 0 : 1)
	}
}