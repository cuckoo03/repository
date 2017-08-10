package com.mahout.mahoutinaction.ch01

import groovy.transform.TypeChecked;

import java.nio.file.FileSystems
import java.nio.file.Path

import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood
import org.apache.mahout.cf.taste.recommender.RecommendedItem
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.similarity.UserSimilarity
import org.apache.mahout.common.RandomUtils

@TypeChecked
class RecommenderIntro {
	static main(args) {
		def p = FileSystems.getDefault().getPath("C:/Users/cuckoo03/workspace/mahout-study/target/classes/resources/intro.csv")
		def dataModel = new FileDataModel(p.toFile())

		def similarity = new PearsonCorrelationSimilarity(dataModel)
		def neighborhood = new NearestNUserNeighborhood(2,
				similarity, dataModel)

		def recommender = new GenericUserBasedRecommender(
				dataModel, neighborhood, similarity)

//		List<RecommendedItem> recommendations = recommender.recommend(1, 2)

//		for (RecommendedItem recommendation : recommendations) {
//			println recommendation
//		}
		
		def user = dataModel.getUserIDs()
		while (user.hasNext()) {
			def userId = user.nextLong()
			
			def recommendations = recommender.recommend(userId, 1)
			for (RecommendedItem recommendation : recommendations){
				println userId + "," + recommendation.getItemID() + "," + recommendation.getValue()
			} 
		}
	}
}