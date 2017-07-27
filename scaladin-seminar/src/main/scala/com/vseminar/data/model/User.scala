package com.vseminar.data.model

/**
 * @author TC
 */
class User {
  var id: Long = 0
  var name: String = null
  var email: String = null
  var password: String = null
  var imgPath: String = null
  var role: RoleType = RoleType.User

  def this(other: User) {
    this()
    this.id = other.id
    this.name = other.name
    this.email = other.email
    this.password = other.password
    this.imgPath = other.imgPath
    this.role = other.role
  }

  def this(name: String, email: String) {
    this()
    this.name = name
    this.email = email
    this.role = RoleType.User
  }
}