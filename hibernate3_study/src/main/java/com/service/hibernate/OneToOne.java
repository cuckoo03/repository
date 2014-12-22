package com.service.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.service.hibernate.onetoone.Board;
import com.service.hibernate.onetoone.BoardDetail;

public class OneToOne {
	/**
	 * 일대일 관계(동일한 키값을 갖는 1-1 관계)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SessionFactory factory = null;
		factory = new Configuration().configure().buildSessionFactory();

		Session session = factory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();

		Board board = new Board();
		board.setTitle("title");
		board.setUserName("userName");

		BoardDetail detail = new BoardDetail();
		detail.setEmail("email");
		detail.setContent("content");

		board.setBoardDetail(detail);
		detail.setBoard(board);

		session.save(board);

		onetoone2(session, board);

		tx.commit();
		session.close();
	}

	// 부모 객체 저장시 cascade 속성에 의해 자동으로 자식 객체가 저장이 된다.
	public static void onetoone(Session session) {
		org.hibernate.Criteria c = session.createCriteria(Board.class, "b");
		List list = c.list();
		System.out
				.println("--------------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			Board b = (Board) list.get(i);
			System.out.println("child content:"
					+ b.getBoardDetail().getContent());
		}
	}

	// 부모 삭제시 자식도 자동삭제가 된다
	public static void onetoone2(Session session, Board board) {
		session.delete(board);

		Criteria c = session.createCriteria(BoardDetail.class, "bd");
		List list = c.list();
		System.out
				.println("--------------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}
