package com.mahout.mahoutinaction.ch01

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


class RecommenderIntro {
	static main(args) {
		Path p = FileSystems.getDefault().getPath("C:/Users/cuckoo03/workspace/mahout-study/target/classes/resources/intro.csv")
		DataModel dataModel = new FileDataModel(p.toFile())

		UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel)
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(2,
				similarity, dataModel)

		Recommender recommender = new GenericUserBasedRecommender(
				dataModel, neighborhood, similarity)

//		List<RecommendedItem> recommendations = recommender.recommend(1, 2)

//		for (RecommendedItem recommendation : recommendations) {
//			println recommendation
//		}
		
		LongPrimitiveIterator user = dataModel.getUserIDs()
		while (user.hasNext()) {
			long userId = user.nextLong()
			
			List<RecommendedItem> recommendations = recommender.recommend(userId, 1)
			for (RecommendedItem recommendation : recommendations){
				println userId + "," + recommendation.getItemID() + "," + recommendation.getValue()
			} 
		}
	}
}