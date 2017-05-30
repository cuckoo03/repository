package com.beans

import org.springframework.orm.hibernate3.HibernateTemplate
import org.springframework.stereotype.Repository

import javax.annotation.Resource


/**
 * @author TC
 */
@Repository
class ScalaDao {
//  @Resource(name = "hibernateTemplate")
  var hibernateTemplate:HibernateTemplate = null 
  
  def setHibernateTemplate(hibernateTemplate:HibernateTemplate):Unit = {
    this.hibernateTemplate = hibernateTemplate
  }
  
  def select(): String = "select"
}