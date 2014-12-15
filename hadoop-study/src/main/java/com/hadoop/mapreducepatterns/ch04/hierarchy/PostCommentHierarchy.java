package com.hadoop.mapreducepatterns.ch04.hierarchy;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hadoop.mapreducepatterns.MRDPUtils;

/**
 * 게시글과 코멘트에 대해 게시글과 연관된 코멘트를 포함하고 있는 구조화된 XML 계층을 생성한다.
 * 
 * @author cuckoo03
 *
 */
public class PostCommentHierarchy {
	static class PostMapper extends Mapper<LongWritable, Text, Text, Text> {
		private Text outkey = new Text();
		private Text outvalue = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value
					.toString());
			// 외래 조인 키는 게시글 ID
			if (parsed.get("Id") == null) {
				return;
			}
			outkey.set(parsed.get("Id"));
			outvalue.set("P" + value.toString().trim());

			context.write(outkey, outvalue);
		}
	}

	static class CommentMapper extends Mapper<LongWritable, Text, Text, Text> {
		private Text outkey = new Text();
		private Text outvalue = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value
					.toString());
			if (parsed.get("PostId") == null) {
				return;
			}
			outkey.set(parsed.get("PostId"));
			outvalue.set("C" + value.toString().trim());

			context.write(outkey, outvalue);
		}
	}

	static class PostCommentHierarchyReducer extends
			Reducer<Text, Text, Text, NullWritable> {
		private List<String> comments = new ArrayList<String>();
		private DocumentBuilderFactory dbf = DocumentBuilderFactory
				.newInstance();
		private String post = null;

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			// 변수 초기화
			post = null;
			comments.clear();

			for (Text t : values) {
				// 게시글 레코드라면 플래그 제거하고 저장
				if (t.toString().substring(0) == "P") {
					post = t.toString().substring(1, t.toString().length())
							.trim();
				} else {
					// 플래그를 제거하고 목록에 추가
					comments.add(t.toString()
							.substring(1, t.toString().length()).trim());
				}
			}
			// 코멘트가 없다면 코멘트 목록은 비어 있다
			
			// 게시글이 null이 아니라면 게시글과 코멘트를 조합
			if (post != null) {
				System.out.println("*****************************");
				System.out.println("post is not null");
				String postWithCommentChildren;
				try {
					postWithCommentChildren = nestElements(post, comments);
					context.write(
							new Text(postWithCommentChildren),
							NullWritable.get());
				} catch (ParserConfigurationException | TransformerException
						| SAXException e) {
					e.printStackTrace();
				}
			}
		}

		private String nestElements(String post, List<String> comments)
				throws ParserConfigurationException, TransformerException,
				SAXException, IOException {
			// XML을 만들기 위한 새 문서 생성
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.newDocument();

			// 부모 노드를 문서에 복사
			Element postEl = getXmlElementFromString(post);
			Element toAddPostEl = doc.createElement("post");
			copyAttributesToElements(postEl.getAttributes(), toAddPostEl);

			// 각 코멘트를 게시글 노드에 복사
			for (String commentXml : comments) {
				Element commentEl = getXmlElementFromString(commentXml);
				Element toAddCommentEl = doc.createElement("comments");

				// 원 코멘트 항목의 속성을 새로운 항목에 복사
				copyAttributesToElements(commentEl.getAttributes(),
						toAddCommentEl);

				// 복사된 코멘트를 게시글 항목에 추가
				toAddPostEl.appendChild(toAddCommentEl);
			}

			// 게시글 항목을 문서에 추가
			doc.appendChild(toAddPostEl);
			// 문서를 XML 문자열로 변환하고 반환
			return transformDocumentToString(doc);
		}

		private Element getXmlElementFromString(String xml)
				throws ParserConfigurationException, SAXException, IOException {
			DocumentBuilder builder = dbf.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(xml)))
					.getDocumentElement();
		}

		private void copyAttributesToElements(NamedNodeMap attributes,
				Element element) {
			// 각 속성을 항목에 복사
			for (int i = 0; i < attributes.getLength(); i++) {
				Attr toCopy = (Attr) attributes.item(i);
				element.setAttribute(toCopy.getName(), toCopy.getValue());
			}
		}

		private String transformDocumentToString(Document doc)
				throws TransformerException {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));

			// 한 줄단 레코드 하나를 가질 수 있게 모든 개행 문자를 공백 문자로 치환
			return writer.getBuffer().toString().replaceAll("\n|\r", "");
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 3) {
			System.out
					.println("Usage: PostCommentHierachy <posts file> <comments file> <outdir>");
			System.exit(2);
		}
		Job job = new Job(conf, "PostCommentHierarchy");
		job.setJarByClass(PostCommentHierarchy.class);

		MultipleInputs.addInputPath(job, new Path(otherArgs[0]),
				TextInputFormat.class, PostMapper.class);
		MultipleInputs.addInputPath(job, new Path(otherArgs[1]),
				TextInputFormat.class, CommentMapper.class);

		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setReducerClass(PostCommentHierarchyReducer.class);

		Path outputDir = new Path(otherArgs[2]);
		TextOutputFormat.setOutputPath(job, outputDir);

		FileSystem.get(conf).delete(outputDir, true);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}