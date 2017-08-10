package com.mahoutinaction.ch02

import java.io.File
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import scala.collection.JavaConverters._
import org.apache.mahout.cf.taste.recommender.RecommendedItem

/**
 * @author TC
 */
object RecommenderIntro {
  def main(args: Array[String]) {
    val model = new FileDataModel(new File("intro.csv"))

    val similarity = new PearsonCorrelationSimilarity(model)
    val neighborhood = new NearestNUserNeighborhood(2, similarity, model)

    val recommender = new GenericUserBasedRecommender(model, neighborhood, similarity)
    
    var recommendations = recommender.recommend(1, 1)
    
    val iter = recommendations.iterator()
    while (iter.hasNext()) {
      println(iter.next())
    }
  }
}