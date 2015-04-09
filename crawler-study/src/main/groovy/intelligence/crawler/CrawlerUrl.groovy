package intelligence.crawler

import groovy.transform.TypeChecked

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@TypeChecked
class CrawlerUrl {
	private static final Logger LOG = LoggerFactory.getLogger(CrawlerUrl.class)

	private int depth = 0 // url depth
	private String urlString = null
	private URL url = null
	private boolean isAllowedToVisit//이 url을 크롤링할수 있는가
	private boolean isCheckedForPermissions = false // 이 Url을 이미 방문했는가
	private boolean isVisited = false

	public CrawlerUrl(String urlString, int depth) {
		this.depth = depth
		this.urlString = urlString
		computeUrl()
	}
	public void computeUrl() {
		try {
			url = new URL(urlString)
		} catch (MalformedURLException e) {
			e.printStackTrace()
		}
	}

	public java.net.URL getURL() {
		return this.url
	}
	public int getDepth() {
		return this.depth
	}
	public boolean isAllowedToVisit() {
		return isAllowedToVisit
	}
	public void setAllowedToVisit(boolean isAllowedToVisit) {
		this.isAllowedToVisit = isAllowedToVisit
		this.isCheckedForPermissions = true
	}
	public boolean isCheckedForPermission() {
		return isCheckedForPermissions
	}
	public boolean isVisited() {
		return isVisited
	}
	public void setIsVisited() {
		this.isVisited = true
	}
	public String getUrlString() {
		return  this.urlString
	}
	@Override
	public String toString() {
		return this.urlString + " depth=" + depth + " visit=" +
				this.isAllowedToVisit + " check=" + this.isCheckedForPermissions
	}
	@Override
	public int hashCode() {
		return this.urlString.hashCode()
	}
	@Override
	public boolean equals(Object obj) {
		return obj.hashCode() == this.hashCode()
	}
}