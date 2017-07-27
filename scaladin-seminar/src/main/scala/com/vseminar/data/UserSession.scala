package com.vseminar.data

import com.vaadin.server.WrappedSession
import com.vaadin.server.VaadinService

/**
 * @author TC
 */
class UserSession extends Serializable {

}

object UserSession {
  private def getCurrentSession(): WrappedSession = {
    val request = VaadinService.getCurrentRequest()
    if (request == null) {
      throw new IllegalStateException("no request")
    }
    val session = request.getWrappedSession()
    if (session == null) {
      throw new IllegalStateException("no session")
    }

    session
  }
}