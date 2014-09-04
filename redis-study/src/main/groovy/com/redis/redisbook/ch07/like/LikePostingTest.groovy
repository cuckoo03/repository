package com.redis.redisbook.ch07.like

import static org.junit.Assert.*

import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.redis.redisbook.ch07.JedisHelper
class LikePostingTest {
	static JedisHelper helper
	private LikePosting likePosting
	private static Random rand = new Random()
	private static final int POSTING_COUNT = 10
	private static final int TESTUSER = rand.nextInt(100)
	private static String[] POSTLIST = new String[POSTING_COUNT]

	@BeforeClass
	public static void setUpClass() {
		helper = JedisHelper.getInstance()
		POSTING_COUNT.times {
			POSTLIST[it] = String.valueOf(it + 1)
		}
	}

	@AfterClass
	public static void tearDownClass() {
		helper.destroyPool()
	}

	@Before
	public void setUp() {
		this.likePosting = new LikePosting(helper)
		assertNotNull(likePosting)
	}

	@Test
	public void testLike() {
		String postingNo = String.valueOf(this.rand.nextInt(POSTING_COUNT))
		if (this.likePosting.isLiked(postingNo, String.valueOf(TESTUSER))) {
			this.likePosting.unlike(postingNo, String.valueOf(TESTUSER))
		}
		assertTrue(this.likePosting.like(postingNo, String.valueOf(TESTUSER)))
	}

	@Test
	public void testUnlike() {
		String postingNo = String.valueOf(this.rand.nextInt(POSTING_COUNT))
		if (this.likePosting.isLiked(postingNo, String.valueOf(TESTUSER))) {
			assertTrue(this.likePosting.unlike(postingNo,
					String.valueOf(TESTUSER)))
		} else {
			assertTrue(this.likePosting.like(postingNo,
					String.valueOf(TESTUSER)))
			assertTrue(this.likePosting.unlike(postingNo,
					String.valueOf(TESTUSER)))
		}
	}

	@Test
	public void testGetLikeCount() {
		String postingNo = String.valueOf(this.rand.nextInt(POSTING_COUNT))
		if (this.likePosting.isLiked(postingNo, String.valueOf(TESTUSER))) {
			assertTrue(this.likePosting.unlike(postingNo,
					String.valueOf(TESTUSER)))
		}

		Long prevCount = this.likePosting.getLikeCount(postingNo)
		this.likePosting.like(postingNo, String.valueOf(TESTUSER))
		assertEquals(this.likePosting.getLikeCount(postingNo),
				prevCount.plus(1))
	}

	@Test
	public void testGetLikeCountList() {
		List<Long> countList = this.likePosting.getLikeCountList(POSTLIST)
		assertEquals(countList.size(), POSTING_COUNT)
	}

	@Test
	public void testIsLiked() {
		String postingNo = String.valueOf(this.rand.nextInt(POSTING_COUNT))
		this.likePosting.like(postingNo, String.valueOf(TESTUSER))
		assertTrue(this.likePosting.isLiked(postingNo, String.valueOf(TESTUSER)))
	}
	
	@Test
	public void testDeleteLikeInfo() {
		String postingNo = "A1234567890"
		this.likePosting.like(postingNo, String.valueOf(TESTUSER))
		assertTrue(this.likePosting.deleteLikeInfo(postingNo))
	}
	
	@Test
	public void testRandomLike() {
		POSTING_COUNT.each {
			String sudoRandomUser = String.valueOf(rand.nextInt(100))
			this.likePosting.like(String.valueOf(it), sudoRandomUser)
		}
	}
}
