package com.netty.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.util.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.nio.*;
import org.apache.commons.lang.StringUtils;

public class SmsClient {

	private InetAddress addr = null;
	private String host = null;
	private String port = null;
	// private String host = "127.0.0.1";
	// private String port = "8999";

	private String szCAller = null; // 발신번호(정회원 인증 070번호)
	private String szCallback = null; // 회신 번호
	private String szCallee = null; // 착신 번호
	private String szSmsData = null; // sms 내용
	private String seq = null;
	int Sendian = 0; // 보내는 엔디안 0:리틀 엔디안 1:빅엔디안
	int Rendian = 0; // 받는 엔디안 0:리틀 엔디안 1:빅엔디안

	public SmsClient() {

	}

	public HashMap send(String szCAller, String szCallback, String szCallee,
			String szSmsData, String seq, int Sendian, int Rendian)
			throws IOException {
		// Socket Creation
		Socket socket = null;
		BufferedOutputStream bout = null;
		BufferedInputStream bin = null;
		HashMap result = null;

		this.host = "127.0.0.1";
		this.port = "10001";
		this.szCAller = szCAller; // 발신번호(정회원 인증 070번호)
		this.szCallback = szCallback; // 회신 번호
		this.szCallee = szCallee; // 착신 번호
		this.szSmsData = szSmsData; // sms 내용
		this.seq = seq; // 순번
		this.Sendian = Sendian; // 보내는 엔디안 0:리틀 엔디안 1:빅엔디안
		this.Rendian = Rendian; // 받는 엔디안 0:리틀 엔디안 1:빅엔디안

		try {
			System.out.println("host" + this.host);
			addr = InetAddress.getByName(this.host);

			socket = new Socket(this.addr, Integer.parseInt(this.port));
			socket.setSoTimeout(10000); // Timeout : 10 secs.

			System.out.println("socket = " + socket);

			// 향상된 버퍼 스트림
			bout = new BufferedOutputStream(socket.getOutputStream());

			bin = new BufferedInputStream(socket.getInputStream());

			ByteBuffer buffer = ByteBuffer.allocate(220); // 220바이트 공간 설정
			if (Sendian == 0) {
				buffer.order(ByteOrder.LITTLE_ENDIAN);
			} // c언어는 little_endian방식으로 전송한다. 자바는 big_endian방식사용
			else {
				buffer.order(ByteOrder.BIG_ENDIAN);
			}

			System.out.println("Byte order : " + buffer.order()); // 엔디안 방식 보여줌

			buffer.putShort((short) 0xfefe); // 2바이트 -기본 값 정해져 있음

			buffer.putShort((short) 220); // 2바이트 -총 길이(바디+헤더)

			buffer.putInt((int) 0x0000); // 4바이트 -0x0000 :요청 -ox0001:응답

			buffer.putInt(Integer.parseInt(this.seq)); // 4바이트 -순번

			buffer.putInt((int) 0x0100); // 4바이트 -기본값

			String ret = ""; // 전송하는 문자의 빈공간을 40바이트가 될때까지 null로 채움

			// 발신번호(정회원 인증 070번호)
			if (this.szCAller.getBytes().length > 40) {
				System.out.println("alert !!! szCAller over 40!!!");
			}
			ret = StringUtils.rightPad(this.szCAller, 40, (char) 0x00); // 빈공간을
																		// 채움
			buffer.put(ret.getBytes()); // szCAller[40] //바이트 공간에 발신번호를 넣음

			// 회신 번호
			if (this.szCallback.getBytes().length > 40) {
				System.out.println("alert !!! szCallback over 40!!!");
			}
			ret = StringUtils.rightPad(this.szCallback, 40, (char) 0x00); // 빈공간을
																			// 채움
			buffer.put(ret.getBytes()); // szCallback[40] //바이트 공간에 회신 번호를 넣음

			// 착신 번호
			if (this.szCallee.getBytes().length > 40) {
				System.out.println("alert !!! szCallee over 40!!!");
			}
			ret = StringUtils.rightPad(this.szCallee, 40, (char) 0x00); // 빈공간을
																		// 채움
			buffer.put(ret.getBytes()); // szCallee[40] //착신 번호

			// sms 내용
			if (this.szSmsData.getBytes("euc-kr").length > 80) {
				System.out.println("alert !!! szSmsData over 80!!! cut!!!!");
			}

			byte[] cotent = new byte[84]; // 84바이트 공간이지만 실제로는 80바이트만 사용
			System.arraycopy(this.szSmsData.getBytes("euc-kr"), 0, cotent, 0,
					this.szSmsData.getBytes("euc-kr").length); // sms내용을 84바이트
																// 배열에 복사
			buffer.put(cotent); // szSmsData[84] //바이트 공간에 복사한 배열을 넣음

			buffer.flip();

			byte[] results = buffer.array(); // 버퍼에 들어있는 내용을 바이트 배열에 집어 넣는다

			System.out.println("send packet--->" + new String(results));
			System.out.println("send size--->" + results.length);

			bout.write(results); // 신식send 스트림을 바이트배열 형식으로 서버에 보낸다
			bout.flush(); // 버퍼 내용을 비운다

			byte[] t = new byte[20]; // 20바이트 만듬
			bin.read(t, 0, 20); // 응답 스트림으로 부터 20바이트를 한꺼번에 읽는다

			ByteBuffer buffer2 = ByteBuffer.allocate(20);

			if (Rendian == 0) {
				buffer2.order(ByteOrder.LITTLE_ENDIAN);
			} // c언어는 little_endian방식으로 전송한다. 자바는 big_endian방식사용
			else {
				buffer2.order(ByteOrder.BIG_ENDIAN);
			}

			byte[] t_sub1 = getbytes(t, 8, 4);
			byte[] t_sub2 = getbytes(t, 16, 4);

			buffer2.put(t_sub1); // 시퀀스값만 버퍼에 넣음

			buffer2.put(t_sub2); // 전송결과값만 버퍼에 넣음

			int i1 = buffer2.getInt(0); // 시퀀스값만 인트형으로 추출
			int i2 = buffer2.getInt(4); // 전송결과값만 인트형으로 추출

			System.out.println("recive seq: " + i1); // 받은 시퀀스
			System.out.println("recive result: " + i2); // 받은 전송결과값

			result = new HashMap();
			result.put("seq", i1 + "");
			result.put("result", i2 + "");

		} catch (SocketException e1) {
			System.out.println("SOCKET EXCEPTION : " + e1.toString());
		} catch (UnknownHostException e) {
			System.out.println("Server Address Error");
		} catch (Exception e2) {
			System.out.println("EXCEPTION :" + e2.toString());
		} finally {

			if (bout != null) {
				try {
					bout.close();
				} catch (Exception e3) {
				}
			}

			if (bin != null) {
				try {
					bin.close();
				} catch (Exception e4) {
				}
			}

			System.out.println("Socket Closing....");
			socket.close();
		}

		return result;

	}

	public static final byte[] getbytes(byte src[], int offset, int length) {
		byte dest[] = new byte[length];
		System.arraycopy(src, offset, dest, 0, length);
		return dest;
	}

}