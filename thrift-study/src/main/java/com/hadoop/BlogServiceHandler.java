package com.hadoop;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.thrift.TException;

import com.thrift.BlogArticle;
import com.thrift.BlogException;
import com.thrift.BlogService;
import com.thrift.ServiceStatus;

/**
 * 작성중
 * 
 * @author cuckoo03
 *
 */
public class BlogServiceHandler implements BlogService.Iface {
	public static final String BLOG_ROOT = "/user/blog/article";
	private BlogServer server;
	private FileSystem fs;

	public BlogServiceHandler(BlogServer server, String hdfsUri)
			throws IOException {
		this.server = server;
		Configuration conf = new Configuration();
		conf.set("fs.default.name", hdfsUri);
		this.fs = FileSystem.get(conf);
		if (!fs.exists(new Path(BLOG_ROOT))) {
			fs.mkdirs(new Path(BLOG_ROOT));
		}
	}

	@Override
	public ServiceStatus getServiceStatus() throws TException {
		ServiceStatus serviceStatus = new ServiceStatus();

		serviceStatus.setHostName(server.getHostName());
		serviceStatus.setPort(server.getPort());
		serviceStatus.setStatus("OK");
		return serviceStatus;
	}

	@Override
	public void saveBlog(BlogArticle article) throws TException {
		article.setArticleId(System.currentTimeMillis() + System.nanoTime());
		Path blogPath = new Path(BLOG_ROOT + "/" + article.getUserId());

		try {
			if (!fs.exists(blogPath)) {
				fs.mkdirs(blogPath);
			}

			DataOutputStream dout = fs.create(new Path(blogPath, String
					.valueOf(article.getArticleId())));

			try {
				writeArticle(article, dout);
			} finally {
				dout.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new TException(e.getMessage());
		}
	}

	private void writeArticle(BlogArticle article, DataOutputStream out)
			throws IOException {
		out.writeInt(article.getUserId().getBytes().length);
		out.write(article.getUserId().getBytes());

		out.writeLong(article.getArticleId());

		out.writeInt(article.getTitle().getBytes().length);
		out.write(article.getContents());
	}

	@Override
	public BlogArticle getBlog(String userId, long articleId)
			throws BlogException, TException {
		Path articlePath = new Path(BLOG_ROOT + "/" + userId + "/" + articleId);

		try {
			DataInputStream in = fs.open(articlePath);
			return readArticle(in);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BlogException(server.getHostName(), e.getMessage());
		}
	}

	private BlogArticle readArticle(DataInputStream in) throws IOException {
		BlogArticle article =new BlogArticle();
		article.setUserId(readString(in));
		article.setArticleId(in.readLong());
		article.setTitle(readString(in));
		article.setContents(ByteBuffer.wrap(readBytes(in)));
		return article;
	}

	private byte[] readBytes(DataInputStream in) throws IOException {
		byte[] buf = new byte[in.readInt()];
		in.readFully(buf);
		return buf;
	}

	private String readString(DataInputStream in) throws IOException {
		byte[] buf = new byte[in.readInt()];
		in.readFully(buf);
		return new String(buf, 0, buf.length);
	}

	@Override
	public List<BlogArticle> searchBlogByUserId(String userId)
			throws BlogException, TException {
		List<BlogArticle> result = new ArrayList<>();

		try {
			FileStatus[] files = fs.listStatus(new Path(BLOG_ROOT + "/"
					+ userId));
			for (FileStatus eachFile : files) {
				result.add(getBlog(userId,
						Long.parseLong(eachFile.getPath().getName())));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BlogException(server.getHostName(), e.getMessage());
		}
		return result;
	}
}