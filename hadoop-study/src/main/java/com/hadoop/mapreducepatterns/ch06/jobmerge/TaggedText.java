package com.hadoop.mapreducepatterns.ch06.jobmerge;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class TaggedText implements WritableComparable<TaggedText> {
	private String tag = "";
	private Text text = new Text();

	public TaggedText() {
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		tag = in.readUTF();
		text.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(tag);
		text.write(out);
	}

	/**
	 * 항목은 먼저 태그, 그다음 Text 값으로 정렬된다
	 */
	@Override
	public int compareTo(TaggedText o) {
		int compare = tag.compareTo(o.getTag());
		if (compare == 0) {
			return text.compareTo(o.getText());
		} else {
			return compare;
		}
	}

	@Override
	public String toString() {
		return tag.toString() + ":" + text.toString();
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Text getText() {
		return text;
	}

	public void setText(String text) {
		this.text.set(text);
	}

	public void setText(Text text) {
		this.text = text;
	}
}